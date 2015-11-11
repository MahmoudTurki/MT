package com.turki.androidtask.flickr.utilities;

/**
 * @author Mahmoud Turki
 * Constant identifers of Tags, Api keys, Urls, etc.. in the application
 */
public class Constant {

    public static class App{
        public static final String TAG = "FlickrApi";
        public static final String API_KEY = "f0e6fbb5fdf1f3842294a1d21f84e8a6";
        public static final String SIGNED_API_URL = "https://api.flickr.com/services/rest/?method=%s&format=json&api_key=" + API_KEY;
        public static final int PAGE_SIZE = 100;
    }

    public static class Search{
        //incomplete size independent url for photos that can be cached per photo
        public static final String CACHEABLE_PHOTO_URL = "http://farm%s.staticflickr.com/%s/%s_%s_";
        public static final int PUBLIC_SEARCH = 1;
    }

    public static class UserPhoto{
        public static final int USER_SEARCH = 2;
        public static final String OWNER_ID = "ownerID";
    }

}
