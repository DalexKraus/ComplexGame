package at.dalex.grape.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import at.dalex.grape.developer.GameInfo;
import at.dalex.grape.toolbox.MemoryManager;

public class ImageUtils {

	public static Image convertBufferedImage(BufferedImage image) {
		int width, height;
		int textureId;
		width = image.getWidth();
		height = image.getHeight();
		
		int[] rawPixels = image.getRGB(0, 0, width, height, null, 0, width);

		ByteBuffer pixelBuffer = BufferUtils.createByteBuffer(width * height * 4);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = rawPixels[i * width + j];
				pixelBuffer.put((byte) ((pixel >> 16) & 0xFF)); //Red
				pixelBuffer.put((byte) ((pixel >> 8) & 0xFF));  //Green
				pixelBuffer.put((byte) (pixel & 0xFF));         //Blue
				pixelBuffer.put((byte) ((pixel >> 24) & 0xFF)); //Alpha
			}
		}
		pixelBuffer.flip();
		textureId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		MemoryManager.createdTextures.add(textureId);
		
		return new Image(textureId, width, height);
	}
	
	public static Image loadImage(File file) {
		return convertBufferedImage(loadBufferedImage(file.getPath()));
	}
	
	public static BufferedImage loadBufferedImage(String file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(GameInfo.engine_location + "/" + file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
