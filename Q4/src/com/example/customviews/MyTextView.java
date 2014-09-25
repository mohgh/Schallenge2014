package com.example.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * @author Moh
 *
 * this is a custom textView with custom font BROYA
 */
public class MyTextView extends TextView{

	public MyTextView(Context context) {
		super(context);
		setFont(context);
	}
	
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont(context);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont(context);
	}

	private void setFont(Context context) {
		Typeface font=Typeface.createFromAsset(context.getAssets(), "BROYA.TTF");
		this.setTypeface(font);
	}
	
	public void strike(){
		setPaintFlags(getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	}

}
