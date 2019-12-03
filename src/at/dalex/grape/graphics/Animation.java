package at.dalex.grape.graphics;

import java.awt.image.BufferedImage;

/**
 * This class takes in an image (atlas) an converts it into an animation cycle.
 *
 * Calling <code>getImage()</code> will give you the appropriate
 * image in the animation cycle.
 *
 * //TODO: Update rendering system to only use a single texture internally. (Not hundreds)
 *
 * @since 1.0
 * @author dalex (David Kraus)
 */
public class Animation {

    private Image[] frames;             //Array of images
    private double currentFrame = 0;    //Current frame index
    private int numFrames;              //Count of total frames
    private int framesPerSecond = 2;    //Amount of frames per second.

    /**
     * Private constructor to avoid instantiation
     */
    private Animation() { }

    /**
     * Sets the images of the animation
     * @param frames The images of the animation
     */
    public void setFrames(Image[] frames) {
        this.frames = frames;
        numFrames = frames.length;
    }

    /**
     * Updates the animation respecting the delta time for a consistent animation cycle.
     * @param delta The time it took the last frame to render.
     */
    public void update(double delta) {
        currentFrame += (framesPerSecond * delta);
        if ((int) currentFrame >= numFrames) currentFrame = 0;
    }

    /**
     * @return The amount of frames per second
     */
    public int getFramesPerSecond() {
        return this.framesPerSecond;
    }

    /**
     * Sets the amount of frames per second
     * @param framesPerSecond The amount of frames the animation should have per second.
     */
    public void setFramesPerSecond(int framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    /**
     * @return The index of the current frame
     */
    public int getCurrentFrame() {
        return (int) currentFrame;
    }

    /**
     * Sets a new frame as current in the animation cycle
     * @param index Index of the frame in the atlas
     */
    public void setCurrentFrame(int index) {
        currentFrame = index;
    }

    /**
     * @return The number of frames this animation has
     */
    public int getFrameCount() {
        return frames.length;
    }

    /**
     * @return The Image of the current frame in the animation cycle
     */
    public Image getImage() {
        return frames[(int) currentFrame];
    }

    /**
     * Loads a new Animation from disk using an animation atlas.
     *
     * @param animationAtlas The image where every frame is stored
     * @param frameWidth The width of one frame
     * @param frameHeight The height of one frame
     * @param framesPerSecond The amount of frames the animation should have per second.
     * @return The created {@link Animation}
     */
    public static Animation loadAnimation(BufferedImage animationAtlas, int frameWidth, int frameHeight, int framesPerSecond) {
        Animation animation = new Animation();
        Image[] frames = new Image[animationAtlas.getWidth() / frameWidth];

        for (int index = 0; index < (animationAtlas.getWidth() / frameWidth); index++) {
            frames[index] = ImageUtils.convertBufferedImage(animationAtlas.getSubimage(index * frameWidth, 0, frameWidth, frameHeight));
        }

        animation.setFrames(frames);
        animation.setFramesPerSecond(framesPerSecond);

        return animation;
    }
}