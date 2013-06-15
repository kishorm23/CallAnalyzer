package com.example.callanalyzer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "callDuration";
	private static final String TABLE_STATS = "stats";
	private static final String KEY_ID = "id";
	private static final String KEY_CALLS = "duration";
	private static final String KEY_R_DATA = "dataRecieved";
	private static final String KEY_T_DATA = "dataTransffered";
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
		db.execSQL(CREATE_STATS_TABLE);
		Log.i("DATABASE","database created");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);

		// Create tables again
		onCreate(db);
	}
	
	void intializeDB() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CALLS, 0);
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
}
