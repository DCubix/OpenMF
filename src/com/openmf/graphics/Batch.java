package com.openmf.graphics;

public class Batch {
	public int offset, count;
	public Texture texture;
	public float z;
	public PrimitiveType primType;
	public BlendMode blendMode;
	
	public Batch(int offset, int count, Texture texture, float z, PrimitiveType prim, BlendMode blendMode) {
		this.offset = offset;
		this.count = count;
		this.texture = texture;
		this.z = z;
		this.primType = prim;
		this.blendMode = blendMode;
	}
	
}
