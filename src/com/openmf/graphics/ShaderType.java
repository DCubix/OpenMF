package com.openmf.graphics;

import static android.opengl.GLES20.*;

public enum ShaderType {
	VERTEX(GL_VERTEX_SHADER),
	FRAGMENT(GL_FRAGMENT_SHADER);
	
	public int value;
	ShaderType(int v) {
		value = v;
	}
}
