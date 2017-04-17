package com.openmf.graphics;

import com.openmf.math.MathUtils;
import com.openmf.math.Vec3;
import com.openmf.math.Vec4;

public class Color {

	public static final int SIZE = 16;
	
	public static final Color BLACK = new Color(0.0f);
	public static final Color WHITE = new Color(1.0f);
	
	public float r, g, b, a;

	public Color(Color c) {
		this.r = c.r;
		this.g = c.g;
		this.b = c.b;
		this.a = c.a;
	}

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Color(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}

	public Color(float v) {
		this(v, v, v);
	}

	public Color(int grey, int alpha) {
		this(grey / 255f, grey / 255f, grey / 255f, (float) alpha / 255f);
	}

	public Color(long hex) {
		long _a = (hex & 0xFF000000) >> 24;
		long _r = (hex & 0x00FF0000) >> 16;
		long _g = (hex & 0x0000FF00) >> 8;
		long _b = (hex & 0x000000FF);
		r = (float) _r / 255.0f;
		g = (float) _g / 255.0f;
		b = (float) _b / 255.0f;
		a = (float) _a / 255.0f;
	}

	public Color set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	public int hex() {
		int _r = (int) (255 * this.r);
		int _g = (int) (255 * this.g);
		int _b = (int) (255 * this.b);
		return ((_r & 0xFF) << 16) | ((_g & 0xFF) << 8) | (_b & 0xFF);
	}

	public Color blend(Color cb, float f) {
		return new Color(
			MathUtils.clamp((r + cb.r) * f, 0.0f, 1.0f),
			MathUtils.clamp((g + cb.g) * f, 0.0f, 1.0f),
			MathUtils.clamp((b + cb.b) * f, 0.0f, 1.0f),
			MathUtils.clamp((a + cb.a) * f, 0.0f, 1.0f)
		);
	}

	public float[] toArray() {
		return new float[] { r, g, b, a };
	}
	
	public byte[] toByteArray() {
		return new byte[] { (byte) (r * 255), (byte) (g * 255), (byte) (b * 255), (byte) (a * 255) };
	}

	public Vec4 xyzw() {
		return new Vec4(r, g, b, a);
	}

	public Vec3 xyz() {
		return new Vec3(r, g, b);
	}

	public Color bright(float v) {
		return new Color(
			MathUtils.clamp((r * v), 0.0f, 1.0f),
			MathUtils.clamp((g * v), 0.0f, 1.0f),
			MathUtils.clamp((b * v), 0.0f, 1.0f),
			MathUtils.clamp(a, 0.0f, 1.0f)
		);
	}

	public Color add(Color rhs) {
		return new Color(
			r + rhs.r,
			g + rhs.g,
			b + rhs.b,
			a + rhs.a
		);
	}
	
	public Color sub(Color rhs) {
		return new Color(
			r - rhs.r,
			g - rhs.g,
			b - rhs.b,
			a - rhs.a
		);
	}
	
	public Color mul(Color rhs) {
		return new Color(
			r * rhs.r,
			g * rhs.r,
			b * rhs.r,
			a * rhs.a
		);
	}
	
	public Color mul(float rhs) {
		return new Color(
			r * rhs,
			g * rhs,
			b * rhs,
			a * rhs
		);
	}
	
	public Color div(float rhs) {
		return new Color(
			r / rhs,
			g / rhs,
			b / rhs,
			a / rhs
		);
	}
	
	@Override
	public boolean equals(Object c) {
		Color o = (Color) c;
		return o.r == r && o.g == g && o.b == b && o.a == a;
	}

	@Override
	public String toString() {
		return r + ", " + g + ", " + b + ", " + a;
	}
}
