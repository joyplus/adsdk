package com.pmp.sdk;

import java.util.ArrayList;
import java.util.List;

import com.pmp.sdk.mode.Report;
import com.pmp.sdk.mode.ReportController;
import android.content.Context;

public class AdSDKManager {
	private Context mContext;
	private static AdSDKManager mInstance;
	public static AdSDKManager getInstance(){
		return mInstance;
	}
	public Context getContext(){
		return mContext;
	}
	private static boolean isInited = false;
	public static synchronized void init(Context context) throws AdSDKManagerException{
		if(!isInited){
			mInstance = new AdSDKManager(context);
			isInited = true;
		}
	}
	public static synchronized void tearDown(){
		if(isInited){
			if(mInstance != null){
				Context context = mInstance.getContext();
				ConfigInfo.tearDown(context);
				if(mReportController != null){
					mReportController.tearDown();
				}
				mReportController = null;
			}
			isInited = false;
		}
	}
	public static boolean isInited(){
		return isInited;
	}
	private AdSDKManager() throws AdSDKManagerException{this(null);}
	private AdSDKManager(Context context) throws AdSDKManagerException{
		if(context == null)throw new AdSDKManagerException("why context is null");
		mContext = context.getApplicationContext();
		ConfigInfo.init(mContext);
		mReportController = new ReportController();
	}
	/////////////////
	private static ReportController mReportController;
	public static boolean reportImpTracking(List<String> uri){
		return addReport(Report.TYPE_IMPTRACKINT, uri);
	}
	public static boolean reportClkTracking(List<String> uri){
		return addReport(Report.TYPE_CLKTRACKING, uri);
	}
	private static boolean addReport(int type, List<String> uri){
		if(isInited() && mReportController != null){
			if(uri != null && uri.size()>0){
				List<Report> lists = new ArrayList<Report>();
				for(String u : uri){
					lists.add(new Report(type, u));
				}
				mReportController.addReport(lists);
			}
			return true;
		}
		return false;
	}
}
