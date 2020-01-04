package at.dalex.grape.graphics;

import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class ParallaxPlane {

    private FrameBufferObject planeFBO;
    private ArrayList<PlaneComponent> components = new ArrayList<>();

    private int planeWidth;
    private int planeHeight;

    private float scrollFactor;

    public ParallaxPlane(int planeWidth, int planeHeight, float scrollFactor) {
        this.planeWidth = planeWidth;
        this.planeHeight = planeHeight;
        this.scrollFactor = scrollFactor;
        this.planeFBO = new FrameBufferObject(planeWidth, planeHeight);
    }

    public void bufferComponents() {
        planeFBO.bindFrameBuffer();
        {
            glClear(GL_COLOR_BUFFER_BIT);
            Graphics.enableBlending(true);
            for (PlaneComponent comp : components) {
                Image tex = comp.texture;
                Graphics.drawImage(tex, comp.x, comp.y, comp.width, comp.height, planeFBO.getProjectionMatrix());
            }
        }
        planeFBO.unbindFrameBuffer();
    }

    public void drawPlane(float scrollPosition, Matrix4f projectionMatrix) {
        float planeYPos = scrollPosition * scrollFactor;

        //Two planes which silde on the screen seamlessly, one covering the gap of the other.
        for (int i = 0; i < 2; i++) {
            int count = (int) (planeYPos / planeHeight);
            float offset = planeYPos - (count * planeHeight);
            int planeY = (int) (offset + i * planeHeight);
            Graphics.drawFrameBufferObject(planeFBO, 0, (int) planeY, planeWidth, planeHeight, projectionMatrix);
        }
    }

    public int getPlaneWidth() {
        return planeWidth;
    }

    public int getPlaneHeight() {
        return planeHeight;
    }

    public ArrayList<PlaneComponent> getComponents() {
        return components;
    }

    public static class PlaneComponent {
        //Component position (in plane space)
        private int x, y, width, height;
        //Comonent texture
        private Image texture;

        public PlaneComponent(int x, int y, int width, int height, Image texture) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.texture = texture;
        }
    }
}
