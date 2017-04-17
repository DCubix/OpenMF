package com.openmf.math;

public class Vec2 {

	public static final int SIZE = 8;
	public float x, y;

	public Vec2() {
		this(0);
	}

	public Vec2(float d) {
		this(d, d);
	}

	public Vec2(Vec2 b) {
		this(b.x, b.y);
	}

	public Vec2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vec2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public float dot(Vec2 b) {
		return x * b.x + y * b.y;
	}

	public float perpendicularDot(Vec2 b) {
		return x * b.y - y * b.x;
	}

	public float length() {
		return (float) Math.sqrt(dot(this));
	}

	public Vec2 add(Vec2 b) {
		return new Vec2(x + b.x, y + b.y);
	}

	public Vec2 sub(Vec2 b) {
		return new Vec2(x - b.x, y - b.y);
	}

	public Vec2 mul(Vec2 b) {
		return new Vec2(x * b.x, y * b.y);
	}

	public Vec2 div(Vec2 b) {
		return new Vec2(x / b.x, y / b.y);
	}

	public Vec2 add(float b) {
		return add(new Vec2(b));
	}

	public Vec2 sub(float b) {
		return sub(new Vec2(b));
	}

	public Vec2 mul(float b) {
		return mul(new Vec2(b));
	}

	public Vec2 div(float b) {
		return div(new Vec2(b));
	}

	public Vec2 negated() {
		return this.mul(-1);
	}

	public Vec2 normalized() {
		float l = length();
		return new Vec2(x / l, y / l);
	}

	public float angle() {
		return (float) Math.atan2(y, x);
	}

	@Override
	public String toString() {
		return x + ", " + y;
	}
}
