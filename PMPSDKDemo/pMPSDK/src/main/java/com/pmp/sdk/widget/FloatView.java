package com.pmp.sdk.widget;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import com.pmp.sdk.AdSDKManagerCompat;
import com.pmp.sdk.data.AnimationUtils;
import com.pmp.sdk.data.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class FloatView extends RelativeLayout {
	public static final String IMAGE_BODY = "<body style='\"'margin: 0px; padding: 0px; text-align:center;'\"'><img src='\"'{0}'\"' width='\"'{1}'dp\"' height='\"'{2}'dp\"'/>{3}</body>";
	//	public static final String IMAGE_BODY = "<body style='\"'margin: 0px; padding: 0px; text-align:center;'\"'><div style=\"background-image:{0} width='\"'{1}'dp\"' height='\"'{2}'dp\"'></div></body>";
	public static final String REDIRECT_URI = "REDIRECT_URI";

	public static final String IMP_TRACKING_TEMPLATE = "<img src='\"'{0}'\"' />";

	public static final String HIDE_BORDER = "<style>* { -webkit-tap-highlight-color: rgba(0,0,0,0);} img {width:100%;height:100%}</style>";
	//	public static final String HIDE_BORDER = "<style>* { -webkit-tap-highlight-color: rgba(0,0,0,0) }</style>";

	private   boolean       mAnimation;
	private   Response      mResponse;
    private   String        mContent;
	private   WebSettings   mWebSettings;
	private   Context       mContext = null;
	private   WebView       mfirstWebView;
	private   static Method mWebView_SetLayerType;
	private   static Field  mWebView_LAYER_TYPE_SOFTWARE;

	public FloatView(final Context context, final Response response){
		this(context, response, true);
	}

	public FloatView(final Context context, final Response response, final boolean animation) {
		super(context);
		mResponse = response;
		mContext = context;
		this.mAnimation = animation;
	}

    public FloatView(Context context, String content, final boolean animation) {
        super(context);
        mContent = content;
        mContext = context;
        this.mAnimation = animation;
    }

    public FloatView(Context context, String content) {
        this(context, content, true);
    }

    private WebView createWebView(final Context context) {
//		final WebView webView = new WebView(context) {
//			@Override
//			public void draw(final Canvas canvas) {
//				if (this.getWidth() > 0 && this.getHeight() > 0)
//					super.draw(canvas);
//			}
//		};
		WebView webView = new WebView(context){
			@Override
			public boolean onTouchEvent(MotionEvent event) {
				// TODO Auto-generated method stub
				if(mResponse != null && mResponse.adunit != null 
						&& (TextUtils.isEmpty(mResponse.adunit.clickUrl) || mResponse.isloadUriNoImage())){
						return super.onTouchEvent(event);
				}
				return false;
			}
		};
		webView.setBackgroundColor(Color.TRANSPARENT);
		mWebSettings = webView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		setLayer(webView);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				android.util.Log.d("PMP","shouldOverrideUrlLoading-->" + url);
				if(mResponse.isloadUriNoImage()){
					doOpenUrl(url);
					return true;
				}
				view.loadUrl(url);
				return true;
			}
		});
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		return webView;
	}

	private void doOpenUrl(final String url) {
		notifyAdClicked();
		if(TextUtils.isEmpty(url))return;
		if ((url.startsWith("http://") || url.startsWith("https://"))) {
			if(url.endsWith(".mp4")){
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.parse(url), "video/mp4");
				getContext().startActivity(i);
			}
		} 
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		getContext().startActivity(intent);
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

	private void setLayer(WebView webView){
		if (mWebView_SetLayerType != null && mWebView_LAYER_TYPE_SOFTWARE !=null) {
			try {
				mWebView_SetLayerType.invoke(webView, mWebView_LAYER_TYPE_SOFTWARE.getInt(WebView.class), null);
			} catch (InvocationTargetException ite) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}
	
	private void buildBannerView(){
		if(mfirstWebView != null){
			mfirstWebView.startAnimation(AnimationUtils.GetExitTranslateAnimation(mTranslateAnimationType));
			FloatView.this.removeView(mfirstWebView);
			mfirstWebView = null;
		}
		mfirstWebView = createWebView(mContext);
		setLayoutParams(new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT
				,android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		this.addView(mfirstWebView, params);
	}
	
	public void Show(){
		Show(mResponse);
	}
	public void Show(final Response ad) {
		if(ad == null)return;
        mResponse  = ad; 
        if(AdSDKManagerCompat.isAviableResponse(mResponse)){
        	initCompatibility();
        	buildBannerView();
        	showContent();
        }
	}

    public void Show(final String ad) {
        if(ad == null)return;
        mContent  = ad;
        if(mContent != null && !"".equals(mContent)){
            initCompatibility();
            buildBannerView();
            showContent();
        }
    }

	private void notifyAdClicked() {
		android.util.Log.d("PMP","notifyAdClicked");
		AdSDKManagerCompat.reportClick(mResponse);
	}
	private void notifyLoadAdSucceeded() {
		android.util.Log.d("PMP","notifyLoadAdSucceeded");
		AdSDKManagerCompat.reportImp(mResponse);
	}
	private void showContent() {
		  try {

              if (mContent != null) {
                  mContent = mContent.replaceAll("localhost", "10.0.2.2");
                  mfirstWebView.loadData(mContent, "text/html", "UTF-8");
                  if(mAnimation){
                      mfirstWebView.startAnimation(AnimationUtils.GetTranslateAnimation(mTranslateAnimationType));
                  }
                  return;

              } else if(mResponse.isloadUriNoImage()){
				 mfirstWebView.clearCache(false);
				 mfirstWebView.loadUrl(mResponse.adunit.creativeUrls.get(0));
			 }else{
            	mfirstWebView.clearCache(false);
//					String text = mResponse.GetCreative_res_url();
//					text = Uri.encode(Const.HIDE_BORDER + text);
//					mfirstWebView.loadData(text, "text/html", Const.ENCODING);
            	android.util.Log.d("PMP","load image -->" + mResponse.adunit.creativeUrls.get(0));
				String strImpUrls ="";
				 for(String impUrl:mResponse.adunit.getImpTrackingUrls()){
					 String strImpUrl = MessageFormat.format(IMP_TRACKING_TEMPLATE, impUrl);
					 strImpUrls +=strImpUrl;
				 }

            	String text = MessageFormat.format(IMAGE_BODY, mResponse.adunit.creativeUrls.get(0) ,"","",strImpUrls);

				 android.util.Log.d("PMP","load html -->" + text);

        		text = Uri.encode(HIDE_BORDER + text);
        		mfirstWebView.loadData(text, "text/html","UTF-8");
			    if(!TextUtils.isEmpty(mResponse.adunit.clickUrl)){
			    	mfirstWebView.setClickable(true);
			    	mfirstWebView.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							doOpenUrl(mResponse.adunit.clickUrl);
							notifyAdClicked();
						}
			    	});
			    }
			    //String baseUrl = mResponse.adunit.creativeUrls.get(0);    
			    //mfirstWebView.loadUrl(baseUrl);
			} 
			notifyLoadAdSucceeded();
            if(mAnimation){
            	mfirstWebView.startAnimation(AnimationUtils.GetTranslateAnimation(mTranslateAnimationType));
            }
		} catch (Throwable t) {}
	}
	public boolean onUserClick(){
		if(mResponse!=null && mResponse.adunit!=null && !TextUtils.isEmpty(mResponse.adunit.clickUrl)){
	    	doOpenUrl(mResponse.adunit.clickUrl);
	    } 
		return false;
	}
	public void SetAnimation(AnimationUtils.TranslateAnimationType type){
		mTranslateAnimationType = type;
	}
	private AnimationUtils.TranslateAnimationType mTranslateAnimationType = AnimationUtils.TranslateAnimationType.RANDOM;
}
