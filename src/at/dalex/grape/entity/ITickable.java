package at.dalex.grape.entity;

import org.joml.Matrix4f;

public interface ITickable {

	void update(double delta);
	void draw(Matrix4f projectionAndViewMatrix);
}
