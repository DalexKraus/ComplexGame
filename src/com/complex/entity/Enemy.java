package com.complex.entity;

import at.dalex.grape.Launcher;
import at.dalex.grape.entity.Entity;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Image;
import at.dalex.grape.input.Input;
import com.complex.entity.bullet.Bullet;
import com.complex.entity.bullet.LaserBullet;
import com.complex.weapon.FireMode;
import com.complex.weapon.WeaponCallback;
import com.complex.weapon.WeaponController;
import org.joml.Matrix4f;

public class Enemy extends Entity implements WeaponCallback, Hurtable {

    /* Miscellaneous */
    private Image image;
    private double rotation;
    private final int WIDTH = Player.PLAYER_WIDTH;
    private final int HEIGHT = Player.PLAYER_HEIGHT;

    private final float MOVEMENT_SPEED = 256;

    /* Shooting */
    private WeaponController weaponController;
    private boolean aimingAtPlayer;
    private boolean moveTowardsPlayer;

    /* AI */
    private AIController aiController;

    public Enemy(Image image, double x, double y) {
        super(x, y);
        setBounds(x, y, WIDTH, HEIGHT);
        this.image = image;

        this.aiController = new AIController(this);
        aiController.startThinking();

        this.weaponController = new WeaponController(this);
        weaponController.setBulletsPerSecondBurst(16);
        weaponController.setFireMode(FireMode.BURST_FIRE);
    }

    @Override
    public void update(double delta) {
        if (aimingAtPlayer)
            rotateToPlayer(delta);

        if (moveTowardsPlayer)
            moveTowardsPlayer(delta);

        //Update bounds
        int size = Player.PLAYER_WIDTH;
        setBounds((int) getX() - size / 2f, (int) getY() - size / 2f, size, size);

        weaponController.setShooting(Input.isButtonPressed(1));
        weaponController.update(delta);
    }

    @Override
    public void draw(Matrix4f projectionAndViewMatrix) {
        int size = Player.PLAYER_WIDTH;
        float angleDegrees = (float) Math.toDegrees(rotation);
        Graphics.enableBlending(true);
        Graphics.drawRotatedImage(image, (int) getX() - size / 2, (int) getY() - size / 2, size, size, angleDegrees, projectionAndViewMatrix);
    }

    @Override
    public void fireBullet(WeaponController weaponController) {
        Bullet bullet = new LaserBullet((int) getX(), (int) getY(), rotation, 6144, this);
        Launcher.getInstance().getPlayState().getBulletManager().spawnBullet(bullet);
    }

    @Override
    public void onHit(Bullet bullet, Entity shooter) {
        System.out.println("ENEMEY HIT!");
    }

    /**
     * Let the enemy aim at the player
     * @param shouldTarget Whether or not the player should be targetted
     */
    protected void targetPlayer(boolean shouldTarget) {
        this.aimingAtPlayer = shouldTarget;
    }

    /**
     * Tells the enemy to move towards the player.
     * @param shouldMove Whether or not the enemy should move
     */
    protected void moveTowardsPlayer(boolean shouldMove) {
        this.moveTowardsPlayer = shouldMove;
    }

    /**
     * Rotates the enemy towards the player.
     * To understand the math in here, please take a look at the following screenshot:
     * https://prnt.sc/qv2fni
     */
    private void rotateToPlayer(double delta) {
        double angleToPlayer = getAngleToPlayer() + Math.PI / 2D;
        double gamma = rotation - angleToPlayer;
        double phi = Math.PI * 2 - gamma;

        if (gamma > phi) {
            rotation += phi * delta * 6;
        }
        else {
            rotation -= gamma * delta * 6;
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

    /**
     * Moves the enemy towards the player while maintaining a certain speed
     */
    private void moveTowardsPlayer(double delta) {
        double angleToPlayer = getAngleToPlayer();
        double dX = Math.cos(angleToPlayer) * MOVEMENT_SPEED * delta;
        double dY = Math.sin(angleToPlayer) * MOVEMENT_SPEED * delta;
        setX(getX() + dX);
        setY(getY() + dY);
    }

    /**
     * Returns the angle to the player from the enemy's current position in world space.
     * @return The angle in radians
     */
    protected double getAngleToPlayer() {
        Player playerInstance = Launcher.getInstance().getPlayState().getPlayer();
        double dx = playerInstance.getX() - getX();
        double dy = playerInstance.getY() - getY();
        return Math.atan2(dy, dx);
    }

    public void onDeath() {
        aiController.stopThinking();
    }
}
