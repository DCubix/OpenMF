package com.openmf.math;

import java.nio.FloatBuffer;
import android.opengl.Matrix;

import com.openmf.core.BufferUtils;

public class Mat4 {

	public static final int M00 = 0;
	public static final int M01 = 4;
	public static final int M02 = 8;
	public static final int M03 = 12;
	public static final int M10 = 1;
	public static final int M11 = 5;
	public static final int M12 = 9;
	public static final int M13 = 13;
	public static final int M20 = 2;
	public static final int M21 = 6;
	public static final int M22 = 10;
	public static final int M23 = 14;
	public static final int M30 = 3;
	public static final int M31 = 7;
	public static final int M32 = 11;
	public static final int M33 = 15;
   
	public static final int SIZE = 4 * 4 * 4;
	
	private float[] m;

	public Mat4() {
		m = new float[16];
	}
	
	public Mat4(float... m) {
		this.m = m;
	}

	public static Mat4 identity() {
		Mat4 ret = new Mat4();
		Matrix.setIdentityM(ret.m, 0);
		return ret;
	}

	public static Mat4 translation(float x, float y, float z) {
		Mat4 ret = Mat4.identity();
		Matrix.translateM(ret.m, 0, x, y, z);
		return ret;
	}

	public static Mat4 translation(Vec3 t) {
		return translation(t.x, t.y, t.z);
	}

	public static Mat4 rotation(float x, float y, float z) {
		Mat4 ret = new Mat4();
		Matrix.setRotateEulerM(ret.m, 0, x, y, z);
		return ret;
	}

	public static Mat4 rotation(Vec3 axis, float a) {
		Mat4 ret = new Mat4();
		Matrix.setRotateM(ret.m, 0, a * (float)Math.PI / 180.0f, axis.x, axis.y, axis.z);
		return ret;
	}

	public static Mat4 scale(float x, float y, float z) {
		Mat4 ret = Mat4.identity();
		Matrix.scaleM(ret.m, 0, x, y, z);
		return ret;
	}

	public static Mat4 scale(Vec3 s) {
		return scale(s.x, s.y, s.z);
	}

	public static Mat4 perspective(float fov, float aspectRatio, float zNear, float zFar) {
		Mat4 ret = Mat4.identity();
		Matrix.perspectiveM(ret.m, 0, fov, aspectRatio, zNear, zFar);
		return ret;
	}

	public static Mat4 ortho(float l, float r, float b, float t, float znear, float zfar) {
		Mat4 ret = Mat4.identity();
		Matrix.orthoM(ret.m, 0, l, r, b, t, znear, zfar);
		return ret;
	}

	public Mat4 transpose() {
		Mat4 cp = clone();
		Matrix.transposeM(m, 0, cp.m, 0);
		return this;
	}
	
	public Mat4 invert() {
		Mat4 cp = clone();
		Matrix.invertM(m, 0, cp.m, 0);
		return this;
	}
	
	public Vec3 transform(Vec3 r, float w) {
		return mul(new Vec4(r.x, r.y, r.z, w)).xyz();
	}

	public Mat4 mul(Mat4 r) {
		Mat4 res = new Mat4();
		Matrix.multiplyMM(res.m, 0, m, 0, r.m, 0);
		return res;
	}
	
	public Vec4 mul(Vec4 rhs) {
		float[] rvec = new float[] { rhs.x, rhs.y, rhs.z, rhs.w };
		float[] vec = new float[4];
		Matrix.multiplyMV(vec, 0, m, 0, rvec, 0);
		return new Vec4(vec[0], vec[1], vec[2], vec[3]);
	}

	public Mat4 clone() {
		float[] _m = new float[16];
		System.arraycopy(m, 0, _m, 0, 16);
		return new Mat4(_m);
	}

	public float[] getM() {
		return m;
	}

	public float get(int line, int col) {
		return m[line * 4 + col];
	}

	private final static FloatBuffer direct = BufferUtils.createFloatBuffer(16);

	public FloatBuffer get() {
		direct.clear();
		for (int i = 0; i < 16; i++) {
			direct.put(m[i]);
		}
		direct.flip();
		return direct;
	}

	@Override
	public String toString() {
		return "[ " + get(0, 0) + ", " + get(0, 1) + ", " + get(0, 2) + ", " + get(0, 3) + " ]\n"
			   + "[ " + get(1, 0) + ", " + get(1, 1) + ", " + get(1, 2) + ", " + get(1, 3) + " ]\n"
			   + "[ " + get(2, 0) + ", " + get(2, 1) + ", " + get(2, 2) + ", " + get(2, 3) + " ]\n"
			   + "[ " + get(3, 0) + ", " + get(3, 1) + ", " + get(3, 2) + ", " + get(3, 3) + " ]";
	}
}
