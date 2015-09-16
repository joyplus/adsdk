package com.pmp.sdk.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;


public abstract class RequestAd<T> {

	protected InputStream is        = null;
    private   boolean     Debug     = true;
    
	public T sendRequest(AdRequest request)
			throws RequestException {
		if (is == null) { 
			try {
				return parse(sendRequestStream(request));
			} catch (RequestException e) {
				throw e;
			} catch (Throwable t) {
				throw new RequestException("Error in HTTP request Throwable " + t.toString(), t);
			}
		} else {
			return parseTestString();
		}
	}

	public String sendRequestSimple(AdRequest request)
			throws RequestException {
		if (is == null) {
			try {
				InputStream is = sendRequestStream(request);
				if (is != null) {
					return getString(is);
				}
			} catch (Throwable t) {
				throw new RequestException("Error in HTTP request Throwable " + t.toString(), t);
			}
		}
		return "";
	}

	private InputStream sendRequestStream(AdRequest request)
			throws RequestException {
		if (is == null) {
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setSoTimeout(client.getParams(),
						HttpManager.SOCKET_TIMEOUT);
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						HttpManager.CONNECTION_TIMEOUT);
				HttpGet get = new HttpGet(request.toUri().toString());//uri maybe null
				HttpResponse response;
				if(Debug){
					android.util.Log.d("PMP","sendRequest-->"+request.toUri().toString());
				}
				response = client.execute(get);
				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					return response.getEntity().getContent();
				} else {
					return null;
				}
			} catch (ClientProtocolException e) {
				throw new RequestException("Error in HTTP request ClientProtocolException " + e.toString(), e);
			} catch (IOException e) {
				throw new RequestException("Error in HTTP request IOException " + e.toString(), e);
			} catch (Throwable t) {
				throw new RequestException("Error in HTTP request Throwable " + t.toString(), t);
			}
		} else {
			return null;
		}
	}

	abstract T parseTestString() throws RequestException;

	abstract T parse(InputStream inputStream) throws RequestException;
 
	public static String getString(InputStream inputStream){
		if(inputStream != null){
			String result = "";
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	            String temp = reader.readLine();
	            while(temp!=null){
	            	result+=temp;
	                temp = reader.readLine();
	            }
	            return result;
			}catch(Throwable e){
				android.util.Log.d("PMP","getStringfromResponseInputStream fail-->"+e.toString());
			}finally{
				if(inputStream != null)
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		return null;
	}
}
