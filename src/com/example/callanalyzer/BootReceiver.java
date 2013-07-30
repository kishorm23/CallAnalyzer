package com.example.callanalyzer;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.i("SERVICE","STARTED");
		Toast.makeText(arg0, "Call Analyzer is working in background", 2000).show();
		Intent service = new Intent(arg0, BackgroundService.class);
		arg0.startService(service);
	}

}
