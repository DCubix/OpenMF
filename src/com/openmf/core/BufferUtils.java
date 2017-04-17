package com.openmf.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {

	public static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
	}
	
	public static FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size * 4).asFloatBuffer();
	}
	
	public static IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size * 4).asIntBuffer();
	}
	
	public static ByteBuffer wrap(byte...values) {
		ByteBuffer b = createByteBuffer(values.length);
		b.put(values);
		b.flip();
		return b;
	}

	public static FloatBuffer wrap(float...values) {
		FloatBuffer fb = createFloatBuffer(values.length);
		fb.put(values);
		fb.flip();
		return fb;
	}
	
	public static IntBuffer wrap(int...values) {
		IntBuffer ib = createIntBuffer(values.length);
		ib.put(values);
		ib.flip();
		return ib;
	}
	
}
