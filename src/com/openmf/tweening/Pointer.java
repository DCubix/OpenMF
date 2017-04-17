package com.openmf.tweening;

import java.lang.reflect.Field;

public class Pointer<T extends Object> {

	private Object object;
	private String field;
	
	public Pointer(Object object, String field) {
		this.object = object;
		this.field = field;
	}
	
	public T getValue() {
		try {
			Field f = object.getClass().getDeclaredField(field);
			return (T) f.get(object);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setValue(T value) {
		try {
			Field f = object.getClass().getDeclaredField(field);
			f.set(object, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
}
