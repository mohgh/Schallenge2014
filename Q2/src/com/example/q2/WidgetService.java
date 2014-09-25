package com.example.q2;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return (new ViewsFactory(this.getApplicationContext(), intent));
	}
}