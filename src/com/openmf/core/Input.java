package com.openmf.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.openmf.math.Vec3;

public class Input implements InputProcessor {

	public static final int MAX_TOUCHES = 20;
	
	protected boolean[] down = new boolean[MAX_TOUCHES];
	protected boolean[] touched = new boolean[MAX_TOUCHES];
	protected boolean[] released = new boolean[MAX_TOUCHES];
	protected Touch[] touches = new Touch[MAX_TOUCHES];
	protected int touchCount = 0;

	public Input() {
		Gdx.input.setInputProcessor(this);
	}
	
	public int getTouchCount() {
		return touchCount;
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
	
	public Touch getTouch(int index) {
		return touches[index];
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
		if (touches[p] == null) {
			touches[p] = new Touch();
		} else {
			touches[p].position.set(x, y);
			touches[p].deltaPosition.set(
				Gdx.input.getDeltaX(p),
				Gdx.input.getDeltaY(p)
			);
		}
		touchCount++;
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int p) {
		if (touches[p] == null) {
			touches[p] = new Touch();
		} else {
			touches[p].position.set(x, y);
			touches[p].deltaPosition.set(
				Gdx.input.getDeltaX(p),
				Gdx.input.getDeltaY(p)
			);
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int p, int btn) {
		released[p] = true;
		down[p] = false;
		if (touches[p] == null) {
			touches[p] = new Touch();
		} else {
			touches[p].position.set(x, y);
			touches[p].deltaPosition.set(
				Gdx.input.getDeltaX(p),
				Gdx.input.getDeltaY(p)
			);
		}
		touchCount--;
		return false;
	}
		
}
