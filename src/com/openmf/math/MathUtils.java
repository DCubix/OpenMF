package com.openmf.math;

public class MathUtils {

	public static <T extends Comparable<T>> T clamp(T val, T min, T max) {
		if (val.compareTo(min) < 0) {
			return min;
		} else if (val.compareTo(max) > 0) {
			return max;
		} else {
			return val;
		}
	}
	
	public static float remap(float val, float omin, float omax, float min, float max) {
		float oldRange = omax - omin;
		if (oldRange == 0) {
			return min;
		} else {
		    float newRange = (max - min);  
		    return (((val - omin) * newRange) / oldRange) + min;
		}
	}
	
	public static float lerp(float a, float b, float t) {
		return (1.0f - t) * a + b * t;
	}

}
