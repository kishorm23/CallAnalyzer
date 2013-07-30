package com.example.callanalyzer;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView httpStuff;
	JSONObject json;
	DatabaseHandler db = new DatabaseHandler(this);
	int i=0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		httpStuff = (TextView) findViewById(R.id.tvHttp);
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
					  
							//Phone state listener
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
						    

					        if(db.getNumberOfRows()==0) db.intializeDB(); //if database is empty
						    
						    IntentFilter filter = new IntentFilter(Intent.ACTION_SHUTDOWN);
						    BroadcastReceiver mReceiver = new ShutDownReceiver();
						    registerReceiver(mReceiver, filter);
					        
					        /**
					         * CRUD Operations
					         * */
					        long initialDuration= db.getDuration(1);
					        Log.i("DEBUG","Initial duration: "+initialDuration);
new Read().execute("detail");

    }


    public class MyPhoneStateListener extends PhoneStateListener {
		  public void onCallStateChanged(int state,String incomingNumber){
			  if(state==TelephonyManager.CALL_STATE_IDLE&&i!=0){
				  try {
					Thread.sleep(2000); //wait till last call is logged in call log
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
    
	public class Read extends AsyncTask<String, Integer, JSONArray>
	{
		@Override
		protected JSONArray doInBackground(String... params) {

			try {
				JSONArray json;
				json=JSONParser.getRechargePacks("airtel","mh");
				return json;
				
			} catch (JSONException e) {
				
				e.printStackTrace();
				
			} catch (ClientProtocolException e) {
				
				e.printStackTrace();
			
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(JSONArray result) {
			
			Log.i("DEBUG",""+result.length());
			try {
				int e=0;
				while(e<result.length())
				{
				JSONObject pack=result.getJSONObject(e);
				String params[]=new String[5];
				double price=0;
				double talktime=0;
				if(!pack.isNull("category")) params[0]=pack.get("category").toString();
				if(!pack.isNull("detail")) params[1]=pack.get("detail").toString();
				if(!pack.isNull("price")) price=pack.getDouble("price");
				if(!pack.isNull("keywords")) params[2]=pack.get("keywords").toString();
				if(!pack.isNull("updated")) params[3]=pack.get("updated").toString();
				if(!pack.isNull("validity")) params[4]=pack.get("category").toString();
				if(!pack.isNull("talktime")) talktime=pack.getDouble("talktime");
				
				
				Log.i("JSON",e+") "+params[0]+" "+talktime+" "+params[1]+" "+params[2]+" "+params[3]+" "+params[4]+" "+talktime);
				db.insertRechargePack(params, price, talktime);
				e++;
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
				
			}
		}
		
	}    

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
