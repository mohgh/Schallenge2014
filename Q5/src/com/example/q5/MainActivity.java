package com.example.q5;

import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.q5.ConncetToWebservice.WebServiceListener;

public class MainActivity extends Activity implements DiscountListener,
		OnClickListener {

	String id;
	int activeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// /
		Log.d("checking", "start");

		// conncet to webservice for getting id
		new ConncetToWebservice(this, new WebServiceListener() {

			@Override
			public void OnIdRetrieved(String id) {
				MainActivity.this.id = id;
				// conncet to webservice to get discount data
				new GetDiscounts(MainActivity.this, id, MainActivity.this)
						.execute();
			}
		}).execute();

		// radio group for setting list mode
		((RadioGroup) findViewById(R.id.toggleGroup1))
				.setOnCheckedChangeListener(ToggleListener);
		list = (ListView) findViewById(R.id.listView1);
		activeId = R.id.toggleButton2;
		refreshList();
	}

	@Override
	public void OnDiscountsRetrieved(JSONObject object) {
		try {
			JSONArray discounts = object.getJSONArray("offObjects");
			int length = discounts.length();
			Log.d("checking", "length: " + length);
			for (int i = 0; i < length; i++) {
				JSONObject discount = discounts.getJSONObject(i);
				String text = discount.getString("t");
				int price = discount.getInt("p");
				int r = discount.getInt("r");
				int e = discount.getInt("e");
				String url = discount.getString("u");
				int c = discount.getInt("c");
				int n = discount.getInt("n");
				long date = discount.getLong("a");
				String city = discount.getString("y");
				// creating model object and save it in database
				DatabaseHelper helper = new DatabaseHelper(this);
				helper.addDiscount(new Discount(text, price, r, e, url, c, n,
						date, city));
			}
			refreshList();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
			for (int j = 0; j < radioGroup.getChildCount(); j++) {
				final ToggleButton view = (ToggleButton) radioGroup
						.getChildAt(j);
				view.setChecked(view.getId() == i);
			}
		}
	};
	private ListView list;

	public void onToggle(View view) {
		((RadioGroup) view.getParent()).check(view.getId());
		activeId = view.getId();
		refreshList();
	}

	private void refreshList() {
		// opeing database and getting discount saved before
		DatabaseHelper helper = new DatabaseHelper(this);
		List<Discount> discounts = null;
		if (activeId == R.id.toggleButton1) {
			discounts = helper.getAllDiscounts(" ORDER BY "
					+ DatabaseHelper.KEY_OFF + " DESC");
		} else {// in newest mode all discounts are brought sorted by their date
			discounts = helper.getAllDiscounts(" ORDER BY "
					+ DatabaseHelper.KEY_DATE + " DESC");
		}
		list.setAdapter(new MyAdapter(discounts));
	}

	// this is the custom adapter for listview
	public class MyAdapter extends BaseAdapter {

		private List<Discount> items;
		int hourPartition = 3600 * 1000;

		public MyAdapter(List<Discount> items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int pos, View arg1, ViewGroup arg2) {
			// inflating layout file and setting values of each discount
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View disItem = inflater
					.inflate(R.layout.discount_item, null, false);
			TextView text = (TextView) disItem.findViewById(R.id.text);
			TextView off = (TextView) disItem.findViewById(R.id.off);
			TextView date = (TextView) disItem.findViewById(R.id.date);
			TextView place = (TextView) disItem.findViewById(R.id.location);
			TextView price = (TextView) disItem.findViewById(R.id.price);
			Discount discount = items.get(pos);
			text.setText(discount.text);
			off.setText("تخفیف: " + discount.offPercent + " درصد");
			place.setText("مکان: " + discount.city);
			price.setText("قیمت: " + discount.price + " تومان");
			Calendar cal = Calendar.getInstance();
			long now = cal.getTimeInMillis();
			if ((now - discount.date) / (24 * hourPartition) >= 1) {
				int day = (int) ((now - discount.date) / (24 * hourPartition));
				date.setText(day + " روز پیش");
			} else {
				int hours = (int) ((now - discount.date) / (hourPartition));
				date.setText(hours + " ساعت پیش");
			}
			disItem.setTag(discount.url);
			disItem.setOnClickListener(MainActivity.this);
			return disItem;
		}
	}

	@Override
	public void onClick(View view) {
		String url = (String) view.getTag();
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(browserIntent);
	}

}
