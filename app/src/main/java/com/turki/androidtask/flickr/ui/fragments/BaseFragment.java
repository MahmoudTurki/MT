package com.turki.androidtask.flickr.ui.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.turki.androidtask.R;

/**
 * @author Mahmoud Turki
 * Base fragment view for all fragment handle the progress dialog in any newtwork operations
 */
public abstract class BaseFragment extends Fragment {

	private ProgressDialog mDialog;

	/**
	 * This method is used to show a progress dialog for the screen.
	 * @return True if the progress dialog was actually shown due this call.
	 */
	public boolean showLoadingDialog() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setCancelable(false);
		}
		
		if (!mDialog.isShowing()) {
			try {
				mDialog.show();
			} catch (Exception e) {
				// Nothing
			}
			return true;
		}
		return false;
	}
	
	/**
	 * This method is used to hide the progress dialog off this screen.
	 * @return True if the dialog was actually dismissed due this call.
	 */
	public boolean hideLoadingDialog() {
		if (getActivity() != null && !getActivity().isFinishing() && mDialog != null && mDialog.isShowing()) {
			try {
				mDialog.dismiss();
			} catch (Exception e) {
				// Nothing
			}
			return true;
		}
		return false;
	}
}
