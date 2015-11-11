package com.turki.androidtask.flickr.listeners;

import com.turki.androidtask.flickr.model.Photo;

import java.util.List;

/**
 * @author Mahmoud Turki
 * An interface for listening for search results from the Flickr API.
 */
public interface SearchListener {
    /**
     * Called when a search completes successfully.
     *
     * @param searchString The term that was searched for.
     * @param photos A list of images that were found for the given search term.
     */
    public void onSearchCompleted(String searchString, List<Photo> photos);

    /**
     * Called when a search fails.
     *
     * @param searchString The term that was searched for.
     * @param e The exception that caused the search to fail.
     */
    public void onSearchFailed(String searchString, Exception e);
}