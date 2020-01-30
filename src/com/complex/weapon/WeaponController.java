package com.complex.weapon;

public class WeaponController {

    private WeaponCallback weaponCallback;
    private double bulletWaitTime;
    private int bulletsPerSecond;
    private int bulletsPerSecondBurst;

    private double lastBulletTime;
    private double lastBurstTime;

    private FireMode fireMode = FireMode.FULL_AUTO;
    private double burstFireDuration = 0.3;
    private double burstFireWaitTime = 1;

    private boolean isShooting;
    private boolean isBurstFiring;

    public WeaponController(WeaponCallback weaponCallback) {
        this.weaponCallback = weaponCallback;
    }

    public void update(double delta) {
        lastBulletTime += delta;
        lastBurstTime  += delta;

        if (isBurstFiring) {
            tryFireFullAuto();

            if (lastBurstTime > burstFireDuration) {
                isBurstFiring = false;
            }
            return;
        }

        if (!isShooting)
            return;

        //Try to shoot
        if (fireMode == FireMode.FULL_AUTO) {
            tryFireFullAuto();
        }

        if (fireMode == FireMode.BURST_FIRE) {
            if (lastBurstTime > burstFireWaitTime) {
                lastBurstTime = 0;
                isBurstFiring = true;
            }
        }
    }

    private void tryFireFullAuto() {
        if (lastBulletTime > bulletWaitTime) {
            lastBulletTime = 0;
            weaponCallback.fireBullet(this);
        }
    }

    private void calculateBulletsPerSecond() {
        this.bulletWaitTime = 1D / (fireMode == FireMode.FULL_AUTO ? bulletsPerSecond : bulletsPerSecondBurst);
    }

    public void setShooting(boolean shoot) {
        this.isShooting = shoot;
    }

    public int getBulletsPerSecond() {
        return bulletsPerSecond;
    }

    public void setBulletsPerSecond(int bulletsPerSecond) {
        this.bulletsPerSecond = bulletsPerSecond;
        calculateBulletsPerSecond();
    }

    public FireMode getFireMode() {
        return fireMode;
    }

    public void setFireMode(FireMode fireMode) {
        this.fireMode = fireMode;
        calculateBulletsPerSecond();
    }

    public int getBulletsPerSecondBurst() {
        return bulletsPerSecondBurst;
    }

    public void setBulletsPerSecondBurst(int bulletsPerSecondBurst) {
        this.bulletsPerSecondBurst = bulletsPerSecondBurst;
        calculateBulletsPerSecond();
    }

    public double getBurstFireWaitTime() {
        return burstFireWaitTime;
    }

    public void setBurstFireWaitTime(double burstFireWaitTime) {
        this.burstFireWaitTime = burstFireWaitTime;
    }

    public double getBurstFireDuration() {
        return burstFireDuration;
    }

    public void setBurstFireDuration(double burstFireDuration) {
        this.burstFireDuration = burstFireDuration;
    }

}
