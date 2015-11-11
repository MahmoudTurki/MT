package com.turki.androidtask.flickr.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.turki.androidtask.R;
import com.turki.androidtask.flickr.ui.adapter.FlickrPhotoListAdapter;
import com.turki.androidtask.flickr.model.Photo;
import com.turki.androidtask.flickr.listeners.PhotoViewerListener;

import java.util.List;

/**
 * @author Mahmoud Turki
 * A fragment that shows cropped image thumbnails half the width of the screen in a scrolling list.
 */
public class FlickrPhotoListFragment extends BaseFragment implements PhotoViewerListener {
    private static final String STATE_POSITION_INDEX = "state_position_index";
    private static final String STATE_POSITION_OFFSET = "state_position_offset";
    private FlickrPhotoListAdapter adapter;
    private List<Photo> currentPhotos;
    private RecyclerView list;
    private  LinearLayoutManager linearLayout;

    public static FlickrPhotoListFragment newInstance() {
        return new FlickrPhotoListFragment();
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     * Initiate the list view and setting the adapter
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.flickr_photo_list, container, false);
        list = (RecyclerView) result.findViewById(R.id.flickr_photo_list);
        list.setHasFixedSize(true);

        linearLayout = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayout);

        adapter = new FlickrPhotoListAdapter(getActivity());
        list.setAdapter(adapter);

        if (currentPhotos != null) {
            adapter.setPhotos(currentPhotos);
        }

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt(STATE_POSITION_INDEX);
            int offset = savedInstanceState.getInt(STATE_POSITION_OFFSET);
            linearLayout.scrollToPositionWithOffset(index, offset);
        }
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (list != null) {
            int index = linearLayout.findFirstVisibleItemPosition();
            View topView = list.getChildAt(0);
            int offset = topView != null ? topView.getTop() : 0;
            outState.putInt(STATE_POSITION_INDEX, index);
            outState.putInt(STATE_POSITION_OFFSET, offset);
        }
    }

    /**
     * @param photos The loaded photos.
     * Listener triggered after parsing the response to load and refresh the adapter list
     */
    @Override
    public void onPhotosUpdated(List<Photo> photos) {
        currentPhotos = photos;
        if (adapter != null) {
            adapter.setPhotos(currentPhotos);
        }
    }

}