package at.dalex.grape.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import at.dalex.grape.toolbox.MemoryManager;

/**
 * This class is used by OpenGL to store data.
 */
public class VertexArrayObject {

	private int vaoID;

	/**
	 * Creates a new {@link VertexArrayObject}.
	 */
	public VertexArrayObject() {
		vaoID = GL30.glGenVertexArrays();
		MemoryManager.createdVAOs.add(vaoID);
	}
	
	/**
	 * Store an array of float values in a new attribute slot of this {@link VertexArrayObject}.
	 * @param attributeNumber Attribute slot to store data in
	 * @param data The data you want to store
	 *
	 * @return The ID of the generated VertexBufferObject.
	 */
	public int storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data, boolean dynamic) {
		return storeDataInAttributeList(attributeNumber, coordinateSize, data, 0, dynamic);
	}

	/**
	 * Store an array of float values in a new attribute slot of this {@link VertexArrayObject} with a specific stride.
	 * @param attributeNumber Attribute slot to store data in
	 * @param data The data you want to store
	 * @param stride The stride of the data
	 *
	 * @return The ID of the generated VertexBufferObject.
	 */
	public int storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data, int stride, boolean dynamic) {
		bindVAO();

		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		MemoryManager.createdVBOs.add(vboID);

		FloatBuffer buffer = storeDataInNewFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, dynamic ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW);

		//Store data
		GL20.glEnableVertexAttribArray(attributeNumber);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, stride, 0);
		GL20.glDisableVertexAttribArray(attributeNumber);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		unbindVAO();

		return vboID;
	}
	
	/**
	 * Convert an float array of data into a new {@link FloatBuffer}.
	 * 
	 * @param data The data which you want to be stored
	 * @return Generated {@link FloatBuffer} with stored data inside
	 */
	public FloatBuffer storeDataInNewFloatBuffer(float[] data) {
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
		floatBuffer.put(data);
		floatBuffer.flip();
		return floatBuffer;
	}
	
	/**
	 * Convert an Integer-array of data into a new {@link IntBuffer}.
	 * 
	 * @param data The data which you want to be stored
	 * @return Generated {@link IntBuffer} with stored data inside
	 */
	private IntBuffer storeDataInNewIntBuffer(int[] data) {
		IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
		intBuffer.put(data);
		intBuffer.flip();
		return intBuffer;
	}
	
	public void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		MemoryManager.createdVBOs.add(vboID);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		
		IntBuffer buffer = storeDataInNewIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Bind this {@link VertexArrayObject}
	 */
	public void bindVAO() {
		GL30.glBindVertexArray(vaoID);
	}
	
	/**
	 * Unbind this {@link VertexArrayObject}
	 */
	public void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Get the ID of this {@link VertexArrayObject}.
	 * @return ID of this {@link VertexArrayObject} 
	 */
	public int getID() {
		return this.vaoID;
	}
}
