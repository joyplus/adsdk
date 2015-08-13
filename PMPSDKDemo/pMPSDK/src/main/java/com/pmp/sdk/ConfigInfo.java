package com.pmp.sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pmp.sdk.data.RequestParams;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class ConfigInfo {
	
	public static String pkgname;
	public static String appname;//maybe change by location.
	public static String osv;//
	public static String device;
	
	public static void init(Context context){
		ApplicationInfo applicationInfo = context.getApplicationInfo();
		pkgname = applicationInfo.packageName;
		appname = applicationInfo.labelRes > 0 ? context.getString(applicationInfo.labelRes) : "";
		osv     = TextUtils.isEmpty(Build.VERSION.RELEASE) ? "4.2" : Build.VERSION.RELEASE;//for default is 4.3
		device  = new Build().MODEL;
	}
	public static void tearDown(Context context){
		pkgname = null;
		appname = null;
		osv     = null;
		device  = null;
	}
	public static String getImei(Context context){
		try{
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getDeviceId();//tm.getiemi(); params 0 . its hide.
		}catch(Throwable e){android.util.Log.d("PMP","getiemi fail -->"+e.toString());}
		return "";
	}
	public static String getMAC(Context context){
		String mac = getWIFIMacAddress(context);
		if(!TextUtils.isEmpty(mac)){
			mac = mac.trim().replaceAll(":", "");
			return mac.toUpperCase();
		}
		return "";
	}
	private static String getWIFIMacAddress(Context context){
		String macAddress = "";
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (info != null) {
			macAddress = info.getMacAddress();
		}
		return macAddress;
    } 
	public static boolean checkParams(){
		return !(TextUtils.isEmpty(appname)
				||TextUtils.isEmpty(pkgname)
				||TextUtils.isEmpty(osv)
				||TextUtils.isEmpty(device));
	}
	public static Uri.Builder appendQUeryParamter(Uri.Builder builder){
		if(builder != null){
			builder.appendQueryParameter("appname", appname);
			builder.appendQueryParameter("pkgname", pkgname);
			builder.appendQueryParameter("osv", osv);
			builder.appendQueryParameter("device", device);
		}
		return builder;
	}
	
	private static ArrayList<String> CARRIER_CHINA_MOBLIE_LIST = new ArrayList<String>(){
		{add("46000"); add("46002");}
	};
	private static List<String> CARRIER_CHINA_UNICOM_LIST = new ArrayList<String>(){
		{add("46001");}
	};
	private static List<String> CARRIER_CHINA_TELECOM_LIST = new ArrayList<String>(){
		{add("46003");}
	};
	
	public static String getcarrier(Context context){
		try{
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String carrier = tm.getSimOperator();
			if(!TextUtils.isEmpty(carrier)){
				if(CARRIER_CHINA_MOBLIE_LIST.contains(carrier)) return RequestParams.CARRIER_CHINA_MOBLIE;
				if(CARRIER_CHINA_UNICOM_LIST.contains(carrier)) return RequestParams.CARRIER_CHINA_UNICOM;
				if(CARRIER_CHINA_TELECOM_LIST.contains(carrier)) return RequestParams.CARRIER_CHINA_TELECOM;
			}
		}catch(Throwable e){android.util.Log.d("PMP","getcarrier fail -->"+e.toString());}
		return RequestParams.CARRIER_CHINA_UNICOM;
	}
	
	public static String getconn(Context context){
		int networkStatePermission = context
				.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
		if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null) {
				return RequestParams.CONN_UNKNOW;
			}
			int netType = info.getType();
			int netSubtype = info.getSubtype();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				return RequestParams.CONN_WIFI;
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				switch (netSubtype) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case 14:
				case 12:
				case 8:
				case 10:
				case 15:
				case 9:
				case 11:
					return RequestParams.CONN_2G;
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return RequestParams.CONN_3G;
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case 13:
					return RequestParams.CONN_4G;
				}
			}
		}
		return RequestParams.CARRIER_UNKNOW;
	}
	
	public static String getdensity(Context context){
		return  "" + context.getResources().getDisplayMetrics().density;
	}
	
}
