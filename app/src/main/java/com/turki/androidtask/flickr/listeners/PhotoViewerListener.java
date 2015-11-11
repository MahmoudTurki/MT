package com.turki.androidtask.flickr.listeners;

import com.turki.androidtask.flickr.model.Photo;

import java.util.List;

/**
 * @author Mahmoud Turki
 * An interface for an object that displays Photo objects.
 */
public interface PhotoViewerListener {
    /**
     * Called whenever new Photos are loaded.
     *
     * @param photos The loaded photos.
     */
    public void onPhotosUpdated(List<Photo> photos);
}
