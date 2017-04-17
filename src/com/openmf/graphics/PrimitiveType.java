package com.openmf.graphics;

import static android.opengl.GLES20.*;

public enum PrimitiveType {
	TRIANGLES(GL_TRIANGLES),
	TRIANGLE_STRIP(GL_TRIANGLE_STRIP),
	TRIANGLE_FAN(GL_TRIANGLE_FAN),
	LINES(GL_LINES),
	LINE_LOOP(GL_LINE_LOOP),
	LINE_STRIP(GL_LINE_STRIP),
	POINTS(GL_POINTS);
	
	public int value;
	private PrimitiveType(int v) {
		value = v;
	}
}
