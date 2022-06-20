package hr.cloudwalk.currweather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoFullScreen extends Activity {
	final static String TAG = "TVW PhotoFullScreen";
	String remoteURI = null;
	String title = null;
	Bitmap b = null;;
	LoadPhotoAsyncTask asyncTask = null;

	class LoadPhotoAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(Void... params) {
			try {
				b = Utils.loadBitmapFromUrl(remoteURI, null, false, title);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			findViewById(R.id.progressBar1).setVisibility(View.GONE);
			if (b != null) {
				ImageView imageView = (ImageView) findViewById(R.id.photo);
				imageView.setImageBitmap(b);
			} else {
				// TODO
			}
		};

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_full_screen);

		ImageView imageView = (ImageView) findViewById(R.id.photo);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				return;
			}
		});
		try {
			TextView textView = (TextView) findViewById(R.id.title);
			title = getIntent().getStringExtra("title");
			textView.setText(title);
			if (getIntent().hasExtra("remoteURI")) {
				remoteURI = getIntent().getStringExtra("remoteURI");
				asyncTask = new LoadPhotoAsyncTask();
				asyncTask.execute((Void) null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
