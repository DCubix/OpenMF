package com.openmf.logic;

import java.util.ArrayList;
import java.util.Iterator;

import com.openmf.core.IEngineObject;
import com.openmf.core.IReplicable;
import com.openmf.graphics.Renderer2D;
import com.openmf.math.Vec2;
import com.openmf.math.Vec3;

public class Entity implements IEngineObject, IReplicable {

	private Vec3 position;
	private Vec2 scale;
	private float rotation;

	private int flags; // Any value for messaging

	private ArrayList<Component> components;
	
	private Scene scene;

	protected Entity(Scene scene) {
		reset(scene);
	}

	protected void reset(Scene scene) {
		this.position = new Vec3();
		this.scale = new Vec2(1.0f);
		this.rotation = 0.0f;
		this.flags = 0;
		this.components = new ArrayList<>();
		this.scene = scene;
	}
	
	protected void reset(Scene scene, Entity model) {
		this.position = new Vec3(model.position);
		this.scale = new Vec2(model.scale);
		this.rotation = model.rotation;
		this.flags = model.flags;
		this.components = new ArrayList<>();
		for (Iterator<Component> it = model.components.iterator(); it.hasNext();) {
			Component c = it.next();
			addComponent((Component) c.getReplica());
		}
		this.scene = scene;
	}
	
	public void destroy(float timeOut) {
		scene.destroy(this, timeOut);
	}
	
	public void addComponent(Component comp) {
		if (comp != null && !components.contains(comp)) {
			comp.owner = this;
			components.add(comp);
		}
	}

	public <C extends Component> void removeComponent(Class<C> cls) {
		for (Iterator<Component> it = components.iterator(); it.hasNext();) {
			Component c = it.next();
			if (c.getClass().isAssignableFrom(cls)) {
				it.remove();
				break;
			}
		}
	}
	
	public <C extends Component> boolean hasComponent(Class<C> cls) {
		for (Iterator<Component> it = components.iterator(); it.hasNext();) {
			Component c = it.next();
			if (c.getClass().isAssignableFrom(cls)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onStart() {
		for (Iterator<Component> it = components.iterator(); it.hasNext();) {
			Component c = it.next();
			if (c.enabled) {
				c.onStart(scene.getGameEngine());
				c.started = true;
			}
		}
	}

	@Override
	public void onUpdate(float dt) {
		for (Iterator<Component> it = components.iterator(); it.hasNext();) {
			Component c = it.next();
			if (c.enabled) {
				if (!c.started) {
					c.onStart(scene.getGameEngine());
				}
				c.onUpdate(dt);
			}
		}
	}

	@Override
	public void onRender(Renderer2D renderer) {
		for (Iterator<Component> it = components.iterator(); it.hasNext();) {
			Component c = it.next();
			if (c.enabled) {
				c.onRender(renderer);
			}
		}
	}

	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public Vec2 getScale() {
		return scale;
	}

	public void setScale(Vec2 scale) {
		this.scale = scale;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public ArrayList<Component> getComponents() {
		return components;
	}

	public Scene getScene() {
		return scene;
	}

	@Override
	public Object getReplica() {
		Entity e = new Entity(scene);
		e.flags = flags;
		e.position = new Vec3(position);
		e.rotation = rotation;
		e.scale = new Vec2(scale);
		for (Iterator<Component> it = components.iterator(); it.hasNext();) {
			Component c = it.next();
			e.addComponent((Component) c.getReplica());
		}
		return e;
	}

}
