package com.openmf.graphics;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.graphics.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import com.openmf.core.BufferUtils;
import com.openmf.core.IDisposable;
import com.openmf.math.Mat4;
import com.openmf.math.Vec2;
import com.openmf.math.Vec3;
import com.openmf.math.Vec4;

public class ShaderProgram implements IDisposable {

	private HashMap<String, Integer> uniforms;
	private ArrayList<String> shader_sources;
	private int program = -1;
	private boolean valid = true, bound = false;
	
	public ShaderProgram() {
		this.program = Gdx.gl.glCreateProgram();
		this.uniforms = new HashMap<>();
		this.shader_sources = new ArrayList<>();
	}
	
	public void addShader(String src, ShaderType type) {
		if (src == null) {
			System.err.println("Your shader source cannot be null.");
			valid = false;
		}
		if (src.isEmpty()) {
			System.err.println("Your shader source cannot be empty.");
			valid = false;
		}
		if (program == -1) {
			System.err.println("The program was not created. Hence you cannot do this.");
			valid = false;
		}
		
		if (!valid) {
			return;
		}
		
		int shader = createShader(type.value, src);
		if (shader != -1) {
			Gdx.gl.glAttachShader(this.program, shader);
			shader_sources.add(src);
		}
	}
	
	public void link() {
		if (!valid) {
			return;
		}
		
		Gdx.gl.glLinkProgram(this.program);

		if (GL.getProgramI(this.program, GL_LINK_STATUS) == GL_FALSE) {
			valid = false;
			System.out.println(Gdx.gl.glGetProgramInfoLog(this.program));
		} else {
			for (String src : shader_sources) {
				parseUniforms(src);
			}
			shader_sources.clear();
		}
	}
	
	public int getAttributeLocation(String attr) {
		return Gdx.gl.glGetAttribLocation(program, attr);
	}

	public boolean addUniform(String name) {
		int loc = Gdx.gl.glGetUniformLocation(program, name);
		if (loc != -1) {
			uniforms.put(name, loc);
			return true;
		} else {
			System.err.println("Could not find an uniform named \"" + name + "\" or the uniform isn't used. " + program);
		}
		return false;
	}

	public boolean addUniformArray(String name, int size) {
		boolean ok = true;
		for (int i = 0; i < size; i++) {
			ok = addUniform(name + "[" + i + "]");
		}
		return ok;
	}

	public boolean hasUniform(String name) {
		return uniforms.containsKey(name);
	}

	public int getUniform(String name) {
		if (!hasUniform(name)) {
			return -1;
		}
		return uniforms.get(name);
	}

	public void setUniformFloat(String name, float value) {
		if (hasUniform(name)) {
			Gdx.gl.glUniform1f(getUniform(name), value);
		}
	}

	public void setUniformFloat2(String name, Vec2 value) {
		if (hasUniform(name)) {
			Gdx.gl.glUniform2f(getUniform(name), value.x, value.y);
		}
	}

	public void setUniformFloat3(String name, Vec3 value) {
		if (hasUniform(name)) {
			Gdx.gl.glUniform3f(getUniform(name), value.x, value.y, value.z);
		}
	}

	public void setUniformFloat4(String name, Vec4 value) {
		if (hasUniform(name)) {
			Gdx.gl.glUniform4f(getUniform(name), value.x, value.y, value.z, value.w);
		}
	}

	public void setUniformMatrix4(String name, Mat4 value) {
		if (hasUniform(name)) {
			Gdx.gl.glUniformMatrix4fv(getUniform(name), 1, false, value.getM(), 0);
		}
	}
	
	public void setUniformMatrix4Array(String name, Mat4... values) {
		if (hasUniform(name)) {
			FloatBuffer mats = BufferUtils.createFloatBuffer(values.length * 16);
			for (Mat4 mat : values) {
				mats.put(mat.get());
			}
			mats.flip();
			Gdx.gl.glUniformMatrix4fv(getUniform(name), values.length, false, mats);
		}
	}

	public void setUniformInt1(String name, int value) {
		if (hasUniform(name)) {
			Gdx.gl.glUniform1i(getUniform(name), value);
		}
	}

	public void setSampler(String name, int sampler) {
		if (hasUniform(name)) {
			Gdx.gl.glUniform1i(getUniform(name), sampler);
		}
	}

	public void bind() {
		if (isValid()) {
			Gdx.gl.glUseProgram(program);
			bound = true;
		}
	}

	public void unbind() {
		Gdx.gl.glUseProgram(0);
		bound = false;
	}

	private static int createShader(int type, String src) {
		int shader = Gdx.gl.glCreateShader(type);
		Gdx.gl.glShaderSource(shader, src);
		Gdx.gl.glCompileShader(shader);

		if (GL.getShaderI(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.out.println(Gdx.gl.glGetShaderInfoLog(shader));
			System.err.println("Could not load shader. " + shader);
			Gdx.gl.glDeleteShader(shader);
			return -1;
		}
		return shader;
	}

	public int getProgram() {
		return program;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isBound() {
		return bound;
	}

	@Override
	public void dispose() {
		Gdx.gl.glDeleteProgram(program);
	}

	private void parseUniforms(String src) {
		String curr = "";
		boolean foundUniform = false;
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);
			if (!foundUniform) {
				curr += c;
				if (c == ';') {
					curr = "";
				}
				if (curr.equals("uniform")) {
					foundUniform = true;
					curr = "";
				}
			} else {
				while (c != ';') {
					curr += c;
					c = src.charAt(i);
					i++;
				}
				curr = curr.trim();
				
				String[] raw_name = curr.split(" ");
				String rname = raw_name[1].trim();
				if (rname.contains("[")) { // Is array
					String name = rname.substring(0, rname.indexOf('[')-1);
					String count_str = rname
							.substring(rname.indexOf('[')+1)
							.replace("[", "")
							.replace("]", "");
					int count = -1;
					try {
						count = Integer.parseInt(count_str);
					} catch (NumberFormatException e) {}
					addUniformArray(name, count);
				} else {
					addUniform(rname);
				}
				foundUniform = false;
				curr = "";
			}
		}
	}
	
}
