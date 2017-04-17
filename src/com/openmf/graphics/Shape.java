package com.openmf.graphics;

import java.util.ArrayList;

public class Shape {

	protected ArrayList<Vertex> vertices;
	protected Texture texture;
	protected PrimitiveType primType;
	protected BlendMode blendMode;
	
	public Shape() {
		vertices = new ArrayList<>();
		texture = null;
		blendMode = BlendMode.NORMAL;
	}

	public Shape(Vertex...vs) {
		this();
		for (Vertex v : vs) {
			this.vertices.add(v);
		}
	}
	
	public Shape(Texture texture, Vertex...vs) {
		this(vs);
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public PrimitiveType getPrimType() {
		return primType;
	}

	public void setPrimType(PrimitiveType primType) {
		this.primType = primType;
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public BlendMode getBlendMode() {
		return blendMode;
	}

	public void setBlendMode(BlendMode blendMode) {
		this.blendMode = blendMode;
	}
	
}
