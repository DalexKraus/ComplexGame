package at.dalex.grape.graphics.shader;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import at.dalex.grape.graphics.mesh.TexturedModel;
import at.dalex.grape.resource.FileContentReader;
import at.dalex.grape.toolbox.MemoryManager;

public class ImageShader extends ShaderProgram {

	private int position_projectionMatrix;
	private int position_use_atlas;
	private int position_atlas_offset;
	private int position_atlas_rows;
	
	public ImageShader() {
		super(FileContentReader.readFile("/shaders/ImageShader.vsh"), FileContentReader.readFile("/shaders/ImageShader.fsh"));
	}
	
	@Override
	public void getAllUniformLocations() {
		position_projectionMatrix 	= getUniformLoader().getUniformLocation("projectionMatrix");
		position_atlas_offset 		= getUniformLoader().getUniformLocation("atlas_offset");
		position_atlas_rows 		= getUniformLoader().getUniformLocation("atlas_rows");
		position_use_atlas			= getUniformLoader().getUniformLocation("use_atlas");
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
	}

	public void drawMesh(TexturedModel model, Matrix4f projectionAndViewMatrix) {
		start();
		model.getBaseModel().getVao().bindVAO();
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		getUniformLoader().loadMatrix(position_projectionMatrix, projectionAndViewMatrix);
		getUniformLoader().loadBoolean(position_use_atlas, model.isUsingTextureAtlas());

		if (model.isUsingTextureAtlas()) {
			getUniformLoader().loadFloat(position_atlas_rows,  model.getTextureAtlas().getNumberOfRows());
			getUniformLoader().load2DVector(position_atlas_offset, model.getAtlasTextureUVOffset());
		}


		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.isUsingTextureAtlas() ? model.getTextureAtlas().getTextureId() : model.getTextureId());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getBaseModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		MemoryManager.verticesAmount += model.getBaseModel().getVertexCount();
		MemoryManager.drawCallsAmount++;
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		model.getBaseModel().getVao().unbindVAO();
		stop();
	}
}
