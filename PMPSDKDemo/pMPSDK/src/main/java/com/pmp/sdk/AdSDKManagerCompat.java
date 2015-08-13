package com.pmp.sdk;

import java.util.ArrayList;

import android.text.TextUtils;

import com.pmp.sdk.data.Response;

public class AdSDKManagerCompat {
	
	public static void reportClick(final Response response){
		if(response != null && response.adunit != null){
			AdSDKManager.reportClkTracking(new ArrayList<String>(){{
				add(response.adunit.clkTrackingUrls);
			}});
		}
	}
	public static void reportImp(Response response){
		if(response != null && response.adunit != null){
			AdSDKManager.reportClkTracking(response.adunit.impTrackingUrls);
		}
	}
	
	public static boolean isAviableResponse(Response response){
		try{
			if(response != null && response.adunit != null 
					&& Integer.parseInt(response.statusCode) == 200){
				return !(response.adunit.creativeUrls ==null
							|| response.adunit.creativeUrls.size()<=0
							||TextUtils.isEmpty(response.adunit.creativeType));
		}
		}catch(Throwable e){android.util.Log.d("PMP","isAviableResponse fail -->"+e.toString());}
		return false;
	}
}
