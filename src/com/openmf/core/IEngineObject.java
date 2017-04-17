package com.openmf.core;

import com.openmf.graphics.Renderer2D;

public interface IEngineObject {
	void onStart();
	void onUpdate(float dt);
	void onRender(Renderer2D renderer);
}
