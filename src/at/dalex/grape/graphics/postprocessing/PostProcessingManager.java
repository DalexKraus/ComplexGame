package at.dalex.grape.graphics.postprocessing;

import at.dalex.grape.graphics.FrameBufferObject;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.shader.ShaderProgram;

public class PostProcessingManager {

    public static FrameBufferObject applyProcessor(FrameBufferObject frameBufferObject, PostProcessor processor) {
        int width = frameBufferObject.getWidth();
        int height = frameBufferObject.getHeight();
        ShaderProgram processorShader = processor.getShaderProgram();

        FrameBufferObject processingFBO = new FrameBufferObject(width, height);
        processingFBO.bindFrameBuffer();
        processorShader.start();

        //Draw the previous image
        //The processor's shader will then apply effects while the image is being drawn
        Graphics.drawFrameBufferObject(frameBufferObject, 0, 0, width, height, frameBufferObject.getProjectionMatrix());

        processorShader.stop();
        processingFBO.unbindFrameBuffer();
        return processingFBO;
    }
}
