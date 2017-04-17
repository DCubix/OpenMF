package com.openmf.core;

import java.text.DecimalFormat;

public class StringUtils {

	public static String format(String pattern, float value) {
		return new DecimalFormat(pattern).format(value);
	}
	
	public static String format(String pattern, double value) {
		return new DecimalFormat(pattern).format(value);
	}
	
}
