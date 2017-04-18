package com.openmf.logic.components;

import com.openmf.core.GameEngine;
import com.openmf.graphics.Renderer2D;
import com.openmf.logic.Component;
import com.openmf.math.Mat4;
import com.openmf.math.MathUtils;
import com.openmf.math.Vec3;

public class Camera extends Component {

	private float zoom;
	
	public Camera() {
		this.zoom = 1.0f;
	}
	
	@Override
	public void onStart(GameEngine ge) {
		
	}
	
	@Override
	public void onUpdate(float dt) {
		
	}
	
	@Override
	public void onRender(Renderer2D renderer) {
		float midW = renderer.getScreenWidth() * 0.5f;
		float midH = renderer.getScreenHeight() * 0.5f;
		
		Vec3 pos = new Vec3(getOwner().getPosition().xy(), 0);
		Mat4 T1 = Mat4.translation(midW, midH, 0);
		Mat4 S = Mat4.scale(zoom, zoom, 1);
		Mat4 R = Mat4.rotation(new Vec3(0, 0, 1), -getOwner().getRotation());
		//Mat4 T2 = Mat4.translation(-midW, -midH, 0);
		Mat4 T3 = Mat4.translation(pos.negated());
		
		Mat4 view = T1.mul(S).mul(R).mul(T3);
		
		renderer.setView(view);
	}
	
	@Override
	public Object getReplica() {
		Camera cam = new Camera();
		cam.zoom = zoom;
		return cam;
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float zoom) {
		this.zoom = MathUtils.clamp(zoom, 0.1f, 20.0f);
	}
	
}
