package com.example.q2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		Log.d("checking", "sms received!");
		String str = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			String sender = null;
			String body = null;
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += "SMS from " + msgs[i].getOriginatingAddress();
				sender = msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				body = msgs[i].getMessageBody().toString();
				str += "\n";
			}
			// ---display the new SMS message---
			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
			// show status bar notification here
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(
					R.drawable.ic_launcher, "New Message",
					System.currentTimeMillis());
			Intent notificationIntent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);
			notification.setLatestEventInfo(context, "SMS from: " + sender,
					body, pendingIntent);
			notificationManager.notify(9999, notification);
			// play ringtone
			try {
				//default notification
				Uri notification1 = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				String ring = context.getSharedPreferences("prefs", 0)
						.getString("ring", "");
				if (ring.length() > 0) {
					notification1 = Uri.parse(ring);
				}
				Ringtone r = RingtoneManager
						.getRingtone(context, notification1);
				r.play();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}