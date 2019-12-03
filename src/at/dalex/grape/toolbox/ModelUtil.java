package at.dalex.grape.toolbox;

import at.dalex.grape.graphics.mesh.Model;

/**
 * This class is used to provide models for basic shapes.
 * NOTE: This class is most likely going to be deprecated
 * and OR be redundant, since the GraphicsUtil has been updated for
 * using the same model for all rectangles (Just with different transformation).
 */
public class ModelUtil {

	/**
	 * NOTE: THIS METHOD IS DEPRECATED AND DOES NOT WORK WITH
	 * 		 NEWLY DESIGNED GRAPHICSUTIL ANYMORE!
	 *
	 * Returns a new model, with the size of the given parameters.
	 * @param x The position of the model on the x-axis
	 * @param y The position of the model on the y-axis
	 * @param w The width of the model
	 * @param h The height of the model.
	 * @return The generated Model.
	 */
	public Model createRectangle(int x, int y, int w, int h) {
		int sh = 0;
		float[] vertices = new float[] {
				x, (sh - y), 0.0f,
				x, sh - (y + h), 0.0f,

				x + w, sh - (y + h), 0.0f,
				x + w, (sh - y), 0.0f
		};

		int[] indices = new int[] {
				0, 1, 2, 0, 2, 3
		};

		return new Model(vertices, indices, 3);
	}
}
