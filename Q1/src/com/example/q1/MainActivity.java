package com.example.q1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 
 * @author MohGH
 *	
 */

public class MainActivity extends Activity implements OnClickListener {

	private EditText fnameEdit;
	private EditText lnameEdit;
	private EditText numberEdit;
	private EditText emailEdit;
	private Spinner spinner;
	private RelativeLayout profilePic;

	byte[] pic = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// finding views from layout
		fnameEdit = (EditText) findViewById(R.id.editText1);
		lnameEdit = (EditText) findViewById(R.id.editText2);
		numberEdit 	= (EditText) findViewById(R.id.editText3);
		emailEdit = (EditText) findViewById(R.id.editText4);
		// number type spinner
		spinner = (Spinner) findViewById(R.id.spinner1);

		profilePic = (RelativeLayout) findViewById(R.id.pic);

		findViewById(R.id.button1).setOnClickListener(this);
		profilePic.setOnClickListener(this);
		// //
		// here is the directory of saved photo
		dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				+ "/myContactsPhotos/";
		File newdir = new File(dir);
		if (!newdir.isDirectory())
			newdir.mkdirs();
	}

	private void InsertContact(String DisplayName, String MobileNumber,
			String HomeNumber, String WorkNumber, String emailID, byte[] pic) {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				.build());

		// ------------------------------------------------------ Names
		if (DisplayName != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(
							ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
							DisplayName).build());
		}

		// ------------------------------------------------------ Mobile Number
		if (MobileNumber != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
							MobileNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					.build());
		}

		// ------------------------------------------------------ Home Numbers
		if (HomeNumber != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
							HomeNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
					.build());
		}

		// ------------------------------------------------------ Work Numbers
		if (WorkNumber != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
							WorkNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
					.build());
		}

		// ------------------------------------------------------ Email
		if (emailID != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Email.DATA,
							emailID)
					.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
							ContactsContract.CommonDataKinds.Email.TYPE_WORK)
					.build());
		}

		// ------------------------------------------------------ Photo
		if (pic != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Photo.DATA15,
							pic).build());
		}

		// Asking the Contact provider to create a new contact
		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			toast("Contact Successfully added!");
		} catch (Exception e) {
			e.printStackTrace();
			toast("Exception: " + e.getMessage());
		}
	}

	String numberRegex = "[0-9]+";
	String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private String dir;
	private String file;

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button1) {
			// getting string values from editTexes
			String fName = fnameEdit.getText().toString();
			String lName = lnameEdit.getText().toString();
			String number = numberEdit.getText().toString();
			String email = emailEdit.getText().toString();

			int contancType = spinner.getSelectedItemPosition();
			// validation of data
			if (number.length() == 0) {
				toast("please enter a number");
				return;
			}
			if (!number.matches(numberRegex)) {
				toast("number should be just digits.");
				return;
			}
			if (email.length() > 0 && !email.matches(emailRegex)) {
				toast("not a valid email structure!");
				return;
			}
			String surname = fName + " " + lName;
			InsertContact(surname, contancType == 1 ? number : null,
					contancType == 0 ? number : null, contancType == 2 ? number
							: null, email, pic);
		} else {
			// using current time and date for naming saved photo
			String time = new SimpleDateFormat("yyyy-MM-dd-HH-mm")
					.format(Calendar.getInstance().getTime());
			file = dir + time + ".jpg";
			File newfile = new File(file);
			try {
				newfile.createNewFile();
			} catch (IOException e) {
			}
			// the intent for staring camera application and taking photo
			Uri outputFileUri = Uri.fromFile(newfile);
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

			startActivityForResult(cameraIntent, 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if the photo successfully retrieved from camera then save the picture and set it as profile photo
		if (requestCode == 0 && resultCode == RESULT_OK) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(file, options);
			profilePic.setBackgroundDrawable(new BitmapDrawable(bitmap));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			pic = baos.toByteArray();
		}
	}

	// function for toasing 
	private void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
}
