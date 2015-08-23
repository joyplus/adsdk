package com.pmp.sdk.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

public class ResponseAdunit {
	
	public String cid;
	public String clickUrl;
	public String displayText;
	public String creativeType;
	public List<String> creativeUrls;
	public List<String> impTrackingUrls;
	public List<String> clkTrackingUrls;
	public String adWidth;
	public String adHeight;
	
	public static ResponseAdunit parserJsonObj(JSONObject jsonobject){
		try {
			ResponseAdunit responseAdunit = new ResponseAdunit();
			responseAdunit.cid             = jsonobject.getString("cid");
			responseAdunit.clickUrl        = jsonobject.getString("clickUrl");
			responseAdunit.displayText     = jsonobject.getString("displayText");
			responseAdunit.creativeType    = jsonobject.getString("creativeType");
			///////
			responseAdunit.creativeUrls = new ArrayList<String>();
			String creativeUrlsjson     = jsonobject.getString("creativeUrls");
			if(!TextUtils.isEmpty(creativeUrlsjson)){
				JSONArray creativeUrlsjsonArray = new JSONArray(creativeUrlsjson);
				if(creativeUrlsjsonArray != null){
					for(int i=0 ; i<creativeUrlsjsonArray.length(); i++){
						responseAdunit.creativeUrls.add(creativeUrlsjsonArray.getString(i));
					}
				}
			}
			///////
			responseAdunit.impTrackingUrls = new ArrayList<String>();
			
			String impTrackingUrlsjson     = jsonobject.getString("impTrackingUrls");
			android.util.Log.d("PMP","impTrackingUrlsjson="+impTrackingUrlsjson + "," + jsonobject.has("impTrackingUrls"));
			if(!TextUtils.isEmpty(impTrackingUrlsjson)){
				JSONArray impTrackingUrlsjsonArray = new JSONArray(impTrackingUrlsjson);
				if(impTrackingUrlsjsonArray != null){
					for(int i=0 ; i<impTrackingUrlsjsonArray.length(); i++){
						responseAdunit.impTrackingUrls.add(impTrackingUrlsjsonArray.getString(i));
					}
				}
			}
			//////
			String clkTrackingUrlsjson = jsonobject.getString("clkTrackingUrls");
			if(!TextUtils.isEmpty(clkTrackingUrlsjson)){
				JSONArray clkTrackingUrlsjsonArray = new JSONArray(clkTrackingUrlsjson);
				if(clkTrackingUrlsjsonArray != null){
					for(int i=0 ; i<clkTrackingUrlsjsonArray.length(); i++){
						responseAdunit.clkTrackingUrls.add(clkTrackingUrlsjsonArray.getString(i));
					}
				}
			}

			//responseAdunit.clkTrackingUrls = jsonobject.getString("clkTrackingUrls");
			responseAdunit.adWidth 		   = jsonobject.getString("adWidth");
			responseAdunit.adHeight        = jsonobject.getString("adHeight");
			return responseAdunit;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			android.util.Log.d("PMP", "ResponseAdunit fail -->"+ e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("ResponseAdunit{")
			.append("cid="+cid)
			.append(", clickUrl="+clickUrl)
			.append(", displayText="+displayText)
			.append(", creativeType="+creativeType)
			.append(", creativeUrls="+creativeUrls)
			.append(", impTrackingUrls="+impTrackingUrls)
			.append(", clkTrackingUrls="+clkTrackingUrls)
			.append(", adWidth="+adWidth)
			.append(", adHeight="+adHeight)
			.append("}");
		return sb.toString();
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getCreativeType() {
		return creativeType;
	}

	public void setCreativeType(String creativeType) {
		this.creativeType = creativeType;
	}

	public List<String> getCreativeUrls() {
		return creativeUrls;
	}

	public void setCreativeUrls(List<String> creativeUrls) {
		this.creativeUrls = creativeUrls;
	}

	public List<String> getImpTrackingUrls() {
		return impTrackingUrls;
	}

	public void setImpTrackingUrls(List<String> impTrackingUrls) {
		this.impTrackingUrls = impTrackingUrls;
	}

	public List<String> getClkTrackingUrls() {
		return clkTrackingUrls;
	}

	public void setClkTrackingUrls(List<String> clkTrackingUrls) {
		this.clkTrackingUrls = clkTrackingUrls;
	}

	public String getAdWidth() {
		return adWidth;
	}

	public void setAdWidth(String adWidth) {
		this.adWidth = adWidth;
	}

	public String getAdHeight() {
		return adHeight;
	}

	public void setAdHeight(String adHeight) {
		this.adHeight = adHeight;
	}
	
	
	
}
