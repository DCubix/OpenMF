package com.openmf.tweening;

public class Value {

	protected float start, end;
	protected FloatPointer value;
	
	public Value(FloatPointer value, float end) {
		this.value = value;
		this.end = end;
	}
	
	public float getStart() {
		return start;
	}

	public void setStart(float start) {
		this.start = start;
	}

	public float getEnd() {
		return end;
	}

	public void setEnd(float end) {
		this.end = end;
	}

	public FloatPointer getValue() {
		return value;
	}

	public void setValue(FloatPointer value) {
		this.value = value;
	}
	
}
