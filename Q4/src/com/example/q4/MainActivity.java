package com.example.q4;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.customviews.RangeSeekBar;
import com.example.customviews.RangeSeekBar.OnRangeSeekBarChangeListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		final TextView minText = (TextView) findViewById(R.id.min);
		final TextView maxText = (TextView) findViewById(R.id.max);
		// /

		// instantiating seekbar
		RangeSeekBar<Integer> seekBar = new RangeSeekBar<Integer>(0, 200, this);
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// updating textviews representing min and max values
				minText.setText(minValue * 100000 + " تومان");
				maxText.setText(maxValue * 100000 + " تومان");
			}
		});

		// add RangeSeekBar to pre-defined layout
		ViewGroup layout = (ViewGroup) findViewById(R.id.frame);
		layout.addView(seekBar);
	}

}
