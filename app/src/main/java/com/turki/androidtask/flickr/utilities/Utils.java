package com.turki.androidtask.flickr.utilities;

import android.content.Context;

import com.turki.androidtask.R;

/**
 * @author Mahmoud Turki
 * Utility class to get screen & list dimenssions
 */
public class Utils {

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getListHeightSize(Context context){
        return context.getResources().getDimensionPixelSize(R.dimen.flickr_list_item_height);
    }

}
