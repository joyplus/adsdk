package com.pmp.sdk.data;


import android.net.Uri;
import android.text.TextUtils;

public class AdRequest {

	private RequestParams mRequestParams;
	private String Random;
	private String AdType;
	private AdRequest(){
		this(null, null, null);
	}
	public AdRequest(RequestParams requestParams){
		this(requestParams, null, null);
	}
	public AdRequest(RequestParams requestParams, String adtype, String random){
		mRequestParams = requestParams;
		Random = random;
		AdType = adtype;
	}
	public Uri toUri(){
		try{
			Uri.Builder b = Uri.parse(HttpManager.PMP_SERVICE).buildUpon();
			if(mRequestParams != null){
				mRequestParams.setAdtype(AdType);
				if(mRequestParams.checkAllMustParams()){
					b = mRequestParams.appendQUeryParamter(b, Random);
					return b.build();
				}
			} 
		}catch(Throwable e){ 
			android.util.Log.d("PMP","toUri fail-->"+ e.toString());
			e.printStackTrace();
		}
		return null;
	}
}
