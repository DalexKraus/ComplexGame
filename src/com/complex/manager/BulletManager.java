package com.complex.manager;

import com.complex.entity.bullet.Bullet;
import java.util.ArrayList;

public class BulletManager {

    // 5 sek as max. lifetime
    private final long MAX_LIFETIME = 5 * 1000;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    public void spawnBullet(Bullet bulletInstance) {
        bullets.add(bulletInstance);
    }

    public void validateBullets() {
        ArrayList<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.getLifetime() > MAX_LIFETIME) {
                toRemove.add(bullet);
            }
        }
        bullets.removeAll(toRemove);
    }

    public Iterable<Bullet> getBullets() {
        return bullets;
    }
}
