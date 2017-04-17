package com.openmf.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class Messenger {

	public class Message {
		public int from, to; // Entity flags
		public Object userData;
		public int flags;
		public float time;
	}
	
	private ArrayList<Message> messageQueue;
	private Scene scene;
	
	public Messenger(Scene scene) {
		this.messageQueue = new ArrayList<>();
		this.scene = scene;
	}
	
	public void processQueue(float dt) {
		for (Iterator<Message> it = messageQueue.iterator(); it.hasNext();) {
			Message msg = it.next();
			if (msg.time > 0) {
				msg.time -= dt;
			} else {
				for (Iterator<Entity> eit = scene.getEntities().iterator(); eit.hasNext();) {
					Entity e = eit.next();
					if (msg.to != 0) {
						if ((msg.to & e.getFlags()) == msg.to) {
							for (Iterator<Component> cit = e.getComponents().iterator(); cit.hasNext();) {
								Component c = cit.next();
								if (c.enabled) {
									c.onReceive(msg);
								}
							}
							break;
						}
					} else {
						for (Iterator<Component> cit = e.getComponents().iterator(); cit.hasNext();) {
							Component c = cit.next();
							if (c.enabled) {
								c.onReceive(msg);
							}
						}
					}
				}
				it.remove();
			}
		}
	}

	public void sendMessage(int from, int flags) {
		sendMessage(from, 0, flags, null, 0);
	}
	
	public void sendMessage(int from, int flags, float delay) {
		sendMessage(from, 0, flags, null, delay);
	}
	
	public void sendMessage(int from, int flags, Object userData, float delay) {
		sendMessage(from, 0, flags, userData, delay);
	}
	
	public void sendMessage(int from, int to, int flags, Object userData, float delay) {
		Message msg = new Message();
		msg.flags = flags;
		msg.from = from;
		msg.time = delay;
		msg.to = to;
		msg.userData = userData;
		messageQueue.add(msg);
	}
	
}
