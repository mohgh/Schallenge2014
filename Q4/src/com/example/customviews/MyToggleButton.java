package com.example.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * @author Moh
 * 
 *         this is a custom textView with custom font BROYA
 */
public class MyToggleButton extends ToggleButton {

	public MyToggleButton(Context context) {
		super(context);
		setFont(context);
	}

	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont(context);
	}

	public MyToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont(context);
	}

	private void setFont(Context context) {
		Typeface font = Typeface.createFromAsset(context.getAssets(),
				"BROYA.TTF");
		this.setTypeface(font);
	}

	public void strike() {
		setPaintFlags(getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	}

}
