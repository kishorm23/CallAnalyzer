package com.example.callanalyzer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.util.Log;

public class CircleCode {
	
	public static String URL="http://epicwhale.org/tools/mobile/trace/";
	
	// constructor
	public void getCircleCode() {

	}

	public static String getCircleCode(String number) throws ClientProtocolException, IOException, JSONException{
		
		StringBuilder url=new StringBuilder(URL);
		url.append(number);
		HttpClient client = new DefaultHttpClient();
		HttpGet get=new HttpGet(url.toString());
		HttpResponse r=client.execute(get);
		int status=r.getStatusLine().getStatusCode();
		if(status==200){
			HttpEntity e=r.getEntity();
			String data=EntityUtils.toString(e);
			
			// the pattern to search for
		    Pattern p = Pattern.compile("<div class=\"right\">(.*)</div>");
		    Matcher m = p.matcher(data);
		    String provider=m.group(1);
		    String circle=m.group(1);
		    
		      // print the group
		      Log.i("REGEX",circle);
			return circle;
		}
		else{
			return null;
		}
	}
	
}
