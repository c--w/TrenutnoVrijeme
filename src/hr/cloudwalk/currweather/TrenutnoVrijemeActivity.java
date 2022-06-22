package hr.cloudwalk.currweather;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TrenutnoVrijemeActivity extends BaseActivity {
	final static String TAG = "TVW TrenutnoVrijeme";

	static String SAT24 = "https://en.sat24.com/image?region=%1$s&type=%2$s&timestamp=%3$s";
	static String KOKA = "http://feniks.hr/koka/snapshot.jpg";
	static String KOKA_ARCHIVE = "http://feniks.hr/koka/movie/snapshot%1$s-%2$04d.jpg";
	//static String KOKA_ARCHIVE = "http://feniks.hr/koka/movie/snapshot1004-0706.jpg";
	static String KOKA2 = "http://www.feniks.hr/koka/smjer.png";
	static String KOKA3 = "http://www.feniks.hr/koka/jacina.png";
	static String JAP_WEBCAM_JAPETIC1 = "http://japetic.dtdns.net/webcam/kam5/lastsnap.jpg";
	static String JAP_WEBCAM_JAPETIC2 = "http://japetic.dnsalias.com:33182/webcam/kam2/lastsnap.jpg";
	static String JAP_WEBCAM_SVJANA1 = "http://japetic.dtdns.net/webcam2/kam1/lastsnap.jpg";
	static String JAP_WEBCAM_SVJANA2 = "http://japetic.dtdns.net/webcam2/kam2/lastsnap.jpg";
	static String JAP_WEBCAM_SVJANA1_ARCHIVE = "http://japetic.dtdns.net/webcam2/kam1/svjana1-pic-%1$04d.jpg";
	static String JAP_WEBCAM_SVJANA2_ARCHIVE = "http://japetic.dtdns.net/webcam2/kam2/svjana2-pic-%1$04d.jpg";
	static String JAP_WEBCAM_JAP1_ARCHIVE = "http://japetic.dtdns.net/webcam/kam5/japetic5-pic-%1$04d.jpg";
	static String JAP_WEBCAM_JAP2_ARCHIVE = "http://japetic.dnsalias.com:33182/webcam/kam2/japetic2-%1$04d.jpg";
	static String JAP_WIND_SPEED = "http://japetic.dtdns.net/weather/wspeeddaycomp.png";
	static String JAP_WIND_DIR = "http://japetic.dtdns.net/weather/wdirday.png";
	// static String JAP_TEMP = "http://japetic.dnsalias.com:33182/weather/tempdaycomp.png";
	static String WEBCAM_SLJEME = "http://zilion.hr/vidikovac/slika.jpg";
	static String SLJEME_WIND_DIR = "http://pljusak.com/sljeme/WindDirection.gif";
	static String SLJEME_WIND = "http://pljusak.com/sljeme/WindSpeed.gif";
	static String NAJAVE = "http://hpgf.org/kalendar2.php";
	static String OLC_DANAS = "http://hpgf.org/olc.php";
	static String RASPADALICA = "http://hpgf.org/istramet.php?link=http://www.istramet.hr/postaja/buzet-majcani/";
	static String VOJAK = "http://hpgf.org/istramet.php?link=http://www.istramet.com/postaja/ucka-vojak/";
	static String WEBCAM_PLESO = "http://89.164.90.200/local/viewer/SlikeZaWeb/image.jpg";
	static String WEBCAM_TRNOVEC = "http://pljusak.com/trnovec1/webcam1.jpg";
	static String WEBCAM_SAMOBOR = "http://www.pljusak.com/samobor1/image2.jpg";
	static String WEBCAM_OGULIN = "http://radioklub-ogulin.hr/kamera/kula.jpg";
	// static String WEBCAM_ROC =
	// "http://www.istramet.com/roc/webcam/webcam.jpg	";
	//static String WEBCAM_TRIBALJ = "http://www.opcina-dobrinj.hr/dobrinj.jpg";
	static String WEBCAM_ROC = "http://www.istrastream.com/cam/roc/cam_1.jpg";
	static String WEBCAM_BUZET = "http://www.istrastream.com/cam/sveti_martin/cam_1.jpg";
	static String WEBCAM_HUM = "http://www.istrastream.com/cam/hum/cam_1.jpg";
	static String WEBCAM_VOJAK = "http://www.istramet.hr/webcam/vojak/webcam.jpg";
	static String WEBCAM_ARSO = "http://meteo.arso.gov.si/uploads/probase/www/observ/webcam/%1$s_dir/siwc_%1$s_%2$s_latest.jpg";
	static String WEBCAM_ARSO_HIST = "http://meteo.arso.gov.si/uploads/probase/www/observ/webcam/%1$s_dir/siwc_%2$s-%3$s_%1$s_%4$s.jpg";
	static String WEBCAM_TRIBALJ = "http://flumen.club/fotke/front.jpg?raw=1";
	static String WEBCAM_TRIBALJ2 = "http://flumen.club/fotke/left.jpg?raw=1";
	static String METEO_TRIBALJ ="http://hpgf.org/istramet.php?link=http://www.istramet.com/postaja/tribalj-pjk-flumen/";
	static String KOMPOZITNI_RADAR = "http://vrijeme.hr/kompozit-stat.png";
	static String KOMPOZITNI_RADAR_HIST = "http://vrijeme.hr/radar/kompozit_%1$02d00.png";
	static String BILOGORA_RADAR = "http://vrijeme.hr/bilogora-stat.png";
	static String BILOGORA_RADAR_HIST = "http://vrijeme.hr/radar/bilogora_%1$02d00.png";
	static String OSIJEK_RADAR = "http://vrijeme.hr/osijek-stat.png";
	static String OSIJEK_RADAR_HIST = "http://vrijeme.hr/radar/osijek_%1$02d00.png";
	static String PUNTIJARKA_RADAR = "http://vrijeme.hr/puntijarka-stat.png";
	static String PUNTIJARKA_RADAR_HIST = "http://vrijeme.hr/radar/puntijarka_%1$02d00.png";
	static String DHMZ_DANAS_SUTRA = "http://prognoza.hr/hr%1$s.jpg";
	static String DHMZ_DANAS = "http://hpgf.org/dhmz_danas.php";
	static String DHMZ_SUTRA = "http://hpgf.org/dhmz_sutra.php";
	static String DHMZ_DANAS_SUTRA_ZG = "http://hpgf.org/zg_danas_sutra.php";
	static String DHMZ_FRONTE = "http://prognoza.hr/web_fronte_sutra12.jpg";
	static String DHMZ_4d = "http://prognoza.hr/web_RH_d0%1$s.jpg";
	static String DHMZ_AKT = "http://hpgf.org/dhmz_akt.php";
	static String METEOADRIATIC_3d = "http://meteoadriatic.net/prognoza/tablice/%1$s.php";
	static String RADARSLO = "http://www.arso.gov.si/vreme/napovedi%20in%20podatki/radar.gif";
	static String RADAR_FVG = "http://www.meteo.fvg.it/radar/img/vmi/20160812/%2$04d.png";
	//static String RADAR_FVG = "http://www.osmer.fvg.it/radar/img/vmi/%1$s/%2$04d.gif";
	static String RADARSLOTUCA = "http://meteo.arso.gov.si/uploads/probase/www/warning/graphic/warning_hp_si_latest.jpg";
	static String RADAR_MUNJE = "http://pljusak.com/blitz/blitz_rh.php";
	static String RADAR_MUNJE2 = "http://blitzortung.net/Images/image_b_gr.png";
	static String GFS_MR850_THW = "http://wxmaps.org/pix/euro4.%1$shr.png";
	static String MODIS_TERRA_PREPARE = "https://lance.modaps.eosdis.nasa.gov/imagery/subsets/?project=aeronet&subset=Venise.%1$s.terra.1km";
	static String MODIS_TERRA = "https://lance.modaps.eosdis.nasa.gov/imagery/subsets/tmp/AERONET_Venise.%1$s.terra.1km.jpg";
	static String MODIS_AQUA_PREPARE = "https://lance.modaps.eosdis.nasa.gov/imagery/subsets/?project=aeronet&subset=Venise.%1$s.aqua.1km";
	static String MODIS_AQUA = "https://lance.modaps.eosdis.nasa.gov/imagery/subsets/tmp/AERONET_Venise.%1$s.aqua.1km.jpg";
	static String MODIS_IOTD = "https://modis.gsfc.nasa.gov/gallery/images/image%1$s_1km.jpg";
	static String MODIS_IOTD_URL = "https://modis.gsfc.nasa.gov/gallery/individual.php?db_date=%1$s";
	static String SKEW_T_CMC = "http://meteocentre.com/models/gemglb_eur_%1$s/skewt/skewt_Zagreb_%2$03d.png";
	//static String SKEW_T_PREP = "http://gamma.meteoadriatic.net/skewts/return.php?lat=%1$s&lon=%2$s&hh=%3$s";
	static String SKEW_T_PREP = "http://gamma.meteoadriatic.net/meteoadriatic/ma2022/skewts/return.php?lat=%1$s&lon=%2$s&hh=%3$s";
	//static String SKEW_T = "http://gamma.meteoadriatic.net/skewts/output/%1$s-%2$s-%3$s.png?city=%4$s";
	static String SKEW_T = "http://gamma.meteoadriatic.net/meteoadriatic/ma2022/skewts/output/%1$s-%2$s-%3$s.png";
	//static String METEOGRAMI3d_PREP = "http://gamma.meteoadriatic.net/meteograms/return.php?lat=%1$s&lon=%2$s";
	static String METEOGRAMI3d_PREP = "http://gamma.meteoadriatic.net/meteoadriatic/irb/arw/meteograms/return.php?lat=%1$s&lon=%2$s";
	//static String METEOGRAMI3d = "http://gamma.meteoadriatic.net/meteograms/output/%1$s-%2$s.png";
	static String METEOGRAMI3d = "http://gamma.meteoadriatic.net/meteoadriatic/irb/arw/meteograms/output/%1$s-%2$s.png";
	static String METEOGRAMI4d = "http://gamma.meteoadriatic.net/images/arw3/d01/meteograms/%1$s.png";
	static String ALADIN2_WIND925 = "https://meteo.arso.gov.si/uploads/probase/www/model/aladin/field/as_%1$s-%4$s_vf%2$s_si-neighbours_%3$03d.png";
	static String ALADIN2_WIND850 = "https://meteo.arso.gov.si/uploads/probase/www/model/aladin/field/as_%1$s-%4$s_r-t-vf%2$s_si-neighbours_%3$03d.png";
	static String ALADIN2_CLOUD = "https://meteo.arso.gov.si/uploads/probase/www/model/aladin/field/as_%1$s-%3$s_tcc-rr_si-neighbours_%2$03d.png";
	static String ALADIN2_TEMP2M = "https://meteo.arso.gov.si/uploads/probase/www/model/aladin/field/as_%1$s-%3$s_t2m_si-neighbours_%2$03d.png";
	static String ALADIN2_RADAR = "https://meteo.arso.gov.si/uploads/probase/www/observ/radar/si0_%1$s-%2$s_zm_si.jpg";
	static String ALADIN = "http://www.arso.gov.si/vreme/napovedi%20in%20podatki/aladin/";
	static String ALADIN_WIND = "AW00_veter_%1$s_%2$03d.png";
	static String ALADIN_CLOUD = "AW00_oblpad_%1$03d.png";
	static String ALADIN_HR = "http://prognoza.hr/aladinHR/web_uv10_HRv8_%1$02d.png";
	static String ALADIN_HR2 = "http://prognoza.hr/aladinHR/web_uv10_%1$s_%2$02d.png";
	static String ALADIN_HR3 = "http://prognoza.hr/aladinHR/web_6h_ob_uk_%1$02d.png";
	//static String NAOBLAKA_MA = "http://gamma.meteoadriatic.net/images/nmm3/d02/clouds/clouds_%1$02d.png";
	static String NAOBLAKA_MA = "http://gamma.meteoadriatic.net/meteoadriatic/irb/arw/S1/clouds/clouds_%1$03d.png";
	//static String KONDENZACIJA_MA = "http://gamma.meteoadriatic.net/images/nmm3/d02/lcl/lcl_%1$02d.png";
	static String KONDENZACIJA_MA = "http://gamma.meteoadriatic.net/meteoadriatic/irb/arw/S1/lcl/lcl_%1$03d.png";
	//static String CAPE_MA = "http://gamma.meteoadriatic.net/images/nmm3/d02/mlcape/mlcape_%1$02d.png";
	static String CAPE_MA = "http://gamma.meteoadriatic.net/meteoadriatic/irb/arw/S1/mlcape/mlcape_%1$03d.png";
	static String WIND_MI = "http://www.meteo-info.hr/wrf_arw_letaci/vjetar900_%1$03d.png";
	static String NAOBLAKA_MI = "http://www.meteo-info.hr/wrf_arw_letaci/naoblaka_%1$03d.png";
	static String KONDENZACIJA_MI = "http://www.meteo-info.hr/wrf_arw_letaci/conv_base_height_%1$03d.png";
	static String CAPE_MI = "http://www.meteo-info.hr/wrf_arw_letaci/cape_%1$03d.png";
	static String YRNO_ZG = "http://www.yr.no/place/Croatia/Grad_Zagreb/Zagreb/meteogram.png";
	static String YRNO_VZ = "http://www.yr.no/place/Croatia/Vara%C5%BEdin/Vara%C5%BEdin/meteogram.png";
	static String YRNO_PJ = "http://www.yr.no/place/Croatia/Lika-Senj/Plitvice_Lakes_National_Park/meteogram.png";
	static String YRNO_OS = "http://www.yr.no/place/Croatia/Osijek-Baranja/Osijek/meteogram.png";
	static String YRNO_RI = "http://www.yr.no/place/Croatia/Primorje-Gorski/Rijeka/meteogram.png";
	static String YRNO_ST = "http://www.yr.no/place/Croatia//Split-Dalmatia/Split/meteogram.png";
	static String METEOALARM = "http://www.meteoalarm.eu/map.php?iso=%1$s&data=%2$s";
	static String PELUD = "http://vrijeme.hr/bio/images/pelud%1$s.gif";
	static String UV = "http://prognoza.hr/uvi/uvi_maps_h.png";
	static String UGODA = "http://vrijeme.hr/bp_simboli.png";
	static String SLO_JUTRI_POJUTRI = "https://meteo.arso.gov.si/uploads/probase/www/fproduct/graphic/sl/fcast_si-neighbours_d%1$sh15.png";
	static String PLJUSAK = "https://pljusak.com/trenutno-vrijeme-tablica.php";
	static String ENS_15D = "http://www.wetterzentrale.de/pics/MS_%2$s%1$s_g05.png";
	static String ENS2_15D = "http://www.wetterzentrale.de/pics/MS_%2$s%1$s_ens.png";
	static String ARSO_VISINSKA_NAPOVED = "https://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/forecast_si-upperAir_latest.html";
	static String ARSO_NAPOVED_BESEDA = "http://www.meteo.si/uploads/probase/www/aviation/fproduct/text/sl/letala.wap";
	static String DELIM = ":::";
	static String[] ssMapping = { "nw", "n", "ne", "w", "", "e", "sw", "s", "se" };
	static Map<String, String> existingCameras;
	static Map<String, String> japCameras, japArchives, japArchiveNames;

	public static DisplayMetrics outMetrics = new DisplayMetrics();
	public Bitmap[] images = new Bitmap[3];
	public String[] titles = new String[3];
	public int touchWidth = 0;
	public int touchHeight = 0;
	public float touchX = 0;
	public float touchY = 0;
	Animation left2Right = null;
	Animation right2Left = null;
	Animation fadeIn = null;
	final static SimpleDateFormat nasaFormat = new SimpleDateFormat("MMddyyyy");
	final static SimpleDateFormat nasaFormatUrl = new SimpleDateFormat("yyyy-MM-dd");
	final static SimpleDateFormat aladinFormat = new SimpleDateFormat("yyyyMMdd");
	final static SimpleDateFormat japFormat = new SimpleDateFormat("yyMMdd");
	final static SimpleDateFormat kokaFormat = new SimpleDateFormat("ddMM");
	final static SimpleDateFormat peludFormat = new SimpleDateFormat("ddMMyyyy");
	final static SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
	final static SimpleDateFormat timeHourOnly = new SimpleDateFormat("HH00");
	final static SunriseSunset sunriseSunset = new SunriseSunset(46., 16., new Date(), 0.);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		try {
			Log.w(TAG, "SUNRIZE SUNSET: " + sunriseSunset.getSunrise() + " " + sunriseSunset.getSunset());
			PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
			getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

			usage = getUsage();
			left2Right = AnimationUtils.loadAnimation(TrenutnoVrijemeActivity.this, R.anim.left2right_enter);
			right2Left = AnimationUtils.loadAnimation(TrenutnoVrijemeActivity.this, R.anim.right2left_enter);
			fadeIn = AnimationUtils.loadAnimation(TrenutnoVrijemeActivity.this, R.anim.fade_in);
			setDefaultAnim4ImageViews();
			Utils.initDirs(this);
			String title = getResources().getString(R.string.sat24_bilogora_munje);
			if (getIntent() != null && getIntent().hasExtra("title")) {
				title = getIntent().getStringExtra("title");
			}
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("title", title);
			msg.setData(data);
			handlerIntent.sendMessageDelayed(msg, 200);

			ImageView img = (ImageView) findViewById(R.id.img1);
			img.setOnClickListener(onClickListener);
			img.setOnLongClickListener(onLongClickListener);
			img.setOnTouchListener(onTouchListener);
			setupCameraListeners(img);
			((View) img.getParent()).findViewById(R.id.shareimg).setOnClickListener(onClickListener2);
			((View) img.getParent()).findViewById(R.id.view_full_screen).setOnClickListener(onClickListener3);
//            ((View) img.getParent()).findViewById(R.id.view_menu).setOnClickListener(onClickListener4);
			img = (ImageView) findViewById(R.id.img2);
			img.setOnClickListener(onClickListener);
			img.setOnLongClickListener(onLongClickListener);
			img.setOnTouchListener(onTouchListener);
			setupCameraListeners(img);
			((View) img.getParent()).findViewById(R.id.shareimg).setOnClickListener(onClickListener2);
			((View) img.getParent()).findViewById(R.id.view_full_screen).setOnClickListener(onClickListener3);
//            ((View) img.getParent()).findViewById(R.id.view_menu).setOnClickListener(onClickListener4);
			img = (ImageView) findViewById(R.id.img3);
			img.setOnClickListener(onClickListener);
			img.setOnLongClickListener(onLongClickListener);
			img.setOnTouchListener(onTouchListener);
			setupCameraListeners(img);
			((View) img.getParent()).findViewById(R.id.shareimg).setOnClickListener(onClickListener2);
			((View) img.getParent()).findViewById(R.id.view_full_screen).setOnClickListener(onClickListener3);
//            ((View) img.getParent()).findViewById(R.id.view_menu).setOnClickListener(onClickListener4);

			WebView webView = (WebView) findViewById(R.id.web1);
			webView.setPictureListener(myPictureListener);
			webView = (WebView) findViewById(R.id.web2);
			webView.setPictureListener(myPictureListener);
			webView = (WebView) findViewById(R.id.web3);
			webView.setPictureListener(myPictureListener);
			checkFirst();
			existingCameras = new HashMap<String, String>();
			existingCameras.put("LJUBL", ",nw,n,ne,w,e,sw,s,se,");
			existingCameras.put("MARIBOR", ",nw,n,ne,w,e,sw,s,se,");
			existingCameras.put("PORTOROZ", ",nw,n,ne,w,e,sw,s,se,");
			existingCameras.put("BOVEC", ",nw,n,ne,w,e,sw,s,se,");
			existingCameras.put("BRNIK", ",nw,n,ne,w,e,sw,s,se,");
			existingCameras.put("KOPER", ",n,e,");
			existingCameras.put("KREDA", ",e,se,");
			existingCameras.put("MURSK", ",nw,sw,");
			existingCameras.put("LISCA", ",w,ne,se,");
			existingCameras.put("JAP", ",n,ne,s,");
			japCameras = new HashMap<String, String>();
			japCameras.put("n", JAP_WEBCAM_SVJANA2);
			japCameras.put("ne", JAP_WEBCAM_SVJANA1);
			japCameras.put("s", JAP_WEBCAM_JAPETIC1);
			//japCameras.put("se", JAP_WEBCAM_JAPETIC2);
			japArchives = new HashMap<String, String>();
			japArchives.put("n", JAP_WEBCAM_SVJANA2_ARCHIVE);
			japArchives.put("ne", JAP_WEBCAM_SVJANA1_ARCHIVE);
			japArchives.put("s", JAP_WEBCAM_JAP1_ARCHIVE);
			//japArchives.put("se", JAP_WEBCAM_JAP2_ARCHIVE);
			japArchiveNames = new HashMap<String, String>();
			japArchiveNames.put("svjana2-pic", "n");
			japArchiveNames.put("svjana1-pic", "ne");
			japArchiveNames.put("japetic5-pic", "s");
			//japArchiveNames.put("japetic2", "se");
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	private class MyPictureListener implements PictureListener {

		@Override
		public void onNewPicture(WebView view, Picture picture) {
			try {

				Bitmap b = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas c = new Canvas(b);
				picture.draw(c);
				try {
					Utils.createThumbnail(b, view.getOriginalUrl().toString(), (String) view.getTag(), true);
				} catch (Exception e) {
					Log.e(TAG, e.toString(), e);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	MyPictureListener myPictureListener = new MyPictureListener();

	public String selectedMenuItemTitle = "-";
	JSONObject usage = null;

	public void setMyTitle() {
		if (titles[0] != null && titles[0].trim().length() > 0) {
			((TextView) findViewById(R.id.title1)).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.title1)).setText(titles[0]);
		} else
			((TextView) findViewById(R.id.title1)).setVisibility(View.GONE);
		if (titles[1] != null && titles[1].trim().length() > 0) {
			((TextView) findViewById(R.id.title2)).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.title2)).setText(titles[1]);
		} else
			((TextView) findViewById(R.id.title2)).setVisibility(View.GONE);
		if (titles[2] != null && titles[2].trim().length() > 0) {
			((TextView) findViewById(R.id.title3)).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.title3)).setText(titles[2]);
		} else
			((TextView) findViewById(R.id.title3)).setVisibility(View.GONE);
	}

	private String getArsoPlace(String url) {
		String place = "";
		if (url.contains("BOVEC")) {
			place = "BOVEC";
		} else if (url.contains("LISCA")) {
			place = "LISCA";
		} else if (url.contains("BRNIK")) {
			place = "BRNIK";
		} else if (url.contains("KOPER")) {
			place = "KOPER";
		} else if (url.contains("KREDA")) {
			place = "KREDA";
		} else if (url.contains("MURSK")) {
			place = "MURSK";
		} else if (url.contains("LJUBL")) {
			place = "LJUBL";
		} else if (url.contains("MARIBOR")) {
			place = "MARIBOR";
		} else if (url.contains("PORTOROZ")) {
			place = "PORTOROZ";
		} else if (url.contains("dtdns")) {
			place = "JAP";
		}
		return place;
	}

	private boolean cameraExists(String place, String ss) {
		String directions = existingCameras.get(place);
		if (directions == null)
			return false;
		return directions.contains("," + ss + ",");
	}

	private void setupCameras(ImageView img) {
		try {
			FrameLayout frameLayout = (FrameLayout) ((View) img.getParent()).findViewById(R.id.camera_buttons);
			frameLayout.setVisibility(View.VISIBLE);
			String url = img.getTag().toString();
			String place = getArsoPlace(url);
			TextView textView = null;
			for (int i = 0; i < frameLayout.getChildCount(); i++) {
				textView = (TextView) frameLayout.getChildAt(i);
				if (cameraExists(place, textView.getText().toString())) {
					textView.setVisibility(View.VISIBLE);
				} else {
					textView.setVisibility(View.GONE);
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	private void setupCameraListeners(ImageView img) {
		try {
			FrameLayout frameLayout = (FrameLayout) ((View) img.getParent()).findViewById(R.id.camera_buttons);
			TextView textView = null;
			for (int i = 0; i < frameLayout.getChildCount(); i++) {
				textView = (TextView) frameLayout.getChildAt(i);
				textView.setOnClickListener(onClickListenerCameras);
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	class Loader implements Runnable {
		String url;
		String pingUrl;
		int what;
		private boolean ignoreCache = false;
		ImageView img;
		WebView webView;
		ProgressBar progressBar;
		String title;

		Handler progressHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				try {
					switch (msg.what) {
					case 0:
						long total = msg.getData().getLong("total");
						long imgLength = PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getLong(
								url.substring(0, url.length() - 7), 200000);
						if (imgLength == 0)
							imgLength = 200000;
						long progress = total * 1000 / imgLength;
						progressBar.setVisibility(View.VISIBLE);
						progressBar.setProgress((int) progress);
						break;
					case 1:
						total = msg.getData().getLong("total");
						if (total != 0)
							PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).edit()
									.putLong(url.substring(0, url.length() - 7), total).commit();
						img.setVisibility(View.VISIBLE);
						webView.setVisibility(View.GONE);
						Bitmap b = images[what];
						img.setImageBitmap(b);
						Animation anim = getImgAnimation(img);
						if (anim != null && PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getBoolean("animations", false))
							img.startAnimation(anim);
						progressBar.setVisibility(View.GONE);
						progressBar.setProgress(0);
						if (img.getTag().toString().contains("observ/webcam")
								|| (img.getTag().toString().contains("dtdns") && img.getTag().toString().contains("webcam"))) {
							setupCameras(img);
						} else {
							((View) img.getParent()).findViewById(R.id.camera_buttons).setVisibility(View.GONE);
						}
						break;
					case 2:
						img.setVisibility(View.VISIBLE);
						webView.setVisibility(View.GONE);
						img.setImageDrawable((getResources().getDrawable(R.drawable.error)));
						progressBar.setVisibility(View.GONE);
						progressBar.setProgress(0);
						// String error = msg.getData().getString("error");
						// Toast.makeText(TrenutnoVrijemeActivity.this,
						// "Greška: " + error, Toast.LENGTH_LONG).show();
						break;
					default:
						break;
					}
				} catch (Exception e) {
					Log.e(TAG, e.toString(), e);
				}
			}

		};

		Loader(String url, int what) {
			this.url = url;
			this.what = what;
			this.title = selectedMenuItemTitle;
			switch (what) {
			case 0:
				img = (ImageView) findViewById(R.id.img1);
				webView = (WebView) findViewById(R.id.web1);
				progressBar = (ProgressBar) findViewById(R.id.progress1);
				break;
			case 1:
				img = (ImageView) findViewById(R.id.img2);
				webView = (WebView) findViewById(R.id.web2);
				progressBar = (ProgressBar) findViewById(R.id.progress2);
				break;
			case 2:
				img = (ImageView) findViewById(R.id.img3);
				webView = (WebView) findViewById(R.id.web3);
				progressBar = (ProgressBar) findViewById(R.id.progress3);
				break;

			default:
				break;
			}
			try {
				progressBar.setProgress(0);
				img.setTag("" + what + url);
				ImageView imageView = (ImageView) ((View) img.getParent()).findViewById(R.id.shareimg);
				imageView.setTag(title + DELIM + url);
				imageView = (ImageView) ((View) img.getParent()).findViewById(R.id.view_full_screen);
				imageView.setTag(title + DELIM + url);
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}

		@Override
		public void run() {
			Bitmap b = null;
			try {
				if (pingUrl != null)
					Utils.pingURL(pingUrl);
				Thread.sleep(100);
				b = Utils.loadBitmapFromUrl(url, progressHandler, ignoreCache, title);
				images[what] = b;
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}

		public boolean isIgnoreCache() {
			return ignoreCache;
		}

		public void setIgnoreCache(boolean ignoreCache) {
			this.ignoreCache = ignoreCache;
		}

	}

	OnClickListener onClickListenerCameras = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				View img = null;
				FrameLayout frameLayout = (FrameLayout) v.getParent().getParent();
				for (int i = 0; i < frameLayout.getChildCount(); i++) {
					if (frameLayout.getChildAt(i) instanceof ImageView) {
						img = frameLayout.getChildAt(i);
						break;
					}
				}
				String dir = (String) ((TextView) v).getText();
				move(0, dir, img, false);
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}
	};

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				int i = 0;
				if (touchX / touchWidth < 0.36) {
					i = -1;
				} else if ((touchWidth - touchX) / touchWidth < 0.36) {
					i = 1;
				} else {
					View view = ((View) v.getParent()).findViewById(R.id.action_buttons);
					if (view.getVisibility() == View.GONE) {
						view.setVisibility(View.VISIBLE);
						view.startAnimation(fadeIn);
					} else {
						view.setVisibility(View.GONE);
					}
					return;
				}
				move(i, "", v, false);

			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}
	};

	OnLongClickListener onLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			try {
				int i = 0;
				if (touchX / touchWidth < 0.333) {
					i = -1;
				} else if ((touchWidth - touchX) / touchWidth < 0.333) {
					i = 1;
				} else {
					String url = ((String) v.getTag()).substring(1);
					v.setTag(R.id.ANIMATION, 0);
					int what = Integer.parseInt(((String) v.getTag()).substring(0, 1));
					Loader l = new Loader(url, what);
					l.setIgnoreCache(true);
					Utils.executor.execute(l);

					Toast.makeText(TrenutnoVrijemeActivity.this, "Dohvat najnovije slike...", Toast.LENGTH_SHORT).show();
					return true;
				}
				startStopMoveAnimator(v, i);
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
			return true;
		}
	};

	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			touchWidth = v.getWidth();
			touchHeight = v.getHeight();
			touchX = event.getX();
			touchY = event.getY();
			return false;

		}
	};

	private MyRunnable moveAnimator = new MyRunnable() {

		Handler moveHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				int result;
				try {
					result = move(direction, "", v, true);
					if (result == 0) {
						direction = -direction;
					}
				} catch (Exception e) {
					running = false;
					Log.e(TAG, e.toString(), e);
				}
			}
		};

		@Override
		public void run() {
			int interval = Integer
					.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("animation_interval", "1000"));
			running = true;
			finished = false;
			while (running) {
				try {
					Thread.sleep(interval);
					moveHandler.sendEmptyMessage(0);
				} catch (Exception e) {
					Log.e(TAG, e.toString(), e);
				}
			}
			finished = true;
		}
	};

	private abstract class MyRunnable implements Runnable {
		public boolean running = false;
		public boolean finished = true;
		public View v = null;
		int direction = 1;

		@Override
		public abstract void run();

	}

	private void startStopMoveAnimator(View v, int direction) {
		try {
			if (moveAnimator.running && moveAnimator.v == v) {
				moveAnimator.running = false;
				Toast.makeText(TrenutnoVrijemeActivity.this, "Zaustavljam animaciju...", Toast.LENGTH_SHORT).show();
				return;
			}
			int result = move(direction, "", v, true);
			if (result == -1)
				return;
			moveAnimator.running = false;
			Toast.makeText(TrenutnoVrijemeActivity.this, "Počinjem animaciju...", Toast.LENGTH_SHORT).show();
			if (result == 0) {
				direction = -direction;
			}
			while (!moveAnimator.finished) {
				Thread.sleep(10);
			}
			moveAnimator.direction = direction;
			moveAnimator.v = v;
			Thread t = new Thread(moveAnimator);
			t.start();
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	private int move(int i, String ss, View v, boolean loop) throws Exception {
		String url = ((String) v.getTag()).substring(1);
		int what = Integer.parseInt(((String) v.getTag()).substring(0, 1));
		boolean last = false;
		if (url.contains("cam/hum")) {
			int interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("roc_interval", "10"));
			int pos = Integer.parseInt(url.substring(url.lastIndexOf("archive") + "archive".length(), url.indexOf(".jpg")));
			pos += (-i) * interval;
			if (pos < 0 || pos > 1500)
				last = true;
			else
				_loadHUM(pos, what);
		} else if (url.contains("cam/roc")) {
			int interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("roc_interval", "10"));
			int pos = Integer.parseInt(url.substring(url.lastIndexOf("archive") + "archive".length(), url.indexOf(".jpg")));
			pos += (-i) * interval;
			if (pos < 0 || pos > 1500)
				last = true;
			else
				_loadROC(pos, what);
		} else if (url.contains("cam/sveti_martin")) {
			int interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("roc_interval", "10"));
			int pos = Integer.parseInt(url.substring(url.lastIndexOf("archive") + "archive".length(), url.indexOf(".jpg")));
			pos += (-i) * interval;
			if (pos < 0 || pos > 1500)
				last = true;
			else
				_loadBUZET(pos, what);
		} else if (url.contains("vojak/webcam")) {
			int interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("roc_interval", "10"));
			int pos = Integer.parseInt(url.substring(url.indexOf("webcamimage") + "webcamimage".length(), url.indexOf(".jpg")));
			pos += (-i) * interval;
			if (pos < 0 || pos > 1500)
				last = true;
			else
				_loadVOJAK(pos, what);
		} else if (url.contains("en.sat24.com/image")) {
			String s1 = url.substring(url.indexOf('?') + 1);
			String[] sa = s1.split("\\&");
			String country = sa[0].split("=")[1];
			String sat = sa[1].split("=")[1];
			String datetime = sa[2].split("=")[1];
			String dateString = datetime.substring(0, 8);
			String timeString = datetime.substring(8, 12);
			int timeNumber = Integer.parseInt(timeString);
			int hour = timeNumber / 100;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, timeNumber - hour * 100);
			cal.add(Calendar.MINUTE, 5 * i);
			Calendar calCurrent = Calendar.getInstance();
			calCurrent.add(Calendar.MINUTE, -10);
			int offsetMinutes = (calCurrent.get(Calendar.ZONE_OFFSET) + calCurrent.get(Calendar.DST_OFFSET)) / 60000;
			calCurrent.add(Calendar.MINUTE, -offsetMinutes);
			if (cal.after(calCurrent))
				last = true;
			else {
				String newTimeString = timeFormat.format(cal.getTime());
				int newTimeNumber = Integer.parseInt(newTimeString);
				int minutes = newTimeNumber - newTimeNumber / 100 * 100;
				newTimeNumber = newTimeNumber - minutes % 5;

				Date dateCurrent = calCurrent.getTime();
				String timeString2 = timeFormat.format(dateCurrent);
				int timeNumber2 = Integer.parseInt(timeString2) + dateCurrent.getTimezoneOffset() / 60 * 100;
				if (timeNumber2 - newTimeNumber >= 100)
					last = true;
				else {
					newTimeString = String.format("%1$04d", newTimeNumber);
					_loadSAT24(country, sat, dateString + newTimeString);
				}
			}

		} else if (url.contains("kompozit")
				|| url.contains("osijek") || url.contains("bilogora") || url.contains("puntijarka")) {
			if (!url.contains("stat")) {
				int hour = Integer.parseInt(url.substring(url.length() - 8, url.length() - 6)) + i;
				Date date = new Date();
				int currentHourUTC = date.getHours() + date.getTimezoneOffset() / 60;
				if (hour < 0)
					last = true;
				else {
					if (hour > currentHourUTC) {
						hour = -1;
					}
					if (url.contains("oweb"))
						loadOSIJEK_RADAR(hour);
					if (url.contains("pweb"))
						loadPUNTIJARKA_RADAR(hour);
					else if (url.contains("kweb"))
						loadKOMPOZITNI_RADAR(hour);
					else
						loadBILOGORA_RADAR(hour);
				}
			} else {
				if (i == 1)
					last = true;
				else {
					Date date = new Date();
					int currentHourUTC = date.getHours() + date.getTimezoneOffset() / 60;
					if (url.contains("osijek"))
						loadOSIJEK_RADAR(currentHourUTC);
					else if (url.contains("kompozit"))
						loadKOMPOZITNI_RADAR(currentHourUTC);
					else if (url.contains("puntijarka"))
						loadPUNTIJARKA_RADAR(currentHourUTC);
					else
						loadBILOGORA_RADAR(currentHourUTC);
				}
			}
		} else if (url.contains("observ/webcam")) {
			int interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("arso_interval", "10"));
			String city = url.substring(url.indexOf("webcam/") + "webcam/".length(), url.indexOf("_dir/"));
			int ind = url.indexOf("siwc_") + "siwc_".length();
			String dateString = url.substring(ind, ind + 8);
			String timeString = url.substring(ind + 9, ind + 13);
			String dir = url.substring(url.lastIndexOf("_") + 1, url.lastIndexOf('.'));
			if (!ss.equals("")) {
				i = 0;
				dir = ss;
			}
			int timeNumber = Integer.parseInt(timeString);
			int hour = timeNumber / 100;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, timeNumber - hour * 100);
			cal.add(Calendar.MINUTE, interval * i);
			Calendar calCurrent = Calendar.getInstance();
			calCurrent.add(Calendar.MINUTE, -interval);
			int offsetMinutes = (calCurrent.get(Calendar.ZONE_OFFSET) + calCurrent.get(Calendar.DST_OFFSET)) / 60000;
			calCurrent.add(Calendar.MINUTE, -offsetMinutes);
			if (cal.after(calCurrent))
				last = true;
			else {
				String newTimeString = timeFormat.format(cal.getTime());
				int newTimeNumber = Integer.parseInt(newTimeString);
				int minutes = newTimeNumber - newTimeNumber / 100 * 100;
				newTimeNumber = newTimeNumber - minutes % interval;
				if (newTimeNumber < 230)
					last = true;
				else {
					newTimeString = String.format("%1$04d", newTimeNumber);
					_loadARSO_WEBCAM(city, dateString, newTimeString, dir, what);
				}
			}
		} else if (url.contains("observ/radar")) {
			int interval = 10;
			int ind = url.indexOf("radar/si0_") + "radar/si0_".length();
			String dateString = url.substring(ind, ind + 8);
			String timeString = url.substring(ind + 9, ind + 13);
			int timeNumber = Integer.parseInt(timeString);
			int hour = timeNumber / 100;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, timeNumber - hour * 100);
			cal.add(Calendar.MINUTE, interval * i);
			Calendar calCurrent = Calendar.getInstance();
			calCurrent.add(Calendar.MINUTE, -interval);
			int offsetMinutes = (calCurrent.get(Calendar.ZONE_OFFSET) + calCurrent.get(Calendar.DST_OFFSET)) / 60000;
			calCurrent.add(Calendar.MINUTE, -offsetMinutes);
			if (cal.after(calCurrent))
				last = true;
			else {
				String newTimeString = timeFormat.format(cal.getTime());
				int newTimeNumber = Integer.parseInt(newTimeString);
				int minutes = newTimeNumber - newTimeNumber / 100 * 100;
				newTimeNumber = newTimeNumber - minutes % interval;
				if (newTimeNumber < 230)
					last = true;
				else {
					newTimeString = String.format("%1$04d", newTimeNumber);
					_loadARSO_RADAR(dateString, newTimeString, what);
				}
			}
		} else if (url.contains("japetic.dtdns.net")) {
			int interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("jap_interval", "1"));
			if (!url.contains("lastsnap")) {
				int hourmin = Integer.parseInt(url.substring(url.length() - 8, url.length() - 4));
				int start = url.lastIndexOf('/');
				String camname = url.substring(start + 1, url.length() - 9);
				String dir = japArchiveNames.get(camname);
				if (!ss.equals("")) {
					i = 0;
					dir = ss;
				}
				Calendar cal = Calendar.getInstance();
				int hour = hourmin / 100;
				cal.set(Calendar.HOUR_OF_DAY, hour);
				cal.set(Calendar.MINUTE, hourmin - hour * 100);
				cal.add(Calendar.MINUTE, i * interval);
				hour = cal.get(Calendar.HOUR_OF_DAY);
				hourmin = hour * 100 + cal.get(Calendar.MINUTE);
				long currentTime = (new Date()).getTime();
				if (hour < 7)
					last = true;
				else if (cal.getTime().getTime() + 60000 * interval > currentTime || hour >= 20) {
					hourmin = -1;
				}
				_loadJAPWebcam(dir, hourmin);
			} else {
				if (i == 1)
					last = true;
				else if (i == 0) {
					_loadJAPWebcam(ss, -1);
				} else {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MINUTE, i * interval);
					int hourmin = cal.get(Calendar.HOUR_OF_DAY) * 100 + cal.get(Calendar.MINUTE);
					String dir = "s";
					if (url.contains("dtdns")) {
						if (url.contains("kam5")) {
							dir = "s";
						} else {
							dir = "se";
						}
					} else {
						if (url.contains("kam1")) {
							dir = "ne";
						} else {
							dir = "n";
						}
					}
					_loadJAPWebcam(dir, hourmin);
				}
			}
		} else if (url.contains("feniks.hr/koka")) {
			int interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("koka_interval", "2"));
			if (url.contains("movie")) {
				int hourmin = Integer.parseInt(url.substring(url.length() - 8, url.length() - 4));
				String ddMM = url.substring(url.length() - 13, url.length() - 9);
				Calendar cal = Calendar.getInstance();
				int hour = hourmin / 100;
				int min = hourmin - hour * 100;
				min = (min / 2) * 2;
				cal.set(Calendar.HOUR_OF_DAY, hour);
				cal.set(Calendar.MINUTE, hourmin - hour * 100);
				cal.add(Calendar.MINUTE, i * interval);
				hour = cal.get(Calendar.HOUR_OF_DAY);
				hourmin = hour * 100 + cal.get(Calendar.MINUTE);
				long currentTime = (new Date()).getTime();
				if (hour < 7)
					last = true;
				else if (cal.getTime().getTime() + 60000 * interval > currentTime || hour >= 20) {
					hourmin = -1;
				}
				_loadKOKAWebcam(ddMM, hourmin);
			} else {
				if (i == 1)
					last = true;
				else {
					Calendar cal = Calendar.getInstance();
					int min = cal.get(Calendar.MINUTE);
					min = (min / 2) * 2;
					cal.set(Calendar.MINUTE, min);
					cal.add(Calendar.MINUTE, i * interval);
					int hourmin = cal.get(Calendar.HOUR_OF_DAY) * 100 + cal.get(Calendar.MINUTE);
					String ddMM = kokaFormat.format(cal.getTime());
					_loadKOKAWebcam(ddMM, hourmin);
				}
			}
		} else if (url.contains("wxmaps.org/pix/euro4")) {
			int ind1 = url.indexOf("euro4") + 6;
			int ind2 = url.indexOf("hr");
			int hour = Integer.parseInt(url.substring(ind1, ind2)) + 24 * i;
			if (hour < 0 || hour > 144)
				last = true;
			else {
				_loadGFS_MR850_THW(hour, what);
			}
		} else if (url.contains("modis.gsfc.nasa.gov/gallery/images/image")) {
			String dateString = url.substring(url.length() - 16, url.length() - 8);
			Date imgDate = nasaFormat.parse(dateString);
			Date currDate = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(imgDate);
			c.add(Calendar.DATE, i);
			imgDate = c.getTime();
			if (imgDate.after(currDate))
				last = true;
			else {
				_loadNASAIOTD(nasaFormat.format(imgDate), what);
			}
		} else if (url.contains("terra")) {
			int year_day = Integer.parseInt(url.substring(url.length() - 21, url.length() - 14)) + i;
			int current_year_day = getYearDay();
			if (year_day > current_year_day)
				last = true;
			else
				_loadMODIS_TERRA(year_day);
		} else if (url.contains("aqua")) {
			int year_day = Integer.parseInt(url.substring(url.length() - 20, url.length() - 13)) + i;
			int current_year_day = getYearDay();
			if (year_day > current_year_day)
				last = true;
			else
				_loadMODIS_AQUA(year_day);
		} else if (url.contains("skewt")) {
			String city = url.substring(url.lastIndexOf('=') + 1);
			int ind = url.lastIndexOf('/');
			String lat = url.substring(ind + 1, ind + 5);
			String lon = url.substring(ind + 6, ind + 10);
			int hour = Integer.parseInt(url.substring(ind + 11, ind + 13)) + i * 6;
			if (hour < 12 || hour > 72)
				last = true;
			else
				_loadSKEWT(city, lat, lon, hour, what);
		} else if (url.contains("gemglb_eur")) {
			int index = url.indexOf("eur_") + 4;
			String run = url.substring(index, index + 2);
			int min = 0;
			int max = 60;
			if (run.equals("12")) {
				min = 12;
				max = 72;
			}
			int hour = Integer.parseInt(url.substring(url.length() - 6, url.length() - 4)) + i * 3;
			if (hour < min || hour > max)
				last = true;
			else
				_loadSKEWT_CMC(run, hour, what);
		} else if (url.contains("clouds/clouds")) {
			int hour = Integer.parseInt(url.substring(url.length() - 6, url.length() - 4)) + i;
			if (hour < 9 || hour > 72)
				last = true;
			else
				_loadNaoblakaMA(hour, what);
		} else if (url.contains("lcl/lcl")) {
			int hour = Integer.parseInt(url.substring(url.length() - 6, url.length() - 4)) + i;
			if (hour < 9 || hour > 72)
				last = true;
			else
				_loadKondenzacijaMA(hour, what);
		} else if (url.contains("mlcape/mlcape")) {
			int hour = Integer.parseInt(url.substring(url.length() - 6, url.length() - 4)) + i;
			if (hour < 9 || hour > 72)
				last = true;
			else
				_loadCapeMA(hour, what);
		} else if (url.contains("vjetar900")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4)) + i;
			if (hour < 0 || hour > 167)
				last = true;
			else
				_loadWindMI(hour, what);
		} else if (url.contains("wrf_arw_letaci/naoblaka")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4)) + i;
			if (hour < 0 || hour > 167)
				last = true;
			else
				_loadNaoblakaMI(hour, what);
		} else if (url.contains("wrf_arw_letaci/conv_base_height")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4)) + i;
			if (hour < 0 || hour > 167)
				last = true;
			else
				_loadKondenzacijaMI(hour, what);
		} else if (url.contains("wrf_arw_letaci/cape")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4)) + i;
			if (hour < 0 || hour > 167)
				last = true;
			else
				_loadCapeMI(hour, what);
		} else if (url.contains("aladinHR")) {
			int hour = Integer.parseInt(url.substring(url.length() - 6, url.length() - 4));
			if (url.contains("HRv8")) {
				hour += 3 * i;
				if (hour < 3 || hour > 72)
					last = true;
				else
					_loadAladinHR(hour, what);
			} else if (url.contains("6h_ob_uk")) {
				hour += 6 * i;
				if (hour < 6 || hour > 72)
					last = true;
				else
					_loadAladinHR_OB(hour, what);
			} else {
				hour += 3 * i;
				String city = url.substring(url.length() - 11, url.length() - 7);
				if (hour < 3 || hour > 72)
					last = true;
				else
					_loadAladinDA(city, hour, what);
			}
		} else if (url.contains("00_vf")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4));
			int height = Integer.parseInt(url.substring(url.length() - 25, url.length() - 22));
			int start = url.indexOf("/as") + 4;
			String date = url.substring(start, start + 8);
			String modelTime = url.substring(start + 9, start + 13);
			hour += i * 3;
			if (hour < 6 || hour > 72)
				last = true;
			else
				_loadAladinWind2(date, height, hour, modelTime, what);
		} else if (url.contains("00_r-t-vf")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4));
			int height = Integer.parseInt(url.substring(url.length() - 25, url.length() - 22));
			int start = url.indexOf("/as") + 4;
			String date = url.substring(start, start + 8);
			String modelTime = url.substring(start + 9, start + 13);
			hour += i * 3;
			if (hour < 6 || hour > 72)
				last = true;
			else
				_loadAladinWind2(date, height, hour, modelTime, what);
		} else if (url.contains("00_tcc")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4));
			int start = url.indexOf("/as") + 4;
			String date = url.substring(start, start + 8);
			String modelTime = url.substring(start + 9, start + 13);
			hour += i * 3;
			if (hour < 6 || hour > 72)
				last = true;
			else
				_loadAladinCloud2(date, hour, modelTime, what);
		} else if (url.contains("00_t2m")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4));
			int start = url.indexOf("/as") + 4;
			String date = url.substring(start, start + 8);
			String modelTime = url.substring(start + 9, start + 13);
			hour += i * 3;
			if (hour < 6 || hour > 72)
				last = true;
			else
				_loadAladinTemp2(date, hour, modelTime, what);
		} else if (url.contains("AW00_veter")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4));
			int height = Integer.parseInt(url.substring(url.length() - 11, url.length() - 8));
			if (i == 1 && (hour < 18))
				hour += 3;
			else if (i == -1 && (hour <= 18))
				hour -= 3;
			else
				hour += i * 6;
			if (hour < 6 || hour > 48)
				last = true;
			else
				_loadAladinWind(height, hour, what);
		} else if (url.contains("AW00_oblpad")) {
			int hour = Integer.parseInt(url.substring(url.length() - 7, url.length() - 4)) + i * 6;
			if (hour < 6 || hour > 48)
				last = true;
			else
				_loadAladinCloud(hour, what);
		} else {
			return -1;
		}

		if (last) {
			if (!loop) {
				String w = null;
				if (i < 0)
					w = "raniji";
				else
					w = "kasniji";
				Toast.makeText(TrenutnoVrijemeActivity.this, "Nema dijagrama za " + w + " termin.", Toast.LENGTH_SHORT).show();
			}
			return 0;
		} else {
			v.setTag(R.id.ANIMATION, i);
			return 1;
		}
	}

	Handler shareHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String localURI = msg.getData().getString("localURI");
			String title = msg.getData().getString("title");
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/*");
			Uri uri = Uri.parse("file://" + localURI);
			share.putExtra(Intent.EXTRA_STREAM, uri);
			share.putExtra(Intent.EXTRA_TITLE, title);
			share.putExtra(Intent.EXTRA_TEXT, title);
			share.putExtra(Intent.EXTRA_SUBJECT, title);
			startActivity(Intent.createChooser(share, "Podijeli sliku:"));
		};
	};
	OnClickListener onClickListener2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				((View) v.getParent()).setVisibility(View.GONE);
				String titleUrl = (String) v.getTag();
				int index = titleUrl.indexOf(DELIM);
				String title = titleUrl.substring(0, index);
				String url = titleUrl.substring(index + DELIM.length());
				String localURI = Utils.generateLocalURI(url);
				File f = new File(localURI);
				if (!f.exists()) {
					Toast.makeText(TrenutnoVrijemeActivity.this, "Cashe period je istekao. Osvježite sliku pa pokušajte ponovo.", Toast.LENGTH_LONG).show();
					return;
				}
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("localURI", localURI);
				data.putString("title", title);
				msg.setData(data);
				Toast.makeText(TrenutnoVrijemeActivity.this, "Otvaranje 'Share' dijaloga... (može potrajati nekoliko sekundi)", Toast.LENGTH_LONG).show();
				shareHandler.sendMessage(msg);
				return;
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}
	};

	OnClickListener onClickListener3 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				((View) v.getParent()).setVisibility(View.GONE);
				String titleUrl = (String) v.getTag();
				int index = titleUrl.indexOf(DELIM);
				String title = titleUrl.substring(0, index);
				String url = titleUrl.substring(index + DELIM.length());
				Intent fullScreenIntent = new Intent().setClass(TrenutnoVrijemeActivity.this, PhotoFullScreen.class);
				fullScreenIntent.putExtra("remoteURI", url);
				fullScreenIntent.putExtra("title", title);
				startActivity(fullScreenIntent);
				return;
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}
	};

    OnClickListener onClickListener4 = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                openOptionsMenu();
                return;
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }
        }
    };

	private JSONObject getUsage() throws JSONException {
		String usageString = PreferenceManager.getDefaultSharedPreferences(this).getString("usageStat", "{}");
		return new JSONObject(usageString);
	}

	private void saveUsage(JSONObject usage) {
		String usageString = usage.toString();
		PreferenceManager.getDefaultSharedPreferences(this).edit().putString("usageStat", usageString).commit();
	}

	Handler handlerIntent = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String title = msg.getData().getString("title");
			try {
				openOptionsMenu();
				closeOptionsMenu();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				select(title);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	public static String getSAT24HRURL() {
		String url = "";
		Date date = new Date();
		Date sunrise = sunriseSunset.getSunrise();
		Date sunset = sunriseSunset.getSunset();
		if (date.before(sunrise) || date.after(sunset))
			url = String.format(SAT24, "gr", "infraPolair", getDatetimeSAT24());
		else
			url = String.format(SAT24, "ba", "visual", getDatetimeSAT24());
		return url;
	}

	public static String getSAT24EUURL() {
		String url = "";
		Date date = new Date();
		Date sunrise = sunriseSunset.getSunrise();
		Date sunset = sunriseSunset.getSunset();
		if (date.before(sunrise) || date.after(sunset))
			url = String.format(SAT24, "eu", "infraPolair", getDatetimeSAT24());
		else
			url = String.format(SAT24, "eu", "visual", getDatetimeSAT24());
		return url;
	}

	private void loadSAT24_RADAR_MUNJE() {
		Date date = new Date();
		/*
		 * int hour = date.getHours(); int m = Math.abs(date.getMonth() - 6) + 1; if (hour < (5 + m / 2) || hour > (21 - m / 2)) loadSAT24("ba", "ir", 2); else
		 * loadSAT24("ba", "vis", 2);
		 */
		Date sunrise = sunriseSunset.getSunrise();
		Date sunset = sunriseSunset.getSunset();
		if (date.before(sunrise) || date.after(sunset))
			loadSAT24("gr", "infraPolair");
		else
			loadSAT24("ba", "visual");
		loadBILOGORA_RADAR(-1);
		loadRADARMUNJE();
	}

	@SuppressLint("NewApi")
	private void cleanUp4WebView(ImageView imageView, WebView webView) {
		webView.setVisibility(View.VISIBLE);
		webView.getSettings().setJavaScriptEnabled(true);
		try {
			webView.getSettings().setSupportZoom(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setDisplayZoomControls(true);
			webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
			webView.getSettings().setTextSize(TextSize.NORMAL);
			webView.getSettings().setTextZoom(100);
		} catch (Throwable e) {
		}
		webView.loadUrl("about:blank");
		webView.setTag(selectedMenuItemTitle);
		imageView.setVisibility(View.GONE);
		((View) imageView.getParent()).findViewById(R.id.camera_buttons).setVisibility(View.GONE);
	}

	private void loadPLJUSAK() {
		titles[0] = "Aktualno: pljusak.com";
		WebView webView = (WebView) findViewById(R.id.web1);
		ImageView imageView = (ImageView) findViewById(R.id.img1);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		webView.loadUrl(PLJUSAK);
		setMyTitle();
	}

	@SuppressLint("NewApi")
	private void loadDHMZ_AKT() {
		titles[0] = "Aktualno: DHMZ";
		WebView webView = (WebView) findViewById(R.id.web1);
		ImageView imageView = (ImageView) findViewById(R.id.img1);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		try {
			webView.getSettings().setTextSize(TextSize.SMALLEST);
			webView.getSettings().setTextZoom(60);
		} catch (Throwable e) {
		}
		webView.loadUrl(DHMZ_AKT);
		setMyTitle();
	}

	private void loadNAJAVE() {
		titles[0] = "PG Kalendar letenja";
		WebView webView = (WebView) findViewById(R.id.web1);
		ImageView imageView = (ImageView) findViewById(R.id.img1);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		webView.loadUrl(NAJAVE);
		setMyTitle();
	}

	@SuppressLint("NewApi")
	private void loadOLC_DANAS() {
		titles[0] = "OLC";
		WebView webView = (WebView) findViewById(R.id.web1);
		ImageView imageView = (ImageView) findViewById(R.id.img1);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		try {
			webView.getSettings().setTextSize(TextSize.SMALLEST);
			webView.getSettings().setTextZoom(40);
		} catch (Throwable e) {
		}
		webView.loadUrl(OLC_DANAS);
		setMyTitle();
	}

	@SuppressLint("NewApi")
	private void loadTRIBALJ() {
		_loadTRIBALJ(0);
		_loadTRIBALJ2(1);
		WebView webView = (WebView) findViewById(R.id.web3);
		ImageView imageView = (ImageView) findViewById(R.id.img3);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		try {
			webView.getSettings().setTextSize(TextSize.SMALLEST);
			webView.getSettings().setTextZoom(60);
		} catch (Throwable e) {
		}
		webView.loadUrl(METEO_TRIBALJ);
		titles[2] = "Tribalj meteo";
		setMyTitle();
	}

	private void _loadTRIBALJ(int where) {
		Loader l = new Loader(String.format(WEBCAM_TRIBALJ), where);
		Utils.executor.execute(l);
		titles[where] = "Tribalj start";

		setMyTitle();
	}

	private void _loadTRIBALJ2(int where) {
		Loader l = new Loader(String.format(WEBCAM_TRIBALJ2), where);
		Utils.executor.execute(l);
		titles[where] = "Tribalj padina";

		setMyTitle();
	}

	@SuppressLint("NewApi")
	private void loadRASPADALICA() {
		_loadBUZET(0, 0);
		_loadROC(0, 1);
		WebView webView = (WebView) findViewById(R.id.web3);
		ImageView imageView = (ImageView) findViewById(R.id.img3);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		try {
			webView.getSettings().setTextSize(TextSize.SMALLEST);
			webView.getSettings().setTextZoom(60);
		} catch (Throwable e) {
		}
		webView.loadUrl(RASPADALICA);
		titles[2] = "Raspadalica postaja";
		setMyTitle();
	}

	@SuppressLint("NewApi")
	private void loadUCKA() {
		_loadVOJAK(0, 0);
		WebView webView = (WebView) findViewById(R.id.web2);
		ImageView imageView = (ImageView) findViewById(R.id.img2);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		try {
			webView.getSettings().setTextSize(TextSize.NORMAL);
			webView.getSettings().setTextZoom(100);
		} catch (Throwable e) {
		}
		webView.loadUrl(VOJAK);
		titles[1] = "Učka postaja";
		setMyTitle();
	}

	private void loadDHMZDanasSutraW() {
		titles[0] = "Hrvatska danas";
		WebView webView = (WebView) findViewById(R.id.web1);
		ImageView imageView = (ImageView) findViewById(R.id.img1);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		webView.loadUrl(DHMZ_DANAS);

		titles[1] = "Hrvatska sutra";
		webView = (WebView) findViewById(R.id.web2);
		imageView = (ImageView) findViewById(R.id.img2);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		webView.loadUrl(DHMZ_SUTRA);
		setMyTitle();
	}

	private void loadDHMZDanasSutraZG() {
		titles[0] = "ZG danas/sutra";
		WebView webView = (WebView) findViewById(R.id.web1);
		ImageView imageView = (ImageView) findViewById(R.id.img1);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		webView.loadUrl(DHMZ_DANAS_SUTRA_ZG);
		setMyTitle();
	}

	private void loadMA3d_ZG_VZ_RI() {
		_loadMA3d("Zagreb", 0);
		_loadMA3d("Varazdin", 1);
		_loadMA3d("Rijeka", 2);
	}

	private void loadMA3d_PU_OS_ST() {
		_loadMA3d("Pula", 0);
		_loadMA3d("Osijek", 1);
		_loadMA3d("Split", 2);
	}

	private void _loadMA3d(String city, int index) {
		titles[index] = "Meteoadriatic 3d - " + city + "";
		WebView webView = null;
		ImageView imageView = null;

		switch (index) {
		case 0:
			webView = (WebView) findViewById(R.id.web1);
			imageView = (ImageView) findViewById(R.id.img1);
			break;

		case 1:
			webView = (WebView) findViewById(R.id.web2);
			imageView = (ImageView) findViewById(R.id.img2);

			break;

		case 2:
			webView = (WebView) findViewById(R.id.web3);
			imageView = (ImageView) findViewById(R.id.img3);

			break;

		default:
			break;
		}
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		webView.loadUrl(String.format(METEOADRIATIC_3d, city));
		setMyTitle();
	}

	public static String getDatetimeSAT24() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		Date date = cal.getTime();
		String dateString = aladinFormat.format(date);
		String timeString = timeFormat.format(date);
		int timeNumber = Integer.parseInt(timeString) + date.getTimezoneOffset() / 60 * 100;
		int minutes = timeNumber - timeNumber / 100 * 100;
		timeNumber = timeNumber - minutes % 5;
		timeString = String.format("%1$04d", timeNumber);

		String datetime = dateString + timeString;
		return datetime;
	}

	private void loadSAT24(String country, String sat) {
		_loadSAT24(country, sat, getDatetimeSAT24());
	}

	private void _loadSAT24(String country, String sat, String datetime) {
		Loader l = new Loader(String.format(SAT24, country, sat, datetime), 0);
		Utils.executor.execute(l);
		if (country.equals("gr") || country.equals("ba"))
			country = "hr";
		if (sat.equals("infraPolair"))
			sat = "ir";
		else
			sat = "vis";
		titles[0] = "SAT24 (" + country + "," + sat + "," + (datetime.substring(8)) + "UTC)";
		setMyTitle();
	}

	private void loadKOMPOZITNI_RADAR(int hour) {
		if (hour == -1) {
			Loader l = new Loader(KOMPOZITNI_RADAR, 1);
			Utils.executor.execute(l);
			titles[1] = "Radar kompozitni";
		} else {
			Loader l = new Loader(String.format(KOMPOZITNI_RADAR_HIST, hour), 1);
			Utils.executor.execute(l);
			titles[1] = "Radar kompozitni (" + hour + ":00 UTC)";
		}
		setMyTitle();
	}

	private void loadBILOGORA_RADAR(int hour) {
		if (hour == -1) {
			Loader l = new Loader(BILOGORA_RADAR, 1);
			Utils.executor.execute(l);
			titles[1] = "Radar Bilogora";
		} else {
			Loader l = new Loader(String.format(BILOGORA_RADAR_HIST, hour), 1);
			Utils.executor.execute(l);
			titles[1] = "Radar Bilogora (" + hour + ":00 UTC)";
		}
		setMyTitle();
	}

	private void loadOSIJEK_RADAR(int hour) {
		if (hour == -1) {
			Loader l = new Loader(OSIJEK_RADAR, 1);
			Utils.executor.execute(l);
			titles[1] = "Radar Osijek";
		} else {
			Loader l = new Loader(String.format(OSIJEK_RADAR_HIST, hour), 1);
			Utils.executor.execute(l);
			titles[1] = "Radar Osijek (" + hour + ":00 UTC)";
		}
		setMyTitle();
	}

	private void loadPUNTIJARKA_RADAR(int hour) {
		if (hour == -1) {
			Loader l = new Loader(PUNTIJARKA_RADAR, 1);
			Utils.executor.execute(l);
			titles[1] = "Radar Puntijarka";
		} else {
			Loader l = new Loader(String.format(PUNTIJARKA_RADAR_HIST, hour), 1);
			Utils.executor.execute(l);
			titles[1] = "Radar Puntijarka (" + hour + ":00 UTC)";
		}
		setMyTitle();
	}
	private void loadDHMZDanasSutra() {
		Loader l = new Loader(String.format(DHMZ_DANAS_SUTRA, "danas"), 0);
		Utils.executor.execute(l);
		titles[0] = "DHMZ danas";

		l = new Loader(String.format(DHMZ_DANAS_SUTRA, "sutra"), 1);
		Utils.executor.execute(l);
		titles[1] = "DHMZ sutra";

		l = new Loader(DHMZ_FRONTE, 2);
		Utils.executor.execute(l);
		titles[2] = "Tlak/fronte sutra";

		setMyTitle();
	}

	private void loadDHMZ4d() {
		Loader l = new Loader(String.format(DHMZ_4d, 2), 0);
		Utils.executor.execute(l);
		titles[0] = "DHMZ 4d +1";

		l = new Loader(String.format(DHMZ_4d, 3), 1);
		Utils.executor.execute(l);
		titles[1] = "DHMZ 4d +2";

		l = new Loader(String.format(DHMZ_4d, 4), 2);
		Utils.executor.execute(l);
		titles[2] = "DHMZ 4d +3";

		setMyTitle();
	}

	private void loadRADARSLO() {
		_loadARSO_RADAR((String) null, (String) null, 1);
		/*
		 * Loader l = new Loader(RADARSLO, 1); Utils.executor.execute(l); titles[1] = "Lisca"; setMyTitle();
		 */
	}

	private void loadRADARSLOTUCA() {
		Loader l = new Loader(RADARSLOTUCA, 1);
		Utils.executor.execute(l);
		titles[1] = "Lisca tuča";
		setMyTitle();
	}

	private void loadRADAR_FVG() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		String dateString = aladinFormat.format(date);
		String timeString = timeHourOnly.format(date);
		int timeNumber = Integer.parseInt(timeString) + date.getTimezoneOffset() / 60 * 100;

		Loader l = new Loader(String.format(RADAR_FVG, dateString, timeNumber), 1);
		Utils.executor.execute(l);
		titles[1] = "Fossalon (" + timeString + ")";
		setMyTitle();
	}

	private void loadRADARMUNJE() {
		Loader l = new Loader(RADAR_MUNJE, 2);
		Utils.executor.execute(l);
		titles[2] = "Munje";
		setMyTitle();
	}

	private void loadWEBCAM1() {
		Loader l = new Loader(WEBCAM_PLESO, 0);
		Utils.executor.execute(l);
		titles[0] = "Pleso";

		l = new Loader(WEBCAM_TRNOVEC, 1);
		Utils.executor.execute(l);
		titles[1] = "Trnovec";

		l = new Loader(WEBCAM_SAMOBOR, 2);
		Utils.executor.execute(l);
		titles[2] = "Samobor";

		setMyTitle();
	}

	private void loadWEBCAM2() {
		_loadBUZET(0, 0);
		_loadROC(0, 1);
		_loadHUM(0, 2);
	}

	private void _loadBUZET(int pos, int where) {
		Loader l = new Loader(String.format(WEBCAM_BUZET, pos), where);
		Utils.executor.execute(l);
		String posStr = "";
		if (pos != 0) {
			posStr = " (-" + pos + "min)";
		}
		titles[0] = "Buzet" + posStr;

		setMyTitle();
	}

	private void _loadROC(int pos, int where) {
		Loader l = new Loader(String.format(WEBCAM_ROC, pos), where);
		Utils.executor.execute(l);
		String posStr = "";
		if (pos != 0) {
			posStr = " (-" + pos + "min)";
		}
		titles[1] = "Roč" + posStr;

		setMyTitle();
	}

	private void _loadHUM(int pos, int where) {
		Loader l = new Loader(String.format(WEBCAM_HUM, pos), where);
		Utils.executor.execute(l);
		String posStr = "";
		if (pos != 0) {
			posStr = " (-" + pos + "min)";
		}
		titles[2] = "Hum" + posStr;

		setMyTitle();
	}

	private void _loadVOJAK(int pos, int where) {
		Loader l = new Loader(String.format(WEBCAM_VOJAK, pos), where);
		Utils.executor.execute(l);
		String posStr = "";
		if (pos != 0) {
			posStr = " (-" + pos + "min)";
		}
		titles[0] = "Učka" + posStr;

		setMyTitle();
	}

	private void loadWEBCAM_SLO() {
		_loadARSO_WEBCAM("LJUBL-ANA_BRNIK", null, null, "n", 0);
		_loadARSO_WEBCAM("LISCA", null, null, "se", 1);
		_loadARSO_WEBCAM("KREDA-ICA", null, null, "se", 2);
	}

	private void loadWEBCAM_SLO2() {
		_loadARSO_WEBCAM("BOVEC", null, null, "e", 0);
		_loadARSO_WEBCAM("MURSK-SOB", null, null, "nw", 1);
		_loadARSO_WEBCAM("KOPER_MARKOVEC", null, null, "e", 2);
	}

	private void loadWEBCAM_SLO3() {
		_loadARSO_WEBCAM("LJUBL-ANA_BEZIGRAD", null, null, "n", 0);
		_loadARSO_WEBCAM("MARIBOR_SLIVNICA", null, null, "w", 1);
		_loadARSO_WEBCAM("PORTOROZ_SECOVLJE", null, null, "e", 2);
	}

	private void _loadARSO_WEBCAM(String city, String date, String time, String dir, int what) {
		String url = null;
		if (time == null) {
			int interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(TrenutnoVrijemeActivity.this).getString("arso_interval", "10"));
			Calendar calCurrent = Calendar.getInstance();
			int offsetMinutes = (calCurrent.get(Calendar.ZONE_OFFSET) + calCurrent.get(Calendar.DST_OFFSET)) / 60000;
			calCurrent.add(Calendar.MINUTE, -offsetMinutes);
			calCurrent.add(Calendar.MINUTE, -15);
			String newTimeString = timeFormat.format(calCurrent.getTime());
			Date dateTemp = calCurrent.getTime();
			date = aladinFormat.format(dateTemp);
			int newTimeNumber = Integer.parseInt(newTimeString);
			int minutes = newTimeNumber - newTimeNumber / 100 * 100;
			newTimeNumber = newTimeNumber - minutes % interval;
			time = String.format("%1$04d", newTimeNumber);
		}
		url = String.format(WEBCAM_ARSO_HIST, city, date, time, dir);
		titles[what] = "WEBCAM " + city + " (" + time + " UTC, " + dir + ")";
		Loader l = new Loader(url, what);
		Utils.executor.execute(l);
		setMyTitle();
	}

	private void _loadARSO_RADAR(String date, String time, int what) {
		String url = null;
		if (time == null) {
			int interval = 10;
			Calendar calCurrent = Calendar.getInstance();
			int offsetMinutes = (calCurrent.get(Calendar.ZONE_OFFSET) + calCurrent.get(Calendar.DST_OFFSET)) / 60000;
			calCurrent.add(Calendar.MINUTE, -offsetMinutes);
			calCurrent.add(Calendar.MINUTE, -20);
			String newTimeString = timeFormat.format(calCurrent.getTime());
			Date dateTemp = calCurrent.getTime();
			date = aladinFormat.format(dateTemp);
			int newTimeNumber = Integer.parseInt(newTimeString);
			int minutes = newTimeNumber - newTimeNumber / 100 * 100;
			newTimeNumber = newTimeNumber - minutes % interval;
			time = String.format("%1$04d", newTimeNumber);
		}
		url = String.format(ALADIN2_RADAR, date, time);
		titles[what] = "RADAR LISCA (" + time + " UTC )";
		Loader l = new Loader(url, what);
		Utils.executor.execute(l);
		setMyTitle();
	}

	private void loadENS15D_ZG_VZ_RI() {
		_loadENS15D("460", "160", 0, 0);
		_loadENS15D("465", "165", 1, 0);
		_loadENS15D("455", "145", 2, 0);
	}

	private void loadENS15D_PU_OS_ST() {
		_loadENS15D("450", "140", 3, 3);
		_loadENS15D("455", "185", 4, 3);
		_loadENS15D("435", "165", 5, 3);
	}

	private void loadENS15D_ZG_VZ_RI_2() {
		_loadENS15D_2("46", "16", 0, 0);
		_loadENS15D_2("46", "17", 1, 0);
		_loadENS15D_2("45", "14", 2, 0);
	}

	private void loadENS15D_PU_OS_ST_2() {
		_loadENS15D_2("45", "14", 3, 3);
		_loadENS15D_2("46", "19", 4, 3);
		_loadENS15D_2("44", "16", 5, 3);
	}

	private void _loadENS15D(String lat, String lon, int what, int sub) {
		String url = String.format(ENS_15D, lat, lon);
		String city = "";
		switch (what) {
		case 0:
			city = "ZAGREB";
			break;
		case 1:
			city = "VARAŽDIN";
			break;
		case 2:
			city = "RIJEKA";
			break;
		case 3:
			city = "PULA";
			break;
		case 4:
			city = "OSIJEK";
			break;
		case 5:
			city = "SPLIT";
			break;

		default:
			break;
		}
		what -= sub;
		titles[what] = "Meteogram 7d (" + city + ")";
		Loader l = new Loader(url, what);
		Utils.executor.execute(l);
		setMyTitle();
	}

	private void _loadENS15D_2(String lat, String lon, int what, int sub) {
		String url = String.format(ENS2_15D, lat, lon);
		String city = "";
		switch (what) {
		case 0:
			city = "ZAGREB";
			break;
		case 1:
			city = "VARAŽDIN";
			break;
		case 2:
			city = "RIJEKA";
			break;
		case 3:
			city = "PULA";
			break;
		case 4:
			city = "OSIJEK";
			break;
		case 5:
			city = "SPLIT";
			break;

		default:
			break;
		}
		what -= sub;
		titles[what] = "ENSEMBLE 16d 850 T,6h mm (" + city + ")";
		Loader l = new Loader(url, what);
		Utils.executor.execute(l);
		setMyTitle();
	}

	private void loadYRNO() {
		Loader l = new Loader(YRNO_ZG, 0);
		Utils.executor.execute(l);

		l = new Loader(YRNO_VZ, 1);
		Utils.executor.execute(l);

		l = new Loader(YRNO_PJ, 2);
		Utils.executor.execute(l);

		titles[0] = "yr.no Zagreb";
		titles[1] = "yr.no Varaždin";
		titles[2] = "yr.no Plitvice";
		setMyTitle();
	}

	private void loadYRNO2() {
		Loader l = new Loader(YRNO_RI, 0);
		Utils.executor.execute(l);

		l = new Loader(YRNO_OS, 1);
		Utils.executor.execute(l);

		l = new Loader(YRNO_ST, 2);
		Utils.executor.execute(l);

		titles[0] = "yr.no Rijeka";
		titles[1] = "yr.no Osijek";
		titles[2] = "yr.no Split";
		setMyTitle();
	}

	private void loadSLJEME() {
		Loader l = new Loader(WEBCAM_SLJEME, 0);
		Utils.executor.execute(l);

		l = new Loader(SLJEME_WIND_DIR, 1);
		Utils.executor.execute(l);

		l = new Loader(SLJEME_WIND, 2);
		Utils.executor.execute(l);

		titles[0] = "Sljeme webcam";
		titles[1] = "Sljeme smjer vjetra";
		titles[2] = "Sljeme jačina vjetra";
		setMyTitle();
	}

	private void loadKOKA() {
		Loader l = new Loader(KOKA, 0);
		Utils.executor.execute(l);

		l = new Loader(KOKA2, 1);
		Utils.executor.execute(l);

		l = new Loader(KOKA3, 2);
		Utils.executor.execute(l);

		titles[0] = "KOKA webcam";
		titles[1] = "KOKA smjer vjetra";
		titles[2] = "KOKA jačina vjetra";
		setMyTitle();
	}

	private void loadJAP() {
		_loadJAPWebcam("s", -1);

		Loader l = new Loader(JAP_WIND_DIR, 1);
		Utils.executor.execute(l);

		l = new Loader(JAP_WIND_SPEED, 2);
		Utils.executor.execute(l);

		titles[1] = "JAP vjetar - smjer";
		titles[2] = "JAP vjetar - brzina";
		setMyTitle();
	}

	private void _loadJAPWebcam(String dir, int hourmin) {
		String url = null;
		if (hourmin > 0) {
			url = japArchives.get(dir);
			url = String.format(url, hourmin);
			titles[0] = "JAP webcam " + dir + " (" + hourmin + ")";
		} else {
			url = japCameras.get(dir);
			titles[0] = "JAP webcam " + dir;
		}
		Loader l = new Loader(url, 0);
		Utils.executor.execute(l);
		setMyTitle();
	}

	private void _loadKOKAWebcam(String ddMM, int hourmin) {
		String url = null;
		if (hourmin > 0) {
			url = String.format(KOKA_ARCHIVE, ddMM, hourmin);
			titles[0] = "KOKAP webcam (" + hourmin + ")";
		} else {
			url = KOKA;
			titles[0] = "KOKA webcam";
		}
		Loader l = new Loader(url, 0);
		Utils.executor.execute(l);
		setMyTitle();
	}

	private void loadMETEOALARM(String iso) {
		_loadMETEOALARM(iso, 0, 0);
		_loadMETEOALARM(iso, 1, 1);
	}

	private void _loadMETEOALARM(String iso, int index, int what) {
		Loader l = new Loader(String.format(METEOALARM, iso, index), what);
		Utils.executor.execute(l);
		titles[what] = "Meteoalarm (" + iso + "," + index + ")";
		setMyTitle();
	}

	public static int getYearDay() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("D");
		Date date = new Date();
		String dayOfYearString = dateFormat.format(date);
		int dayOfYear = Integer.parseInt(dayOfYearString);
		int year = date.getYear() + 1900;
		return year * 1000 + dayOfYear;
	}

	private void loadMODIS_TERRA() {
		_loadMODIS_TERRA(getYearDay());
	}

	private void _loadMODIS_TERRA(int year_day) {
		Loader l = new Loader(String.format(MODIS_TERRA, year_day), 2);
		l.pingUrl = String.format(MODIS_TERRA_PREPARE, year_day);
		Utils.executor.execute(l);
		titles[2] = "Modis Terra (" + year_day + ")";
		setMyTitle();
	}

	private void loadMODIS_AQUA() {
		_loadMODIS_AQUA(getYearDay());
	}

	private void _loadMODIS_AQUA(int year_day) {
		Loader l = new Loader(String.format(MODIS_AQUA, year_day), 2);
		l.pingUrl = String.format(MODIS_AQUA_PREPARE, year_day);
		Utils.executor.execute(l);
		titles[2] = "Modis Aqua (" + year_day + ")";
		setMyTitle();
	}

	public static String getCurrentDateNasaString() {
		String s = nasaFormat.format(new Date());
		return s;
	}

	public static String getCurrentDateNasaUrlString(int offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, offset);
		String s = nasaFormatUrl.format(calendar.getTime());
		return s;
	}

	private void loadNASAIOTD() {
		_loadNASAIOTD(getCurrentDateNasaString(), 1);
	}

	private void loadNASAIOTDURL() {
		_loadNASAIOTDURL(getCurrentDateNasaUrlString(0));
	}

	private void _loadNASAIOTD(String s, int what) {
		Loader l = new Loader(String.format(MODIS_IOTD, s), what);
		Utils.executor.execute(l);
		titles[what] = "Modis slika dana (" + s + ")";
		setMyTitle();
	}

	private void _loadNASAIOTDURL(String s) {
		titles[2] = "Modis slika dana WEB";
		WebView webView = (WebView) findViewById(R.id.web3);
		ImageView imageView = (ImageView) findViewById(R.id.img3);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		webView.loadUrl(String.format(MODIS_IOTD_URL, s));
		setMyTitle();
	}

	private void loadNapovedLetalstvo() {
		titles[0] = "Napoved za letalstvo v besedi";
		WebView webView = (WebView) findViewById(R.id.web1);
		ImageView imageView = (ImageView) findViewById(R.id.img1);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		webView.loadUrl(ARSO_NAPOVED_BESEDA);
		titles[1] = "Računska napoved višinskih vrednosti";
		webView = (WebView) findViewById(R.id.web2);
		imageView = (ImageView) findViewById(R.id.img2);
		cleanUp4WebView(imageView, webView);
		webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		webView.loadUrl(ARSO_VISINSKA_NAPOVED);
		setMyTitle();
	}

	private void loadSKEWT(String city) {
		Date date = new Date();
		int hour = date.getHours();
		if (hour > 5 && hour < 17) {
			hour = 18;
		} else {
			hour = 30;
		}
		String lat = "";
		String lon = "";
		if (city.equals("Ivanec")) {
			lat = "46.2";
			lon = "16.1";
		} else if (city.equals("Zaprešić")) {
			lat = "45.9";
			lon = "15.8";
		} else if (city.equals("Jastrebarsko")) {
			lat = "45.7";
			lon = "15.7";
		} else if (city.equals("Kalnik")) {
			lat = "46.1";
			lon = "16.5";
		} else if (city.equals("Osijek")) {
			lat = "45.5";
			lon = "18.7";
		} else if (city.equals("Bjelopolje")) {
			lat = "44.7";
			lon = "15.8";
		} else if (city.equals("Lič")) {
			lat = "45.3";
			lon = "14.7";
		} else if (city.equals("Tribalj")) {
			lat = "45.2";
			lon = "14.7";
		} else if (city.equals("Buzet")) {
			lat = "45.4";
			lon = "14.0";
		} else if (city.equals("Motovun")) {
			lat = "45.3";
			lon = "13.8";
		} else if (city.equals("Greda")) {
			lat = "43.7";
			lon = "16.8";
		} else if (city.equals("Golte")) {
			lat = "46.4";
			lon = "14.9";
		} else if (city.equals("Camp Gabrje")) {
			lat = "46.2";
			lon = "13.7";
		} else if (city.equals("Smuk")) {
			lat = "45.7";
			lon = "15.2";
		} else if (city.equals("Slivnica")) {
			lat = "45.8";
			lon = "14.4";
		} else if (city.equals("Lisca")) {
			lat = "46.1";
			lon = "15.3";
		} else if (city.equals("Udine")) {
			lat = "46.1";
			lon = "13.2";
		}
		_loadSKEWT(city, lat, lon, hour, 0);
		_loadSKEWT(city, lat, lon, hour + 24, 1);
		_loadSKEWT(city, lat, lon, hour + 48, 2);
	}

	private void _loadSKEWT(String city, String lat, String lon, int hour, int what) {
		Loader l = new Loader(String.format(SKEW_T, lat, lon, hour, city), what);
		l.pingUrl = String.format(SKEW_T_PREP, lat, lon, hour);
		Utils.executor.execute(l);
		titles[what] = "SKEWT " + city + "(+" + hour + ")";
		setMyTitle();
	}

	private void loadSKEWT_CMC() {
		Date date = new Date();
		String run = "00";
		int hour = date.getHours();
		if (hour > 5 && hour < 17) {
			run = "00";
			hour = 12;
		} else {
			run = "12";
			hour = 24;
		}
		_loadSKEWT_CMC(run, hour, 0);
		_loadSKEWT_CMC(run, hour + 24, 1);
		_loadSKEWT_CMC(run, hour + 48, 2);
	}

	private void _loadSKEWT_CMC(String run, int hour, int what) {
		Loader l = new Loader(String.format(SKEW_T_CMC, run, hour), what);
		Utils.executor.execute(l);
		titles[what] = "SKEWT Zagreb (+" + hour + ")";
		setMyTitle();
	}

	private void loadMeteogrami3d_zg_vz_pj() {
		Loader l = new Loader(String.format(METEOGRAMI3d, "45.8", "16.0"), 0);
		l.pingUrl = String.format(METEOGRAMI3d_PREP, "45.8", "16.0");
		Utils.executor.execute(l);
		l = new Loader(String.format(METEOGRAMI3d, "46.3", "16.3"), 1);
		l.pingUrl = String.format(METEOGRAMI3d_PREP, "46.3", "16.3");
		Utils.executor.execute(l);
		l = new Loader(String.format(METEOGRAMI3d, "44.7", "15.8"), 2);
		l.pingUrl = String.format(METEOGRAMI3d_PREP, "44.7", "15.8");
		Utils.executor.execute(l);
		titles[0] = "Zagreb";
		titles[1] = "Varaždin";
		titles[2] = "Bjelopolje";
		setMyTitle();
	}

	private void loadMeteogrami3d_ri_sinj_mo() {
		Loader l = new Loader(String.format(METEOGRAMI3d, "45.2", "14.7"), 0);
		l.pingUrl = String.format(METEOGRAMI3d_PREP, "45.2", "14.7");
		Utils.executor.execute(l);
		l = new Loader(String.format(METEOGRAMI3d, "43.7", "16.8"), 1);
		l.pingUrl = String.format(METEOGRAMI3d_PREP, "43.7", "16.8");
		Utils.executor.execute(l);
		l = new Loader(String.format(METEOGRAMI3d, "45.4", "14.0"), 2);
		l.pingUrl = String.format(METEOGRAMI3d_PREP, "45.4", "14.0");
		Utils.executor.execute(l);
		titles[0] = "Tribalj";
		titles[1] = "Greda";
		titles[2] = "Buzet";
		setMyTitle();
	}

	private void loadMeteogrami4d_zg_vz_pj() {
		Loader l = new Loader(String.format(METEOGRAMI4d, "Zagreb"), 0);
		Utils.executor.execute(l);
		l = new Loader(String.format(METEOGRAMI4d, "Varazdin"), 1);
		Utils.executor.execute(l);
		l = new Loader(String.format(METEOGRAMI4d, "Plitvicka_Jezera"), 2);
		Utils.executor.execute(l);
		titles[0] = "Zagreb";
		titles[1] = "Varaždin";
		titles[2] = "Plitvice";
		setMyTitle();
	}

	private void loadMeteogrami4d_os_ri_sinj() {
		Loader l = new Loader(String.format(METEOGRAMI4d, "Osijek"), 0);
		Utils.executor.execute(l);
		l = new Loader(String.format(METEOGRAMI4d, "Rijeka"), 1);
		Utils.executor.execute(l);
		l = new Loader(String.format(METEOGRAMI4d, "Sinj"), 2);
		Utils.executor.execute(l);
		titles[0] = "Osijek";
		titles[1] = "Rijeka";
		titles[2] = "Sinj";
		setMyTitle();
	}

	private void loadAladinDanas() {
		_loadAladinWind(925, 12, 0);
		_loadAladinWind(850, 12, 1);
		_loadAladinCloud(12, 2);
	}

	private void loadAladinSutra() {
		_loadAladinWind(925, 36, 0);
		_loadAladinWind(850, 36, 1);
		_loadAladinCloud(36, 2);
	}

	private void _loadAladinWind(int height, int hour, int what) {
		Loader l = new Loader(ALADIN + String.format(ALADIN_WIND, height, hour), what);
		Utils.executor.execute(l);
		titles[what] = "Vjetar (" + height + ",+" + hour + ")";
		setMyTitle();
	}

	public static String getAladin2Date() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour < 5)
			cal.add(Calendar.DAY_OF_YEAR, -1);
		return aladinFormat.format(cal.getTime());
	}

	public static String getAladin2DateFixed() {
		Calendar cal = Calendar.getInstance();
		return aladinFormat.format(cal.getTime());
	}

	private String getAladin2ModelTime() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour > 17 || hour < 5)
			return "1200";
		else
			return "0000";
	}

	private int getAladin2Offset() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour > 17 || hour < 5)
			return -12;
		else
			return 0;
	}

	private void loadAladin2Danas() {
		String date = getAladin2DateFixed();
		int offset = 12;
		_loadAladinWind2(date, 925, offset, "0000", 0);
		_loadAladinWind2(date, 850, offset, "0000", 1);
		_loadAladinCloud2(date, offset, "0000", 2);
	}

	private void loadAladin2Sutra() {
		String date = getAladin2Date();
		int offset = 36 + getAladin2Offset();
		_loadAladinWind2(date, 925, offset, getAladin2ModelTime(), 0);
		_loadAladinWind2(date, 850, offset, getAladin2ModelTime(), 1);
		_loadAladinCloud2(date, offset, getAladin2ModelTime(), 2);
	}

	private void loadAladin2Prekosutra() {
		String date = getAladin2Date();
		int offset = 60 + getAladin2Offset();
		_loadAladinWind2(date, 925, offset, getAladin2ModelTime(), 0);
		_loadAladinWind2(date, 850, offset, getAladin2ModelTime(), 1);
		_loadAladinCloud2(date, offset, getAladin2ModelTime(), 2);
	}

	private void _loadAladinWind2(String date, int height, int hour, String modelTime, int what) {
		String format = null;
		if (height == 925)
			format = ALADIN2_WIND925;
		else
			format = ALADIN2_WIND850;
		Loader l = new Loader(String.format(format, date, height, hour, modelTime), what);
		Utils.executor.execute(l);
		titles[what] = "Vjetar (" + height + ",+" + hour + ")";
		setMyTitle();
	}

	private void _loadAladinCloud(int hour, int what) {
		Loader l = new Loader(ALADIN + String.format(ALADIN_CLOUD, hour), what);
		Utils.executor.execute(l);
		titles[what] = "Naoblaka (+" + hour + ")";
		setMyTitle();
	}

	private void _loadAladinCloud2(String date, int hour, String modelTime, int what) {
		Loader l = new Loader(String.format(ALADIN2_CLOUD, date, hour, modelTime), what);
		Utils.executor.execute(l);
		titles[what] = "Naoblaka (+" + hour + ")";
		setMyTitle();
	}

	private void loadAladin2Temp() {
		String date = getAladin2Date();
		int offset = 12 + getAladin2Offset();
		_loadAladinTemp2(date, offset, getAladin2ModelTime(), 0);
		_loadAladinTemp2(date, offset + 24, getAladin2ModelTime(), 1);
		_loadAladinTemp2(date, offset + 48, getAladin2ModelTime(), 2);
	}

	private void _loadAladinTemp2(String date, int hour, String modelTime, int what) {
		Loader l = new Loader(String.format(ALADIN2_TEMP2M, date, hour, modelTime), what);
		Utils.executor.execute(l);
		titles[what] = "Temp 2m (+" + hour + ")";
		setMyTitle();
	}

	private void loadNaoblakaMA() {
		Date date = new Date();
		int hour = date.getHours();
		if (hour < 11 || hour >= 23) {
			hour = 24;
		} else {
			hour = 12;
		}
		_loadNaoblakaMA(hour, 0);
		_loadNaoblakaMA(hour + 24, 1);
		_loadNaoblakaMA(hour + 48, 2);
	}

	private void _loadNaoblakaMA(int hour, int what) {
		Loader l = new Loader(String.format(NAOBLAKA_MA, hour), what);
		Utils.executor.execute(l);
		titles[what] = "Naoblaka (+" + hour + ")";
		setMyTitle();
	}

	private void loadKondenzacijaMA() {
		Date date = new Date();
		int hour = date.getHours();
		if (hour < 11 || hour >= 23) {
			hour = 24;
		} else {
			hour = 12;
		}
		_loadKondenzacijaMA(hour, 0);
		_loadKondenzacijaMA(hour + 24, 1);
		_loadKondenzacijaMA(hour + 48, 2);
	}

	private void _loadKondenzacijaMA(int hour, int what) {
		Loader l = new Loader(String.format(KONDENZACIJA_MA, hour), what);
		Utils.executor.execute(l);
		titles[what] = "LCL (+" + hour + ")";
		setMyTitle();
	}

	private void loadCapeMA() {
		Date date = new Date();
		int hour = date.getHours();
		if (hour < 11 || hour >= 23) {
			hour = 24;
		} else {
			hour = 12;
		}
		_loadCapeMA(hour, 0);
		_loadCapeMA(hour + 24, 1);
		_loadCapeMA(hour + 48, 2);
	}

	private void _loadCapeMA(int hour, int what) {
		Loader l = new Loader(String.format(CAPE_MA, hour), what);
		Utils.executor.execute(l);
		titles[what] = "CAPE (+" + hour + ")";
		setMyTitle();
	}

	private void loadWindMI() {
		_loadWindMI(12, 0);
		_loadWindMI(36, 1);
		_loadWindMI(60, 2);
	}

	private void _loadWindMI(int hour, int what) {
		Loader l = new Loader(String.format(WIND_MI, hour), what);
		Utils.executor.execute(l);
		titles[what] = "Vjetar (+" + hour + ")";
		setMyTitle();
	}

	private void loadAladinHR() {
		_loadAladinHR(12, 0);
		_loadAladinHR(36, 1);
		_loadAladinHR(60, 2);
	}

	private void _loadAladinHR(int hour, int what) {
		Loader l = new Loader(String.format(ALADIN_HR, hour), what);
		Utils.executor.execute(l);
		titles[what] = "Vjetar (+" + hour + ")";
		setMyTitle();
	}

	private void loadAladinHR_OB() {
		_loadAladinHR_OB(12, 0);
		_loadAladinHR_OB(36, 1);
		_loadAladinHR_OB(60, 2);
	}

	private void _loadAladinHR_OB(int hour, int what) {
		Loader l = new Loader(String.format(ALADIN_HR3, hour), what);
		Utils.executor.execute(l);
		titles[what] = "Oborine (+" + hour + ")";
		setMyTitle();
	}

	private void loadAladin2(String city) {
		_loadAladinDA(city, 12, 0);
		_loadAladinDA(city, 36, 1);
		_loadAladinDA(city, 60, 2);
	}

	private void _loadAladinDA(String city, int hour, int what) {
		Loader l = new Loader(String.format(ALADIN_HR2, city, hour), what);
		Utils.executor.execute(l);
		titles[what] = "Vjetar (+" + hour + ")";
		setMyTitle();
	}

	private void loadNaoblakaMI() {
		_loadNaoblakaMI(12, 0);
		_loadNaoblakaMI(36, 1);
		_loadNaoblakaMI(60, 2);
	}

	private void _loadNaoblakaMI(int hour, int what) {
		Loader l = new Loader(String.format(NAOBLAKA_MI, hour), what);
		Utils.executor.execute(l);
		titles[what] = "Naoblaka (+" + hour + ")";
		setMyTitle();
	}

	private void loadGFS_MR850_THW0() {
		_loadGFS_MR850_THW(24, 0);
		_loadGFS_MR850_THW(48, 1);
		_loadGFS_MR850_THW(72, 2);
	}

	private void loadGFS_MR850_THW() {
		_loadGFS_MR850_THW(96, 0);
		_loadGFS_MR850_THW(120, 1);
		_loadGFS_MR850_THW(144, 2);
	}

	private void _loadGFS_MR850_THW(int hour, int what) {
		Loader l = new Loader(String.format(GFS_MR850_THW, hour), what);
		Utils.executor.execute(l);
		titles[what] = "GFS MRF 850 THW (+" + hour / 24 + "d)";
		setMyTitle();
	}

	private void loadKondenzacijaMI() {
		_loadKondenzacijaMI(12, 0);
		_loadKondenzacijaMI(36, 1);
		_loadKondenzacijaMI(60, 2);
	}

	private void _loadKondenzacijaMI(int hour, int what) {
		Loader l = new Loader(String.format(KONDENZACIJA_MI, hour), what);
		Utils.executor.execute(l);
		titles[what] = "LCL (+" + hour + ")";
		setMyTitle();
	}

	private void loadCapeMI() {
		_loadCapeMI(12, 0);
		_loadCapeMI(36, 1);
		_loadCapeMI(60, 2);
	}

	private void _loadCapeMI(int hour, int what) {
		Loader l = new Loader(String.format(CAPE_MI, hour), what);
		Utils.executor.execute(l);
		titles[what] = "CAPE (+" + hour + ")";
		setMyTitle();
	}

	private void load_PELUD_UV_UGODA() {
		_loadPELUD(peludFormat.format(new Date()), 0);
		_loadUV(1);
		_loadUGODA(2);
	}

	private void _loadPELUD(String date, int what) {
		Loader l = new Loader(String.format(PELUD, date), what);
		Utils.executor.execute(l);
		titles[what] = "PELUD";
		setMyTitle();
	}

	private void _loadUV(int what) {
		Loader l = new Loader(UV, what);
		Utils.executor.execute(l);
		titles[what] = "UV zračenje";
		setMyTitle();
	}

	private void _loadUGODA(int what) {
		Loader l = new Loader(UGODA, what);
		Utils.executor.execute(l);
		titles[what] = "Osjet ugode";
		setMyTitle();
	}

	private void loadSLO_JUTRI_POJUTRI() {
		_loadSLO_JUTRI_POJUTRI(1, 0);
		_loadSLO_JUTRI_POJUTRI(2, 1);
	}

	private void _loadSLO_JUTRI_POJUTRI(int day, int what) {
		Loader l = new Loader(String.format(SLO_JUTRI_POJUTRI, day), what);
		Utils.executor.execute(l);
		titles[what] = "SLO " + (day == 1 ? "jutri" : "pojutri");
		setMyTitle();
	}

	private void select(int id) throws Exception {
		String title = (String) menu.findItem(id).getTitle();
		selectedMenuItemTitle = title;
		switch (id) {

		case R.id.meteoadriatic_zg_vz_ri:
			loadMA3d_ZG_VZ_RI();
			break;
		case R.id.meteoadriatic_pu_os_st:
			loadMA3d_PU_OS_ST();
			break;
		case R.id.sat_radar_munje_menu_item:
			loadSAT24_RADAR_MUNJE();
			break;
		case R.id.sat24_menu_item:
			loadSAT24("ba", "visual");
			break;
		case R.id.sat24_eu_menu_item:
			loadSAT24("eu", "visual");
			break;
		case R.id.sat24_ir_menu_item:
			loadSAT24("gr", "infraPolair");
			break;
		case R.id.sat24_eu_ir_menu_item:
			loadSAT24("eu", "infraPolair");
			break;
		case R.id.kompozitni_menu_item:
			loadKOMPOZITNI_RADAR(-1);
			break;
		case R.id.bilogora_menu_item:
			loadBILOGORA_RADAR(-1);
			break;
		case R.id.osijek_menu_item:
			loadOSIJEK_RADAR(-1);
			break;
		case R.id.puntijarka_menu_item:
			loadPUNTIJARKA_RADAR(-1);
			break;
		case R.id.slo_menu_item:
			loadRADARSLO();
			break;
		case R.id.meteo_fvg_menu_item:
			loadRADAR_FVG();
			break;
		case R.id.slo_tuca_menu_item:
			loadRADARSLOTUCA();
			break;
		case R.id.munje_menu_item:
			loadRADARMUNJE();
			break;
		case R.id.dhmz_akt:
			loadDHMZ_AKT();
			break;
		case R.id.pljusak:
			loadPLJUSAK();
			break;
		case R.id.koka_menu_item:
			loadKOKA();
			break;
		case R.id.jap_menu_item:
			loadJAP();
			break;
		case R.id.sljeme_menu_item:
			loadSLJEME();
			break;
		case R.id.raspadalica_menu_item:
			loadRASPADALICA();
			break;
		case R.id.tribalj_menu_item:
			loadTRIBALJ();
			break;
		case R.id.najave:
			loadNAJAVE();
			break;
		case R.id.olc_danas:
			loadOLC_DANAS();
			break;
		case R.id.skewt_ivanec:
			loadSKEWT("Ivanec");
			break;
		case R.id.skewt_zapresic:
			loadSKEWT("Zaprešić");
			break;
		case R.id.skewt_kalnik:
			loadSKEWT("Kalnik");
			break;
		case R.id.skewt_jastrebarsko:
			loadSKEWT("Jastrebarsko");
			break;
		case R.id.skewt_os:
			loadSKEWT("Osijek");
			break;
		case R.id.skewt_buzet:
			loadSKEWT("Buzet");
			break;
		case R.id.skewt_bjelopolje:
			loadSKEWT("Bjelopolje");
			break;
		case R.id.skewt_greda:
			loadSKEWT("Greda");
			break;
		case R.id.skewt_tribalj:
			loadSKEWT("Tribalj");
			break;
		case R.id.skewt_lic:
			loadSKEWT("Lić");
			break;
		case R.id.skewt_golte:
			loadSKEWT("Golte");
			break;
		case R.id.skewt_gabrje:
			loadSKEWT("Camp Gabrje");
			break;
		case R.id.skewt_smuk:
			loadSKEWT("Smuk");
			break;
		case R.id.skewt_slivnica:
			loadSKEWT("Slivnica");
			break;
		case R.id.skewt_lisca:
			loadSKEWT("Lisca");
			break;
		case R.id.skewt_udine:
			loadSKEWT("Udine");
			break;
		case R.id.meteogrami3d_zg_vz_pj:
			loadMeteogrami3d_zg_vz_pj();
			break;
		case R.id.meteogrami3d_ri_sinj_mo:
			loadMeteogrami3d_ri_sinj_mo();
			break;
		case R.id.ens_zg_vz_ri:
			loadENS15D_ZG_VZ_RI();
			break;
		case R.id.ens_pu_os_st:
			loadENS15D_PU_OS_ST();
			break;
		case R.id.ens_zg_vz_ri_2:
			loadENS15D_ZG_VZ_RI_2();
			break;
		case R.id.ens_pu_os_st_2:
			loadENS15D_PU_OS_ST_2();
			break;
		case R.id.gfs_mr850_thw0:
			loadGFS_MR850_THW0();
			break;
		case R.id.gfs_mr850_thw:
			loadGFS_MR850_THW();
			break;
		case R.id.aladin_hr:
			loadAladinHR();
			break;
		case R.id.aladin_hr_ob:
			loadAladinHR_OB();
			break;
		case R.id.aladin_senj:
			loadAladin2("SENJ");
			break;
		case R.id.aladin_masl:
			loadAladin2("MASL");
			break;
		case R.id.aladin_spli:
			loadAladin2("SPLI");
			break;
		case R.id.aladin_dubr:
			loadAladin2("DUBR");
			break;
		case R.id.aladin_danas:
			loadAladinDanas();
			break;
		case R.id.aladin_sutra:
			loadAladinSutra();
			break;
		case R.id.aladin2_danas:
			loadAladin2Danas();
			break;
		case R.id.aladin2_sutra:
			loadAladin2Sutra();
			break;
		case R.id.aladin2_prekosutra:
			loadAladin2Prekosutra();
			break;
		case R.id.aladin2_temp2m:
			loadAladin2Temp();
			break;
		case R.id.naoblaka_ma:
			loadNaoblakaMA();
			break;
		case R.id.kondenzacija_ma:
			loadKondenzacijaMA();
			break;
		case R.id.cape_ma:
			loadCapeMA();
			break;
		case R.id.cape_mi:
			loadCapeMI();
			break;
		case R.id.wind_mi:
			loadWindMI();
			break;
		case R.id.naoblaka_mi:
			loadNaoblakaMI();
			break;
		case R.id.kondenzacija_mi:
			loadKondenzacijaMI();
			break;
		case R.id.dhmz_danas_sutra:
			loadDHMZDanasSutra();
			break;
		case R.id.dhmz_danas_sutra_w:
			loadDHMZDanasSutraW();
			break;
		case R.id.dhmz_danas_sutra_zg:
			loadDHMZDanasSutraZG();
			break;
		case R.id.dhmz4d:
			loadDHMZ4d();
			break;
		case R.id.webcam1:
			loadWEBCAM1();
			break;
		case R.id.webcam2:
			loadWEBCAM2();
			break;
		case R.id.webcam_slo:
			loadWEBCAM_SLO();
			break;
		case R.id.yrno:
			loadYRNO();
			break;
		case R.id.yrno2:
			loadYRNO2();
			break;
		case R.id.napoved_letalstvo:
			loadNapovedLetalstvo();
			break;
		case R.id.nasa_menu_item:
			loadNASAIOTD();
			break;
		case R.id.nasa_web_menu_item:
			loadNASAIOTDURL();
			break;
		case R.id.terra_menu_item:
			loadMODIS_TERRA();
			break;
		case R.id.aqua_menu_item:
			loadMODIS_AQUA();
			break;
		case R.id.meteoalarm_hr:
			loadMETEOALARM("HR");
			break;
		case R.id.meteoalarm_si:
			loadMETEOALARM("SI");
			break;
		case R.id.pelud_uv_ugoda:
			load_PELUD_UV_UGODA();
			break;
		case R.id.arso_jutri_pojutri:
			loadSLO_JUTRI_POJUTRI();
			break;
		case R.id.webcam_slo2:
			loadWEBCAM_SLO2();
			break;
		case R.id.webcam_slo3:
			loadWEBCAM_SLO3();
			break;
		case R.id.settings:
			Intent launchPreferencesIntent = new Intent().setClass(this, Preferences.class);
			startActivity(launchPreferencesIntent);
			return;
		case R.id.help:
			Utils.showHelp(this);
			return;
		case R.id.ocijeni:
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=hr.cloudwalk.currweather")));
			return;
		default:
			return;
		}
		int count = 0;
		if (usage.has(title)) {
			count = usage.getInt(title);
		}
		count++;
		usage.put(title, count);
		saveUsage(usage);
		return;

	}

	private void select(String title) throws Exception {
		String menuTitle = null;
		int i = 0;
		for (; i < menuIds.size(); i++) {
			MenuItem menuItem = menu.findItem(menuIds.get(i));
			if (menuItem.getTitle().equals(title)) {
				select(menuItem.getItemId());
				return;
			}
		}
		Toast.makeText(TrenutnoVrijemeActivity.this, "Opcija izbornika: '" + title + "' više ne postoji. Izbrišite thumbnail s 'LONG TAP'!.",
				Toast.LENGTH_SHORT).show();

		return;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			setDefaultAnim4ImageViews();
			select(item.getItemId());
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
		return true;
	}

	private Animation getImgAnimation(ImageView img) {

		Integer anim = (Integer) img.getTag(R.id.ANIMATION);
		switch (anim) {
		case -1:
			return left2Right;
		case 1:
			return right2Left;
		case 0:
			return fadeIn;
		default:
			return null;
		}
	}

	private void setDefaultAnim4ImageViews() {
		((ImageView) findViewById(R.id.img1)).setTag(R.id.ANIMATION, 0);
		((ImageView) findViewById(R.id.img2)).setTag(R.id.ANIMATION, 0);
		((ImageView) findViewById(R.id.img3)).setTag(R.id.ANIMATION, 0);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	void checkFirst() {
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("firstTimeUse", true)) {
			Utils.showHelp(this);
			PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("firstTimeUse", false).commit();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		moveAnimator.running = false;
	}
}