package com.pmp.sdk.data;

import org.json.JSONObject;

import android.text.TextUtils;

public class Response {
	
	public String statusCode;
	public String adspaceKey;
	public String bid;
	public ResponseAdunit adunit;
	
	
	public static Response parserResponse(String responseresult){
		try {
			JSONObject jsonObject = new JSONObject(responseresult);
			Response response = new Response();
			response.statusCode = jsonObject.getString("statusCode");
			response.adspaceKey = jsonObject.getString("adspaceKey");
			response.bid        = jsonObject.getString("bid");
			response.adunit     = ResponseAdunit.parserJsonObj(jsonObject.getJSONObject("adunit"));
			return response;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			android.util.Log.d("PMP","Response fail-->"+e.toString());
		}
		return null;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("Response{")
			.append("statusCode="+statusCode)
			.append(", adspaceKey="+adspaceKey)
			.append(", bid="+bid)
			.append(", adunit=" + (adunit==null ? "" : adunit.toString()))
			.append("}");
		return sb.toString();
	}
	
	public boolean isloadUriNoImage(){
		if(adunit != null){
			return TextUtils.equals("3", adunit.creativeType);
		}
		return false;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public ResponseAdunit getAdunit() {
		return adunit;
	}

	public void setAdunit(ResponseAdunit adunit) {
		this.adunit = adunit;
	}
	
	
	
}
