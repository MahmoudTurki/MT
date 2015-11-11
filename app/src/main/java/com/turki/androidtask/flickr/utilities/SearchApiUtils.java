package com.turki.androidtask.flickr.utilities;


import android.util.SparseArray;

import com.turki.androidtask.flickr.model.Photo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mahmoud Turki
 */
public class SearchApiUtils {

    private static final SparseArray<String> EDGE_TO_SIZE_KEY = new SparseArray<String>() { {
        put(75, "s");
        put(100, "t");
        put(150, "q");
        put(240, "m");
        put(320, "n");
        put(640, "z");
        put(1024, "b");
    } };

    /**
     * @param text
     * @param searchType
     * @return Full url
     * Switching on search type to check if you wanna public search using any keyword or searching on specific user photos
     */
    public static String getSearchUrl(String text, int searchType) {
        if(searchType == Constant.UserPhoto.USER_SEARCH)
            return getUrlForMethod("flickr.people.getPhotos") + "&user_id=" + text + "&per_page=" + Constant.App.PAGE_SIZE;
        else
            return getUrlForMethod("flickr.photos.search") + "&text=" + text + "&per_page=" + Constant.App.PAGE_SIZE;
    }

    public static String getCacheableUrl(Photo photo) {
        return String.format(Constant.Search.CACHEABLE_PHOTO_URL, photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret());
    }

    public static String getPhotoURL(Photo photo, int width, int height) {
        return getPhotoUrl(photo, getSizeKey(width, height));
    }

    private static String getUrlForMethod(String method) {
        return String.format(Constant.App.SIGNED_API_URL, method);
    }

    public static String getPhotoUrl(Photo photo, String sizeKey) {
        return photo.getPartialUrl() + sizeKey + ".jpg";
    }

    private static final List<Integer> SORTED_SIZE_KEYS = new ArrayList<Integer>(EDGE_TO_SIZE_KEY.size());
    static {
        for (int i = 0; i < EDGE_TO_SIZE_KEY.size(); i++) {
            SORTED_SIZE_KEYS.add(EDGE_TO_SIZE_KEY.keyAt(i));
        }
        Collections.sort(SORTED_SIZE_KEYS);
    }

    public static final int SQUARE_THUMB_SIZE = SORTED_SIZE_KEYS.get(0);

    private static String getSizeKey(int width, int height) {
        final int largestEdge = Math.max(width, height);

        String result = EDGE_TO_SIZE_KEY.get(SORTED_SIZE_KEYS.get(SORTED_SIZE_KEYS.size() - 1));
        for (int edge : SORTED_SIZE_KEYS) {
            if (largestEdge <= edge) {
                result = EDGE_TO_SIZE_KEY.get(edge);
                break;
            }
        }
        return result;
    }

}
