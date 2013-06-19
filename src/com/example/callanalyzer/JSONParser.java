package com.example.callanalyzer;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class JSONParser {
	
	public static String URL="http://app.ireff.in:9090/IreffWeb/android";
	
	// constructor
	public JSONParser() {

	}

	public static JSONArray getRechargePacks(String service,String circle) throws ClientProtocolException, IOException, JSONException{
		
		StringBuilder url=new StringBuilder(URL);
		url.append("?service="+service+"&circle="+circle);
		Log.i("URL", url.toString());
		HttpClient client = new DefaultHttpClient();
		HttpGet get=new HttpGet(url.toString());
		HttpResponse r=client.execute(get);
		int status=r.getStatusLine().getStatusCode();
		if(status==200){
			HttpEntity e=r.getEntity();
			String data=EntityUtils.toString(e);
			Log.i("HTTP"," "+data.length());
			JSONArray packsArray=new JSONArray(data);
			return packsArray;
		}
		else{
			//Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG);
			return null;
		}
	}
	
}
