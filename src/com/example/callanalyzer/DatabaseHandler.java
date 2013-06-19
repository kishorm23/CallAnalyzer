package com.example.callanalyzer;

import java.util.ArrayList;
import java.util.List;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.UrlQuerySanitizer.ValueSanitizer;
import android.renderscript.Sampler.Value;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	//Table1
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "callDuration";
	private static final String TABLE_STATS = "stats";
	private static final String KEY_ID = "id";
	private static final String KEY_CALLS = "duration";
	private static final String KEY_R_DATA = "dataRecieved";
	private static final String KEY_T_DATA = "dataTransffered";
	
	//Table2
	private static final String TABLE_RECHARGE = "rechargePacks";
	private static final String KEY_CATEGORY = "category";
	private static final String KEY_DETAIL = "detail";
	private static final String KEY_PRICE = "price";
	private static final String KEY_KEYWORDS = "keywords";
	private static final String KEY_UPDATED = "updated";
	private static final String KEY_VALIDITY = "validity";
	private static final String KEY_SERVICE = "service";
	private static final String KEY_TALKTIME = "talktime";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_STATS_TABLE = "CREATE TABLE " + TABLE_STATS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_CALLS + " INTEGER,"
				+ KEY_R_DATA +" INTEGER,"
				+ KEY_T_DATA+ " INTEGER)";
		
		String CREATE_RECHARGE_TABLE = "CREATE TABLE " + TABLE_RECHARGE + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_CATEGORY + " TEXT,"
				+ KEY_DETAIL + " TEXT,"
				+ KEY_PRICE +" REAL,"
				+ KEY_KEYWORDS+ " TEXT,"
				+ KEY_UPDATED+ " TEXT,"
				+ KEY_VALIDITY+ " TEXT,"
				+ KEY_TALKTIME+ " REAL)";
		db.execSQL(CREATE_STATS_TABLE);
		db.execSQL(CREATE_RECHARGE_TABLE);
		Log.i("DATABASE","database created");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECHARGE);
		// Create tables again
		onCreate(db);
	}
	
	void intializeDB() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CALLS, 0); // Contact Name
		values.put(KEY_R_DATA, 0);
		values.put(KEY_T_DATA, 0);
		// Inserting Row
		db.insert(TABLE_STATS, null, values);
		db.close(); // Closing database connection
	}
	
	int getDuration(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STATS, new String[] { KEY_ID,
				KEY_CALLS}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) cursor.moveToFirst();
		
		return Integer.parseInt(cursor.getString(1));
	}
	
	int updateDuration(int duration)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CALLS, duration);
		return db.update(TABLE_STATS, values, KEY_ID + " = ?",new String[] { String.valueOf(1) });

	}
	int getNumberOfRows()
	{
		String selectQuery = "SELECT  * FROM " + TABLE_STATS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		return cursor.getCount();
	}
	
	int getRxBytes(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STATS, new String[] { KEY_ID,
				KEY_CALLS,KEY_R_DATA}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) cursor.moveToFirst();
		return Integer.parseInt(cursor.getString(2));
	}
	
	int updateRxBytes(long RxBytes)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_R_DATA, RxBytes);
		return db.update(TABLE_STATS, values, KEY_ID + " = ?",new String[] { String.valueOf(1) });

	}
	
	int getTxBytes(long id)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STATS, new String[] { KEY_ID,
				KEY_CALLS,KEY_R_DATA,KEY_T_DATA}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) cursor.moveToFirst();
		return Integer.parseInt(cursor.getString(3));
	}
	
	int updateTxBytes(long TxBytes)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_T_DATA, TxBytes);
		return db.update(TABLE_STATS, values, KEY_ID + " = ?",new String[] { String.valueOf(1) });

	}
	
	void insertRechargePack(String[] params, double price, double talktime)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_CATEGORY,params[0]);
		values.put(KEY_DETAIL,params[1]);
		values.put(KEY_PRICE,price);
		values.put(KEY_KEYWORDS,params[2]);
		values.put(KEY_UPDATED, params[3]);
		values.put(KEY_VALIDITY, params[4]);
		values.put(KEY_TALKTIME,talktime);
		
		db.insert(TABLE_RECHARGE, null, values);
		db.close();
		
	}
}
