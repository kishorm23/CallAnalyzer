package com.example.callanalyzer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.w("SERVICE", "starting service...");
		context.startService(new Intent(context, BootServiceClass.class));
	}

}
