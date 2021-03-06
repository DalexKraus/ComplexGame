package com.complex.entity.bullet;

import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Image;
import at.dalex.grape.graphics.ImageUtils;
import com.complex.entity.Player;
import org.joml.Matrix4f;

import java.io.File;

public class Bullet extends Entity {

    private Image bulletImage;

    private double angleRad;
    private float speed;

    //Bullet spawn time in current milliseconds
    private Entity shooter;
    private long spawnTime;

    public Bullet(double x, double y, double angleRad, float speed, Entity shooter) {
        super(x, y);
        this.angleRad = angleRad - Math.PI / 2;
        this.speed = speed;
        this.bulletImage = ImageUtils.loadImage(new File("textures/bullet.png"));
        this.spawnTime = System.currentTimeMillis();
        this.shooter = shooter;
        setBounds(x, y, bulletImage.getWidth(), bulletImage.getHeight());
    }

    @Override
    public void draw(Matrix4f projectionAndViewMatrix) {
        Graphics.enableBlending(true);

        double halfPlayerW = Player.PLAYER_WIDTH  / 2D;
        double halfPlayerH = Player.PLAYER_HEIGHT / 2D;
        double xPos = Math.cos(angleRad) * halfPlayerW;
        double yPos = Math.sin(angleRad) * halfPlayerH - 10;

        Graphics.drawRotatedImage(bulletImage,
                (int) (getX() + xPos),
                (int) (getY() + yPos),
                bulletImage.getWidth(), bulletImage.getHeight(), (float) Math.toDegrees(angleRad) + 90,
                projectionAndViewMatrix);
    }

    @Override
    public void update(double delta) {
        float xTranslation = (float) (speed * delta * Math.cos(angleRad));
        float yTranslation = (float) (speed * delta * Math.sin(angleRad));
        setX(getX() + xTranslation);
        setY(getY() + yTranslation);
    }

    public long getLifetime() {
        return System.currentTimeMillis() - spawnTime;
    }

    public Entity getShooter() {
        return shooter;
    }
}
