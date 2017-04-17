package com.openmf.logic;

import com.openmf.core.GameEngine;
import com.openmf.core.IReplicable;
import com.openmf.graphics.Renderer2D;
import com.openmf.logic.Messenger.Message;

public abstract class Component implements IReplicable {

	protected Entity owner;
	protected boolean enabled = true, started = false;
	
	public abstract void onStart(GameEngine ge);
	public abstract void onUpdate(float dt);
	public abstract void onRender(Renderer2D renderer);
	public void onReceive(Message message) {}
	
	public Entity getOwner() {
		return owner;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public abstract Object getReplica();
	
}
