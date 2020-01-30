package com.complex.entity;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.*;
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
    private final float PLAYER_SPEED_CAP = 720;
    private final float PLAYER_ACCELERATION = 1024;

    private double rotationRad;
    private Image playerImage;
    public static final int PLAYER_WIDTH = 64, PLAYER_HEIGHT = 64;

    /* Health */
    private int health = 100;

    public Player(double x, double y) {
        super(x, y);
        this.playerImage = ImageUtils.loadImage(new File("textures/entity/player/player.png"));
    }

    @Override
    public void draw(Matrix4f projectionAndViewMatrix) {
        float angleDegrees = (float) Math.toDegrees(rotationRad);
        Graphics.enableBlending(true);
        Graphics.drawRotatedImage(playerImage, (int) getX() - PLAYER_WIDTH / 2, (int) getY() - PLAYER_HEIGHT / 2,
                PLAYER_WIDTH, PLAYER_HEIGHT, angleDegrees, projectionAndViewMatrix);

        Graphics.enableBlending(false);
    }

    @Override
    public void update(double delta) {
        handleInput(delta);

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

    private void handleInput(double delta) {
        boolean movementKeyDown = false;
        if (Input.isKeyDown(GLFW_KEY_W)) {
            circularAccelerate(rotationRad, -PLAYER_ACCELERATION);
            movementKeyDown = true;
        }
        if (Input.isKeyDown(GLFW_KEY_S)) {
            circularAccelerate(rotationRad, PLAYER_ACCELERATION);
            movementKeyDown = true;
        }
        if (Input.isKeyDown(GLFW_KEY_A)) {
            rotationRad += Math.PI * 2 * delta;
        }
        if (Input.isKeyDown(GLFW_KEY_D)) {
            rotationRad -= Math.PI * delta;
        }

        if (!movementKeyDown)
            acceleration.set(0, 0);

        //Update player rotation
//        Camera camera = GrapeEngine.getEngine().getCamera();
//        float mouseX = Input.getMousePosition().x + camera.getPosition().x;
//        float mouseY = Input.getMousePosition().y + camera.getPosition().y;
//        double dX = mouseX - getX();
//        double dY = mouseY - getY();


//        playerRotation = (float) Math.toDegrees(Math.atan2(dY, dX)) + 90f;
    }

    private void circularAccelerate(double angleRad, double amount) {
        angleRad += Math.PI / 2;
        Vector2f normalizedTranslation = new Vector2f((float) Math.cos(angleRad), (float) Math.sin(angleRad));
        acceleration.set(normalizedTranslation.mul((float) amount));
    }

    public void applyDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            onDeath();
        }
    }

    private void onDeath() {
        System.out.println("Player ded uhh.");
    }

    public Vector2f getVelocity() {
        return this.velocity;
    }

    public double getPlayerRotation() {
        return rotationRad;
    }

    public int getHealth() {
        return health;
    }
}
