package com.example.q5;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "database";

	// Contacts table name
	private static final String TABLE_DISCOUNTS = "discounts";

	static final String KEY_TEXT = "text";
	static final String KEY_PRICE = "price";
	static final String KEY_OFF = "off";
	static final String KEY_REAL_PRICE = "real_price";
	static final String KEY_URL = "url";
	static final String KEY_C = "c";
	static final String KEY_N = "n";
	static final String KEY_DATE = "date";
	static final String KEY_CITY = "city";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_BOOKMARKS_TABLE = "CREATE TABLE " + TABLE_DISCOUNTS + "("
				+ KEY_TEXT + " TEXT," + KEY_PRICE + " INTEGER," + KEY_OFF
				+ " INTEGER," + KEY_REAL_PRICE + " INTEGER," + KEY_URL
				+ " TEXT," + KEY_C + " INTEGER," + KEY_N + " INTEGER,"
				+ KEY_DATE + " LONG," + KEY_CITY + " TEXT" + ")";
		db.execSQL(CREATE_BOOKMARKS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISCOUNTS);
		onCreate(db);
	}

	public void addDiscount(Discount discount) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TEXT, discount.text);
		values.put(KEY_PRICE, discount.price);
		values.put(KEY_OFF, discount.offPercent);
		values.put(KEY_REAL_PRICE, discount.realPrice);
		values.put(KEY_URL, discount.url);
		values.put(KEY_C, discount.c);
		values.put(KEY_N, discount.n);
		values.put(KEY_DATE, discount.date);
		values.put(KEY_CITY, discount.city);

		db.insert(TABLE_DISCOUNTS, null, values);
		db.close(); // Closing database connection
	}

	public List<Discount> getAllDiscounts(String q) {
		List<Discount> list = new ArrayList<Discount>();
		String selectQuery = "SELECT * FROM " + TABLE_DISCOUNTS + q;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {

				list.add(new Discount(cursor.getString(0), cursor.getInt(1),
						cursor.getInt(2), cursor.getInt(3),
						cursor.getString(4), cursor.getInt(5),
						cursor.getInt(6), cursor.getLong(7), cursor
								.getString(8)));
			} while (cursor.moveToNext());
		}
		return list;
	}

}
