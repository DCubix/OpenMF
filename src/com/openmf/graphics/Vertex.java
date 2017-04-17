package com.openmf.graphics;

import com.openmf.math.Vec2;
import com.openmf.math.Vec3;

public class Vertex {

	public Vec3 position;
	public Vec2 uv;
	public Color color;
	
	public static int SIZE = 36;
	
	public Vertex() {
		this.position = new Vec3();
		this.uv = new Vec2();
		this.color = new Color(1.0f);
	}
	
	public Vertex(Vec3 position, Vec2 uv, Color color) {
		this.position = position;
		this.uv = uv;
		this.color = color;
	}

}
