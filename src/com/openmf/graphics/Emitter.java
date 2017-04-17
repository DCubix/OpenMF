package com.openmf.graphics;

import com.openmf.math.Vec2;

public class Emitter {

	protected Texture texture;
	protected boolean loop, enabled;
	protected float elapsed, duration, emitCounter;
	protected int emissionRate;
	protected ParticleConfiguration config;
	protected Vec2 position;
	protected ParticleTransformCallback callback;
	protected int renderOrder;

	public Emitter(Texture texture) {
		this.texture = texture;
		this.loop = true;
		this.enabled = true;
		this.elapsed = 0;
		this.duration = 0;
		this.emitCounter = 0;
		this.emissionRate = 20;
		this.config = new ParticleConfiguration();
		this.position = new Vec2();
		this.callback = null;
		this.renderOrder = 0;
	}
	
	public Emitter(Texture texture, float duration) {
		this.texture = texture;
		this.loop = false;
		this.enabled = true;
		this.elapsed = 0;
		this.duration = duration;
		this.emitCounter = 0;
		this.emissionRate = 20;
		this.config = new ParticleConfiguration();
		this.position = new Vec2();
		this.callback = null;
		this.renderOrder = 0;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public float getElapsed() {
		return elapsed;
	}

	public void setElapsed(float elapsed) {
		this.elapsed = elapsed;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public int getEmissionRate() {
		return emissionRate;
	}

	public void setEmissionRate(int emissionRate) {
		this.emissionRate = emissionRate;
	}

	public ParticleConfiguration getConfig() {
		return config;
	}

	public void setConfig(ParticleConfiguration config) {
		this.config = config;
	}

	public Vec2 getPosition() {
		return position;
	}

	public void setPosition(Vec2 position) {
		this.position = position;
	}

	public ParticleTransformCallback getCallback() {
		return callback;
	}

	public void setCallback(ParticleTransformCallback callback) {
		this.callback = callback;
	}

	public int getRenderOrder() {
		return renderOrder;
	}

	public void setRenderOrder(int renderOrder) {
		this.renderOrder = renderOrder;
	}

}
