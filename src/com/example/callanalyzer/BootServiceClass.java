package com.example.callanalyzer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BootServiceClass extends Service {
	
	  public int onStartCommand(Intent intent, int flags, int startId) {
		  Log.i("Debug", "Started");
		    return Service.START_NOT_STICKY;
		  }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.i("Debug", "Started");
		return null;
		
	}
	

}
