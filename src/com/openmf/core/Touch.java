package com.openmf.core;

import com.openmf.math.Vec2;

public class Touch {

	protected int fingerID;
	protected Vec2 position, deltaPosition;
	
	protected Touch() {
		this.fingerID = 0;
		this.position = new Vec2();
		this.deltaPosition = new Vec2();
	}
	
	public int getFingerID() {
		return fingerID;
	}
	
	public Vec2 getPosition() {
		return position;
	}
	
	public Vec2 getDeltaPosition() {
		return deltaPosition;
	}
	
}
