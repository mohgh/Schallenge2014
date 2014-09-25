package com.example.q5;

public class Discount {

	// this is the model for discount entity
	public String text;
	public int price;
	public int offPercent;
	public int realPrice;
	public String url;
	public int c;
	public int n;
	public long date;
	public String city;

	public Discount(String text, int price, int off, int real, String url, int c,
			int n, long date, String city) {
		this.text = text;
		this.price = price;
		this.offPercent = off;
		this.realPrice = real;
		this.url = url;
		this.c = c;
		this.n = n;
		this.date = date;
		this.city = city;
	}
}
