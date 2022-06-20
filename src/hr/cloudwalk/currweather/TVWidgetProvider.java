package hr.cloudwalk.currweather;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class TVWidgetProvider extends AppWidgetProvider {
	final static String TAG = "TVW WidgetProvider";
	public final static String UPDATE_ONE_WIDGET = "UPDATE_ONE_WIDGET";
	public final static String SHOW_BUTTONS = "SHOW_BUTTONS";
	public final static String FULL_SCREEN_VIEW = "FULL_SCREEN_VIEW";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d(TAG, "onUpdate method called");
		// ComponentName thisWidget = new ComponentName(context,
		// TVWidgetProvider.class);
		// int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

		// Update the widgets via the service
		context.startService(intent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
		Log.d(TAG, "onReceive, widgetId:" + widgetId);
		Intent serviceIntent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
		serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		if (intent.getAction().equals(UPDATE_ONE_WIDGET)) {
			Log.d(TAG, "update one: " + widgetId);
			serviceIntent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, UPDATE_ONE_WIDGET);
			context.startService(serviceIntent);
		} else if (intent.getAction().equals(SHOW_BUTTONS)) {
			Log.d(TAG, "show buttons: " + widgetId);
			serviceIntent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, SHOW_BUTTONS);
			context.startService(serviceIntent);
		} else if (intent.getAction().equals(FULL_SCREEN_VIEW)) {
			Log.d(TAG, "full screen view: " + widgetId);
			serviceIntent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, FULL_SCREEN_VIEW);
			context.startService(serviceIntent);
		} else {
			super.onReceive(context, intent);
		}
	}

}
