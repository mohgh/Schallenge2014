package com.example.q5;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ConncetToWebservice extends AsyncTask<Void, Void, String> {

	private Context context;
	private WebServiceListener listener;

	public interface WebServiceListener {
		public void OnIdRetrieved(String id);
	}

	public ConncetToWebservice(Context context, WebServiceListener listener) {
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected String doInBackground(Void... args) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// getting api level
		int API_LEVEL = android.os.Build.VERSION.SDK_INT;
		if (cm.getActiveNetworkInfo() != null) {// check internet
												// connection
			// making an HTTP post request
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://176.9.39.168:8080/TestServer/faces/registerme.xhtml");
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("apk", API_LEVEL + ""));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(list));
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String response = httpClient.execute(httpPost, responseHandler);
				Log.d("checking", "Response : " + response);
				return response;
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null) {
			// geting id in json object
			String id = result.substring(7, 31);
			Log.d("checking", "id: " + id);
			listener.OnIdRetrieved(id);
		} else {
			Toast.makeText(context, "Check Internet Connection!",
					Toast.LENGTH_LONG).show();

		}
	}

}
