package com.openmf.graphics;

import java.nio.IntBuffer;

import com.badlogic.gdx.Gdx;
import com.openmf.core.BufferUtils;

public class GL {

	public static int createTexture() {
		return Gdx.gl.glGenTexture();
	}

	public static void deleteTexture(int id) {
		Gdx.gl.glDeleteTexture(id);
	}
	
	public static int createBuffer() {
		return Gdx.gl.glGenBuffer();
	}
	
	public static void deleteBuffer(int id) {
		Gdx.gl.glDeleteBuffer(id);
	}
	
	public static int getProgramI(int id, int pname) {
		IntBuffer params = BufferUtils.createIntBuffer(1);
		Gdx.gl.glGetProgramiv(id, pname, params);
		return params.get();
	}
	
	public static int getShaderI(int id, int pname) {
		IntBuffer params = BufferUtils.createIntBuffer(1);
		Gdx.gl.glGetShaderiv(id, pname, params);
		return params.get();
	}
	
}
