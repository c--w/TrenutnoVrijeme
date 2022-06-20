package hr.cloudwalk.currweather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapterWidget extends BaseAdapter {
	private static final String TAG = "TVW ImageAdapterWidget";
	private Activity context;
	String[] titles = null;

	public ImageAdapterWidget(Activity context, String[] titles) {
		this.context = context;
		this.titles = titles;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view;

		if (convertView == null) {
			view = new View(context);
			view = inflater.inflate(R.layout.grid_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.grid_item_label);
			holder.imageView = (ImageView) view.findViewById(R.id.grid_item_image);
			Display display = context.getWindowManager().getDefaultDisplay();
			int w = display.getWidth();
			int h = display.getHeight();
			if (h > w)
				holder.imageView.setLayoutParams(new FrameLayout.LayoutParams(w / 4, w / 4));
			else
				holder.imageView.setLayoutParams(new FrameLayout.LayoutParams(w / 6, w / 6));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			view = convertView;
		}
		try {

		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
		String title = (String) titles[position];
		holder.title.setText(title);
		Bitmap b = Utils.getThumbnail4Id(title);
		if (b != null)
			holder.imageView.setImageBitmap(b);
		else
			holder.imageView.setImageDrawable((context.getResources().getDrawable(R.drawable.default200)));

		return view;
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder {
		TextView title;
		ImageView imageView;
	}

}