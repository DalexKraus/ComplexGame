package at.dalex.grape.entity;

import at.dalex.grape.GrapeEngine;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.awt.Rectangle;
import java.util.UUID;

public abstract class Entity implements ITickable {

	protected double x = 0;
	protected double y = 0;
	private Rectangle bounds;
	protected UUID entityId;
	
	public Entity(double x, double y) {
		this.x = x;
		this.y = y;
		this.bounds = new Rectangle((int) x, (int) y, 0, 0);
		this.entityId = EntityManager.genEntityId();
	}
	
	public boolean intersects(Entity ent) {
		return bounds.intersects(ent.getBounds());
	}
	
	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setY(double y) {
		this.y = y;
	}

	public Vector4f getScreenSpacePosition() {
		Vector4f worldPos = new Vector4f((float) x, (float) y, 0f, 1f);
		//Multiply with the projection and view matrix to get the position on screen
		Matrix4f projectionAndViewMatrix = GrapeEngine.getEngine().getCamera().getProjectionAndViewMatrix();
		worldPos.mul(projectionAndViewMatrix);
		return worldPos;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}
	
	public void setBounds(double x, double y, int width, int height) {
		bounds.setBounds((int) x, (int) y, width, height);
	}
	
	public UUID getUniqueId() {
		return this.entityId;
	}
}
