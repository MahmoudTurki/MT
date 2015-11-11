package com.turki.androidtask.flickr.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.turki.androidtask.R;
import com.turki.androidtask.flickr.app.AppController;
import com.turki.androidtask.flickr.listeners.PhotoViewerListener;
import com.turki.androidtask.flickr.listeners.SearchListener;
import com.turki.androidtask.flickr.managers.SearchApi;
import com.turki.androidtask.flickr.model.Photo;
import com.turki.androidtask.flickr.utilities.Constant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mahmoud Turki
 * An activity that allows users to search for images on Flickr and that contains a series of fragments that display
 * retrieved image thumbnails.
 */
public class FlickrUserPhotosActivity extends BaseActivity  implements SearchListener{
    private static final String TAG = "FlickrSearchActivity";
    private static final String STATE_SEARCH_STRING = "state_search_string";

    private View searching;
    private TextView searchTerm;
    private Set<PhotoViewerListener> photoViewerListeners = new HashSet<PhotoViewerListener>();
    private List<Photo> currentPhotos = new ArrayList<Photo>();
    private View searchLoading;
    private String currentSearchString;

    /**
     * @param context
     * @param userID
     * @return Intent object to start the activity with required param
     * Ex:- {
     *          StartActivity( FlickrUserPhotosActivity.getActivityIntent(context, userID) );
     *      }
     */
    public static Intent getActivityIntent(Context context, String userID) {
        return new Intent(context, FlickrUserPhotosActivity.class).putExtra(Constant.UserPhoto.OWNER_ID, userID);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flickr_user_photos_activity);

        initView(savedInstanceState);
        setTitle(R.string.flickr_user_photos);
    }

    /**
     * Initiate the view, set title for the screen and hide BackButton
     * @param savedInstanceState
     */
    private void initView(Bundle savedInstanceState){
        searching      = findViewById(R.id.searching);
        searchLoading  = findViewById(R.id.search_loading);
        searchTerm     = (TextView) findViewById(R.id.search_term);

         if (savedInstanceState != null) {
            String savedSearchString = savedInstanceState.getString(STATE_SEARCH_STRING);
            if (!TextUtils.isEmpty(savedSearchString)) {
                executeSearch(savedSearchString);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(currentSearchString)) {
            outState.putString(STATE_SEARCH_STRING, currentSearchString);
        }
    }

    /** Add Search listeners onResume **/
    @Override
    public void onResume() {
        SearchApi.get(this).registerSearchListener(this);
        executeSearch(getIntent().getStringExtra(Constant.UserPhoto.OWNER_ID));
        super.onResume();
    }

    /** Remove Search listeners onPause **/
    @Override
    public void onPause() {
        SearchApi.get(this).unregisterSearchListener(this);
        super.onPause();
    }

    /** Calling the server to execute the search functionality **/
    private void executeSearch(String searchString) {
        currentSearchString = searchString;

        if (TextUtils.isEmpty(searchString)) {
            return;
        }

        searching.setVisibility(View.VISIBLE);
        searchLoading.setVisibility(View.VISIBLE);
        searchTerm.setText(getString(R.string.searching_for, currentSearchString));

        /** Search Connecting..... **/
        SearchApi.get(this).search(currentSearchString, Constant.UserPhoto.USER_SEARCH);
    }

    /** Update the view list with reponse photos **/
     @Override
     public void onSearchCompleted(String searchString, List<Photo> photos) {
         if (!TextUtils.equals(currentSearchString, searchString)) {
             return;
         }
         searching.setVisibility(View.INVISIBLE);
         for (PhotoViewerListener viewer : photoViewerListeners) {
              viewer.onPhotosUpdated(photos);
         }

         if (backgroundFetcher != null) {
             backgroundFetcher.cancel();
          }

         backgroundFetcher = AppController.getInstance().new ThumbnailFetcher(FlickrUserPhotosActivity.this, photos);
         backgroundHandler.post(backgroundFetcher);
         currentPhotos = photos;
     }

    /** Notify the user with the failure of loading photos from server  **/
    @Override
     public void onSearchFailed(String searchString, Exception e) {
         if (!TextUtils.equals(currentSearchString, searchString)) {
             return;
         }
         searching.setVisibility(View.VISIBLE);
         searchLoading.setVisibility(View.INVISIBLE);
         searchTerm.setText(getString(R.string.search_failed, currentSearchString));
     }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof PhotoViewerListener) {
            PhotoViewerListener photoViewerListener = (PhotoViewerListener) fragment;
            photoViewerListener.onPhotosUpdated(currentPhotos);
            if (!photoViewerListeners.contains(photoViewerListener)) {
                photoViewerListeners.add(photoViewerListener);
            }
        }
    }
}
