package hr.cloudwalk.currweather;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class UpdateWidgetService extends IntentService {

	public UpdateWidgetService(String name) {
		super(name);
	}

	public UpdateWidgetService() {
		super("UpdateWidgetService");
	}

	private static final String TAG = "TVW UpdateWidgetService";
	public static int minScreenDimension = 240;
	public static int maxScreenDimension = 360;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			Log.i(TAG, "Service created");
			DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
			int width = metrics.widthPixels;
			int height = metrics.heightPixels;
			minScreenDimension = Math.min(width, height);
			maxScreenDimension = Math.max(width, height);
		} catch (Exception e) {
		}
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Service destroyed");
		super.onDestroy();
	};

	static String loadTitlePref(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(TVWidgetConfigActivity.PREFS_NAME, 0);
		String title = prefs.getString(TVWidgetConfigActivity.PREF_PREFIX_KEY + appWidgetId, null);
		if (title != null) {
			return title;
		} else {
			return "SAT24 HR";
		}
	}

	static boolean loadActionButtonsVisibilityPref(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(TVWidgetConfigActivity.PREFS_NAME, 0);
		boolean visible = prefs.getBoolean("visibility" + appWidgetId, false);
		return visible;

	}

	static void saveActionButtonsVisibilityPref(Context context, int appWidgetId, boolean visible) {
		context.getSharedPreferences(TVWidgetConfigActivity.PREFS_NAME, 0).edit().putBoolean("visibility" + appWidgetId, visible).commit();
	}

	public static String getURL4Title(Context c, String title) {
		String url = null;
		try {

			if (title.equals(c.getResources().getString(R.string.sat24_hr))) {
				url = TrenutnoVrijemeActivity.getSAT24HRURL();
			} else if (title.equals(c.getResources().getString(R.string.sat24_eu))) {
				url = TrenutnoVrijemeActivity.getSAT24EUURL();
			} else if (title.equals(c.getResources().getString(R.string.radar_bilogora))) {
				url = TrenutnoVrijemeActivity.BILOGORA_RADAR;
			} else if (title.equals(c.getResources().getString(R.string.radar_osijek))) {
				url = TrenutnoVrijemeActivity.OSIJEK_RADAR;
			} else if (title.equals(c.getResources().getString(R.string.radar_puntijarka))) {
				url = TrenutnoVrijemeActivity.PUNTIJARKA_RADAR;
			} else if (title.equals(c.getResources().getString(R.string.radar_kompozitni))) {
				url = TrenutnoVrijemeActivity.KOMPOZITNI_RADAR;
			} else if (title.equals(c.getResources().getString(R.string.radar_lisca))) {
				url = TrenutnoVrijemeActivity.RADARSLO;
			} else if (title.equals(c.getResources().getString(R.string.radar_fossalon))) {
				url = TrenutnoVrijemeActivity.RADAR_FVG;
			} else if (title.equals(c.getResources().getString(R.string.munje))) {
				url = TrenutnoVrijemeActivity.RADAR_MUNJE;
			} else if (title.equals(c.getResources().getString(R.string.slo_al_danas_925_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_WIND925, date, 925, 12, "0000");
			} else if (title.equals(c.getResources().getString(R.string.slo_al_sutra_925_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_WIND925, date, 925, 36, "0000");
			} else if (title.equals(c.getResources().getString(R.string.slo_al_prekosutra_925_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_WIND925, date, 925, 60, "0000");
			} else if (title.equals(c.getResources().getString(R.string.slo_al_danas_850_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_WIND850, date, 850, 12, "0000");
			} else if (title.equals(c.getResources().getString(R.string.slo_al_sutra_850_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_WIND850, date, 850, 36, "0000");
			} else if (title.equals(c.getResources().getString(R.string.slo_al_prekosutra_850_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_WIND850, date, 850, 60, "0000");
			} else if (title.equals(c.getResources().getString(R.string.slo_al_danas_naoblaka_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_CLOUD, date, 12, "0000");
			} else if (title.equals(c.getResources().getString(R.string.slo_al_sutra_naoblaka_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_CLOUD, date, 36, "0000");
			} else if (title.equals(c.getResources().getString(R.string.slo_al_prekosutra_naoblaka_12z))) {
				String date = TrenutnoVrijemeActivity.getAladin2Date();
				url = String.format(TrenutnoVrijemeActivity.ALADIN2_CLOUD, date, 60, "0000");
			} else if (title.equals(c.getResources().getString(R.string.webcam_jap))) {
				url = TrenutnoVrijemeActivity.JAP_WEBCAM_JAPETIC2;
			} else if (title.equals(c.getResources().getString(R.string.webcam_koka))) {
				url = TrenutnoVrijemeActivity.KOKA;
			} else if (title.equals(c.getResources().getString(R.string.webcam_sljeme))) {
				url = TrenutnoVrijemeActivity.WEBCAM_SLJEME;
			} else if (title.equals(c.getResources().getString(R.string.webcam_brnik))) {
				url = String.format(TrenutnoVrijemeActivity.WEBCAM_ARSO, "LJUBL-ANA_BRNIK", "nw");
			} else if (title.equals(c.getResources().getString(R.string.webcam_lisca))) {
				url = String.format(TrenutnoVrijemeActivity.WEBCAM_ARSO, "LISCA", "w");
			} else if (title.equals(c.getResources().getString(R.string.webcam_kredarica))) {
				url = String.format(TrenutnoVrijemeActivity.WEBCAM_ARSO, "KREDA-ICA", "se");
			} else if (title.equals(c.getResources().getString(R.string.webcam_bovec))) {
				url = String.format(TrenutnoVrijemeActivity.WEBCAM_ARSO, "BOVEC", "w");
			} else if (title.equals(c.getResources().getString(R.string.webcam_murska_sobota))) {
				url = String.format(TrenutnoVrijemeActivity.WEBCAM_ARSO, "MURSK-SOB", "nw");
			} else if (title.equals(c.getResources().getString(R.string.webcam_koper))) {
				url = String.format(TrenutnoVrijemeActivity.WEBCAM_ARSO, "KOPER_MARKOVEC", "e");
			} else if (title.equals(c.getResources().getString(R.string.dhmz_danas))) {
				url = String.format(TrenutnoVrijemeActivity.DHMZ_DANAS_SUTRA, "danas");
			} else if (title.equals(c.getResources().getString(R.string.dhmz_sutra))) {
				url = String.format(TrenutnoVrijemeActivity.DHMZ_DANAS_SUTRA, "sutra");
			} else if (title.equals(c.getResources().getString(R.string.dhmz_1))) {
				url = String.format(TrenutnoVrijemeActivity.DHMZ_4d, 2);
			} else if (title.equals(c.getResources().getString(R.string.dhmz_2))) {
				url = String.format(TrenutnoVrijemeActivity.DHMZ_4d, 3);
			} else if (title.equals(c.getResources().getString(R.string.dhmz_3))) {
				url = String.format(TrenutnoVrijemeActivity.DHMZ_4d, 4);
			} else if (title.equals(c.getResources().getString(R.string.modis_terra))) {
				url = String.format(TrenutnoVrijemeActivity.MODIS_TERRA, TrenutnoVrijemeActivity.getYearDay());
			} else if (title.equals(c.getResources().getString(R.string.modis_aqua))) {
				url = String.format(TrenutnoVrijemeActivity.MODIS_AQUA, TrenutnoVrijemeActivity.getYearDay());
			} else if (title.equals(c.getResources().getString(R.string.modis_slika_dana))) {
				url = String.format(TrenutnoVrijemeActivity.MODIS_IOTD, TrenutnoVrijemeActivity.getCurrentDateNasaString());
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
		return url;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			if (intent == null) {
				Log.i(TAG, "Intent null.");
				return;
			}

			// prepare alarms
			AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
			Intent serviceIntent = new Intent(getApplicationContext(), UpdateWidgetService.class);
			PendingIntent pendingIntentAlarm = null;
			
			boolean ignoreCache = false;
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
			int[] allWidgetIds = null;
			String command = null;
			if (intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)) {
				allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
				int widget_interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("widget_interval", "3"));
				if (widget_interval < 30) {
					Log.i(TAG, "Setting an alarm to wakeup this service in minutes: " + widget_interval);
					serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
					pendingIntentAlarm = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
					alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + widget_interval * 1000 * 60, pendingIntentAlarm);
				}
			} else {
				int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
				allWidgetIds = new int[1];
				allWidgetIds[0] = widgetId;
				ignoreCache = true;
				command = intent.getStringExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
			}

			Log.w(TAG, "allWidgetIds:" + String.valueOf(allWidgetIds.length));
			for (int widgetId : allWidgetIds) {

				String title = loadTitlePref(this, widgetId);
				RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_main);
				// Register an onClickListener for show buttons
				Intent clickIntent = new Intent(UpdateWidgetService.this.getApplicationContext(), TVWidgetProvider.class);
				clickIntent.setAction(TVWidgetProvider.SHOW_BUTTONS);
				clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
				Uri data = Uri.withAppendedPath(Uri.parse("tvw://widget/id/"), String.valueOf(widgetId));
				clickIntent.setData(data);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.widget_root, pendingIntent);

				// Register an onClickListener for show update button
				clickIntent = new Intent(UpdateWidgetService.this.getApplicationContext(), TVWidgetProvider.class);
				clickIntent.setAction(TVWidgetProvider.UPDATE_ONE_WIDGET);
				clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
				data = Uri.withAppendedPath(Uri.parse("tvw://widget/id/"), String.valueOf(widgetId));
				clickIntent.setData(data);
				pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.refresh, pendingIntent);

				String url = getURL4Title(this, title);

				// Register an onClickListener for show full screen button
				clickIntent = new Intent(UpdateWidgetService.this.getApplicationContext(), TVWidgetProvider.class);
				clickIntent.setAction(TVWidgetProvider.FULL_SCREEN_VIEW);
				clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
				data = Uri.withAppendedPath(Uri.parse("tvw://widget/id/"), String.valueOf(widgetId));
				clickIntent.setData(data);
				pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.view_full_screen, pendingIntent);

				remoteViews.setTextViewText(R.id.title1, title);
				if (command == null || command.equals(TVWidgetProvider.UPDATE_ONE_WIDGET)) {
					remoteViews.setViewVisibility(R.id.action_buttons, View.GONE);
					saveActionButtonsVisibilityPref(getApplicationContext(), widgetId, false);
					remoteViews.setViewVisibility(R.id.status, View.VISIBLE);

					Log.w(TAG, "Updating widget:" + widgetId + " preference:" + title);
					remoteViews.setTextViewText(R.id.status, getResources().getString(R.string.osvje_avanje_u_tijeku_));
					if (url == null) {
						remoteViews.setTextViewText(R.id.status, "Ne prepoznajem više ovaj izvor: '" + title + "'. Molim uklonite ovaj widget i dodajte novi.");
						appWidgetManager.updateAppWidget(widgetId, remoteViews);
					} else {
						appWidgetManager.updateAppWidget(widgetId, remoteViews);
						
						serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
						pendingIntentAlarm = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						
						//in case process dies
						alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000 * 60, pendingIntentAlarm);

						WidgetWorker.run(appWidgetManager, widgetId, remoteViews, url, title, ignoreCache);
						Log.w(TAG, "Hard work done!");

						// no death so reset alarm for a day
						alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000 * Utils.DAY, pendingIntentAlarm);
						/*
						 * WidgetWorker worker = new WidgetWorker(); worker.url
						 * = url; worker.title = title; worker.appWidgetManager
						 * = appWidgetManager; worker.appWidgetId = widgetId;
						 * worker.remoteViews = remoteViews; worker.ignoreCache
						 * = ignoreCache; Utils.executor.execute(worker);
						 */
					}
				} else if (command.equals(TVWidgetProvider.SHOW_BUTTONS)) {
					boolean visible = loadActionButtonsVisibilityPref(getApplicationContext(), widgetId);
					if (visible) {
						remoteViews.setViewVisibility(R.id.action_buttons, View.GONE);
					} else {
						remoteViews.setViewVisibility(R.id.action_buttons, View.VISIBLE);
					}
					saveActionButtonsVisibilityPref(getApplicationContext(), widgetId, !visible);
					appWidgetManager.updateAppWidget(widgetId, remoteViews);
				} else if (command.equals(TVWidgetProvider.FULL_SCREEN_VIEW)) {
					remoteViews.setViewVisibility(R.id.action_buttons, View.GONE);
					saveActionButtonsVisibilityPref(getApplicationContext(), widgetId, false);
					appWidgetManager.updateAppWidget(widgetId, remoteViews);
					clickIntent = new Intent(UpdateWidgetService.this.getApplicationContext(), PhotoFullScreen.class);
					clickIntent.setAction(Intent.ACTION_VIEW);
					clickIntent.putExtra("title", title);
					clickIntent.putExtra("remoteURI", url);
					data = Uri.withAppendedPath(Uri.parse("tvw://widget/id/"), String.valueOf(widgetId));
					clickIntent.setData(data);
					clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(clickIntent);
				}

			}

		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

}
