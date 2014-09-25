package com.example.q3;

public interface AccelerometerListener {
	
	//this interface is a callback to activity when a shake happens

	void onAccelerationChanged(float x, float y, float z);

	void onShake(float force);
	
}
