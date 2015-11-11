package com.turki.androidtask.flickr.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.*;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.bumptech.glide.request.FutureTarget;
import com.turki.androidtask.flickr.model.Photo;
import com.turki.androidtask.flickr.utilities.SearchApiUtils;
import com.turki.androidtask.flickr.utilities.Utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author Mahmoud Turki
 */
public class AppController extends Application {

	private static final int THREAD_POOL_SIZE = 3;

	private Handler mHandler;
	private ExecutorService mExecutor;
	public static final String TAG = AppController.class.getSimpleName();
	private RequestQueue mRequestQueue;
	public ThumbnailFetcher backgroundFetcher;

	private static AppController mInstance;

	/**
	 * CallBack method to Initiate pool of threads and handler in the Application
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mHandler = new Handler();

		// Thread pool init
		mExecutor = Executors.newScheduledThreadPool(THREAD_POOL_SIZE,
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable runnable) {
						Thread thread = new Thread(runnable,"Background executor");
						thread.setPriority(Thread.MIN_PRIORITY);
						thread.setDaemon(true);
						return thread;
					}
				});


		// Weight values determined experimentally by measuring the number of incurred GCs while scrolling through
		// the various photo grids/lists.
		Glide.get(this).preFillBitmapPool(new PreFillType.Builder(Utils.getScreenWidth(this) / 2, Utils.getListHeightSize(this)).setWeight(6));
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	/**
	 * @return RequestQueue
	 * Create single instance from RequestQueue and set cach file with max 1024 *1024 Byte
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext(), 1024 * 1024);
		}
		return mRequestQueue;
	}

	/**
	 * @param req
	 * @param tag
	 * @param <T>
	 * Add Requests to RequestQueue and set tags to each Request in case you wanna cancel Requests with specific tag
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	/**
	 * @param req
	 * @param <T>
	 * Add Requests to RequestQueue
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	/**
	 * @param tag
	 * Cancel all requests that match specific tags from RequestQueue
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	/**
	 * @param runnable
	 * Process operation in UI thread
	 */
	public void runOnUiThread(Runnable runnable) {
		mHandler.post(runnable);
	}

	/**
	 * @param runnable
	 * Process operation in BackGround thread
	 */
	public void runInBackground(final Runnable runnable) {
		mExecutor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @param level
	 * CallBack method called when the system requests the application cleans up memory, but pass indicator of Application application
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		Glide.get(this).trimMemory(level);
	}

	/**
	 * CallBack method called when the system requests the application cleans up memory
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Glide.get(this).clearMemory();
	}

	public class ThumbnailFetcher implements Runnable {
		private boolean isCancelled;
		private Context context;
		private List<Photo> photos;

		public ThumbnailFetcher(Context context, List<Photo> photos) {
			this.context = context;
			this.photos = photos;
		}

		public void cancel() {
			isCancelled = true;
		}

		@Override
		public void run() {
			android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
			for (Photo photo : photos) {
				if (isCancelled) {
					return;
				}

				FutureTarget<File> futureTarget = Glide.with(context)
						.load(photo)
						.downloadOnly(SearchApiUtils.SQUARE_THUMB_SIZE, SearchApiUtils.SQUARE_THUMB_SIZE);

				try {
					futureTarget.get();
				} catch (InterruptedException e) {
					if (Log.isLoggable(TAG, Log.DEBUG)) {
						Log.d(TAG, "Interrupted waiting for background downloadOnly", e);
					}
				} catch (ExecutionException e) {
					if (Log.isLoggable(TAG, Log.DEBUG)) {
						Log.d(TAG, "Got ExecutionException waiting for background downloadOnly", e);
					}
				}
				Glide.clear(futureTarget);
			}

		}
	}
}
