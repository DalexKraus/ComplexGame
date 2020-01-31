package com.complex.manager;

import at.dalex.grape.Launcher;
import at.dalex.grape.entity.Entity;
import at.dalex.grape.gamestatemanager.PlayState;
import com.complex.entity.Hurtable;
import com.complex.entity.bullet.Bullet;
import java.util.ArrayList;
import java.util.Iterator;

public class BulletManager {

    // 5 sek as max. lifetime
    private final long MAX_LIFETIME = 5 * 1000;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    public void spawnBullet(Bullet bulletInstance) {
        bullets.add(bulletInstance);
    }

    public void validateBullets() {
        PlayState playState = Launcher.getInstance().getPlayState();
        ArrayList<Bullet> toRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            if (bullet.getLifetime() > MAX_LIFETIME) {
                toRemove.add(bullet);
            }
            else {
                for (Iterator<Entity> it = playState.getEntities(); it.hasNext();) {
                    Entity entity = it.next();

                    //Only players and other enemies can be hurt.
                    if (!(entity instanceof Hurtable) || entity instanceof Bullet)
                        continue;

                    if (bullet.intersects(entity) && !entity.equals(bullet.getShooter())) {
                        ((Hurtable) entity).onHit(bullet, bullet.getShooter());
                        toRemove.add(bullet);
                        break;
                    }
                }
            }
        }
        bullets.removeAll(toRemove);
    }

    public Iterable<Bullet> getBullets() {
        return bullets;
    }
}
