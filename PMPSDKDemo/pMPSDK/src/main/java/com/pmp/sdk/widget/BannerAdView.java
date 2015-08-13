package com.pmp.sdk.widget;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import com.pmp.sdk.AdSDKManagerCompat;
import com.pmp.sdk.data.Response;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

public class BannerAdView extends RelativeLayout {
	public static final String IMAGE_BODY = "<body style='\"'margin: 0px; padding: 0px; text-align:center;'\"'><img src='\"'{0}'\"' width='\"'{1}'dp\"' height='\"'{2}'dp\"'/></body>";
	//	public static final String IMAGE_BODY = "<body style='\"'margin: 0px; padding: 0px; text-align:center;'\"'><div style=\"background-image:{0} width='\"'{1}'dp\"' height='\"'{2}'dp\"'></div></body>";
	public static final String REDIRECT_URI = "REDIRECT_URI";

	public static final String HIDE_BORDER = "<style>* { -webkit-tap-highlight-color: rgba(0,0,0,0);} img {width:100%;height:100%}</style>";
	//	public static final String HIDE_BORDER = "<style>* { -webkit-tap-highlight-color: rgba(0,0,0,0) }</style>";

	public static final int LIVE = 0;
	public static final int TEST = 1;

	private boolean animation;

	private boolean isInternalBrowser = false;

	private Response response;
	private Animation fadeInAnimation = null;
	private Animation fadeOutAnimation = null;
	private WebSettings webSettings;

	private Context mContext = null;
	protected boolean mIsInForeground;

	private WebView firstWebView;
	private WebView secondWebView;

	private ViewFlipper viewFlipper;


	private static Method mWebView_SetLayerType;
	private static Field mWebView_LAYER_TYPE_SOFTWARE;

	public BannerAdView(final Context context, final Response response) {
		this(context, response, false);
	}
	
	public BannerAdView(final Context context, final Response response, final boolean animation) {
		super(context);
		this.response = response;
		mContext = context;
		this.animation = animation;
		this.initialize(context);
	}

	private WebView createWebView(final Context context) {
		final WebView webView = new WebView(context) {
			@Override
			public void draw(final Canvas canvas) {
				if (this.getWidth() > 0 && this.getHeight() > 0)
					super.draw(canvas);
			}
		};
		this.webSettings = webView.getSettings();
		this.webSettings.setJavaScriptEnabled(true);
		webView.setBackgroundColor(Color.TRANSPARENT);
		setLayer(webView);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				if(TextUtils.equals("3", response.adunit.creativeType)){//open 
					doOpenUrl(url);
					return true;
				}
				if(!TextUtils.isEmpty(url)){
					view.loadUrl(url);
				}
				return true;
			}
		});
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		return webView;
	}

	private void doOpenUrl(final String url) {
		notifyAdClicked();
		if (!TextUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://"))) {
			if(url.endsWith(".mp4")){
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.parse(url), "video/mp4");
				this.getContext().startActivity(i);
				return;
			}
		}
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		this.getContext().startActivity(intent);
	}

	static {
		initCompatibility();
	};

	private static void initCompatibility() {
		try {
			for(Method m:WebView.class.getMethods()){
				if(m.getName().equals("setLayerType")){
					mWebView_SetLayerType = m;
					break;
				}
			}
			mWebView_LAYER_TYPE_SOFTWARE = WebView.class.getField("LAYER_TYPE_SOFTWARE");
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
	}

	private static void setLayer(WebView webView){
		if (mWebView_SetLayerType != null && mWebView_LAYER_TYPE_SOFTWARE !=null) {
			try {
				mWebView_SetLayerType.invoke(webView, mWebView_LAYER_TYPE_SOFTWARE.getInt(WebView.class), null);
			} catch (InvocationTargetException ite) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
		else{
		}
	}

	private void buildBannerView(){
		this.firstWebView = this.createWebView(mContext);
		this.secondWebView = this.createWebView(mContext);
		this.viewFlipper = new ViewFlipper(this.getContext()) {
			@Override
			protected void onDetachedFromWindow() {
				try {
					super.onDetachedFromWindow();
				} catch (final IllegalArgumentException e) {
					this.stopFlipping();
				}
			}
		};
		final float scale = mContext.getResources().getDisplayMetrics().density;
//		this.setLayoutParams(new RelativeLayout.LayoutParams((int)(300*scale+0.5f), (int)(50*scale+0.5f)));
		this.setLayoutParams(new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT
				,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		final FrameLayout.LayoutParams webViewParams = new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		this.viewFlipper.addView(this.firstWebView, webViewParams);
		this.viewFlipper.addView(this.secondWebView, webViewParams);

		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		this.addView(this.viewFlipper, params);

		if (this.animation) {
			
			this.fadeInAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, +1.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			this.fadeInAnimation.setDuration(1000);

			this.fadeOutAnimation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f);
			this.fadeOutAnimation.setDuration(1000);
			this.viewFlipper.setInAnimation(this.fadeInAnimation);
			this.viewFlipper.setOutAnimation(this.fadeOutAnimation);
		}
	}

	private void initialize(final Context context) {
		initCompatibility();
		buildBannerView();
		showContent();
	}

	public boolean isInternalBrowser() {
		return this.isInternalBrowser;
	}

	private void notifyAdClicked() {
		AdSDKManagerCompat.reportClick(response);
	}
	private void notifyAdImped() {
		AdSDKManagerCompat.reportImp(response);
	}
	
	public void setInternalBrowser(final boolean isInternalBrowser) {
		this.isInternalBrowser = isInternalBrowser;
	}

	private void showContent() {
		try {
			WebView webView;
			if (this.viewFlipper.getCurrentView() == this.firstWebView)
				webView = this.secondWebView;
			else
				webView = this.firstWebView;
			notifyAdImped();//report first
			if(response.isloadUriNoImage()){
				android.util.Log.d("PMP", "isloadUri-->" + response.adunit.creativeUrls);
				webView.loadUrl(response.adunit.creativeUrls.get(0));
			}else{
				String text = MessageFormat.format(IMAGE_BODY,
						response.adunit.creativeUrls,
						response.adunit.adWidth,
						response.adunit.adHeight);
				text = Uri.encode(HIDE_BORDER + text);
				webView.loadData(text, "text/html", "UTF-8");
				android.util.Log.d("PMP", "isloadImg-->" + text);
		    }
			if (this.viewFlipper.getCurrentView() == this.firstWebView) {
				this.viewFlipper.showNext();
			} else {
				this.viewFlipper.showPrevious();
			}
		} catch (final Throwable t) {
		}
	}
	
}
