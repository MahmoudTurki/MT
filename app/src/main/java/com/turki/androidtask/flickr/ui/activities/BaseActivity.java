package com.turki.androidtask.flickr.ui.activities;


import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.turki.androidtask.R;
import com.turki.androidtask.flickr.app.AppController;
import com.turki.androidtask.flickr.managers.SearchApi;

/**
 * @author Mahmoud Turki
 * Base Actvity share common actions and functionality for all activites
 */
public class BaseActivity extends ActionBarActivity {

	private RelativeLayout  titleBarView;
	private ImageButton backBtn;
	private FrameLayout contentLayout;
	private TextView title;
	public HandlerThread backgroundThread;
	public Handler backgroundHandler;
	public AppController.ThumbnailFetcher backgroundFetcher;

	/**
	 * @param arg0
	 * CallBack method to initiate inflate activity view, handle custom ActionBar layout and set events listeners on views.
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		super.setContentView(R.layout.base_activity);

		backgroundThread = new HandlerThread("BackgroundHandlerThread");
		backgroundThread.start();
		backgroundHandler = new Handler(backgroundThread.getLooper());

		View customView = LayoutInflater.from(this).inflate(R.layout.title_bar, null);
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(customView,
				new ActionBar.LayoutParams(
						ActionBar.LayoutParams.WRAP_CONTENT,
						ActionBar.LayoutParams.MATCH_PARENT,
						Gravity.CENTER
				));
		title = (TextView) customView.findViewById(R.id.title);

		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doBack();
			}
		});
		contentLayout = (FrameLayout) findViewById(R.id.base_activity_content);
		titleBarView = (RelativeLayout) findViewById(R.id.titleBar_relativeLayout);

	}

	/**
	 * @param layoutResID
	 * Inflate activity content view using layout resource id.
	 */
	@Override
	public void setContentView(int layoutResID) {
		View content = LayoutInflater.from(this).inflate(layoutResID, contentLayout, true);
	}

	/**
	 * Enable backButton visibility in the activity.
	 */
	public void showBackBtn() {
		backBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * disable backButton visibility in the activity.
	 */
	public void hideBackBtn() {
		backBtn.setVisibility(View.INVISIBLE);
	}

	/**
	 * @param text
	 * Set activity title using string.
	 */
	public void setTitle(String text) {
		title.setText(text);
	}

	/**
	 * @param text
	 * Set activity title using resource id.
	 */
	public void setTitle(int text) {
		title.setText(text);
	}

	/**
	 * Implement click listener on backButton pressed.
	 */
	private void doBack() {
		this.onBackPressed();
	}

	/**
	 * @return  backButton
	 */
	public ImageView getBackButton() {
		return backBtn;
	}

	/**
	 * remove activity from application back stack.
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (backgroundFetcher != null) {
			backgroundFetcher.cancel();
			backgroundFetcher = null;
			backgroundThread.quit();
			backgroundThread = null;
		}
	}
}
