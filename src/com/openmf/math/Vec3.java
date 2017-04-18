package com.openmf.math;

public class Vec3 {

	public static final int SIZE = 12;
	public float x, y, z;

	public Vec3() {
		this(0);
	}

	public Vec3(float d) {
		this(d, d, d);
	}

	public Vec3(Vec2 b, float z) {
		this(b.x, b.y, z);
	}
	
	public Vec3(Vec3 b) {
		this(b.x, b.y, b.z);
	}
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3 set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vec3 set(Vec3 pos) {
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;
		return this;
	}

	public float dot(Vec3 b) {
		return x * b.x + y * b.y + z * b.z;
	}

	public float length() {
		return (float) Math.sqrt(dot(this));
	}

	public Vec3 normalized() {
		float l = length();
		return new Vec3(x / l, y / l, z / l);
	}

	public Vec3 cross(Vec3 b) {
		return new Vec3(
			y * b.z - z * b.y,
			z * b.x - x * b.z,
			x * b.y - y * b.x
		);
	}
	
	public Vec3 project(Mat4 matrix) {
		final float l_mat[] = matrix.getM();
		final float l_w = 1f / (x * l_mat[Mat4.M30] + y * l_mat[Mat4.M31] + z * l_mat[Mat4.M32] + l_mat[Mat4.M33]);
		return new Vec3(
				(x * l_mat[Mat4.M00] + y * l_mat[Mat4.M01] + z * l_mat[Mat4.M02] + l_mat[Mat4.M03]) * l_w,
				(x * l_mat[Mat4.M10] + y * l_mat[Mat4.M11] + z * l_mat[Mat4.M12] + l_mat[Mat4.M13])	* l_w,
				(x * l_mat[Mat4.M20] + y * l_mat[Mat4.M21] + z * l_mat[Mat4.M22] + l_mat[Mat4.M23]) * l_w);
	}

	public Vec3 add(Vec3 b) {
		return new Vec3(x + b.x, y + b.y, z + b.z);
	}

	public Vec3 sub(Vec3 b) {
		return new Vec3(x - b.x, y - b.y, z - b.z);
	}

	public Vec3 mul(Vec3 b) {
		return new Vec3(x * b.x, y * b.y, z * b.z);
	}

	public Vec3 div(Vec3 b) {
		return new Vec3(x / b.x, y / b.y, z / b.z);
	}

	public Vec3 add(float b) {
		return add(new Vec3(b));
	}

	public Vec3 sub(float b) {
		return sub(new Vec3(b));
	}

	public Vec3 mul(float b) {
		return mul(new Vec3(b));
	}

	public Vec3 div(float b) {
		return div(new Vec3(b));
	}

	public Vec3 negated() {
		return new Vec3(-x, -y, -z);
	}

	public Vec3 rotate(Vec3 axis, float angle) {
		float sinAngle = (float) Math.sin(-angle);
		float cosAngle = (float) Math.cos(-angle);

		return this.cross(axis.mul(sinAngle)).add( //Rotation on local X
			(this.mul(cosAngle)).add( //Rotation on local Z
			axis.mul(this.dot(axis.mul(1 - cosAngle))))); //Rotation on local Y
	}

	public Vec3 lerp(Vec3 dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}

	public Vec2 xy() {
		return new Vec2(x, y);
	}
	
	public boolean equals(Vec3 v) {
		return x == v.x && y == v.y && z == v.z;
	}

	@Override
	public String toString() {
		return x + ", " + y + ", " + z;
	}
}
