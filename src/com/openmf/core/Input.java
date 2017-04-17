package com.openmf.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.openmf.math.Vec2;
import com.openmf.math.Vec3;

public class Input implements InputProcessor {

	public static final int MAX_TOUCHES = 20;
	
	protected boolean[] down = new boolean[MAX_TOUCHES];
	protected boolean[] touched = new boolean[MAX_TOUCHES];
	protected boolean[] released = new boolean[MAX_TOUCHES];
	protected Vec2[] positions = new Vec2[MAX_TOUCHES];

	public Input() {
		Gdx.input.setInputProcessor(this);
	}
	
	public boolean isTouched(int index) {
		boolean temp = touched[index];
		touched[index] = false;
		return temp;
	}
	
	public boolean isReleased(int index) {
		boolean temp = released[index];
		released[index] = false;
		return temp;
	}
	
	public boolean isHolded(int index) {
		return down[index];
	}
	
	public Vec2 getPosition(int index) {
		return positions[index];
	}
	
	public Vec3 getAccelerometer() {
		return new Vec3(
			Gdx.input.getAccelerometerX(),
			Gdx.input.getAccelerometerY(),
			Gdx.input.getAccelerometerZ()
		);
	}
	
	protected void update() {
		for (int i = 0; i < MAX_TOUCHES; i++) {
			touched[i] = false;
			released[i] = false;
		}
	}

	@Override
	public boolean keyDown(int arg0) {
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int p, int btn) {
		touched[p] = true;
		down[p] = true;
		if (positions[p] == null) {
			positions[p] = new Vec2(x, y);
		} else {
			positions[p].x = x;
			positions[p].y = y;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int p) {
		if (positions[p] == null) {
			positions[p] = new Vec2(x, y);
		} else {
			positions[p].x = x;
			positions[p].y = y;
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int p, int btn) {
		released[p] = true;
		down[p] = false;
		if (positions[p] == null) {
			positions[p] = new Vec2(x, y);
		} else {
			positions[p].x = x;
			positions[p].y = y;
		}
		return false;
	}
		
}
