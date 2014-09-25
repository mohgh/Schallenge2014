package com.example.q5;

import org.json.JSONObject;

// this is callback from web service when discount objects loaded
public interface DiscountListener {
	public void OnDiscountsRetrieved(JSONObject discounts);
}
