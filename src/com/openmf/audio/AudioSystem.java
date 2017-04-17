package com.openmf.audio;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class AudioSystem {

	private static HashMap<String, Sound> sounds = new HashMap<>();
	private static HashMap<String, Music> musics = new HashMap<>();
	
	public static void flush() {
		sounds.clear();
		musics.clear();
	}
	
	public static void addSound(String name, FileHandle data) {
		if (!sounds.containsKey(name)) {
			sounds.put(name, Gdx.audio.newSound(data));
		}
	}
	
	public static void addMusic(String name, FileHandle data) {
		if (!musics.containsKey(name)) {
			musics.put(name, Gdx.audio.newMusic(data));
		}
	}
	
	public static SoundHandle playSound(String name) {
		if (sounds.containsKey(name)) {
			SoundHandle h = new SoundHandle(sounds.get(name));
			h.play();
			return h;
		}
		return null;
	}
	
	public static Music playMusic(String name) {
		if (musics.containsKey(name)) {
			musics.get(name).play();
			return musics.get(name);
		}
		return null;
	}
	
}
