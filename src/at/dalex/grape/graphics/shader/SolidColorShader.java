package at.dalex.grape.graphics.shader;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform4f;

import java.awt.Color;

import at.dalex.grape.graphics.mesh.Model;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import at.dalex.grape.resource.FileContentReader;
import at.dalex.grape.toolbox.MemoryManager;

public class SolidColorShader extends ShaderProgram {

	private int colorLocation;
	private int positionHandle;
	private int matrixHandle;

	public SolidColorShader() {
		super("shaders/SolidColorShader.vsh", "shaders/SolidColorShader.fsh");
	}

	@Override
	public void getAllUniformLocations() {
		colorLocation = getUniformLoader().getUniformLocation("color");
		positionHandle = getUniformLoader().getUniformLocation("vertices");
		matrixHandle = getUniformLoader().getUniformLocation("projectionMatrix");
	}

	@Override
	public void bindAttributes() {

	}

	public void drawMesh(Model model, Matrix4f projectionAndViewMatrix, Color color) {
		start();
		glUniform4f(colorLocation, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

		GL30.glBindVertexArray(model.getVao().getID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(positionHandle);

		//Apply the projection and view transformation
		getUniformLoader().loadMatrix(matrixHandle, projectionAndViewMatrix);
		//Pass in the color
		getUniformLoader().loadVector(colorLocation, new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f));
		//Draw the triangles
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		MemoryManager.verticesAmount += model.getVertexCount();
		MemoryManager.drawCallsAmount++;

		//Disable vertex array
		glDisableVertexAttribArray(positionHandle);
		glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		stop();
	}
}
