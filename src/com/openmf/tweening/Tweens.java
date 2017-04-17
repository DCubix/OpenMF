package com.openmf.tweening;

import java.util.ArrayList;
import java.util.Iterator;

public class Tweens {

	public static ArrayList<Tween> tweens = new ArrayList<>();
	
	public static void addTween(Tween tween) {
		tweens.add(tween);
	}
	
	public static void update(float dt) {
		for (Iterator<Tween> it = tweens.iterator(); it.hasNext();) {
			Tween tween = it.next();
			tween.update(dt);
			if (tween.finished) {
				it.remove();
			}
		}
	}
	
}
