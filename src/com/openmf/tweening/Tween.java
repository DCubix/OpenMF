package com.openmf.tweening;

import java.util.ArrayList;
import java.util.Iterator;

public class Tween {

	protected TweenCallback onCompleteListener;
	protected EasingFunction easingFunction;
	protected ArrayList<Value> values;
	protected float duration, time;
	protected boolean finished = false;
	
	public Tween(float duration) {
		this(
			duration,
			new EasingFunction() {
				@Override
				public float run(float t, float b, float c, float d) {
					return c * t / d + b;
				}
			},
			null
		);
	}
	
	public Tween(float duration, EasingFunction easing) {
		this(duration, easing, null);
	}
	
	public Tween(float duration, EasingFunction easing, TweenCallback cb) {
		this.onCompleteListener = cb;
		this.easingFunction = easing;
		this.values = new ArrayList<>();
		this.duration = duration;
		this.time = 0.0f;
	}
	
	public void addValue(FloatPointer value, float target) {
		values.add(new Value(value, target));
	}
	
	public void update(float dt) {
		float before = time;
		time += dt;
		for (Iterator<Value> it = values.iterator(); it.hasNext();) {
			Value val = it.next();
			if (before <= 1E-6 && val.value != null) {
				val.start = val.value.getValue();
			}
			
			if (val.value != null) {
				val.value.setValue(easingFunction.run(time, val.start, val.end - val.start, duration));
			}
			
			if (time >= duration) {
				finished = true;
				if (onCompleteListener != null) {
					onCompleteListener.completed();
				}
			}
		}
	}
	
}
