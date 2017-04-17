package com.openmf.audio;

import com.badlogic.gdx.audio.Sound;
import com.openmf.math.MathUtils;

public class SoundHandle {

	protected Sound sp;
	protected long id;

	protected float volume;
	protected float pitch;
	protected float pan;
	protected boolean loop;
	
	protected SoundHandle(Sound sp) {
		this.sp = sp;
		this.volume = 1.0f;
		this.pitch = 1.0f;
		this.pan = 0.0f;
		this.loop = false;
		this.id = -1;
	}
	
	public void play() {
		if (id != -1) {
			id = -1;
		}
		id = sp.play();
	}
	
	public void resume() {
		sp.resume(id);
	}
	
	public void pause() {
		sp.pause(id);
	}
	
	public void stop() {
		sp.stop(id);
	}
	
	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		if (volume != this.volume) {
			this.volume = MathUtils.clamp(volume, 0.0f, 1.0f);
			sp.setVolume(id, volume);
		}
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		if (pitch != this.pitch) {
			this.pitch = MathUtils.clamp(pitch, 0.5f, 2.0f);
			sp.setPitch(id, this.pitch);
		}
	}

	public float getPan() {
		return pan;
	}

	public void setPan(float pan) {
		if (pan != this.pan) {
			this.pan = MathUtils.clamp(pan, -1.0f, 1.0f);
			float leftVolume = pan < 0 ? pan : 1;
			float rightVolume = pan > 0 ? pan : 1;
			sp.setPan(id, leftVolume, rightVolume);
		}
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		if (loop != this.loop) {
			this.loop = loop;
			sp.setLooping(id, loop);
		}
	}

}
