package com.openmf.core;

import android.os.SystemClock;

public class Time {
	public static double getTime() {
		return (float)SystemClock.uptimeMillis() / 1000.0;
	}
}
