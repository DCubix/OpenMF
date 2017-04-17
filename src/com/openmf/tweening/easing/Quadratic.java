package com.openmf.tweening.easing;

import com.openmf.tweening.EasingFunction;

public class Quadratic {

	public static final EasingFunction In = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return c * (t /= d) * t + b;
		}
	};

	public static final EasingFunction Out = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return -c * (t /= d) * (t - 2) + b;
		}
	};

	public static final EasingFunction InOut = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			if ((t /= d / 2) < 1)
				return c / 2 * t * t + b;
			return -c / 2 * ((--t) * (t - 2) - 1) + b;
		}
	};

}
