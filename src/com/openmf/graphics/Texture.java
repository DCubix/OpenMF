package com.openmf.graphics;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.graphics.GL20.*;

import com.openmf.core.BufferUtils;
import com.openmf.core.IDisposable;

import android.graphics.Bitmap;

public class Texture implements IDisposable {

	private int width, height, id;
	private TextureType type;
	
	public Texture(int width, int height, TextureType type) {
		this.width = width;
		this.height = height;
		this.type = type;
		
		init((ByteBuffer)null);
		setFilter(GL_LINEAR, GL_LINEAR);
	}
	
	public Texture(int width, int height, Color color, TextureType type) {
		this.width = width;
		this.height = height;
		this.type = type;
		
		int chans = 3;
		switch (type) {
			case LUMINANCE: chans = 1; break;
			case RGB: chans = 3; break;
			case RGBA: chans = 4; break;
		}
		ByteBuffer buf = BufferUtils.createByteBuffer(width * height * chans);
		byte[] col = color.toByteArray();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				for (int c = 0; c < chans; c++) {
					buf.put(col[c]);
				}
			}
		}
		buf.flip();
		
		init(buf);
		setFilter(GL_NEAREST, GL_NEAREST);
	}
	
	public Texture(byte[] data, int width, int height, TextureType type) {
		this.width = width;
		this.height = height;
		this.type = type;
		
		init(BufferUtils.wrap(data));
		
		setFilter(GL_NEAREST, GL_NEAREST);
	}
	
	public Texture(Bitmap bitmap, TextureType type) {
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.type = type;
		
		init(bitmap);
		
		setFilter(GL_NEAREST, GL_NEAREST);
	}
	
	public void setFilter(int filterMIN, int filterMAG) {
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, id);
		Gdx.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filterMIN);
		Gdx.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filterMAG);
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void setWrap(int wrap) {
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, id);
		Gdx.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		Gdx.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void init(ByteBuffer data) {
		if (id <= 0) {
			id = GL.createTexture();
		}
		
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, id);
		
		Gdx.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		Gdx.gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		switch (type) {
			case RGBA:
				Gdx.gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
				break;
			case RGB:
				Gdx.gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
				break;
			case LUMINANCE:
				Gdx.gl.glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, width, height, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, data);
				break;
		}
		Gdx.gl.glGenerateMipmap(GL_TEXTURE_2D);
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void init(Bitmap data) {
		int size = data.getRowBytes() * height;
		ByteBuffer buf = BufferUtils.createByteBuffer(size);
		data.copyPixelsToBuffer(buf);
		buf.flip();
		init(buf);
	}
	
	public void generateMipMaps() {
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, id);
		Gdx.gl.glGenerateMipmap(GL_TEXTURE_2D);
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void bind(int slot) {
		Gdx.gl.glActiveTexture(GL_TEXTURE0 + slot);
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void unbind() {
		Gdx.gl.glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public int getID() {
		return id;
	}
	
	public TextureType getType() {
		return type;
	}

	@Override
	public void dispose() {
		GL.deleteTexture(id);
		id = -1;
	}
	
}
