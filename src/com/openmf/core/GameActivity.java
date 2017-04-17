package com.openmf.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;

public abstract class GameActivity extends AndroidApplication {

	private GameEngine gameEngine;
	private ScreenOrientation orientation;
	private double fps;

	public abstract void onPreLoad();
	public abstract void onCreate(GameEngine ge);

	public GameActivity(ScreenOrientation orientation, double fps) {
		this.orientation = orientation;
		this.fps = fps;
	}
	
	public GameActivity(ScreenOrientation orientation) {
		this(orientation, 60.0);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		switch (orientation) {
			case LANDSCAPE:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); break;
			case PORTRAIT:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); break;
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		config.useCompass = false;
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		gameEngine = new GameEngine(this, fps);
		initialize(gameEngine, config);
	}

	public int getWidth() {
		return Gdx.graphics.getWidth();
	}
	
	public int getHeight() {
		return Gdx.graphics.getHeight();
	}
	
}
