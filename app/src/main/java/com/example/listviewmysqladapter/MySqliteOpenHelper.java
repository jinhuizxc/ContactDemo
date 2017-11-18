package com.example.listviewmysqladapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "info";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_PHONE = "phone";

	public MySqliteOpenHelper(Context context, String name,
							  CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("create table " + TABLE_NAME
				+ " (id integer primary key autoincrement, " + FIELD_NAME
				+ " char(10), "+FIELD_PHONE+" char(11));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
