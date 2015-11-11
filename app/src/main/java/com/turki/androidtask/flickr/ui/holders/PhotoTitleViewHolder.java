package com.turki.androidtask.flickr.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.turki.androidtask.R;

/**
 * @author Mahmoud Turki
 * ViewHolder of photos RecyclerView adapter which holds references to the relevant views
 */
public class PhotoTitleViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleView;
        public final ImageView imageView;
        public final ImageView goArrowImg;
        public final LinearLayout linearLayout;

        public PhotoTitleViewHolder(View itemView) {
            super(itemView);
            imageView    = (ImageView) itemView.findViewById(R.id.photo_view);
            titleView    = (TextView) itemView.findViewById(R.id.title_view);
            goArrowImg   = (ImageView) itemView.findViewById(R.id.go_arrow);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.rowLinearLayout);
        }
    }
