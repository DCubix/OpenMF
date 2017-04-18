package com.openmf.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.openmf.audio.AudioSystem;
import com.openmf.graphics.Color;
import com.openmf.graphics.Font;
import com.openmf.graphics.ParticleSystem;
import com.openmf.graphics.Renderer2D;
import com.openmf.logic.Scene;
import com.openmf.math.MathUtils;
import com.openmf.math.Vec2;
import com.openmf.math.Vec3;
import com.openmf.tweening.Tweens;

public class GameEngine implements ApplicationListener {

	public static final int MAX_FPS_SAMPLES = 32;
	
	private final double timeStep;
	private final double frameCap;
	
	private int frames = 0;
	private float fps, fpsTime = 0;
	private float[] fpsSamples = new float[MAX_FPS_SAMPLES];
	private ArrayList<Float> fpsGraph = new ArrayList<>();
	private boolean showProfiler;
	private Font profilerFont;
	
	private Renderer2D renderer;
	private ParticleSystem particleSystem;
	private Input input;
	
	private HashMap<String, Scene> scenes;
	private String currentScene, nextScene;
	private boolean switching;
	
	private double lastTime, accum;
	
	private GameActivity parentActivity;
	
	public GameEngine(GameActivity parent, double frameCap) {
		this.scenes = new HashMap<>();
		this.currentScene = "";
		this.nextScene = "";
		this.switching = false;
		this.particleSystem = null;
		this.parentActivity = parent;
		this.timeStep = 1.0f / frameCap;
		this.frameCap = frameCap;
	}

	public Scene registerScene(String name) {
		if (!scenes.containsKey(name)) {
			Scene sce = new Scene(this);
			scenes.put(name, sce);
			return sce;
		}
		return null;
	}
	
	public void setScene(String name) {
		if (!nextScene.equals(name)) {
			nextScene = name;
			switching = true;
		}
	}

	@Override
	public void create() {
		if (renderer == null) {
			renderer = new Renderer2D();
		}
		if (particleSystem == null) {
			particleSystem = new ParticleSystem();
		} else {
			particleSystem.restart();
		}
		
		if (input == null) {
			input = new Input();
		}
		
		Globals.engine = this;
		Globals.input = input;
		Globals.renderer = renderer;
		
		AudioSystem.flush();
		Assets.destroy();
		onPreLoad();
		Assets.reload(parentActivity);
		onReload();
		
		lastTime = Time.getTime();
	}

	@Override
	public void dispose() {
		input = null;
		Assets.destroy();
	}

	@Override
	public void pause() {
		input = null;
		Assets.destroy();
		AudioSystem.flush();
	}

	@Override
	public void render() {
		double currentTime = Time.getTime();
		double delta = currentTime - lastTime;
		
		fpsSamples[frames % MAX_FPS_SAMPLES] = 1.0f / (float) Math.max(delta, timeStep);
		for (int i = 0; i < MAX_FPS_SAMPLES; i++) {
			this.fps += fpsSamples[i];
		}
		this.fps /= MAX_FPS_SAMPLES;
		
		lastTime = currentTime;
		
		accum += delta;
		
		while (accum >= timeStep) {
			accum -= timeStep;
			updateFrame((float)timeStep);
			Tweens.update((float)timeStep);
			input.update();
		}
		
		renderFrame();
		frames++;
	}

	@Override
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}

	@Override
	public void resume() {
		if (input == null) {
			input = new Input();
		}
		
		Globals.engine = this;
		Globals.input = input;
		Globals.renderer = renderer;
		
		onPreLoad();
		Assets.reload(parentActivity);
		onReload();
	}

	private void onPreLoad() {
		parentActivity.onPreLoad();
	}
	
	private void onReload() {
		currentScene = "";
		nextScene = "";
		switching = false;
		scenes.clear();
		parentActivity.onCreate(this);
	}

	private void renderFrame() {
		if (scenes.isEmpty()) {
			return;
		}
		
		Scene current = scenes.get(currentScene);
		
		renderer.clear();
		renderer.begin();
		if (!switching && current != null) {
			renderer.setClearColor(current.getBackColor());
			current.onRender(renderer);
			particleSystem.render(renderer);
			
			if (showProfiler) {
				drawProfiler(renderer, profilerFont, 32, 10, 18, 120, 64);
			}
		}
		renderer.end();
		renderer.render();
	}
	
	private void updateFrame(float dt) {
		if (scenes.isEmpty()) {
			return;
		}
		
		if (currentScene.isEmpty() || nextScene.isEmpty()) {
			setScene((String)scenes.keySet().toArray()[0]);
		}
		
		Scene current = scenes.get(currentScene);
		if (!switching) {
			particleSystem.update(dt);
			current.onUpdate(dt);
			
			fpsTime += dt;
			if (fpsTime >= 0.3f) {
				fpsGraph.add(MathUtils.clamp(fps, 0.0f, 60.0f));
				if (fpsGraph.size() >= 32) {
					fpsGraph.remove(0);
				}
				fpsTime = 0;
			}
			
		} else {
			currentScene = nextScene;
			switching = false;
			
			current = scenes.get(currentScene);
			current.onStart();
			
			Globals.currentScene = current;
		}
	}

	public Renderer2D getRenderer() {
		return renderer;
	}

	public ParticleSystem getParticleSystem() {
		return particleSystem;
	}

	public Input getInput() {
		return input;
	}

	public float getFps() {
		return fps;
	}
	
	public boolean isShowProfiler() {
		return this.showProfiler;
	}
	
	public void setShowProfiler(boolean value, Font profilerFont) {
		this.showProfiler = value;
		this.profilerFont = profilerFont;
	}

	public void drawProfiler(Renderer2D renderer, Font font, int res, int x, int y, int w, int h) {
		// draw fps graph
		renderer.save();
		renderer.setFixed(true);
		
		renderer.setColor(Color.WHITE);
		renderer.drawRect(x, y, w, h, 100);
		renderer.drawLine(x, y, x + w + 6, y, 100);
		renderer.drawLine(x, y + h / 2, x + w + 6, y + h / 2, 100);
		renderer.drawLine(x, y + h, x + w + 6, y + h, 100);
		
		renderer.setColor(new Color(0.0f, 1.0f, 0.0f, 1.0f));
		int xstep = (w + res) / res;
		int gx = 0;
		Vec2[] points = new Vec2[fpsGraph.size()];
		for (int i = 0; i < fpsGraph.size(); i++) {
			float ly = MathUtils.remap(fpsGraph.get(i), 0.0f, (float) frameCap, 0.0f, h);
			Vec2 pos = new Vec2(x + gx, y + (h - ly));
			points[i] = pos;
			gx += xstep;
		}
		renderer.drawLines(99, points);
		
		float ty = (y - 7);
		renderer.setColor(Color.WHITE);
		renderer.setPosition(new Vec3(x + w + 6, ty, 99));
		renderer.drawText(StringUtils.format("##.##", frameCap), font);
		
		renderer.setPosition(new Vec3(x + w + 6, ty + h / 2, 99));
		renderer.drawText(StringUtils.format("##.##", frameCap / 2), font);
		
		renderer.setPosition(new Vec3(x + w + 6, ty + h, 99));
		renderer.drawText("0.0", font);
		
		renderer.setPosition(new Vec3(x + 6, y + h, 99));
		renderer.drawText("FPS: " + StringUtils.format("##.##", fps), font);
		
		renderer.restore();
	}
}
