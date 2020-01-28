package com.complex;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.gamestatemanager.PlayState;

import java.util.UUID;

public class ComplexGame extends GrapeEngine {

    private static ComplexGame instance;
    private UUID gameState;
    private PlayState playState;

    public ComplexGame(String gameLocation) {
        super(gameLocation);
    }

    @Override
    public void onEnable() {
        instance = this;
        this.playState = new PlayState();
        getGameStateManager().addGameState(playState);
        getGameStateManager().setState(playState.getId());
    }

    @Override
    public void onDisable() {

    }

    public PlayState getPlayState() {
        return playState;
    }

    public static ComplexGame getInstance() {
        return instance;
    }
}
