package com.openmf.tweening.easing;

import com.openmf.tweening.EasingFunction;

public class Sine {

	public static final EasingFunction In = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return -c * (float) Math.cos(t / d * ((float) Math.PI / 2.0f)) + c + b;
		}
	};

	public static final EasingFunction Out = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return c * (float) Math.sin(t / d * ((float) Math.PI / 2.0f)) + b;
		}
	};

	public static final EasingFunction InOut = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return -c / 2.0f * ((float) Math.cos((float) Math.PI * t / d) - 1.0f) + b;
		}
	};

}
