package com.example.pmpsdkdemo;

import com.pmp.sdk.AdSDKManager;
import com.pmp.sdk.AdSDKManagerException;

import android.app.Application;

public class AdApplication extends Application{
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try {
			AdSDKManager.init(getApplicationContext());
		} catch (AdSDKManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
