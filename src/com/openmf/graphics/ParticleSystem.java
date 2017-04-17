package com.openmf.graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.openmf.core.IDisposable;
import com.openmf.math.Rect;
import com.openmf.math.Vec2;
import com.openmf.math.Vec3;

public class ParticleSystem implements IDisposable {
	
	public static final int MAX_PARTICLES = 10000;
	
	protected class Particle {
		public Vec2 position, velocity;
		public Color color, deltaColor;
		
		public Texture texture;
		
		public float life = -1;
		public float scale, deltaScale;
		public float rotation, angularSpeed;
		public int z = 0;
		
		public ParticleTransformCallback callback;
		public ParticleConfiguration config;
				
		public Particle() {
			position = new Vec2();
			velocity = new Vec2();
			color = new Color(0);
			deltaColor = new Color(0);
			texture = null;
			callback = null;
			config = new ParticleConfiguration();
		}
		
		public void init() {
			Vec2 posv = new Vec2(
				config.positionVariation.x * randF(-1.0f, 1.0f),
				config.positionVariation.y * randF(-1.0f, 1.0f)
			);

			if (callback != null) {
				posv = callback.run(new Vec2(randF(0.0f, 1.0f), randF(-1.0f, 1.0f)));
			}
			position = posv.add(position);

			angularSpeed = config.angularSpeed + config.angularSpeedVariation * randF(-1.0f, 1.0f);

			float angle = config.angle + config.angleVariation * randF(-1.0f, 1.0f);
			float speed = config.speed + config.speedVariation * randF(-1.0f, 1.0f);

			velocity.x = (float) (speed * Math.cos(angle));
			velocity.y = (float) (speed * Math.sin(angle));

			life = Math.max(0.001f, config.life + config.lifeVariation * randF(-1.0f, 1.0f));

			float startScale = config.startScale + config.startScaleVariation * randF(0.0f, 1.0f);
			float endScale = config.endScale + config.endScaleVariation * randF(0.0f, 1.0f);
			scale = startScale;
			deltaScale = (endScale - startScale) / life;

			Color startColor = config.startColor.add(config.startColorVariation.mul(randF(0.0f, 1.0f)));
			Color endColor = config.endColor.add(config.endColorVariation.mul(randF(0.0f, 1.0f)));

			color = startColor;
			deltaColor = (endColor.sub(startColor)).div(life);
		}
		
		public boolean update(float dt) {
			if (life > 0) {
				velocity = velocity.add(config.gravity.mul(dt));
				position = position.add(velocity.mul(dt));

				scale += deltaScale * dt;
				color.r += deltaColor.r * dt;
				color.g += deltaColor.g * dt;
				color.b += deltaColor.b * dt;
				color.a += deltaColor.a * dt;

				rotation += angularSpeed * dt;
				if (rotation >= (float)Math.PI * 2.0f) {
					rotation = 0;
				}

				life -= dt;
				return false;
			} else {
				scale = 0;
				position.x = -9999;
				return true;
			}
		}
	}
	
	private ArrayList<Particle> particles;
	private ArrayList<Emitter> emitters;
	
	public ParticleSystem() {
		restart();
	}
	
	public void emit(Texture texture,
			Vec2 position,
			ParticleConfiguration config,
			ParticleTransformCallback cb,
			int z
		)
	{
		if (texture != null) {
			addParticle(texture, position, config, cb, z);
		}
	}

	public Emitter createEmitter(Texture tex) {
		Emitter emitter = new Emitter(tex);
		emitters.add(emitter);
		return emitter;
	}

	public void restart() {
		if (particles == null) {
			particles = new ArrayList<>();
		}
		if (emitters == null) {
			emitters = new ArrayList<>();
		}
		
		particles.clear();
		emitters.clear();
	}
	
	public void render(Renderer2D ren) {
		ren.save();
		synchronized (particles) {
			for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
				Particle p = it.next();
				if (p.life <= 0) { continue; }
	
				ren.setBlendMode(p.config.blendMode);
				ren.setColor(p.color);
				ren.setOrigin(new Vec2(0.5f));
				ren.setPosition(new Vec3(p.position, p.z));
				ren.setRotation(p.rotation);
				ren.setScale(new Vec2(p.scale));
				ren.setUV(Rect.DEFAULT);
	
				ren.drawSprite(p.texture);
			}
		}
		ren.restore();
	}
	
	public void update(float dt) {
		for (Iterator<Emitter> it = emitters.iterator(); it.hasNext();) {
			Emitter e = it.next();

			if (!e.enabled) {
				continue;
			}

			if (!e.loop) {
				e.elapsed += dt;
				if (e.elapsed >= e.duration) {
					it.remove();
					continue;
				}
			}

			if (e.emissionRate > 0) {
				float rate = 1.0f / (float)e.emissionRate;
				e.emitCounter += dt;

				if (e.emitCounter > rate) {
					emit(e.texture, e.position, e.config, e.callback, e.renderOrder);
					e.emitCounter = 0;
				}
			}
		}
		
		synchronized (particles) {
			for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
				Particle p = it.next();
				if (p.update(dt)) {
					it.remove();
				}
			}
		}
	}

	private void addParticle(Texture texture, Vec2 position,
			ParticleConfiguration config,
			ParticleTransformCallback cb, int z)
	{
		if (particles.size() > MAX_PARTICLES) {
			return;
		}
		
		Particle p = new Particle();
		p.texture = texture;
		p.z = z;
		p.position = position;
		if (config != null) {
			p.config = config;
		}
		p.callback = cb;
		
		p.init();
		
		particles.add(p);
	}
	
	@Override
	public void dispose() {
		emitters.clear();
		particles.clear();
	}
	
	private static float randF(float min, float max) {
		Random r = new Random();
		return min + (max - min) * r.nextFloat();
	}
}
