package at.dalex.grape.toolbox;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class MemoryManager {

	public static List<Integer> createdVAOs = new ArrayList<Integer>();
	public static List<Integer> createdVBOs = new ArrayList<Integer>();
	public static List<Integer> createdTextures = new ArrayList<Integer>();
	
	public static long verticesAmount = 0;
	public static int drawCallsAmount = 0;
	
	public static void cleanUp() {
		
		for (int vaoID : createdVAOs) {
			GL30.glDeleteVertexArrays(vaoID);
		}
		
		for (int vboID : createdVBOs) {
			GL15.glDeleteBuffers(vboID);
		}
		
		for (int textureID : createdTextures) {
			GL11.glDeleteTextures(textureID);
		}
	}
}
