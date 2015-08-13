package com.pmp.sdk.mode;

import android.text.TextUtils;

public class Report {
	
	public final static int TYPE_UNKNOW      = 0;
	public final static int TYPE_CLKTRACKING = 1;
	public final static int TYPE_IMPTRACKINT = 2;
	public final static int TYPE_MAX         = 3;
	
	private boolean Reported = false;
	private int     type     = TYPE_UNKNOW;
	private String  uri;
	public Report(int type, String uri){
		this.type  = type;
		this.uri   = uri;
		Reported   = false;
	}
	public boolean isAviable(){
		return isAviableType() && !TextUtils.isEmpty(uri);
	}
	public boolean isAviableType(){
		return TYPE_UNKNOW < type && type < TYPE_MAX;
	}
	public boolean isReported(){
		return Reported;
	}
	public boolean setReported(boolean re){
		return (Reported = re);
	}
	public int getType(){
		return type;
	}
	public String getUri(){
		return uri;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("Report{")
			.append("Reported="+Reported)
			.append(", type="+type)
			.append(", uri="+uri)
			.append("}");
		return sb.toString();
	}
}
