package com.example.callanalyzer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver {

	public class MyReceiver extends BroadcastReceiver {
		;
		@Override
		public void onReceive(Context context, Intent intent) {

			Intent myIntent = new Intent(context, BootServiceClass.class);
			context.startService(myIntent);
		}

	}
}
