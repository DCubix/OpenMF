package com.openmf.graphics;

import com.openmf.math.Vec2;

public class ParticleConfiguration {

	protected Vec2 positionVariation = new Vec2();
	protected Vec2 gravity = new Vec2();

	protected float speed = 0, speedVariation = 0;
	protected float angle = 0, angleVariation = 0;
	protected float angularSpeed = 0, angularSpeedVariation = 0;
	protected float life = 1, lifeVariation = 0;
	protected float startScale = 1, startScaleVariation = 0;
	protected float endScale = 0, endScaleVariation = 0;
	protected Color startColor = new Color(1.0f), startColorVariation = new Color(1.0f);
	protected Color endColor = new Color(1.0f), endColorVariation = new Color(1.0f);
	protected BlendMode blendMode = BlendMode.NORMAL;

	public Vec2 getPositionVariation() {
		return positionVariation;
	}

	public void setPositionVariation(Vec2 positionVariation) {
		this.positionVariation = positionVariation;
	}

	public Vec2 getGravity() {
		return gravity;
	}

	public void setGravity(Vec2 gravity) {
		this.gravity = gravity;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeedVariation() {
		return speedVariation;
	}

	public void setSpeedVariation(float speedVariation) {
		this.speedVariation = speedVariation;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngleVariation() {
		return angleVariation;
	}

	public void setAngleVariation(float angleVariation) {
		this.angleVariation = angleVariation;
	}

	public float getAngularSpeed() {
		return angularSpeed;
	}

	public void setAngularSpeed(float angularSpeed) {
		this.angularSpeed = angularSpeed;
	}

	public float getAngularSpeedVariation() {
		return angularSpeedVariation;
	}

	public void setAngularSpeedVariation(float angularSpeedVariation) {
		this.angularSpeedVariation = angularSpeedVariation;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public float getLifeVariation() {
		return lifeVariation;
	}

	public void setLifeVariation(float lifeVariation) {
		this.lifeVariation = lifeVariation;
	}

	public float getStartScale() {
		return startScale;
	}

	public void setStartScale(float startScale) {
		this.startScale = startScale;
	}

	public float getStartScaleVariation() {
		return startScaleVariation;
	}

	public void setStartScaleVariation(float startScaleVariation) {
		this.startScaleVariation = startScaleVariation;
	}

	public float getEndScale() {
		return endScale;
	}

	public void setEndScale(float endScale) {
		this.endScale = endScale;
	}

	public float getEndScaleVariation() {
		return endScaleVariation;
	}

	public void setEndScaleVariation(float endScaleVariation) {
		this.endScaleVariation = endScaleVariation;
	}

	public Color getStartColor() {
		return startColor;
	}

	public void setStartColor(Color startColor) {
		this.startColor = startColor;
	}

	public Color getStartColorVariation() {
		return startColorVariation;
	}

	public void setStartColorVariation(Color startColorVariation) {
		this.startColorVariation = startColorVariation;
	}

	public Color getEndColor() {
		return endColor;
	}

	public void setEndColor(Color endColor) {
		this.endColor = endColor;
	}

	public Color getEndColorVariation() {
		return endColorVariation;
	}

	public void setEndColorVariation(Color endColorVariation) {
		this.endColorVariation = endColorVariation;
	}

	public BlendMode getBlendMode() {
		return blendMode;
	}

	public void setBlendMode(BlendMode blendMode) {
		this.blendMode = blendMode;
	}

}
