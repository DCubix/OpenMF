package com.openmf.logic.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.openmf.core.GameEngine;
import com.openmf.core.IReplicable;
import com.openmf.graphics.BlendMode;
import com.openmf.graphics.Color;
import com.openmf.graphics.Renderer2D;
import com.openmf.graphics.Texture;
import com.openmf.logic.Component;
import com.openmf.math.Rect;
import com.openmf.math.Vec2;

public class Sprite extends Component {

	private class Animation implements IReplicable {
		public float time, speed;
		public int frame;
		public int[] frames;
		public boolean loop;
		
		@Override
		public Object getReplica() {
			Animation ani = new Animation();
			ani.frame = frame;
			ani.frames = frames.clone();
			ani.loop = loop;
			ani.speed = speed;
			ani.time = time;
			return ani;
		}
	}
	
	private Texture texture;
	private Vec2 origin;
	private Color color;
	private Rect clipRect;
	
	// Animation
	private int frameCount;
	private ArrayList<Rect> frames;
	private HashMap<String, Animation> animations;
	private String currentAnimation;
	
	public Sprite(Texture texture) {
		this.texture = texture;
		this.origin = new Vec2();
		this.color = Color.WHITE;
		this.clipRect = Rect.DEFAULT;
		this.frames = new ArrayList<>();
		this.animations = new HashMap<>();
		this.currentAnimation = "";
	}
	
	public Sprite setup(int rows, int cols) {
		frameCount = rows * cols;
		
		float w = 1.0f / cols;
		float h = 1.0f / rows;
		
		frames.clear();
		for (int i = 0; i < frameCount; i++) {
			Rect r = new Rect(0, 0, 0, 0);
			r.x = (float)(i % cols) * w;
			r.y = (float)((int)(i / cols)) * h;
			r.w = w;
			r.h = h;
			frames.add(r);
		}
		
		return this;
	}
	
	public Sprite addAnimation(String name, int... frames) {
		if (!animations.containsKey(name)) {
			Animation anim = new Animation();
			anim.frame = 0;
			anim.frames = frames;
			anim.time = 0.0f;
			anim.loop = false;
			anim.speed = 1.0f;
			animations.put(name, anim);
			
			if (currentAnimation.isEmpty()) {
				currentAnimation = name;
			}
		}
		return this;
	}
	
	public Sprite addAnimation(String name) {
		if (!animations.containsKey(name)) {
			Animation anim = new Animation();
			anim.frame = 0;
			anim.frames = null;
			anim.time = 0.0f;
			anim.loop = false;
			anim.speed = 1.0f;
			animations.put(name, anim);
			
			if (currentAnimation.isEmpty()) {
				currentAnimation = name;
			}
		}
		return this;
	}
	
	public void playAnimation(String name, float speed, boolean loop, int startFrame) {
		if (animations.containsKey(name)) {
			Animation anim = getAnimation(name);
			anim.frame = startFrame;
			anim.speed = speed;
			anim.loop = loop;
			currentAnimation = name;
		}
	}
	
	public Animation getAnimation(String name) {
		if (animations.containsKey(name)) {
			return animations.get(name);
		}
		return null;
	}
	
	@Override
	public void onStart(GameEngine ge) {
		
	}

	@Override
	public void onUpdate(float dt) {
		if (animations.isEmpty()) { return; }
		if (currentAnimation.isEmpty()) { return; }
		
		Animation anim = getAnimation(currentAnimation);
		
		int maxf = frameCount;
		if (anim.frames != null) {
			maxf = anim.frames.length;
		}
		
		if ((anim.time += dt) >= anim.speed) {
			anim.time = 0;
			if (anim.frame++ >= maxf - 1) {
				if (anim.loop) {
					anim.frame = 0;
				} else {
					anim.frame = maxf - 1;
				}
			}
		}
		
		int frame = 0;
		if (anim.frames == null) {
			frame = anim.frame;
		} else {
			frame = anim.frames[anim.frame];
		}
		
		clipRect = frames.get(frame);
	}

	@Override
	public void onRender(Renderer2D renderer) {
		if (texture != null) {
			renderer.save();
			
			renderer.setBlendMode(BlendMode.NORMAL);
			renderer.setColor(color);
			renderer.setOrigin(origin);
			renderer.setPosition(getOwner().getPosition());
			renderer.setRotation(getOwner().getRotation());
			renderer.setScale(getOwner().getScale());
			renderer.setUV(clipRect);
			
			renderer.drawSprite(texture);
			
			renderer.restore();
		}
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Vec2 getOrigin() {
		return origin;
	}

	public void setOrigin(Vec2 origin) {
		this.origin = origin;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Rect getClipRect() {
		return clipRect;
	}

	public void setClipRect(Rect clipRect) {
		this.clipRect = clipRect;
	}

	@Override
	public Object getReplica() {
		Sprite spr = new Sprite(texture);
		spr.clipRect = new Rect(clipRect);
		spr.color = new Color(color);
		spr.currentAnimation = currentAnimation;
		spr.frameCount = frameCount;
		spr.origin = new Vec2(origin);
		for (Iterator<Rect> it = frames.iterator(); it.hasNext();) {
			Rect rec = it.next();
			spr.frames.add(new Rect(rec));
		}
		for (Iterator<Entry<String, Animation>> it = animations.entrySet().iterator(); it.hasNext();) {
			Entry<String, Animation> e = it.next();
			spr.animations.put(e.getKey(), (Animation) e.getValue().getReplica());
		}
		return spr;
	}

}
