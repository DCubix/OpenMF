package com.openmf.tweening.easing;

import com.openmf.tweening.EasingFunction;

public class Bounce {

	public static final EasingFunction In = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			return c - Out.run(d - t, 0.0f, c, d) + b;
		}
	};

	public static final EasingFunction Out = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			if ((t /= d) < (1.0f / 2.75f)) {
				return c * (7.5625f * t * t) + b;
			} else if (t < (2.0f / 2.75f)) {
				return c * (7.5625f * (t -= (1.5f / 2.75f)) * t + 0.75f) + b;
			} else if (t < (2.5f / 2.75f)) {
				return c * (7.5625f * (t -= (2.25f / 2.75f)) * t + 0.9375f) + b;
			} else {
				return c * (7.5625f * (t -= (2.625f / 2.75f)) * t + 0.984375f) + b;
			}
		}
	};

	public static final EasingFunction InOut = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			if (t < d * 0.5f)
				return In.run(t * 2.0f, 0.0f, c, d) * 0.5f + b;
			return Out.run(t * 2.0f - d, 0.0f, c, d) * 0.5f + c * 0.5f + b;
		}
	};

}
