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
import org.json.JSONObject;

import com.example.callanalyzer.R;
import com.example.callanalyzer.MainActivity.Read;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView httpStuff;
	HttpClient client;
	JSONObject json;
	final static String URL="http://app.ireff.in:9090/IreffWeb/android?service=idea&circle=mp";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		httpStuff = (TextView) findViewById(R.id.tvHttp);
		client = new DefaultHttpClient();
		new Read().execute("keywords");
    }

    public JSONObject lastTweet(String string) throws ClientProtocolException, IOException, JSONException{
		StringBuilder url=new StringBuilder(URL);
		//url.append(string);
		
		HttpGet get=new HttpGet(url.toString());
		HttpResponse r=client.execute(get);
		int status=r.getStatusLine().getStatusCode();
		if(status==200){
			HttpEntity e=r.getEntity();
			String data=EntityUtils.toString(e);
			JSONArray timeline=new JSONArray(data);
			JSONObject last=timeline.getJSONObject(1);
			return last;
		}
		else{
			//Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG);
			return null;
		}
	}

	public class Read extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				json=lastTweet("ab");
				return json.getString(params[0]);
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			httpStuff.setText(result);
			
		}
		
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
