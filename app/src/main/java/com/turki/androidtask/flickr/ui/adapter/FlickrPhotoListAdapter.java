package com.turki.androidtask.flickr.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.turki.androidtask.R;
import com.turki.androidtask.flickr.ui.activities.FlickrSearchActivity;
import com.turki.androidtask.flickr.ui.activities.FlickrUserPhotosActivity;
import com.turki.androidtask.flickr.ui.holders.PhotoTitleViewHolder;
import com.turki.androidtask.flickr.model.Photo;
import com.turki.androidtask.flickr.utilities.SearchApiUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author Mahmoud Turki
 * RecyclerView Adapter for photos list
 */
public class FlickrPhotoListAdapter extends RecyclerView.Adapter<PhotoTitleViewHolder>   implements ListPreloader.PreloadModelProvider<Photo> {

    private static final int PRELOAD_AHEAD_ITEMS = 5;
    private final LayoutInflater inflater;
    private List<Photo> photos = Collections.emptyList();
    private Context context;
    private DrawableRequestBuilder<Photo> fullRequest;
    private DrawableRequestBuilder<Photo> thumbRequest;
    private ViewPreloadSizeProvider<Photo> preloadSizeProvider;


    public FlickrPhotoListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        preloadSizeProvider = new ViewPreloadSizeProvider<Photo>();
        ListPreloader<Photo> preloader = new ListPreloader<Photo>(this, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);
        fullRequest = Glide.with(context)
                .from(Photo.class)
                .placeholder(new ColorDrawable(Color.GRAY))
                .centerCrop();

        thumbRequest = Glide.with(context)
                .from(Photo.class)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .override(SearchApiUtils.SQUARE_THUMB_SIZE, SearchApiUtils.SQUARE_THUMB_SIZE);

    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public PhotoTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.flickr_photo_list_item, parent, false);
        PhotoTitleViewHolder vh = new PhotoTitleViewHolder(view);
        preloadSizeProvider.setView(vh.imageView);
        return vh;
    }

    @Override
    public void onBindViewHolder(PhotoTitleViewHolder holder, int position) {
        final Photo current = photos.get(position);
        fullRequest.load(current)
                .thumbnail(thumbRequest.load(current))
                .into(holder.imageView);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof FlickrSearchActivity){
                    context.startActivity(FlickrUserPhotosActivity.getActivityIntent(context, current.getOwner()));
                }
            }
        });

        holder.titleView.setText(current.getTitle());
    }

    /**
     *
     * @param i
     * @return
     * Get object ID from list
     */
    @Override
    public long getItemId(int i) {
        return RecyclerView.NO_ID;
    }

    /**
     * @return count
     * Get size of list
     */
    @Override
    public int getItemCount() {
        return photos.size();
    }

    /**
     * @param position
     * @return
     * Get loaded list
     */
    @Override
    public List<Photo> getPreloadItems(int position) {
        return photos.subList(position, position + 1);
    }

    @Override
    public GenericRequestBuilder getPreloadRequestBuilder(Photo item) {
        return fullRequest.thumbnail(thumbRequest.load(item)).load(item);
    }
}

