package com.example.q2;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

//this class used for creating listview of messages visible in home screen widget
public class ViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	private Context ctxt = null;
	private int appWidgetId;
	private String[][] items;

	public ViewsFactory(Context ctxt, Intent intent) {
		this.ctxt = ctxt;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		// geting msg s from SMSReader class
		items = SMSReader.getSMS(ctxt);
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
		return (items.length);
	}

	@Override
	public RemoteViews getViewAt(int position) {
		// filling each row 
		RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.row);

		row.setTextViewText(android.R.id.text1, items[position][1]);
		row.setTextViewText(android.R.id.text2, items[position][0]);

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