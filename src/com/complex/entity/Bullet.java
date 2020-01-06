package com.complex.entity;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Image;
import at.dalex.grape.graphics.ImageUtils;
import org.joml.Matrix4f;

import java.io.File;

public class Bullet extends Entity {

    private Image bulletImage;

    private float angle;
    private float speed;

    public Bullet(double x, double y, float angle, float speed) {
        super(x, y);
        this.angle = angle - 90f;
        this.speed = speed;
        this.bulletImage = ImageUtils.loadImage(new File("textures/bullet.png"));
    }

    @Override
    public void update(double delta) {
        double bulletAngle = Math.toRadians(angle);
        float xTranslation = (float) (speed * delta * Math.cos(bulletAngle));
        float yTranslation = (float) (speed * delta * Math.sin(bulletAngle));
        setX(getX() + xTranslation);
        setY(getY() + yTranslation);
    }

    @Override
    public void draw(Matrix4f projectionAndViewMatrix) {
        int xPos = (int) (getX() + Player.PLAYER_WIDTH  / 2);
        int yPos = (int) (getY() + Player.PLAYER_HEIGHT / 2);
        Graphics.enableBlending(true);
        Graphics.drawRotatedImage(bulletImage, xPos, yPos, bulletImage.getWidth() * 2, bulletImage.getHeight() * 2, angle + 90f, projectionAndViewMatrix);
    }
}
