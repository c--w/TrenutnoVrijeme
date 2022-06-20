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

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A list view example with separators.
 */
public class FavoritesActivity extends BaseActivity {
	final static String TAG = "TVW FavoritesActivity";
	Set<Integer> menuIdsSet = null;;
	ImageAdapter mAdapter = null;

	Object[] titles = null;

	@Override
	protected void onResume() {
		super.onResume();
		try {
			String usageString = PreferenceManager.getDefaultSharedPreferences(this).getString("usageStat", "{}");
			JSONObject usage = new JSONObject(usageString);
			LinkedHashMap<String, Integer> sortedMap = getSortedMap(usage);
			titles = sortedMap.keySet().toArray();

			GridView gridView = (GridView) findViewById(R.id.gridView1);
			mAdapter = new ImageAdapter(this, titles, sortedMap);
			gridView.setAdapter(mAdapter);

			gridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					String title = (String) titles[position];
					Intent intent = new Intent(FavoritesActivity.this, TrenutnoVrijemeActivity.class);
					intent.putExtra("title", title);
					startActivity(intent);
				}
			});
			gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
					String title = (String) titles[position];
					String usageString = PreferenceManager.getDefaultSharedPreferences(FavoritesActivity.this).getString("usageStat", "{}");

					JSONObject usage;
					try {
						usage = new JSONObject(usageString);
						usage.remove(title);
						PreferenceManager.getDefaultSharedPreferences(FavoritesActivity.this).edit().putString("usageStat", usage.toString())
								.commit();
						LinkedHashMap<String, Integer> sortedMap = getSortedMap(usage);
						titles = sortedMap.keySet().toArray();
						GridView gridView = (GridView) findViewById(R.id.gridView1);
						gridView.setAdapter(new ImageAdapter(FavoritesActivity.this, titles, sortedMap));
					} catch (JSONException e) {
						Log.e(TAG, e.toString(), e);
					}
					return true;
				}
			});
			
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	};

	@TargetApi(14)
	boolean checkV14hasPermanentMenuKey() {
		return ViewConfiguration.get(this).hasPermanentMenuKey();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			String usageString = PreferenceManager.getDefaultSharedPreferences(this).getString("usageStat", "{}");
			if (usageString.equals("{}")) {
				Intent intent = new Intent(FavoritesActivity.this, TrenutnoVrijemeActivity.class);
				startActivity(intent);
			} else if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("useFavorite", false)) {
				Intent intent = new Intent(FavoritesActivity.this, TrenutnoVrijemeActivity.class);
				JSONObject usage = new JSONObject(usageString);
				LinkedHashMap<String, Integer> sortedMap = getSortedMap(usage);
				final Object[] titles = sortedMap.keySet().toArray();
				String title = (String) titles[0];
				intent.putExtra("title", title);
				startActivity(intent);
			}
			setContentView(R.layout.grid);
//			if(Build.VERSION.SDK_INT <= 10 || (Build.VERSION.SDK_INT >= 14 && checkV14hasPermanentMenuKey())) {
//				findViewById(R.id.menuButton).setVisibility(View.GONE);
//			}
				findViewById(R.id.menuButton).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						openOptionsMenu();
					}
				});

		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		menuIdsSet = new HashSet<Integer>(menuIds);
		Integer itemId = item.getItemId();
		switch (itemId) {
		case R.id.settings:
			Intent launchPreferencesIntent = new Intent().setClass(this, Preferences.class);
			startActivity(launchPreferencesIntent);
			break;
		case R.id.help:
			Utils.showHelp(this);
			break;
		case R.id.ocijeni:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=hr.cloudwalk.currweather")));
			break;
		default:
			if (menuIdsSet.contains(itemId)) {
				try {
					String title = (String) item.getTitle();
					Intent intent = new Intent(FavoritesActivity.this, TrenutnoVrijemeActivity.class);
					intent.putExtra("title", title);
					startActivity(intent);
				} catch (Exception e) {
					Log.e(TAG, e.toString(), e);
				}
			} 
			break;
		}

		return true;
	}

	private LinkedHashMap<String, Integer> getSortedMap(JSONObject usage) throws JSONException {
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator it = usage.keys(); it.hasNext();) {
			String key = (String) it.next();
			int count = usage.getInt(key);
			sortedMap.put(key, count);
		}
		return (LinkedHashMap<String, Integer>) sortByValue(sortedMap);
	}

	<K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return -(o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
