package com.pmp.sdk.data;

import com.pmp.sdk.AdSDKManager;
import com.pmp.sdk.AdSDKManagerException;
import com.pmp.sdk.ConfigInfo;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.text.TextUtils;

public class RequestParams {
	private String bid = "";
	private String adspaceid = "";
	private String pkgname = "";
	private String appname = "";
	private String conn = "";
	private String adtype = "";
	public final static String CONN_UNKNOW = "0";
	public final static String CONN_WIFI   = "1";
	public final static String CONN_2G     = "2";
	public final static String CONN_3G     = "3";
	public final static String CONN_4G     = "4";
	private String carrier = "";
	public final static String CARRIER_UNKNOW        = "0";
	public final static String CARRIER_CHINA_MOBLIE  = "1";
	public final static String CARRIER_CHINA_UNICOM  = "2";
	public final static String CARRIER_CHINA_TELECOM = "3";
	private final static String os = "0";//0 android, 1 iOS, 2 WP, 3 others
	private static String osv = "";
	private String imei = "";
	private String wma = "";
	private String aid = "";
	private String aaid = "";
	
	private final static String idfa = "";//for android no use
	private final static String oid  = "";//for android unuse
	private final static String uid  = "";//for android unuse
	
	private String device = "";//
	private String width = "";//
	private String height = "";//
	private String pcat = "";//
	private String density = "";
	
	private String lon = "";
	private String lat = "";
	
	public boolean checkAllMustParams() throws AdSDKManagerException{
		String result = null;
		if(TextUtils.isEmpty(adspaceid)){
			result = "adspaceid";
		}else if(TextUtils.isEmpty(os)){
			result = "os";
		}else if(TextUtils.isEmpty(width)){
			result = "width";
		}else if(TextUtils.isEmpty(height)){
			result = "height";
		}else if(TextUtils.isEmpty(pcat)){
			result = "pcat";
		}else if(TextUtils.isEmpty(adtype)){
			result = "adtype";
		}
		if(TextUtils.isEmpty(result)){
			return true;
		}
		throw new AdSDKManagerException(result);
	}
	private String buildbid(String random){
		return adspaceid + System.currentTimeMillis() + random;
	}
	public Uri.Builder appendQUeryParamter(Uri.Builder builder, String random){
		if(builder != null){
			Context context = AdSDKManager.getInstance().getContext();
			builder.appendQueryParameter("adtype", adtype);
			builder.appendQueryParameter("bid", buildbid(random));
			builder.appendQueryParameter("adspaceid", adspaceid);
			//pkgname, appname, device, conn, carrier, os, osv, imei, wma, aid, aaid
			builder = ConfigInfo.appendQUeryParamter(builder);//pkgname, appname, osv, device
			builder.appendQueryParameter("conn", 
					TextUtils.isEmpty(conn) ? ConfigInfo.getconn(context) : conn);
			builder.appendQueryParameter("carrier", 
					TextUtils.isEmpty(carrier) ? ConfigInfo.getcarrier(context) : carrier);
			builder.appendQueryParameter("os", os);
			builder.appendQueryParameter("imei", 
					TextUtils.isEmpty(imei) ? ConfigInfo.getImei(context) : imei);
			builder.appendQueryParameter("wma", 
					TextUtils.isEmpty(wma) ? ConfigInfo.getMAC(context) : wma);
			builder.appendQueryParameter("aid", aid);
			builder.appendQueryParameter("aaid", aaid);
			builder.appendQueryParameter("idfa", idfa);
			builder.appendQueryParameter("oid", oid);
			builder.appendQueryParameter("uid", uid);
			builder.appendQueryParameter("device", 
					TextUtils.isEmpty(device) ? ConfigInfo.device : device);
			builder.appendQueryParameter("width", width);
			builder.appendQueryParameter("height", height);
			builder.appendQueryParameter("pcat", pcat);
			builder.appendQueryParameter("density", 
					TextUtils.isEmpty(density) ? ConfigInfo.getdensity(context) : density);
		}
		return builder;
	}
	////////////////////////////////////////////////////////////////
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getAdspaceid() {
		return adspaceid;
	}
	public void setAdspaceid(String adspaceid) {
		this.adspaceid = adspaceid;
	}
	public String getPkgname() {
		return pkgname;
	}
	public void setPkgname(String pkgname) {
		this.pkgname = pkgname;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getConn() {
		return conn;
	}
	public void setConn(String conn) {
		this.conn = conn;
	}
	public String getAdtype() {
		return adtype;
	}
	public void setAdtype(String adtype) {
		this.adtype = adtype;
	}
	public String getCarrier() {
		return carrier;
	}
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	public static String getOsv() {
		return osv;
	}
	public static void setOsv(String osv) {
		RequestParams.osv = osv;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getWma() {
		return wma;
	}
	public void setWma(String wma) {
		this.wma = wma;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getAaid() {
		return aaid;
	}
	public void setAaid(String aaid) {
		this.aaid = aaid;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getPcat() {
		return pcat;
	}
	public void setPcat(String pcat) {
		this.pcat = pcat;
	}
	public String getDensity() {
		return density;
	}
	public void setDensity(String density) {
		this.density = density;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public static String getOs() {
		return os;
	}
	public static String getIdfa() {
		return idfa;
	}
	public static String getOid() {
		return oid;
	}
	public static String getUid() {
		return uid;
	}
	
	//////////////
	
}
