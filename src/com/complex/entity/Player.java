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
    private Vector2f acceleration = new Vector2f();
    private Vector2f velocity = new Vector2f();
    private final float VELOCITY_FALLOFF = 0.9995f;
    private final float PLAYER_SPEED_CAP = 256;
    private final float PLAYER_ACCELERATION = 1024;

    private float playerRotation = 0.0f;
    private Image playerImage;
    private final int PLAYER_WIDTH = 32, PLAYER_HEIGHT = 32;

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

        //Apply acceleration
        if (velocity.length() < PLAYER_SPEED_CAP) {
            velocity.x += (float) (acceleration.x * delta);
            velocity.y += (float) (acceleration.y * delta);
        }

        //Apply velocity to position
        double newX = getX() + velocity.x * delta;
        double newY = getY() + velocity.y * delta;
        setX(newX);
        setY(newY);

        //Apply velocity falloff
        float velocityFalloffFactor = (float) (1f - VELOCITY_FALLOFF * delta);
        velocity.mul(velocityFalloffFactor);
    }

    private void handleInput() {
        boolean movementKeyDown = false;
        if (Input.isKeyDown(GLFW_KEY_W)) {
            acceleration.y = -PLAYER_ACCELERATION;
            movementKeyDown = true;
        }
        if (Input.isKeyDown(GLFW_KEY_S)) {
            acceleration.y = PLAYER_ACCELERATION;
            movementKeyDown = true;
        }
        if (Input.isKeyDown(GLFW_KEY_A)) {
            acceleration.x = -PLAYER_ACCELERATION;
            movementKeyDown = true;
        }
        if (Input.isKeyDown(GLFW_KEY_D)) {
            acceleration.x = PLAYER_ACCELERATION;
            movementKeyDown = true;
        }

        if (!movementKeyDown)
            acceleration.set(0, 0);

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
