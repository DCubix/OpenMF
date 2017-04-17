package com.openmf.math;

public class Rect {

	public static Rect DEFAULT = new Rect(0, 0, 1, 1);

	public float x, y, w, h;

	public Rect(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Rect(Rect r) {
		this(r.x, r.y, r.w, r.h);
	}

	public Rect add(Rect o) {
		return new Rect(o.x + x, o.y + y, w, h);
	}

	public Rect sub(Rect o) {
		return new Rect(o.x - x, o.y - y, w, h);
	}

	public Rect set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Rect set(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		return this;
	}

	public boolean intersects(Rect b) {
		return x < b.x + b.w
			   && x + w > b.x
			   && y < b.y + b.h
			   && y + h > b.y;
	}

	public void inflate(float ifx, float ify) {
		x -= ifx;
		y -= ify;
		w += ifx * 2;
		h += ify * 2;
	}
	
	public boolean contains(Vec2 p) {
		return intersects(new Rect(p.x, p.y, 1, 1));
	}

	public Vec2 intersectsSide(Rect b) {
		float AcenterX = w / 2 + x;
		float AcenterY = h / 2 + y;
		float BcenterX = b.w / 2 + b.x;
		float BcenterY = b.h / 2 + b.y;
		float rw = 0.5f * (w + b.w);
		float rh = 0.5f * (h + b.h);
		float dx = AcenterX - BcenterX;
		float dy = AcenterY - BcenterY;

		if (Math.abs(dx) <= rw && Math.abs(dy) <= rh) {
			float wy = rw * dy;
			float hx = rw * dx;
			if (wy > hx) {
				if (wy > -hx) {
					return new Vec2(0, -1);
				} else {
					return new Vec2(-1, 0);
				}
			} else if (wy > -hx) {
				return new Vec2(1, 0);
			} else {
				return new Vec2(0, 1);
			}
		}
		return new Vec2(0);
	}

	public Rect getIntersection(Rect r) {
		float tx1 = this.x;
		float ty1 = this.y;
		float rx1 = r.x;
		float ry1 = r.y;
		float tx2 = tx1;
		tx2 += this.w;
		float ty2 = ty1;
		ty2 += this.h;
		float rx2 = rx1;
		rx2 += r.w;
		float ry2 = ry1;
		ry2 += r.h;
		if (tx1 < rx1) {
			tx1 = rx1;
		}
		if (ty1 < ry1) {
			ty1 = ry1;
		}
		if (tx2 > rx2) {
			tx2 = rx2;
		}
		if (ty2 > ry2) {
			ty2 = ry2;
		}
		tx2 -= tx1;
		ty2 -= ty1;

		if (tx2 < Float.MIN_VALUE) {
			tx2 = Float.MIN_VALUE;
		}
		if (ty2 < Float.MIN_VALUE) {
			ty2 = Float.MIN_VALUE;
		}
		return new Rect(tx1, ty1, tx2, ty2);
	}

	public Vec2 getLocation() {
		return new Vec2(x, y);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + "," + w + "," + h + ")";
	}
}
