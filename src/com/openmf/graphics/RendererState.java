package com.openmf.graphics;

import com.openmf.math.Rect;
import com.openmf.math.Vec2;
import com.openmf.math.Vec3;

public class RendererState {

	public Vec3 position = new Vec3();
	public Vec2 scale = new Vec2(1.0f);
	public Rect uv = new Rect(0, 0, 1, 1);
	public Vec2 origin = new Vec2();
	public Color color = new Color(1.0f);
	public float rotation = 0;
	public BlendMode blendMode = BlendMode.NORMAL;
	
	public RendererState copy() {
		RendererState cpy = new RendererState();
		cpy.color = new Color(color);
		cpy.origin = new Vec2(origin);
		cpy.position = new Vec3(position);
		cpy.rotation = rotation;
		cpy.scale = new Vec2(scale);
		cpy.uv = new Rect(uv);
		cpy.blendMode = blendMode;
		return cpy;
	}
}
