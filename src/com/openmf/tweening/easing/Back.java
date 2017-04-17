package com.openmf.tweening.easing;

import com.openmf.tweening.EasingFunction;

public class Back {

	public static final EasingFunction In = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			float s = 1.70158f;
			return c * (t /= d) * t * ((s + 1.0f) * t - s) + b;
		}
	};

	public static final EasingFunction Out = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			float s = 1.70158f;
			return c * ((t = t / d - 1.0f) * t * ((s + 1.0f) * t + s) + 1.0f) + b;
		}
	};

	public static final EasingFunction InOut = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			float s = 1.70158f;
			if ((t /= d / 2) < 1.0f)
				return c / 2 * (t * t * (((s *= (1.525f)) + 1.0f) * t - s)) + b;
			return c / 2 * ((t -= 2.0f) * t * (((s *= (1.525f)) + 1.0f) * t + s) + 2.0f) + b;
		}
	};

}
