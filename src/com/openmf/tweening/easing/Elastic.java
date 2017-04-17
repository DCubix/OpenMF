package com.openmf.tweening.easing;

import com.openmf.tweening.EasingFunction;

public class Elastic {

	public static final EasingFunction In = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			float s = 1.70158f;
			float p = 0;
			float a = c;
			if (t == 0)
				return b;
			if ((t /= d) == 1.0f)
				return b + c;
			if (p == 0)
				p = d * 0.3f;
			if (a < (float) Math.abs(c)) {
				a = c;
				s = p / 4.0f;
			} else
				s = p / (2.0f * (float) Math.PI) * (float) Math.asin(c / a);
			return -(a * (float) Math.pow(2.0f, 10.0f * (t -= 1))
					* (float) Math.sin((t * d - s) * (2.0f * (float) Math.PI) / p)) + b;
		}
	};

	public static final EasingFunction Out = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			float s = 1.70158f;
			float p = 0;
			float a = c;
			if (t == 0.0f)
				return b;
			if ((t /= d) == 1.0f)
				return b + c;
			if (p == 0)
				p = d * 0.3f;
			if (a < (float) Math.abs(c)) {
				a = c;
				s = p / 4.0f;
			} else
				s = p / (2.0f * (float) Math.PI) * (float) Math.asin(c / a);
			return a * (float) Math.pow(2.0f, -10.0f * t) * (float) Math.sin((t * d - s) * (2.0f * (float) Math.PI) / p)
					+ c + b;
		}
	};

	public static final EasingFunction InOut = new EasingFunction() {
		@Override
		public float run(float t, float b, float c, float d) {
			float s = 1.70158f;
			float p = 0;
			float a = c;
			if (t == 0)
				return b;
			if ((t /= d / 2) == 2)
				return b + c;
			if (p == 0)
				p = d * (0.3f * 1.5f);
			if (a < (float) Math.abs(c)) {
				a = c;
				s = p / 4;
			} else {
				s = p / (2 * (float) Math.PI) * (float) Math.asin(c / a);
			}
			if (t < 1)
				return -0.5f * (a * (float) Math.pow(2.0f, 10.0f * (t -= 1))
						* (float) Math.sin((t * d - s) * (2.0f * (float) Math.PI) / p)) + b;
			return a * (float) Math.pow(2.0f, -10.0f * (t -= 1.0f))
					* (float) Math.sin((t * d - s) * (2.0f * (float) Math.PI) / p) * 0.5f + c + b;
		}
	};

}
