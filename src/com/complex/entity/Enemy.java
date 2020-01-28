package com.complex.entity;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Image;
import com.complex.ComplexGame;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public abstract class Enemy extends Entity {

    private final double ACTIVATION_DISTANCE = 512;
    private final Vector2f VEC_RIGHT = new Vector2f(1, 0);

    private AIState currentState = AIState.INACTIVE;
    private Player playerInstance;
    private Image image;
    private double angleRad;


    public Enemy(Image image, double x, double y) {
        super(x, y);
        this.image = image;
        this.playerInstance = ComplexGame.getInstance().getPlayState().getPlayer();
    }

    @Override
    public void update(double delta) {
        think();

        //Rotate to player's direction if searching
        if (currentState == AIState.SEARCHING) {
            //Translate angle each tick in direction to the player
            double angleToPlayer = getAngleToPlayer();
            double deltaAngle = angleToPlayer - angleRad;
            deltaAngle *= 0.005f;
            angleRad += deltaAngle;
        }
    }

    @Override
    public void draw(Matrix4f projectionAndViewMatrix) {
        float angleDegrees = (float) Math.toDegrees(angleRad);
        Graphics.enableBlending(true);
        Graphics.drawRotatedImage(image, (int) getX(), (int) getY(), 64, 64, angleDegrees, projectionAndViewMatrix);
    }

    private void think() {
        if (currentState == AIState.INACTIVE && getDistanceToPlayer() < ACTIVATION_DISTANCE) {
            currentState =  AIState.SEARCHING;
        }
    }

    private double getDistanceToPlayer() {
        double x2 = playerInstance.getX() * playerInstance.getX();
        double y2 = playerInstance.getY() * playerInstance.getY();
        return Math.sqrt(x2 + y2);
    }

    private double getAngleToPlayer() {
        Vector2f playerPos = new Vector2f((float) playerInstance.getX(), (float) playerInstance.getY());
        Vector2f enemyPos  = new Vector2f((float) getX(), (float) getY());
        Vector2f toPlayer = playerPos.sub(enemyPos);
        return toPlayer.angle(VEC_RIGHT);
    }
}
