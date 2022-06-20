package hr.cloudwalk.currweather;

import android.appwidget.AppWidgetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetWorker implements Runnable {
	final static String TAG = "TVW WidgetWorker";
	AppWidgetManager appWidgetManager;
	int appWidgetId;
	RemoteViews remoteViews;
	String url;
	String title;
	boolean ignoreCache = false;

	public static void run(AppWidgetManager appWidgetManager, int appWidgetId, RemoteViews remoteViews, String url, String title, boolean ignoreCache) {
		try {
			Log.e(TAG, "Started for widget: " + appWidgetId);
			Bitmap b = Utils.loadBitmapFromUrl(url, null, ignoreCache, title);
			if (b != null) {
				try {

					float ratio = UpdateWidgetService.minScreenDimension * 1f / b.getWidth();
					if (ratio * b.getHeight() > UpdateWidgetService.maxScreenDimension)
						ratio = UpdateWidgetService.maxScreenDimension / b.getHeight();
					Log.i(TAG, "resizing bitmap with factor: " + ratio + "for " + appWidgetId);
					if (ratio != 1.0) {
						Matrix matrix = new Matrix();
						matrix.setScale(ratio, ratio);
						Bitmap newBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
						b = newBitmap;
					}
				} catch (Exception e) {
					Log.e(TAG, e.toString(), e);
				}
				remoteViews.setBitmap(R.id.img1, "setImageBitmap", b);
				remoteViews.setViewVisibility(R.id.status, View.GONE);
			} else {
				remoteViews.setTextViewText(R.id.status, "Osvježavanje nije uspjelo.");
			}
		} catch (Exception e) {
			remoteViews.setTextViewText(R.id.status, "Greška.");
			Log.e(TAG, e.toString(), e);
		}
		try {
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	@Override
	public void run() {
		run(appWidgetManager, appWidgetId, remoteViews, url, title, ignoreCache);
	}

}
