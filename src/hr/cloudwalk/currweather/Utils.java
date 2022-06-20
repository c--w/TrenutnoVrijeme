package hr.cloudwalk.currweather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.cert.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Utils {
	final static String TAG = "TVW Utils";
	static String IMAGE_CACHE_DIR;
	static String TB_CACHE_DIR;
	final static int CACHE_DURATION = 7;
	final static long DAY = 1000 * 60 * 60 * 24;
	final static Object mutex = new Object();
	public static LinkedHashMap<String, SoftReference<Bitmap>> mMemoryCache = new MyLinkedHashMap<String, SoftReference<Bitmap>>(100);

	static {
		trustAllHosts();
	}

	public static void initDirs(Context ctx) {
		try {
			Log.d(TAG, "initDirs");
			IMAGE_CACHE_DIR = ctx.getExternalCacheDir().getAbsolutePath()  + "/.pgvrijeme-cache/";
			TB_CACHE_DIR = ctx.getExternalCacheDir().getAbsolutePath() + "/.pgvrijeme-thumbnails/";
			Log.d(TAG, IMAGE_CACHE_DIR);
			File dir = new File(IMAGE_CACHE_DIR);
			if (!dir.exists())
				dir.mkdirs();
			dir = new File(TB_CACHE_DIR);
			if (!dir.exists())
				dir.mkdirs();
			clearOldImageCache(CACHE_DURATION);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	public static long getImageCacheSize() {
		File f = new File(IMAGE_CACHE_DIR);
		File[] files = f.listFiles();
		long length = 0;
		for (File file : files) {
			length += file.length();
		}
		return length;
	}

	public static void clearOldImageCache(final int days) {
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(35000);
				} catch (InterruptedException e) {
				}
				try {
					File dir = new File(IMAGE_CACHE_DIR);
					if (!dir.exists())
						dir.mkdirs();
					File[] files = dir.listFiles();
					Date current = new Date();
					int i = 0;
					for (File file : files) {
						if (current.getTime() - file.lastModified() > days * DAY) {
							boolean deleted = file.delete();
							if (deleted)
								Log.d(TAG, "Deleted file: " + file.getName());
							else
								Log.d(TAG, "File not deleted!");
						}
						if (i++ % 10 == 0)
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
							}
					}
				} catch (Exception e) {
				}
				return (Void) null;
			}

		}).execute((Void) null);
	}

	public static String getImageCachePath() {
		return IMAGE_CACHE_DIR;
	}

	public static Bitmap getThumbnail4Id(String title) {
		try {
			String localURI = TB_CACHE_DIR + title.hashCode() + ".png";
			Bitmap b = null;
			if (mMemoryCache.containsKey(localURI)) {
				SoftReference<Bitmap> drawableRef = mMemoryCache.get(localURI);
				if (drawableRef != null && (b = drawableRef.get()) != null)
					return b;
				else
					Log.d(TAG, "Reference empty");
			}
			File f = new File(localURI);
			if (f.exists()) {
				Log.d(TAG, "Loading from disk: " + title);
				BitmapFactory.Options options = new BitmapFactory.Options();
				// options.inDensity = DisplayMetrics.DENSITY_LOW;
				b = BitmapFactory.decodeFile(localURI);
				if (b != null) {
					SoftReference<Bitmap> bitmapRef = new SoftReference<Bitmap>(b);
					mMemoryCache.put(localURI, bitmapRef);
				}
				return b;
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
		return null;
	}

	public static String generateLocalURI(String remoteURI) {
		long divisor;
		if (remoteURI.contains("webcam_archive")
				|| (remoteURI.contains("http://meteo.arso.gov.si/uploads/probase/www/observ/webcam") && !remoteURI.contains("latest")))
			divisor = (1000 * 60 * 60 * 12);
		else if (remoteURI.matches(".*koka.*|.*japetic.*|.*zilion.*|.*webcam.*|.*pljusak.*|.*event.*|.*dropboxusercontent.*"))
			divisor = (1000 * 60 * 5);
		else if (remoteURI.matches(".*radar.*|.*warning_hp.*|.*blitzortung.*"))
			divisor = (1000 * 60 * 15);
		else if (remoteURI.matches(".*202.*|.*wrf_arw_letaci.*|.*neighbours.*"))
			divisor = (1000 * 60 * 60 * 12);
		else if (remoteURI.matches(".*gsfc.*"))
			divisor = (1000 * 60 * 60 * 24 * 365 * 2);
		else if (remoteURI.contains("arhiva/webcamimage"))
			divisor = (1000 * 60 * 60 * 24 * 365 * 1);
		else
			divisor = (1000 * 60 * 60 * 24);
		String s = remoteURI + (System.currentTimeMillis() / divisor);
		return Utils.getImageCachePath() + s.hashCode() + ".jpg";
	}

	public static String loadBitmapFromUrlReturnString(String remoteUrl, Handler h, boolean ignoreCache, String title) throws Exception {
		String localURI = generateLocalURI(remoteUrl);
		Bitmap b = loadBitmapFromUrl(remoteUrl, h, ignoreCache, title);
		if (b != null)
			return localURI;
		return null;
	}

	public static Uri loadBitmapFromUrlReturnUri(String remoteUrl, Handler h, boolean ignoreCache, String title) throws Exception {
		String localURI = generateLocalURI(remoteUrl);
		Bitmap b = loadBitmapFromUrl(remoteUrl, h, ignoreCache, title);
		if (b != null)
			return Uri.fromFile(new File(localURI));
		return null;
	}

	public static Bitmap loadLocalBitmap(String localURI) throws Exception {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inDensity = DisplayMetrics.DENSITY_LOW;
		Bitmap b = BitmapFactory.decodeFile(localURI, options);
		return b;
	}

	public static void pingURL(String remoteUrl) {
	URL request;
	try {
		Log.i(TAG, remoteUrl);
		request = new URL(remoteUrl);
		request.openConnection();
		request.getContent();
	} catch (Exception e) {
		Log.e(TAG, e.getMessage(), e);
	}

}
	
	public static Bitmap loadBitmapFromUrl(String remoteUrl, Handler h, boolean ignoreCache, String title) throws Exception {
		String localURI = generateLocalURI(remoteUrl);
		File local = new File(localURI);
		Bitmap b = null;
		Bundle bundle = new Bundle();
		BitmapFactory.Options options = new BitmapFactory.Options();
		//options.inDensity = DisplayMetrics.DENSITY_LOW;
		long total = 0;
		boolean loadingFromWeb = ignoreCache || !local.exists();
		try {
			if (loadingFromWeb) {
				localURI = generateLocalURI(remoteUrl);
				Log.i(TAG, "loading image from web: " + remoteUrl);
				// send start
				Message msg = new Message();
				bundle.putLong("total", 0);
				msg.what = 0;
				msg.setData(bundle);
				if (h != null)
					h.sendMessage(msg);

				// load remotely
				URL request = new URL(remoteUrl);
				URLConnection  urlConnection = request.openConnection();
				urlConnection.setConnectTimeout(10000);
				InputStream is = (InputStream) urlConnection.getContent();
				//InputStream is = (InputStream) request.getContent();
				FileOutputStream fos = new FileOutputStream(localURI);
				byte[] buffer = new byte[4096];
				total = 0;
				int l;
				while ((l = is.read(buffer)) != -1) {
					total += l;
					bundle.putLong("total", total);
					msg = new Message();
					msg.what = 0;
					msg.setData(bundle);
					if (h != null)
						h.sendMessage(msg);
					fos.write(buffer, 0, l);
				}
				is.close();
				fos.flush();
				fos.close();
				b = BitmapFactory.decodeFile(localURI, options);
				if (b != null) {
					// crop
					if (remoteUrl.contains("region=")) {
						if (remoteUrl.contains("region=ba"))
							b = Bitmap.createBitmap(b, 0, 0, 640, 400);
						else if (remoteUrl.contains("region=gr"))
							b = Bitmap.createBitmap(b, 0, 0, 480, 350);
					} else if (remoteUrl.contains("AERONET_Venise")) {
						b = Bitmap.createBitmap(b, 400, 180, 560, 500);
					} else if ((remoteUrl.contains("bradar") || remoteUrl.contains("oradar") || remoteUrl.contains("kradar") || remoteUrl.contains("pradar")
							|| remoteUrl.contains("web_CAP_Z_020") || remoteUrl.contains("kweb_SRI_R"))) {
						b = Bitmap.createBitmap(b, 0, 0, 480, 480);
					} else if (remoteUrl.contains("/radar.gif")) {
						b = Bitmap.createBitmap(b, 120, 50, 690, 600);
					} else if (remoteUrl.contains("www.arso.gov.si/vreme/napovedi%20in%20podatki/aladin/")) {
						b = Bitmap.createBitmap(b, 0, 0, 585, 567);
					} else if (remoteUrl.contains("blitzortung")) {
						b = Bitmap.createBitmap(b, 0, 12, 410, 360);
					} else if (remoteUrl.contains("blipmapper")) {
						b = Bitmap.createBitmap(b, 840, 600, 360, 340);
					} else if (remoteUrl.contains("gemglb")) {
						b = Bitmap.createBitmap(b, 0, 211, 521, 321);
					} else if (remoteUrl.contains("dobrinj")) {
						b = Bitmap.createBitmap(b, 0, 50, 1280, 400);
					} else if(remoteUrl.contains("33182/weather/w")) {
						Bitmap newBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Config.RGB_565); 
						Canvas canvas = new Canvas(newBitmap);
						canvas.drawColor(Color.WHITE);
						canvas.drawBitmap(b, 0,  0, null);
						b = newBitmap;
					}
					// b.setDensity(DisplayMetrics.DENSITY_LOW);
					// overwrite with cropped
					FileOutputStream out = new FileOutputStream(localURI);

					// prevent conflicts
					synchronized (mutex) {
						b.compress(Bitmap.CompressFormat.PNG, 90, out);
						out.close();
					}
					SoftReference<Bitmap> bitmapRef = new SoftReference<Bitmap>(b);
					mMemoryCache.put(localURI, bitmapRef);
				} else {
					Log.i(TAG, "decoding failed after download:" + remoteUrl + " file content as string: " + new String(buffer));
				}
			} else {
				if (mMemoryCache.containsKey(localURI)) {
					SoftReference<Bitmap> drawableRef = mMemoryCache.get(localURI);
					if (drawableRef != null)
						b = drawableRef.get();
				}
				if (b == null || b.isRecycled()) {
					b = BitmapFactory.decodeFile(localURI, options);
					SoftReference<Bitmap> bitmapRef = new SoftReference<Bitmap>(b);
					mMemoryCache.put(localURI, bitmapRef);
				}
				Log.i(TAG, "loaded from cache:" + remoteUrl);
			}
			// generate & save thumbnail
			createThumbnail(b, remoteUrl, title, loadingFromWeb);
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
		if (h != null) {
			Message msg = new Message();
			bundle.putLong("total", total);
			msg.setData(bundle);
			if (b == null) {
				msg.what = 2;
			} else {
				msg.what = 1;
			}
			h.sendMessageDelayed(msg, 100);
		}
		return b;
	}

	public static void createThumbnail(Bitmap b, String url, String title, boolean loadingFromWeb) throws IOException {
		String thumbURI = TB_CACHE_DIR + title.hashCode() + ".png";
		File f = new File(thumbURI);
		if (b != null && (!f.exists() || loadingFromWeb)) {
			Log.i(TAG, "Updating thumbnail:" + url + " Title:" + title);
			float aspekt = (1f * b.getWidth()) / b.getHeight();
			int thumbWidth = 0;
			if (TrenutnoVrijemeActivity.outMetrics.widthPixels != 0) {
				thumbWidth = Math.min(TrenutnoVrijemeActivity.outMetrics.widthPixels, TrenutnoVrijemeActivity.outMetrics.heightPixels);
			} else {
				thumbWidth = UpdateWidgetService.minScreenDimension;
			}
			int width = thumbWidth / 4;
			int height = (int) (width / aspekt);
			Bitmap tempBitmap = Bitmap.createScaledBitmap(b, width, height, true);
			Bitmap thumbBitmap = tempBitmap;
			if (height > width) {
				thumbBitmap = Bitmap.createBitmap(tempBitmap, 0, 0, width, width);
				if(tempBitmap != b)
					tempBitmap.recycle();
			}
			SoftReference<Bitmap> bitmapRef = new SoftReference<Bitmap>(thumbBitmap);
			mMemoryCache.put(thumbURI, bitmapRef);
			FileOutputStream out = new FileOutputStream(thumbURI);

			// prevent conflicts
			synchronized (mutex) {
				thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
				out.close();
			}
		}

	}

	public static void showHelp(Activity a) {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;
		LayoutInflater inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.welcome_dialog, (ViewGroup) a.findViewById(R.id.welcome_message));

		builder = new AlertDialog.Builder(a);
		builder.setView(layout);

		builder.setTitle(R.string.help);
		builder.setCancelable(true);
		builder.setPositiveButton("OK", null);
		alertDialog = builder.create();

		alertDialog.show();

	}

	public static ExecutorService executor;

	static {
		executor = Executors.newSingleThreadExecutor();
	}

	public static String getContentResult(URL url) throws IOException {

		InputStream in = url.openStream();
		StringBuffer sb = new StringBuffer();

		byte[] buffer = new byte[256];

		while (true) {
			int byteRead = in.read(buffer);
			if (byteRead == -1)
				break;
			for (int i = 0; i < byteRead; i++) {
				sb.append((char) buffer[i]);
			}
		}
		return sb.toString();
	}

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			private KeyStore trustStore;

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
										   String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
										   String authType) throws CertificateException {
				try {
					X509Certificate[] reorderedChain = reorderCertificateChain(chain);
					CertPathValidator validator = CertPathValidator.getInstance("PKIX");
					CertificateFactory factory = CertificateFactory.getInstance("X509");
					CertPath certPath = factory.generateCertPath(Arrays.asList(reorderedChain));
					PKIXParameters params = new PKIXParameters(trustStore);
					params.setRevocationEnabled(false);
					validator.validate(certPath, params);
				} catch (Exception e) {
					if(e instanceof CertificateException)
						throw new CertificateException("Certificate not valid or trusted.");
				}
			}
			private X509Certificate findSigner(X509Certificate signedCert, List<X509Certificate> certificates) {
				X509Certificate signer = null;

				for(X509Certificate cert : certificates) {
					Principal certSubjectDN = cert.getSubjectDN();
					Principal issuerDN = signedCert.getIssuerDN();
					if(certSubjectDN.equals(issuerDN)) {
						signer = cert;
						break;
					}
				}

				return signer;
			}

			private X509Certificate findRootCert(List<X509Certificate> certificates) {
				X509Certificate rootCert = null;

				for(X509Certificate cert : certificates) {
					X509Certificate signer = findSigner(cert, certificates);
					if(signer == null || signer.equals(cert)) { // no signer present, or self-signed
						rootCert = cert;
						break;
					}
				}

				return rootCert;
			}
			private X509Certificate findSignedCert(X509Certificate signingCert, List<X509Certificate> certificates) {
				X509Certificate signed = null;

				for(X509Certificate cert : certificates) {
					Principal signingCertSubjectDN = signingCert.getSubjectDN();
					Principal certIssuerDN = cert.getIssuerDN();
					if(certIssuerDN.equals(signingCertSubjectDN) && !cert.equals(signingCert)) {
						signed = cert;
						break;
					}
				}

				return signed;
			}

			private X509Certificate[] reorderCertificateChain(X509Certificate[] chain) {

				X509Certificate[] reorderedChain = new X509Certificate[chain.length];
				List<X509Certificate> certificates = Arrays.asList(chain);

				int position = chain.length - 1;
				X509Certificate rootCert = findRootCert(certificates);
				reorderedChain[position] = rootCert;

				X509Certificate cert = rootCert;
				while((cert = findSignedCert(cert, certificates)) != null && position > 0) {
					reorderedChain[--position] = cert;
				}

				return reorderedChain;
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
