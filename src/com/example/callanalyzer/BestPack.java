package com.example.callanalyzer;

import org.json.JSONArray;

import android.database.Cursor;
import android.util.Log;

public class BestPack {
	int getBestRechargePack()
	{
		DatabaseHandler db = new DatabaseHandler(null);
		Cursor cursorToTable = db.getRPackDatabase(0);
		long price=Integer.parseInt(cursorToTable.getString(3));
		long talktime=Integer.parseInt(cursorToTable.getString(7));
		Log.i("DEBUG","price: "+price+" talktime: "+talktime);
		return 0;
	}

}
