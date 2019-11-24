package com.complex.entity;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.graphicsutil.Graphics;
import at.dalex.grape.graphics.graphicsutil.Image;
import at.dalex.grape.graphics.graphicsutil.ImageUtils;
import at.dalex.grape.input.Input;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.io.File;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {

    /* Movement */
    private Vector2f velocity = new Vector2f(0, 0);
    private final float VELOCITY_FALLOFF = 1f;
    private final float PLAYER_SPEED = 168;

    private float playerRotation = 0.0f;
    private Image playerImage;
    private final int PLAYER_WIDTH = 128, PLAYER_HEIGHT = 128;

    public Player(double x, double y) {
        super(x, y);
        this.playerImage = ImageUtils.loadImage(new File("textures/entity/player/player.png"));
    }

    @Override
    public void draw(Matrix4f projectionAndViewMatrix) {
        Graphics.enableBlending(true);
        Graphics.drawRotatedImage(playerImage, (int) getX(), (int) getY(),
                PLAYER_WIDTH, PLAYER_HEIGHT, playerRotation, projectionAndViewMatrix);
        Graphics.enableBlending(false);
    }

    @Override
    public void update(double delta) {
        handleInput();

        //Apply velocity to position
        double newX = getX() + velocity.x * delta;
        double newY = getY() + velocity.y * delta;
        setX(newX);
        setY(newY);
    }

    private void handleInput() {
        if (Input.isKeyDown(GLFW_KEY_W))
            velocity.y = -PLAYER_SPEED;
        else if (Input.isKeyDown(GLFW_KEY_S))
            velocity.y = PLAYER_SPEED;
        else if (Input.isKeyDown(GLFW_KEY_A))
            velocity.x = -PLAYER_SPEED;
        else if (Input.isKeyDown(GLFW_KEY_D))
            velocity.x = PLAYER_SPEED;

        //Update player rotation
        float mouseX = Input.getMousePosition().x;
        float mouseY = Input.getMousePosition().y;
        double dX = mouseX - getX();
        double dY = mouseY - getY();
        playerRotation = (float) Math.toDegrees(Math.atan2(dY, dX)) + 90f;
    }

    public Vector2f getVelocity() {
        return this.velocity;
    }
}
