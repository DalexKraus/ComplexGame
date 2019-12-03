package at.dalex.grape.graphics.mesh;

import at.dalex.grape.graphics.TextureAtlas;
import at.dalex.grape.toolbox.Dialog;
import org.joml.Vector2f;

public class TexturedModel {

	private Model baseModel;

	private int textureId;
	private TextureAtlas textureAtlas;
	private Vector2f atlas_texture_offset;
	private int active_atlas_texture;
	private boolean use_textureAtlas = false;

	public TexturedModel(Model baseModel, int textureId) {
		this.baseModel = baseModel;
		this.textureId = textureId;
	}

	public TextureAtlas getTextureAtlas() {
		return this.textureAtlas;
	}

	public void setTextureAtlas(TextureAtlas atlas) {
		this.textureAtlas = atlas;
	}

	public boolean isUsingTextureAtlas() {
		return this.use_textureAtlas;
	}

	public void useTextureAtlas(boolean use) {
		this.use_textureAtlas = use;
	}

	public int getActiveAtlasTextureIndex() {
		return this.active_atlas_texture;
	}

	public void setActiveAtlasTextureIndex(int textureIndex) {
		this.active_atlas_texture = textureIndex;
		//Recalculate uv-offset
		if (isUsingTextureAtlas()) {
			int cellX = active_atlas_texture % textureAtlas.getNumberOfRows();
			int cellY = active_atlas_texture / textureAtlas.getNumberOfRows();
			float normalizedCellSize = 1.0f / textureAtlas.getNumberOfRows();
			atlas_texture_offset = new Vector2f(cellX * normalizedCellSize, cellY * normalizedCellSize);
		} else Dialog.error("Engine Error", "Unable to set Atlas UV-Offset while no Atlas is set!");
	}

	public Vector2f getAtlasTextureUVOffset() {
		return atlas_texture_offset;
	}

	public Model getBaseModel() {
		return this.baseModel;
	}

	public int getTextureId() {
		return this.textureId;
	}
}
