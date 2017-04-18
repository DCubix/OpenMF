package com.openmf.graphics;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;

import static com.badlogic.gdx.graphics.GL20.*;

import com.openmf.core.BufferUtils;
import com.openmf.core.IDisposable;
import com.openmf.math.Mat4;
import com.openmf.math.Rect;
import com.openmf.math.Vec2;
import com.openmf.math.Vec3;

public class Renderer2D implements IDisposable {

	private int vbo;
	private ArrayList<Shape> shapes;
	private ArrayList<Batch> batches;
	private int prevVBOSize = 0;
	
	private int drawCalls = 0, shapesDrawn = 0;
	
	private Mat4 projection, view;
	private int screenWidth, screenHeight;
	private int v_position = -1, v_uv = -1, v_color = -1;
	
	private ShaderProgram shader;
	private Texture defaultTexture;
	
	private RendererState state, prevState;
	
	public Renderer2D() {
		state = new RendererState();
		
		vbo = GL.createBuffer();
		shapes = new ArrayList<>();
		batches = new ArrayList<>();
		
		String vs =
				"precision highp float;\n" +
				"attribute vec3 v_position;\n" +
				"attribute vec2 v_uv;\n" +
				"attribute vec4 v_color;\n" +
				"uniform mat4 view;\n" +
				"uniform mat4 projection;\n" +
				"varying vec2 vs_uv;\n" +
				"varying vec4 vs_color;\n" +
				"void main() {\n" +
				"	gl_Position = projection * view * vec4(v_position, 1.0);\n" +
				"	vs_uv = v_uv;\n" +
				"	vs_color = v_color;\n" +
				"}";
		String fs = 
				"precision mediump float;\n" +
				"varying vec2 vs_uv;\n" +
				"varying vec4 vs_color;\n" +
				"uniform sampler2D tex0;\n" +
				"void main() {\n" +
				"	gl_FragColor = texture2D(tex0, vs_uv) * vs_color;\n" +
				"}";
		shader = new ShaderProgram();
		shader.addShader(vs, ShaderType.VERTEX);
		shader.addShader(fs, ShaderType.FRAGMENT);
		shader.link();
		
		this.screenWidth = 32;
		this.screenHeight = 32;
		projection = Mat4.ortho(0, 32, 32, 0, -100, 100);
		view = Mat4.identity();
		
		Gdx.gl.glEnable(GL_TEXTURE_2D);
		Gdx.gl.glDisable(GL_CULL_FACE);
		Gdx.gl.glDisable(GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL_BLEND);
		Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		Gdx.gl.glViewport(0, 0, 32, 32);
		
		defaultTexture = new Texture(2, 2, Color.WHITE, TextureType.RGB);
	}
	
