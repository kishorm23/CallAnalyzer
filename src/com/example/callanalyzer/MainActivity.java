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
import com.example.callanalyzer.MainActivity.MyPhoneStateListener;

import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView httpStuff;
	HttpClient client;
	JSONObject json;
	DatabaseHandler db = new DatabaseHandler(this);
	final static String URL="http://app.ireff.in:9090/IreffWeb/android?service=idea&circle=mp";
	int i=0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		httpStuff = (TextView) findViewById(R.id.tvHttp);
		client = new DefaultHttpClient();
		new Read().execute("keywords");
		 TelephonyManager telManager = (TelephonyManager)
					getSystemService(Context.TELEPHONY_SERVICE);
		 
		 					//to get Provider name and Phone number of user 
					  		String opName = telManager.getNetworkOperatorName();
					  		Log.i("telephony", "operator name = " + opName);
					  		String phoneNumber = telManager.getLine1Number();
					  		Log.i("telephony", "phone number = " + phoneNumber);
					  		String providerName = telManager.getSimOperatorName();
					  		Log.i("telephony", "provider name = " + providerName);
					  
							//Phone state listner
						    MyPhoneStateListener phoneListener=new MyPhoneStateListener(); 
						    getSystemService(Context.TELEPHONY_SERVICE);
						    telManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
						    
						    //counter for sent messages
						    Uri mSmsinboxQueryUri = Uri.parse("content://sms/sent");
						    Cursor cursor1 = getContentResolver().query(mSmsinboxQueryUri,
						    new String[] { "_id", "thread_id", "address", "person", "date",
						                                "body", "type" }, null, null, null);
						    String[] columns = new String[] { "address", "person", "date", "body","type" };
						    if (cursor1.getCount() > 0) {
						        String count = Integer.toString(cursor1.getCount()); //number of sent messages
						        Log.i("Count",count);
						    }
						    
						    //counts network data transfer
						    long mStartRX = TrafficStats.getMobileRxBytes(); //Bytes received over mobile interface
						    long mStartTX = TrafficStats.getMobileTxBytes(); //Bytes sent over mobile interface
						    if (mStartRX ==TrafficStats.UNSUPPORTED || mStartTX ==TrafficStats.UNSUPPORTED) {
						    AlertDialog.Builder alert = new AlertDialog.Builder(this);
						    Log.i("DEBUG","UNSUPPORTED: " + TrafficStats.UNSUPPORTED);
						    }
						    else {
						    	Log.i("DEBUG","Total bytes sent: " + mStartTX);
						    	Log.i("DEBUG","Total bytes received: " + mStartRX);
						    	Log.i("DEBUG","Total data transfer: " + (mStartTX+mStartRX));
						    	}
						    
					        /**
					         * CRUD Operations
					         * */
						    if(db.getNumberOfRows()==0) db.intializeDB(); //initialize if database is empty
					        long initialDuration= db.getDuration(1);
					        long initialRxBytes = db.getRxBytes(1);
					        long initialTxBytes = db.getTxBytes(1);
					        Log.i("DEBUG","Initial duration: "+initialDuration);
					        Log.i("DEBUG","Initial Recieved bytes: "+initialRxBytes);
					        Log.i("DEBUG","Initial transferred bytes: "+initialTxBytes);
					        
					        db.updateRxBytes(initialRxBytes+mStartRX);
					        db.updateTxBytes(initialTxBytes+mStartTX);
					        
					        Log.i("DEBUG","Final Recieved bytes: "+db.getRxBytes(1));
					        Log.i("DEBUG","Final transferred bytes: "+db.getTxBytes(1));
    }

    public JSONObject lastPack() throws ClientProtocolException, IOException, JSONException{
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

    public class MyPhoneStateListener extends PhoneStateListener {
		  public void onCallStateChanged(int state,String incomingNumber){
			  if(state==TelephonyManager.CALL_STATE_IDLE&&i!=0){
				  try {
					Thread.sleep(500); //wait till last call is logged in call log
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  String[] strFields = {
				        android.provider.CallLog.Calls.NUMBER, 
				        android.provider.CallLog.Calls.TYPE,
				        android.provider.CallLog.Calls.CACHED_NAME,
				        android.provider.CallLog.Calls.CACHED_NUMBER_TYPE,
				        android.provider.CallLog.Calls.DURATION
				        };
				String strOrder = android.provider.CallLog.Calls.DATE + " DESC"; 
				 
				Cursor mCallCursor = getContentResolver().query(
				        android.provider.CallLog.Calls.CONTENT_URI,
				        strFields,
				        null,
				        null,
				        strOrder
				        );
				// get start of cursor
				if(mCallCursor.moveToFirst()){
					 
					long last = 0;
					  int type=(int) mCallCursor.getLong(mCallCursor.getColumnIndex(CallLog.Calls.TYPE));
					  long durationMillis = mCallCursor.getLong(mCallCursor.getColumnIndex(CallLog.Calls.DURATION));
					  if(type==2){ last=durationMillis;
					  Log.i("DEBUG", "durationismillis= " + durationMillis);
					  Log.i("DEBUG", "type= " + mCallCursor.getLong(mCallCursor.getColumnIndex(CallLog.Calls.TYPE)));
					  Log.i("DEBUG", "number= " + mCallCursor.getString(mCallCursor.getColumnIndex(CallLog.Calls.NUMBER)));
					  }
				 
				  Log.i("DEBUG","Total Outgoing=" + last );
				  
				  TextView a;
		    		int total=db.getDuration(1);
		    		Log.i("DEBUG","DB DURATION_ini: "+total);
		    		total=total+(int)last;
		    		int updateDuration = db.updateDuration(total);
		    		Log.i("DEBUG","DB DURATION_fin: "+total);
		    		a=(TextView) findViewById(R.id.tvHttp);
		    		a.setText("Duration in seconds: "+total);
				}
		    }
			  if(i==0) i++; //prevents executing phone state listener when activity starts for first time
		  } 
		}
    
	public class Read extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				json=lastPack();
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
