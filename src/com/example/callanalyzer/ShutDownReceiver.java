package com.example.callanalyzer;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.util.Log;

public class ShutDownReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) 
        {
        		DatabaseHandler db =new DatabaseHandler(context);
        	    long mStartRX = TrafficStats.getMobileRxBytes(); //Bytes received over mobile interface since last boot
			    long mStartTX = TrafficStats.getMobileTxBytes(); //Bytes sent over mobile interface since last boot
			    if (mStartRX ==TrafficStats.UNSUPPORTED || mStartTX ==TrafficStats.UNSUPPORTED) {
			    AlertDialog.Builder alert = new AlertDialog.Builder(context);
			    Log.i("DEBUG","UNSUPPORTED: " + TrafficStats.UNSUPPORTED);
			    }
			    else {
			    	Log.i("DEBUG","Total bytes sent: " + mStartTX);
			    	Log.i("DEBUG","Total bytes received: " + mStartRX);
			    	Log.i("DEBUG","Total data transfer: " + (mStartTX+mStartRX));
			    }
			    

		        long initialRxBytes = db.getRxBytes(1);
		        long initialTxBytes = db.getTxBytes(1);
		        
		        Log.i("DEBUG","Initial Recieved bytes: "+initialRxBytes);
		        Log.i("DEBUG","Initial transferred bytes: "+initialTxBytes);
		        
		        db.updateRxBytes(initialRxBytes+mStartRX);
		        db.updateTxBytes(initialTxBytes+mStartTX);
		        
		        Log.i("DEBUG","Final Recieved bytes: "+db.getRxBytes(1));
		        Log.i("DEBUG","Final transferred bytes: "+db.getTxBytes(1));
		        
     	Log.i("DEBUG","Device is shutting down");
        }             
	}

}
