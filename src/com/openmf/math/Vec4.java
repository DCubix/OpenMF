package com.openmf.math;

public class Vec4 {

	public static final int SIZE = 16;
	public float x, y, z, w;

	public Vec4() {
		this(0);
	}

	public Vec4(float d) {
		this(d, d, d, d);
	}

	public Vec4(Vec4 b) {
		this(b.x, b.y, b.z, b.w);
	}

	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vec4 set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Vec4 set(int i, float v) {
		switch (i) {
			case 0: x = v; break;
			case 1: y = v; break;
			case 2: z = v; break;
			case 3: w = v; break;
		}
		return this;
	}
	
	public float get(int i) {
		float v = 0;
		switch (i) {
			case 0: v = x; break;
			case 1: v = y; break;
			case 2: v = z; break;
			case 3: v = w; break;
		}
		return v;
	}

	public float dot(Vec4 b) {
		return x * b.x + y * b.y + z * b.z + w * b.w;
	}

	public float length() {
		return (float) Math.sqrt(dot(this));
	}

	public Vec4 add(Vec4 b) {
		return new Vec4(x + b.x, y + b.y, z + b.z, w + b.w);
	}

	public Vec4 sub(Vec4 b) {
		return new Vec4(x - b.x, y - b.y, z - b.z, w - b.w);
	}

	public Vec4 mul(Vec4 b) {
		return new Vec4(x * b.x, y * b.y, z * b.z, w * b.w);
	}

	public Vec4 div(Vec4 b) {
		return new Vec4(x / b.x, y / b.y, z / b.z, w / b.w);
	}

	public Vec4 add(float b) {
		return add(new Vec4(b));
	}

	public Vec4 sub(float b) {
		return sub(new Vec4(b));
	}

	public Vec4 mul(float b) {
		return mul(new Vec4(b));
	}

	public Vec4 div(float b) {
		return div(new Vec4(b));
	}

	public Vec4 negated() {
		return this.mul(-1);
	}

	public Vec4 normalized() {
		float l = length();
		return new Vec4(x / l, y / l, z / l, w / l);
	}

	public Vec3 xyz() {
		return new Vec3(x, y, z);
	}

	@Override
	public String toString() {
		return x + ", " + y + ", " + z + ", " + w;
	}
	
}
