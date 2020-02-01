package com.complex.entity;

import at.dalex.grape.Launcher;

public class AIController implements Runnable {

    private Thread thinkThread;
    private boolean isThinking;

    private Enemy enemyInstance;
    private Player playerInstance;
    private AIState aiState = AIState.WAITING;

    public AIController(Enemy instance) {
        this.enemyInstance = instance;
        this.playerInstance = Launcher.getInstance().getPlayState().getPlayer();

        this.thinkThread = new Thread(this);
        thinkThread.setDaemon(true);
    }

    @Override
    public void run() {
        while (isThinking) {
            try {
                think();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void think() {
        //Check if player is in range
        double distanceToPlayer = getDistanceToPlayer();
        aiState = (distanceToPlayer < 1024) ? aiState = AIState.SEARCHING : AIState.WAITING;

        enemyInstance.targetPlayer(aiState == AIState.SEARCHING);
        enemyInstance.moveTowardsPlayer(aiState == AIState.SEARCHING && distanceToPlayer > 128);
    }

    private double getDistanceToPlayer() {
        double dX = enemyInstance.getX() - playerInstance.getX();
        double dY = enemyInstance.getY() - playerInstance.getY();
        return Math.sqrt(dX * dX + dY * dY);
    }

    public void startThinking() {
        isThinking = true;
        thinkThread.start();
    }

    public void stopThinking() {
        isThinking = false;
    }
}
