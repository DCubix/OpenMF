package com.openmf.logic;

import java.util.ArrayList;
import java.util.Iterator;

import com.openmf.core.GameEngine;
import com.openmf.core.IEngineObject;
import com.openmf.graphics.Color;
import com.openmf.graphics.Renderer2D;

public class Scene implements IEngineObject {

	private GameEngine gameEngine;
	private ArrayList<Entity> entities, inactive;
	private ArrayList<TimedDestructor> toDestroy;
	
	private Color backColor = new Color(0.0f);
	
	private Messenger messenger;
	
	public Scene(GameEngine engine) {
		this.entities = new ArrayList<>();
		this.inactive = new ArrayList<>();
		this.toDestroy = new ArrayList<>();
		this.gameEngine = engine;
		this.messenger = new Messenger(this);
	}
	
	public Entity create() {
		Entity e = null;
		if (inactive.isEmpty()) {
			e = new Entity(this);
		} else {
			e = inactive.get(0);
			e.reset(this);
			inactive.remove(0);
		}
		if (e != null) {
			entities.add(e);
		}
		return e;
	}
	
	public Entity spawn(Entity model) {
		Entity e = null;
		if (inactive.isEmpty()) {
			e = (Entity) model.getReplica();
		} else {
			e = inactive.get(0);
			e.reset(this, model);
			inactive.remove(0);
		}
		if (e != null) {
			entities.add(e);
		}
		return e;
	}
	
	public void destroy(Entity entity, float timeOut) {
		for (Iterator<TimedDestructor> it = toDestroy.iterator(); it.hasNext();) {
			TimedDestructor d = it.next();
			if (d.entity == entity) {
				return;
			}
		}
		if (entity.getScene() != this) {
			return;
		}
		
		toDestroy.add(new TimedDestructor(entity, timeOut));
	}
	
	@Override
	public void onStart() {
		for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity e = it.next();
			e.onStart();
		}
	}

	@Override
	public void onUpdate(float dt) {
		messenger.processQueue(dt);
		
		for (Iterator<TimedDestructor> it = toDestroy.iterator(); it.hasNext();) {
			TimedDestructor d = it.next();
			if (d.time > 0) {
				d.time -= dt;
			} else {
				inactive.add(d.entity);
				entities.remove(d.entity);
				it.remove();
			}
		}
		
		for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity e = it.next();
			e.onUpdate(dt);
		}
	}

	@Override
	public void onRender(Renderer2D renderer) {
		for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity e = it.next();
			e.onRender(renderer);
		}
	}

	public <T extends Component> Entity getEntity(Class<T> cls) {
		for (Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity e = it.next();
			for (Iterator<Component> cit = e.getComponents().iterator(); cit.hasNext();) {
				Component c = cit.next();
				if (c.getClass().isAssignableFrom(cls)) {
					return e;
				}
			}
		}
		return null;
	}
	
	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public Color getBackColor() {
		return backColor;
	}
	
	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}

	public Messenger getMessenger() {
		return messenger;
	}
	
}
