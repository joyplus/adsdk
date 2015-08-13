package com.example.pmpsdk;

import com.pmp.sdk.AdSDKManager;
import com.pmp.sdk.AdSDKManagerCompat;
import com.pmp.sdk.AdSDKManagerException;
import com.pmp.sdk.data.AdImageRequest;
import com.pmp.sdk.data.RequestException;
import com.pmp.sdk.data.RequestParams;
import com.pmp.sdk.data.Response;
import com.pmp.sdk.widget.BannerAdView;
import com.pmp.sdk.widget.FloatView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					AdSDKManager.init(MainActivity.this);
					final Response response = new AdImageRequest(getDemoRequestParams()).sendRequest();
					android.util.Log.e("PMP", "response fff -->" + (response==null ? "null" : response.toString()));
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(AdSDKManagerCompat.isAviableResponse(response)){
								RelativeLayout ad = (RelativeLayout) findViewById(R.id.ad);
								FloatView bad  = new FloatView(MainActivity.this, response);
								ad.addView(bad, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
								bad.Show();
							}else{ 
								android.util.Log.d("PMP","fail isAviableResponse fail");
							}
						}
					});
					
					
				} catch (RequestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					android.util.Log.d("PMP","fail 1-->"+e.toString());
				} catch (AdSDKManagerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					android.util.Log.d("PMP","fail 2-->"+e.toString());
				}
			}
		}).start();
    }
    //  TE57BSC5FA3FFACC  TE57AES5FA3FFACC  TE57SKB5FA3FFACC 
    RequestParams getDemoRequestParams(){
    	RequestParams requestParams = new RequestParams();
    	requestParams.setAdspaceid("TE57AES5FA3FFACC");
    	requestParams.setWidth("640");
    	requestParams.setHeight("100");
    	requestParams.setPcat("IAB12");
    	return requestParams;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
