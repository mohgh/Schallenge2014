package com.example.q5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

// this is a asynctask for getting discount objects from web service
public class GetDiscounts extends AsyncTask<Void, Void, String> {

	private Context context;
	private String id;
	private DiscountListener listener;
	private SharedPreferences prefs;
	private boolean isFirst;
	ProgressDialog progress;

	public GetDiscounts(Context con, String id, DiscountListener listener) {
		this.context = con;
		this.id = id;
		this.listener = listener;
		// progress bar showed on beginning of loading
		progress = new ProgressDialog(context);
		progress.setMessage("لطفا کمی صبر کنید...");
		progress.setCancelable(false);
		progress.show();
		prefs = context.getSharedPreferences("prefs", 0);
	}

	@Override
	protected String doInBackground(Void... arg0) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		isFirst = prefs.getBoolean("isFirst", true);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -2);
		long date = prefs.getLong("date", cal.getTimeInMillis());
		if (cm.getActiveNetworkInfo() != null) {// check internet
												// connection
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://176.9.39.168:8080/TestServer/faces/data.xhtml");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("id", id));
			list.add(new BasicNameValuePair("cat", 1 + ""));
			list.add(new BasicNameValuePair("date", date + ""));
			list.add(new BasicNameValuePair("isFirst", isFirst + ""));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(list));
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String response = httpClient.execute(httpPost, responseHandler);
				Log.d("checking", "Response: " + response);
				return response;
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context, "Check Internet Connection!",
					Toast.LENGTH_LONG).show();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progress.dismiss();
		if (result != null) {
			try {
				Editor editor = prefs.edit();
				if (isFirst) {
					editor.putBoolean("isFirst", false);
				}
				// update date value
				Calendar cal1 = Calendar.getInstance();
				editor.putLong("date", cal1.getTimeInMillis());
				editor.commit();
				// calling interface with json object created from response string
				listener.OnDiscountsRetrieved(new JSONObject(result));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