	public void resize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
		projection = Mat4.ortho(0, width, height, 0, -100, 100);
		Gdx.gl.glViewport(0, 0, width, height);
	}
	
	public void save() {
		prevState = state;
		state = new RendererState();
	}
	
	public void setPosition(Vec3 pos) { state.position = pos; }
	public void setScale(Vec2 scl) { state.scale = scl; }
	public void setUV(Rect uv) { state.uv = uv; }
	public void setOrigin(Vec2 ori) { state.origin = ori; }
	public void setColor(Color col) { state.color = col; }
	public void setRotation(float rot) { state.rotation = rot; }
	public void setBlendMode(BlendMode blendMode) { state.blendMode = blendMode; }
	public void setFixed(boolean fixed) { state.fixed = fixed; }
	
	public Vec3 unproject (Vec3 screenCoords, float vx, float vy, float vw, float vh) {
		float x = screenCoords.x, y = screenCoords.y;
		x = x - vx;
		y = getScreenHeight() - y - 1;
		y = y - vy;
		screenCoords.x = (2 * x) / vw - 1;
		screenCoords.y = (2 * y) / vh - 1;
		screenCoords.z = 2 * screenCoords.z - 1;
		screenCoords = screenCoords.project(projection.mul(view).invert());
		return screenCoords;
	}
	
	public void restore() {
		state = prevState;
	}
	
	public void drawRect(float x, float y, float w, float h, float z) {
		drawLine(x, y, x + w, y, z);
		drawLine(x + w, y, x + w, y + h, z);
		drawLine(x + w, y + h, x, y + h, z);
		drawLine(x, y, x, y + h, z);
	}
	
	public void drawLine(float x1, float y1, float x2, float y2, float z) {
		Shape shp = new Shape();
		shp.vertices.add(new Vertex(new Vec3(x1, y1, z), new Vec2(), state.color));
		shp.vertices.add(new Vertex(new Vec3(x2, y2, z), new Vec2(), state.color));
		
		shp.texture = defaultTexture;
		shp.primType = PrimitiveType.LINES;
		
		shp.blendMode = state.blendMode;
		
		submit(shp);
	}
	
	public void drawLines(float z, Vec2...points) {
		Shape shp = new Shape();
		
		for (Vec2 p : points) {
			shp.vertices.add(new Vertex(new Vec3(p, z), new Vec2(), state.color));
		}
		
		shp.texture = defaultTexture;
		shp.primType = PrimitiveType.LINE_STRIP;
		
		shp.blendMode = state.blendMode;
		
		submit(shp);
	}
	
	public void drawText(String text, Font font) {
		drawText(text, font, TextAlignment.ALIGN_LEFT, 1.0f);
	}
	
	public void drawText(String text, Font font, TextAlignment align) {
		drawText(text, font, align, 1.0f);
	}
	
	public void drawText(String text, Font font, TextAlignment align, float scale) {
		if (font == null) { return; }
		if (font.getTexture() == null) { return; }
		
		String[] lines = text.split("\n");
		
		Vec3 pos = state.position;
		
		save();
		
		float x = 0;
		float y = 0;
		for (String line : lines) {
			switch (align) {
				case ALIGN_LEFT:
					x = 0;
					break;
				case ALIGN_CENTER:
					x = -computeTextWidth(line, font, scale) / 2;
					break;
				case ALIGN_RIGHT:
					x = -computeTextWidth(line, font, scale);
					break;
			}
			
			for (int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				Font.Char chr;
				if (font.containsChar(c)) {
					chr = font.getCharMap().get((int) c);
				} else {
					chr = font.getCharMap().get(font.getNumChars() - 1);
				}
				
				if (!Character.isWhitespace(c)) {
					setUV(chr.clipRect);
					setPosition(
						new Vec3(pos.x + (x + chr.xoffset * scale) - font.getPadding()[0],
								pos.y + (y + chr.yoffset * scale) - font.getPadding()[1],
								pos.z)
					);
					setScale(new Vec2(scale));
					setColor(state.color);
					
					drawSprite(font.getTexture());
				}
				x += (chr.xadvance - font.getPadding()[2]) * scale;
			}
			y += (font.getLineHeight() - font.getPadding()[3]) * scale;
		}
		
		restore();
	}
	
	public void drawSprite(Texture texture) {
		if (texture == null) {
			return;
		}
		
		float z = state.position.z;
		Rect dst = new Rect(
			state.position.x,
			state.position.y,
			state.scale.x * (float)texture.getWidth(),
			state.scale.y * (float)texture.getHeight()
		);
		Rect uv = state.uv;
		Vec2 origin = state.origin;
		Color color = state.color;
		float rotation = state.rotation;
		
		float width = dst.w * uv.w;
		float height = dst.h * uv.h;

		float cx = origin.x * width;
		float cy = origin.y * height;
		
		Vec2 tl = new Vec2(-cx, -cy);
		Vec2 tr = new Vec2(width - cx, -cy);
		Vec2 br = new Vec2(width - cx, height - cy);
		Vec2 bl = new Vec2(-cx, height - cy);

		Vec2 pos = new Vec2(dst.x, dst.y);
		Vec2 tlr = rotatePoint(tl, rotation).add(pos);
		Vec2 trr = rotatePoint(tr, rotation).add(pos);
		Vec2 brr = rotatePoint(br, rotation).add(pos);
		Vec2 blr = rotatePoint(bl, rotation).add(pos);

		float u1 = uv.x;
		float v1 = uv.y;
		float u2 = uv.x + uv.w;
		float v2 = uv.y + uv.h;
		
		Shape shp = new Shape();
		Vertex TL = new Vertex();
		TL.position = new Vec3(tlr, z);
		TL.uv = new Vec2(u1, v1);
		TL.color = color;

		Vertex TR = new Vertex();
		TR.position = new Vec3(trr, z);
		TR.uv = new Vec2(u2, v1);
		TR.color = color;

		Vertex BR = new Vertex();
		BR.position = new Vec3(brr, z);
		BR.uv = new Vec2(u2, v2);
		BR.color = color;

		Vertex BL = new Vertex();
		BL.position = new Vec3(blr, z);
		BL.uv = new Vec2(u1, v2);
		BL.color = color;
		
		shp.vertices.add(TL);
		shp.vertices.add(TR);
		shp.vertices.add(BR);
		shp.vertices.add(BR);
		shp.vertices.add(BL);
		shp.vertices.add(TL);
		
		shp.texture = texture;
		shp.primType = PrimitiveType.TRIANGLES;
		
		shp.blendMode = state.blendMode;
		
		submit(shp);
	}
	
	public void submit(Shape shape) {
		if (shape != null) {
			shape.fixed = state.fixed;
			shapes.add(shape);
		}
	}

	public void begin() {
		shapesDrawn = shapes.size();
		drawCalls = batches.size();
		
		shapes.clear();
		batches.clear();
	}
	
	public void end() {
		updateBuffer();
	}
	
	public void render() {
		renderGeometry(shader);
	}
	
	public void renderGeometry(ShaderProgram shaderP) {
		shaderP.bind();
		shaderP.setUniformMatrix4("projection", projection);
		shaderP.setUniformMatrix4("view", view);
		shaderP.setUniformInt1("tex0", 0);
		
		Collections.sort(batches, new Comparator<Batch>() {
			@Override
			public int compare(Batch a, Batch b) {
				return a.z < b.z ? -1 : 1;
			}
		});
		
		if (v_position == -1) {
			v_position = shaderP.getAttributeLocation("v_position");
		}
		if (v_uv == -1) {
			v_uv = shaderP.getAttributeLocation("v_uv");
		}
		if (v_color == -1) {
			v_color = shaderP.getAttributeLocation("v_color");
		}
		
		Gdx.gl.glBindBuffer(GL_ARRAY_BUFFER, vbo);
		Gdx.gl.glEnableVertexAttribArray(v_position);
		Gdx.gl.glVertexAttribPointer(v_position, 3, GL_FLOAT, false, Vertex.SIZE, 0);
		Gdx.gl.glEnableVertexAttribArray(v_uv);
		Gdx.gl.glVertexAttribPointer(v_uv, 2, GL_FLOAT, false, Vertex.SIZE, 12);
		Gdx.gl.glEnableVertexAttribArray(v_color);
		Gdx.gl.glVertexAttribPointer(v_color, 4, GL_FLOAT, true, Vertex.SIZE, 20);
		
		for (Batch b : batches) {
			b.texture.bind(0);
			if (b.fixed) {
				shaderP.setUniformMatrix4("view", Mat4.identity());
			}
			
			switch (b.blendMode) {
				case NORMAL: Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); break;
				case ADD: Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE); break;
				case SCREEN: Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_COLOR); break;
			}

			Gdx.gl.glDrawArrays(b.primType.value, b.offset, b.count);

			Gdx.gl.glBindTexture(GL_TEXTURE_2D, 0);
		}
		
		Gdx.gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
		shaderP.unbind();
	}
	
	private void updateBuffer() {
		if (shapes.isEmpty()) {
			return;
		}
		
		Collections.sort(shapes, new Comparator<Shape>() {
			@Override
			public int compare(Shape a, Shape b) {
				if (a.texture == null || b.texture == null) { return -1; }
				return a.texture.getID() < b.texture.getID() ? -1 : 1;
			}
		});
		
		ArrayList<Vertex> vertices = new ArrayList<>();
		
		Shape first = shapes.get(0);
		vertices.addAll(first.vertices);
		batches.add(new Batch(0,
				first.vertices.size(),
				first.texture,
				first.vertices.get(0).position.z,
				first.primType,
				first.blendMode,
				first.fixed));
		
		int offset = 0;//first.vertices.size();
		for (int i = 1; i < shapes.size(); i++) {
			Shape a = shapes.get(i);
			Shape b = shapes.get(i - 1);
			float za = a.vertices.isEmpty() ? 0 : a.vertices.get(0).position.z;
			float zb = b.vertices.isEmpty() ? 0 : b.vertices.get(0).position.z;

			if (a.texture.getID() != b.texture.getID() ||
				za != zb ||
				a.primType.value != b.primType.value ||
				a.blendMode != b.blendMode)
			{
				offset += batches.get(batches.size()-1).count;
				batches.add(new Batch(
						offset,
						a.vertices.size(),
						a.texture,
						za, a.primType,
						a.blendMode,
						a.fixed));
			} else {
				batches.get(batches.size()-1).count += a.vertices.size();
			}
			
			vertices.addAll(a.vertices);
		}
		
		FloatBuffer vdata = BufferUtils.createFloatBuffer(vertices.size() * (Vertex.SIZE / 4));
		for (Vertex v : vertices) {
			vdata.put(v.position.x).put(v.position.y).put(v.position.z);
			vdata.put(v.uv.x).put(v.uv.y);
			vdata.put(v.color.r).put(v.color.g).put(v.color.b).put(v.color.a);
		}
		vdata.flip();

		Gdx.gl.glBindBuffer(GL_ARRAY_BUFFER, vbo);
		int vboSize = vertices.size() * Vertex.SIZE;
		if (vboSize > prevVBOSize) {
			Gdx.gl.glBufferData(GL_ARRAY_BUFFER, vboSize, null, GL_DYNAMIC_DRAW);
			prevVBOSize = vboSize;
		}
		Gdx.gl.glBufferSubData(GL_ARRAY_BUFFER, 0, vboSize, vdata);
		Gdx.gl.glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void setClearColor(Color color) {
		Gdx.gl.glClearColor(color.r, color.g, color.b, 1.0f);
	}

	public void clear() {
		Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public RendererState getState() {
		return state;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public Mat4 getView() {
		return view;
	}

	public void setView(Mat4 view) {
		this.view = view;
	}

	public ShaderProgram getDefaultShader() {
		return shader;
	}

	public Mat4 getProjection() {
		return projection;
	}

	public int getDrawCalls() {
		return drawCalls;
	}

	public int getShapesDrawn() {
		return shapesDrawn;
	}

	@Override
	public void dispose() {
		defaultTexture.dispose();
		shader.dispose();
		GL.deleteBuffer(vbo);
		vbo = -1;
	}

	private static Vec2 rotatePoint(Vec2 p, float rad) {
		float c = (float) Math.cos(rad);
		float s = (float) Math.sin(rad);
		return new Vec2(c * p.x - s * p.y, s * p.x + c * p.y);
	}
	
	private static float computeTextWidth(String text, Font fnt, float scale) {
		float w = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			Font.Char chr;
			if (fnt.containsChar(c)) {
				chr = fnt.getCharMap().get(c);
			} else {
				chr = fnt.getCharMap().get((char) (fnt.getNumChars() - 1));
			}
			w += (float) (chr.xadvance - fnt.getPadding()[2]) * scale;
		}
		return w;
	}
}
