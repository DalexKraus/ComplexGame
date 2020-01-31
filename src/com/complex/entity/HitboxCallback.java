package com.complex.entity;

import at.dalex.grape.entity.Entity;
import com.complex.entity.bullet.Bullet;

public interface HitboxCallback {
    
    void onHit(Bullet bullet, Entity shooter);
}
