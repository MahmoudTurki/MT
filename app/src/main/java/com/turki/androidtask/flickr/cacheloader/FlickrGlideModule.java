package com.turki.androidtask.flickr.cacheloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;
import com.turki.androidtask.flickr.model.Photo;

import java.io.InputStream;

public class FlickrGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(Photo.class, InputStream.class, new FlickrModelLoader.Factory());
    }
}
