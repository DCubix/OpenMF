package com.openmf.tweening.easing;

import com.openmf.tweening.EasingFunction;

public class Exponential {

	public static final EasingFunction In = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return (t == 0.0f) ? b : c * (float) Math.pow(2.0f, 10.0f * (t / d - 1.0f)) + b;
		}
	};

	public static final EasingFunction Out = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return (t == d) ? b + c : c * ((float) -Math.pow(2.0f, -10.0f * t / d) + 1) + b;
		}
	};

	public static final EasingFunction InOut = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			if (t == 0.0f)
				return b;
			if (t == d)
				return b + c;
			if ((t /= d / 2.0f) < 1.0f)
				return c / 2.0f * (float) Math.pow(2.0f, 10.0f * (t - 1.0f)) + b;
			return c / 2.0f * ((float) -Math.pow(2.0f, -10.0f * --t) + 2.0f) + b;
		}
	};

}
