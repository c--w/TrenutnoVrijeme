package hr.cloudwalk.currweather;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class Preferences extends PreferenceActivity {

	AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return "" + (Utils.getImageCacheSize() / 1024) + " kb";
		}

		@Override
		protected void onPostExecute(String result) {
			Preference preference = findPreference("imageCache");
			preference.setSummary(result);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		String app_ver;
		try {
			app_ver = getApplicationContext().getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			setTitle("PG Vrijeme - v." + app_ver);
		} catch (PackageManager.NameNotFoundException e1) {
		}

		asyncTask.execute((Void) null);
		Preference preference = findPreference("imageCache");
		preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				// TODO Auto-generated method stub
				Utils.clearOldImageCache(0);
				arg0.setSummary("0 kb");
				return true;
			}
		});

		preference = findPreference("clearUsage");
		String usage = PreferenceManager.getDefaultSharedPreferences(Preferences.this).getString("usageStat", "{}");
		preference.setSummary(usage.length() + " bytes");
		preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference arg0) {
				// TODO Auto-generated method stub
				PreferenceManager.getDefaultSharedPreferences(Preferences.this).edit().putString("usageStat", "{}").commit();
				arg0.setSummary("0 bytes");
				return true;
			}
		});
	}

	@Override
	public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
		super.setPreferenceScreen(preferenceScreen);
	}

}
