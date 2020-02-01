package com.complex.entity;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.*;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Image;
import at.dalex.grape.input.Input;
import com.complex.entity.bullet.Bullet;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.awt.*;
import java.io.File;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity implements Hurtable {

    /* Movement */
    private Vector2f acceleration = new Vector2f();
    private Vector2f velocity = new Vector2f();
    private final float VELOCITY_FALLOFF = 0.9995f;
    private final float PLAYER_SPEED_CAP = 720;
    private final float PLAYER_ACCELERATION = 1024;

    /* Rotation */
    private double rotationRad;
    private double rotationVelocity;
    private double rotationAcceleration;
    private final double ROTATION_FALLOFF = 2D;
    private final double ROTATION_SPEED_CAP = Math.PI;
    private final double ROTATION_ACCELERATION = Math.PI * 3;

    /* Miscellaneous */
    private Image playerImage;
    public static final int PLAYER_WIDTH = 64, PLAYER_HEIGHT = 64;

    /* Health */
    private int health = 100;

    public Player(double x, double y) {
        super(x, y);
        setBounds(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
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

        /* Update movement stuff */
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

        /* Update rotation stuff */
        if (rotationVelocity < ROTATION_SPEED_CAP) {
            rotationVelocity += rotationAcceleration * delta;
        }
        this.rotationRad += rotationVelocity * delta;

        //Apply rotation velocity falloff
        double rotationVelocityFalloffFactor = 1D - ROTATION_FALLOFF * delta;
        rotationVelocity *= rotationVelocityFalloffFactor;

        //Update bounds
        int size = Player.PLAYER_WIDTH;
        setBounds((int) getX() - size / 2f, (int) getY() - size / 2f, size, size);
    }

    private void handleInput(double delta) {
        boolean movementKeyDown = false;
        boolean rotationKeyDown = false;
        if (Input.isKeyDown(GLFW_KEY_W)) {
            circularAccelerate(rotationRad, -PLAYER_ACCELERATION);
            movementKeyDown = true;
        }
        if (Input.isKeyDown(GLFW_KEY_S)) {
            circularAccelerate(rotationRad, PLAYER_ACCELERATION);
            movementKeyDown = true;
        }
        if (Input.isKeyDown(GLFW_KEY_A)) {
            rotationAcceleration = ROTATION_ACCELERATION;
            rotationKeyDown = true;
        }
        if (Input.isKeyDown(GLFW_KEY_D)) {
            rotationAcceleration = -ROTATION_ACCELERATION;
            rotationKeyDown = true;
        }

        if (!movementKeyDown)
            acceleration.set(0, 0);

        if (!rotationKeyDown)
            rotationAcceleration = 0;
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

    @Override
    public void onHit(Bullet bullet, Entity shooter) {
        if (shooter instanceof Player)
            return;

        System.out.println("player was hit");

        applyDamage(5);
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
