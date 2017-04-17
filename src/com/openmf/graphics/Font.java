package com.openmf.graphics;

import static com.badlogic.gdx.graphics.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.openmf.core.Assets;
import com.openmf.math.Rect;

public class Font {

	public class Char {
		public int id;
		public float xoffset;
		public float yoffset;
		public float xadvance;
		public Rect clipRect;
		
		public Char() {
			clipRect = new Rect(0, 0, 1, 1);
			xoffset = 0;
			yoffset = 0;
			xadvance = 0;
		}
	}

	private Texture texture;
	private HashMap<Integer, Char> charMap;
	private float mapWidth, mapHeight, lineHeight;
	private int numChars;
	private float[] padding;
	
	public Font() {
		this.charMap = new HashMap<>();
		this.padding = new float[4];
	}

	public HashMap<Integer, Char> getCharMap() {
		return charMap;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getMapWidth() {
		return mapWidth;
	}

	public float getMapHeight() {
		return mapHeight;
	}

	public void setMapWidth(float mapWidth) {
		this.mapWidth = mapWidth;
	}

	public void setMapHeight(float mapHeight) {
		this.mapHeight = mapHeight;
	}

	public float getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
	}

	public int getNumChars() {
		return numChars;
	}

	public void setNumChars(int numChars) {
		this.numChars = numChars;
	}

	public float[] getPadding() {
		return padding;
	}

	public void setPadding(float[] padding) {
		this.padding = padding;
	}

////// FONT LOADER ///////////////////////////////////////////////////////////////////
	
	private static String RE_SPLIT = "\\S+='.*?'|\\S+=\".*?\"|\\S+";
	
	private static String getValueStr(String v) {
		return v.substring(v.indexOf('=')+1).replace("\"", "");
	}	
	
	private static int getValue(String v) {
		return Integer.parseInt(getValueStr(v));
	}
	
	private static int[] getValueArr(String v) {
		String[] str_vals = getValueStr(v).split(",");
		int[] vals = new int[str_vals.length];
		for (int i = 0; i < str_vals.length; i++) {
			vals[i] = Integer.parseInt(str_vals[i]);
		}
		return vals;
	}
	
	public static Font load(InputStream is) {
		Font ret = new Font();
		float w = 1, h = 1;
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
		    for (String line; (line = br.readLine()) != null; ) {
		    	ArrayList<String> params = new ArrayList<>();
		    	Pattern reg = Pattern.compile(RE_SPLIT);
		    	Matcher m = reg.matcher(line);
		    	while (m.find()) {
		    		params.add(m.group(0));
		    	}
		    	
		    	String command = params.get(0);
		    	if (command.equals("common")) {
		    		w = getValue(params.get(3));
		    		h = getValue(params.get(4));
		    		ret.setMapWidth(w);
		    		ret.setMapHeight(h);
		    		ret.setLineHeight(getValue(params.get(1)));
		    	} else if (command.equals("page")) {
		    		String fileraw = getValueStr(params.get(2));
		    		Texture tex = Assets.getTexture(fileraw);
		    		tex.setFilter(GL_NEAREST, GL_NEAREST);
		    		ret.setTexture(tex);
		    	} else if (command.equals("char")) {
		    		Font.Char char_ = ret.new Char();
		    		char_.id = getValue(params.get(1));
		    		
		    		char_.clipRect.x = (float)getValue(params.get(2)) / w;
		    		char_.clipRect.y = (float)getValue(params.get(3)) / h;
		    		char_.clipRect.w = (float)getValue(params.get(4)) / w;
		    		char_.clipRect.h = (float)getValue(params.get(5)) / h;
		    		char_.xoffset = (float)getValue(params.get(6));
		    		char_.yoffset = (float)getValue(params.get(7));
		    		char_.xadvance = (float)getValue(params.get(8));
		    		ret.getCharMap().put(char_.id, char_);
		    	} else if (command.equals("chars")) {
		    		ret.setNumChars(getValue(params.get(1)));
		    	} else if (command.equals("info")) {
		    		int[] padding = getValueArr(params.get(10));
		    		ret.setPadding(new float[] {
		    				(float)padding[0],
		    				(float)padding[1],
		    				(float)padding[2],
		    				(float)padding[3] 
		    		});
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	public boolean containsChar(char c) {
		return charMap.containsKey((int) c);
	}
	
}
