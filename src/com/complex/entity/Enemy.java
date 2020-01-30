package com.complex.entity;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Image;
import at.dalex.grape.input.Input;
import com.complex.ComplexGame;
import com.complex.entity.bullet.Bullet;
import com.complex.manager.BulletManager;
import org.joml.Matrix4f;

public class Enemy extends Entity {

    private Image image;
    private double rotation;

    public Enemy(Image image, double x, double y) {
        super(x, y);
        this.image = image;
    }

    @Override
    public void update(double delta) {
        aimAtPlayer();

        if (Input.isButtonPressed(1))
            shoot();
    }

    @Override
    public void draw(Matrix4f projectionAndViewMatrix) {
        int size = Player.PLAYER_WIDTH;
        float angleDegrees = (float) Math.toDegrees(rotation);
        Graphics.enableBlending(true);
        Graphics.drawRotatedImage(image, (int) getX(), (int) getY(), size, size, angleDegrees, projectionAndViewMatrix);
    }

    /**
     * Rotates the enemy towards the player.
     * To understand the math in here, please take a look at the following screenshot:
     * https://prnt.sc/qv2fni
     */
    protected void aimAtPlayer() {
        double angleToPlayer = getAngleToPlayer() + Math.PI / 2D;
        double gamma = rotation - angleToPlayer;
        double phi = Math.PI * 2 - gamma;

        if (gamma > phi) {
            rotation += (phi / 8D) * (Math.abs(phi) / Math.PI);
        }
        else {
            rotation -= (gamma / 8D) * (Math.abs(gamma) / Math.PI);
        }

        if (rotation >= 360) {
            int revolutions = (int) (rotation / Math.PI * 2D);
            rotation -= revolutions * Math.PI * 2;
        }

        if (rotation < 0) {
            int revolutions = (int) (rotation / Math.PI * 2D);
            rotation += (1 + revolutions) * Math.PI * 2;
        }
    }

    protected void shoot() {
        float angleDegrees = (float) Math.toDegrees(rotation);
        Bullet spawnedBullet = new Bullet(getX(), getY(), angleDegrees, 4069f);
        ComplexGame.getInstance().getPlayState().getBulletManager().spawnBullet(spawnedBullet);
    }

    /**
     * Returns the angle to the player from the enemy's current position in world space.
     * @return The angle in radians
     */
    protected double getAngleToPlayer() {
        Player playerInstance = ComplexGame.getInstance().getPlayState().getPlayer();
        double dx = playerInstance.getX() - getX();
        double dy = playerInstance.getY() - getY();
        return Math.atan2(dy, dx);
    }
}
