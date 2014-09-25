package com.example.q2;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

public class SMSReader {

	public static String[][] getSMS(Context con) {
		// sms repository for sms inbox
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor c = con.getContentResolver().query(uri, null, null, null, null);
		// this is a 2d array representing contact name and msg s
		// widget will show maximum 20 msgs so array count should be either 20 o
		// less
		String[][] msgs = new String[c.getCount() > 20 ? 20 : c.getCount()][2];
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount() && i < 20; i++) {
				String b = c.getString(c.getColumnIndexOrThrow("body"))
						.toString();
				String n = c.getString(c.getColumnIndexOrThrow("address"))
						.toString();
				// each row of msg array contains 2 String object first one is
				// the message and second one is his/her name
				msgs[i] = new String[] { b, getContactName(con, n) };
				c.moveToNext();
			}
		}
		c.close();
		return msgs;
	}

	// this method look in phone contacts and give name of contacts by their
	// number
	public static String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return contactName;
	}
}
