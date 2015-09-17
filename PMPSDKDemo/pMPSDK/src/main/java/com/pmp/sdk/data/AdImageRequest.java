package com.pmp.sdk.data;

import java.io.InputStream;

import com.google.gson.Gson;

public class AdImageRequest extends RequestAd<Response>{
	
	private AdRequest mAdRequest;
	
	public AdImageRequest(RequestParams requestParams){
		try {
			if(requestParams == null)
				throw new IllegalArgumentException("AdImageRequest RequestParams is null");
			mAdRequest = new AdRequest(requestParams, "2", "0001");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			android.util.Log.d("PMP"," AdSDKManagerException -->"+ e.toString());
		}
	}
	public Response sendRequest() throws RequestException {
		// TODO Auto-generated method stub
		if(mAdRequest == null) return null;
		return super.sendRequest(mAdRequest);
	}
	public String sendRequestSimple() throws RequestException {
		// TODO Auto-generated method stub
		if(mAdRequest == null) return null;
		return super.sendRequestSimple(mAdRequest);
	}
	@Override
	Response parseTestString() throws RequestException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	Response parse(InputStream inputStream) throws RequestException {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		try{ 
			Response response = gson.fromJson(getString(inputStream), Response.class);
			android.util.Log.d("PMP","response-->"+ (response ==null ? "null" : response.toString()));
			return response;
		}catch(Throwable e){android.util.Log.d("PMP","response fail -->"+e.toString());}
		return null;
		//return Response.parserResponse(getString(inputStream));
	}
}
