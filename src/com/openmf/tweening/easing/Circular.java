package com.openmf.tweening.easing;

import com.openmf.tweening.EasingFunction;

public class Circular {

	public static final EasingFunction In = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return -c * ((float) Math.sqrt(1.0f - (t /= d) * t) - 1.0f) + b;
		}
	};

	public static final EasingFunction Out = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return c * (float) Math.sqrt(1.0f - (t = t / d - 1.0f) * t) + b;
		}
	};

	public static final EasingFunction InOut = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			if ((t /= d / 2.0f) < 1.0f)
				return -c / 2.0f * ((float) Math.sqrt(1 - t * t) - 1.0f) + b;
			return c / 2.0f * ((float) Math.sqrt(1 - (t -= 2.0f) * t) + 1.0f) + b;
		}
	};

}
