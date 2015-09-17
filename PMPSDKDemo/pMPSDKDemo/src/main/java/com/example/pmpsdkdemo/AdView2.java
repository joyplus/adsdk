package com.example.pmpsdkdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.pmp.sdk.AdSDKManagerCompat;
import com.pmp.sdk.data.AdImageRequest;
import com.pmp.sdk.data.RequestException;
import com.pmp.sdk.data.RequestParams;
import com.pmp.sdk.data.Response;
import com.pmp.sdk.widget.FloatView;

public class AdView2 extends RelativeLayout{
	private RequestParams mRequestParams;
	public AdView2(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public AdView2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public AdView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mRequestParams = getRequestParams(context, attrs);
	}
	private RequestParams getRequestParams(Context context, AttributeSet attrs){
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.pmp);
    	RequestParams requestParams = new RequestParams();
    	android.util.Log.d("PMP","pmp_pmp_adspaceid="+ta.getString(R.styleable.pmp_pmp_adspaceid));
    	android.util.Log.d("PMP","pmp_pmp_width="+ta.getString(R.styleable.pmp_pmp_width));
    	android.util.Log.d("PMP","pmp_pmp_height="+ta.getString(R.styleable.pmp_pmp_height));
    	android.util.Log.d("PMP","pmp_pmp_pcat="+ta.getString(R.styleable.pmp_pmp_pcat));
    	requestParams.setAdspaceid(ta.getString(R.styleable.pmp_pmp_adspaceid));
    	requestParams.setWidth(ta.getString(R.styleable.pmp_pmp_width));
    	requestParams.setHeight(ta.getString(R.styleable.pmp_pmp_height));
    	requestParams.setPcat(ta.getString(R.styleable.pmp_pmp_pcat));
    	ta.recycle();
    	return requestParams;
    }
	private boolean isWindowAdded = false;
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		isWindowAdded = true;
	}
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		isWindowAdded = false;
		mHanlder.sendEmptyMessage(MSG_STATE_CHANGE);
	}
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		// TODO Auto-generated method stub
		super.onVisibilityChanged(changedView, visibility);
		android.util.Log.d("PMP","onVisibilityChanged isWindowAdded="+isWindowAdded
				+ ", visibility=" + (visibility == VISIBLE));
		if(visibility == VISIBLE ){
			mHanlder.sendEmptyMessage(MSG_REQUESTAD);
		}
	}

	private void requestAd() {
		// TODO Auto-generated method stub
		if(mRequestThread == null && mRequestParams != null){
			mRequestThread = new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						android.util.Log.d("PMP","requestAd requestAd");
						String response = new AdImageRequest(mRequestParams).sendRequestSimple();
						if(response != null && !"".equals(response)){
							mHanlder.removeMessages(MSG_SHOW_AD);
							Message m = mHanlder.obtainMessage(MSG_SHOW_AD);
							m.obj     = response;
							m.sendToTarget();
						}
					} catch (RequestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mRequestThread = null;
				}
			});
			mRequestThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					mRequestThread = null;
				}
			});
			mRequestThread .start();
		}
	}
	private Thread mRequestThread;
	private FloatView mFloatView;
	private final static int MSG_REQUESTAD    = 1;
	private final static int MSG_STATE_CHANGE = 2;
	private final static int MSG_SHOW_AD      = 3;
	private Handler mHanlder = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case MSG_REQUESTAD:{
				requestAd();
			}break;
			case MSG_STATE_CHANGE:{
				if(!isWindowAdded){
					if(mFloatView != null){
						AdView2.this.removeAllViews();
						mFloatView = null;
					}
				}
			}break;
			case MSG_SHOW_AD:{
				String response = (String)msg.obj;
				if(isWindowAdded){
					if(mFloatView == null){
						mFloatView = new FloatView(AdView2.this.getContext(), response);
						AdView2.this.addView(mFloatView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
					}
					mFloatView.Show(response);
				}
			}break;
			}
		};
	};
}
