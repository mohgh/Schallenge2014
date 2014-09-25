package com.example.widget;

import java.util.Calendar;
import java.util.List;

import com.example.q5.DatabaseHelper;
import com.example.q5.Discount;
import com.example.q5.R;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class ViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	private Context ctxt = null;
	private int appWidgetId;
	private List<Discount> items;

	public ViewsFactory(Context ctxt, Intent intent) {
		this.ctxt = ctxt;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		DatabaseHelper helper = new DatabaseHelper(ctxt);
		items = helper.getAllDiscounts(" ORDER BY date DESC LIMIT 3");
	}

	@Override
	public void onCreate() {
		// no-op
	}

	@Override
	public void onDestroy() {
		// no-op
	}

	@Override
	public int getCount() {
		return (items.size());
	}

	@Override
	public RemoteViews getViewAt(int pos) {
		int hourPartition = 3600 * 1000;
		RemoteViews row = new RemoteViews(ctxt.getPackageName(),
				R.layout.discount_item);
		Discount discount = items.get(pos);
		row.setTextViewText(R.id.text, discount.text);
		row.setTextViewText(R.id.off, "تخفیف: " + discount.offPercent + " درصد");
		row.setTextViewText(R.id.location, "مکان: " + discount.city);
		row.setTextViewText(R.id.price, "قیمت: " + discount.price+" تومان");
		Calendar cal = Calendar.getInstance();
		long now = cal.getTimeInMillis();
		if ((now - discount.date) / (24 * hourPartition) >= 1) {
			int day = (int) ((now - discount.date) / (24 * hourPartition));
			row.setTextViewText(R.id.date, day + " روز پیش");
		} else {
			int hours = (int) ((now - discount.date) / (hourPartition));
			row.setTextViewText(R.id.date, hours + " ساعت پیش");
		}
		return (row);
	}

	@Override
	public RemoteViews getLoadingView() {
		return (null);
	}

	@Override
	public int getViewTypeCount() {
		return (1);
	}

	@Override
	public long getItemId(int position) {
		return (position);
	}

	@Override
	public boolean hasStableIds() {
		return (true);
	}

	@Override
	public void onDataSetChanged() {
		// no-op
	}
}
