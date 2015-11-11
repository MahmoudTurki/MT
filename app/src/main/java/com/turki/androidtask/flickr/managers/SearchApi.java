package com.turki.androidtask.flickr.managers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.turki.androidtask.flickr.app.AppController;
import com.turki.androidtask.flickr.listeners.SearchListener;
import com.turki.androidtask.flickr.model.Photo;
import com.turki.androidtask.flickr.utilities.Constant;
import com.turki.androidtask.flickr.utilities.SearchApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mahmoud Turki
 * A class for interfacing with Flickr's http API, and hanlding the Requests & Responses of the Search functionality
 */
public class SearchApi {
    private static SearchApi searchApi;

    /**
     * @param context
     * @return instance from Search Api
     * Create a single instance from search manger to handle all communication of search
     */
    public static SearchApi get(Context context) {
        if (searchApi == null) {
            searchApi = new SearchApi(context);
        }
        return searchApi;
    }

    //private final RequestQueue requestQueue;
    private final Set<SearchListener> searchListeners = new HashSet<SearchListener>();
    private SearchResult lastSearchResult;

    protected SearchApi(Context context) {
        //this.requestQueue = AppController.getInstance().getRequestQueue();
    }

    /**
     * @param searchListener
     * Adding search listeners
     */
    public void registerSearchListener(SearchListener searchListener) {
        searchListeners.add(searchListener);
    }

    /**
     * @param searchListener
     * Removing search listeners
     */
    public void unregisterSearchListener(SearchListener searchListener) {
        searchListeners.remove(searchListener);
    }

    public void search(final String text, int searchType) {

        if (lastSearchResult != null && TextUtils.equals(lastSearchResult.searchString, text)) {
            for (SearchListener listener : searchListeners) {
                listener.onSearchCompleted(lastSearchResult.searchString, lastSearchResult.results);
            }
            return;
        }

        // Check in cache if you sent the same request before to the server or not,
        // If you sent the request before, then get cached data. If not, then send the request to the server to get the data.
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(SearchApiUtils.getSearchUrl(text, searchType));
        if(entry != null){
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                Log.e(Constant.App.TAG, "Cash Search success response=" + data);

                // handle data, like converting it to xml, json, bitmap etc.,
                parseResponse(data, text);
            } catch (UnsupportedEncodingException e) {
                for (SearchListener listener : searchListeners) {
                    listener.onSearchFailed(text, e);
                }
            }
        }else{
            // cached response doesn't exists. Make a network call here
            StringRequest request = new StringRequest(Request.Method.GET, SearchApiUtils.getSearchUrl(text, searchType),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           parseResponse(response, text);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    for (SearchListener listener : searchListeners) {
                        listener.onSearchFailed(text, error);
                    }
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(request);
        }
    }

    /**
     * @param response
     * @param text
     * Parsing the coming response from the server and saving the last search text
     */
    private void parseResponse(String response, String text){
        //cut out initial flickJsonApi(
        JSONObject searchResults;
        JSONArray photos = null;
        try {
             //cut out initial flickJsonApi(
            searchResults = new JSONObject(response.substring(14, response.length() - 1));
            photos = searchResults.getJSONObject("photos").getJSONArray("photo");
            List<Photo> results = new ArrayList<Photo>(photos.length());
            for (int i = 0; i < photos.length(); i++) {
                results.add(new Photo(photos.getJSONObject(i)));
            }
            lastSearchResult = new SearchResult(text, results);
            for (SearchListener listener : searchListeners) {
                listener.onSearchCompleted(text, results);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            for (SearchListener listener : searchListeners) {
                listener.onSearchFailed(text, e);
            }
        }
    }

    private static class SearchResult {
        private final String searchString;
        private final List<Photo> results;

        public SearchResult(String searchString, List<Photo> results) {

            this.searchString = searchString;
            this.results = results;
        }

    }
}
