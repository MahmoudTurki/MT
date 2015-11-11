package com.turki.androidtask.flickr.cacheloader;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.turki.androidtask.flickr.model.Photo;
import com.turki.androidtask.flickr.utilities.SearchApiUtils;

import java.io.InputStream;

/**
 * An implementation of ModelStreamLoader that leverages the StreamOpener class and the ExecutorService backing the
 * Engine to download the image and resize it in memory before saving the resized version
 * directly to the disk cache.
 */
public class FlickrModelLoader extends BaseGlideUrlLoader<Photo> {


    public static class Factory implements ModelLoaderFactory<Photo, InputStream> {
        private final ModelCache<Photo, GlideUrl> modelCache = new ModelCache<Photo, GlideUrl>(500);

        @Override
        public ModelLoader<Photo, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new FlickrModelLoader(context, modelCache);
        }

        @Override
        public void teardown() {
        }
    }

    public FlickrModelLoader(Context context, ModelCache<Photo, GlideUrl> modelCache) {
        super(context, modelCache);
    }

    @Override
    protected String getUrl(Photo model, int width, int height) {
        return SearchApiUtils.getPhotoURL(model, width, height);
    }
}
