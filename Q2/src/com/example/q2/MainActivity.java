package com.example.q2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String chosenRingtone;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.activity_main);
		String word = "Widget added and ready to use.";
		Toast.makeText(this, word, Toast.LENGTH_LONG).show();
		findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// using this intent to show ringtone selection dialog
						Intent intent = new Intent(
								RingtoneManager.ACTION_RINGTONE_PICKER);
						intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
								RingtoneManager.TYPE_NOTIFICATION);
						intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
								"Select Tone");
						intent.putExtra(
								RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
								(Uri) null);
						MainActivity.this.startActivityForResult(intent, 5);
					}
				});
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent intent) {
		// getting selected ringtone here
		if (resultCode == Activity.RESULT_OK && requestCode == 5) {
			Uri uri = intent
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

			if (uri != null) {
				this.chosenRingtone = uri.toString();
				// save selected ringtone as string in sharedPreferece
				Editor editor = getSharedPreferences("prefs", 0).edit();
				editor.putString("ring", chosenRingtone);
				editor.commit();
			} else {
				this.chosenRingtone = null;
			}
		}
	}

}