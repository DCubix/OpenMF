package com.openmf.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.openmf.graphics.Font;
import com.openmf.graphics.Texture;
import com.openmf.graphics.TextureType;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Assets {

	private static HashMap<String, Texture> textures = new HashMap<>();
	private static HashMap<String, FileHandle> sounds = new HashMap<>();
	private static HashMap<String, FileHandle> musics = new HashMap<>();
	private static HashMap<String, Font> fonts = new HashMap<>();
	
	private static HashMap<String, AssetType> rawAssets = new HashMap<>();
	
	public static void destroy() {
		for (Entry<String, Texture> e : textures.entrySet()) {
			e.getValue().dispose();
		}
		fonts.clear();
		textures.clear();
		sounds.clear();
		musics.clear();
	}
	
	public static void addAsset(String path, AssetType type) {
		if (!rawAssets.containsKey(path)) {
			rawAssets.put(path, type);
		}
	}
	
	public static void reload(Context ctx) {
		for (Iterator<Entry<String, AssetType>> it = rawAssets.entrySet().iterator(); it.hasNext();) {
			Entry<String, AssetType> e = it.next();
			
			String path = e.getKey();
			AssetType type = e.getValue();
			switch (type) {
				case MUSIC: {
					if (!musics.containsKey(path)) {
						musics.put(path, Gdx.files.internal(path));
					}
				} break;
				case SOUND_FX: {
					if (!sounds.containsKey(path)) {
						sounds.put(path, Gdx.files.internal(path));
					}
				} break;
				case TEXTURE: {
					if (!textures.containsKey(path)) {
						InputStream is = Gdx.files.internal(path).read();
						Bitmap bmp = BitmapFactory.decodeStream(is);
						textures.put(path, new Texture(bmp, TextureType.RGBA));
					}
				} break;
				case BM_FONT: {
					if (!fonts.containsKey(path)) {
						InputStream is = Gdx.files.internal(path).read();
						fonts.put(path, Font.load(is));
					}
				} break;
			}
			
			System.out.println("\nLoaded: " + path + "("+type.toString()+")");
		}
	}
	
	// TODO: Currently only Texture supports instant loading.
	public static Texture getTexture(String path) {
		if (!textures.containsKey(path)) {
			InputStream is = Gdx.files.internal(path).read();
			Bitmap bmp = BitmapFactory.decodeStream(is);
			textures.put(path, new Texture(bmp, TextureType.RGBA));
		}
		return textures.get(path);
	}
	
	public static FileHandle getSound(String path) {
		return sounds.get(path);
	}
	
	public static FileHandle getMusic(String path) {
		return musics.get(path);
	}
	
	public static Font getFont(String path) {
		return fonts.get(path);
	}
		
}
