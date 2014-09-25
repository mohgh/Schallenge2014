package com.example.q3;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements AccelerometerListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SensorManager sensorManager = (SensorManager) this
				.getSystemService(SENSOR_SERVICE);

		final float[] mValuesMagnet = new float[3];
		final float[] mValuesAccel = new float[3];
		final float[] mValuesOrientation = new float[3];
		final float[] mRotationMatrix = new float[9];

		final TextView txt1 = (TextView) findViewById(R.id.textView1);
		final EditText editTxt1 = (EditText) findViewById(R.id.editText1);

		final SensorEventListener mEventListener = new SensorEventListener() {
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}

			public void onSensorChanged(SensorEvent event) {
				// Handle the events for which we registered
				switch (event.sensor.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
					System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
					break;

				case Sensor.TYPE_MAGNETIC_FIELD:
					System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
					break;
				}
				SensorManager.getRotationMatrix(mRotationMatrix, null,
						mValuesAccel, mValuesMagnet);
				SensorManager.getOrientation(mRotationMatrix,
						mValuesOrientation);
				final CharSequence test;
				// here I get 3 orientation angles and change the values to
				// degree
				int XY = (int) (mValuesOrientation[0] * 180 / Math.PI);
				int YZ = (int) (mValuesOrientation[1] * 180 / Math.PI);
				int XZ = (int) (mValuesOrientation[2] * 180 / Math.PI);
				String enteredValue = editTxt1.getText().toString();
				test = "XY angle: " + XY + "\nYZ angle: " + YZ + "\nXZ angle: "
						+ XZ;
				// show angle values in txt view in order to make testing easier
				txt1.setText(test);
				// check if selected angle value is numeric and only contains
				// digits
				if (enteredValue.length() > 0 && enteredValue.matches("[0-9]+")) {
					int ev = Integer.parseInt(enteredValue);
					if (Math.abs(XY) == ev || Math.abs(YZ) == ev
							|| Math.abs(XZ) == ev) {
						vibrate();
					}
				}

			};
		};
		setListners(sensorManager, mEventListener);
		// here I start another sensor listener to listening for shakes
		if (AccelerometerManager.isSupported(this)) {
			// Start Accelerometer Listening
			AccelerometerManager.startListening(this);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {
			// Start Accelerometer Listening
			AccelerometerManager.stopListening();
		}

	}

	// Vibrating device
	private void vibrate() {
		((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(800);
	}

	public void setListners(SensorManager sensorManager,
			SensorEventListener mEventListener) {
		sensorManager.registerListener(mEventListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(mEventListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onAccelerationChanged(float x, float y, float z) {
	}

	@Override
	public void onShake(float force) {
		Toast.makeText(getBaseContext(), "دستگاه لرزیده است!",
				Toast.LENGTH_SHORT).show();
	}

}
