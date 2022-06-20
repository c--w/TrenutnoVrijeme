/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hr.cloudwalk.currweather;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * A list view example with separators.
 */
public class TVWidgetConfigActivity extends Activity {
	final static String TAG = "TVW WidgetConfig";
	public static final String PREFS_NAME = "hr.cloudwalk.currweather.AppWidgetProvider";
	public static final String PREF_PREFIX_KEY = "prefix_";

	String[] titles = null;
	int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	static void saveTitlePref(Context context, int appWidgetId, String text) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
		prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
		prefs.commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			Log.d(TAG, "Enter");
			setResult(RESULT_CANCELED);
			Intent forWidgetIntent = getIntent();
			if (forWidgetIntent != null && forWidgetIntent.getExtras() != null) {
				appWidgetId = forWidgetIntent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
				if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
					setContentView(R.layout.grid2);
					GridView gridView = (GridView) findViewById(R.id.gridView1);
					titles = getResources().getStringArray(R.array.widgetOptions);
					gridView.setAdapter(new ImageAdapterWidget(this, titles));

					gridView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
							String title = titles[position];
							saveTitlePref(TVWidgetConfigActivity.this, appWidgetId, title);

							AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(TVWidgetConfigActivity.this.getApplicationContext());
							RemoteViews remoteViews = new RemoteViews(TVWidgetConfigActivity.this.getApplicationContext().getPackageName(),
									R.layout.widget_main);
							Log.w(TAG, "Creating widget:" + appWidgetId + " preference:" + title);
							String url = UpdateWidgetService.getURL4Title(TVWidgetConfigActivity.this, title);
							if(url == null) {
								Toast.makeText(TVWidgetConfigActivity.this, ":( Hm, ovo se baš i nije trebalo dogoditi. Ne prepoznajem ovaj izvor: '"+title+"'.", Toast.LENGTH_LONG).show();
								return;
							}

							// Register an onClickListener for show buttons
							Intent clickIntent = new Intent(TVWidgetConfigActivity.this.getApplicationContext(), TVWidgetProvider.class);
							clickIntent.setAction(TVWidgetProvider.SHOW_BUTTONS);
							clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
							Uri data = Uri.withAppendedPath(Uri.parse("tvw://widget/id/"), String.valueOf(appWidgetId));
							clickIntent.setData(data);
							PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
							remoteViews.setOnClickPendingIntent(R.id.widget_root, pendingIntent);

							// Register an onClickListener for show update button
							clickIntent = new Intent(TVWidgetConfigActivity.this.getApplicationContext(), TVWidgetProvider.class);
							clickIntent.setAction(TVWidgetProvider.FULL_SCREEN_VIEW);
							clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
							data = Uri.withAppendedPath(Uri.parse("tvw://widget/id/"), String.valueOf(appWidgetId));
							clickIntent.setData(data);
							pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
							remoteViews.setOnClickPendingIntent(R.id.refresh, pendingIntent);

							// Register an onClickListener for show full screen button
							clickIntent = new Intent(TVWidgetConfigActivity.this.getApplicationContext(), PhotoFullScreen.class);
							clickIntent.setAction(Intent.ACTION_VIEW);
							clickIntent.putExtra("title", title);
							clickIntent.putExtra("remoteURI", url);
							data = Uri.withAppendedPath(Uri.parse("tvw://widget/id/"), String.valueOf(appWidgetId));
							clickIntent.setData(data);
							pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
							remoteViews.setOnClickPendingIntent(R.id.view_full_screen, pendingIntent);

							remoteViews.setTextViewText(R.id.title1, title);
							remoteViews.setTextViewText(R.id.status, getResources().getString(R.string.osvje_avanje_u_tijeku_));
							remoteViews.setViewVisibility(R.id.status, View.VISIBLE);
							appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

							WidgetWorker worker = new WidgetWorker();
							worker.url = url;
							worker.title = title;
							worker.appWidgetManager = appWidgetManager;
							worker.appWidgetId = appWidgetId;
							worker.ignoreCache = false;
							worker.remoteViews = remoteViews;
							Utils.executor.execute(worker);

							Intent resultValue = new Intent();
							resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
							setResult(RESULT_OK, resultValue);
							finish();
						}
					});
				}
			}

		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}
}
