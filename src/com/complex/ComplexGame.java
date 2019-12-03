package com.complex;

import at.dalex.grape.GrapeEngine;
import at.dalex.grape.gamestatemanager.GameState;
import at.dalex.grape.gamestatemanager.IntroState;
import at.dalex.grape.gamestatemanager.PlayState;

import java.util.UUID;

public class ComplexGame extends GrapeEngine {

    private UUID gameState;

    public ComplexGame(String gameLocation) {
        super(gameLocation);
    }

    @Override
    public void onEnable() {
        GameState playState = new PlayState();
        getGameStateManager().addGameState(playState);
        getGameStateManager().setState(playState.getId());
    }

    @Override
    public void onDisable() {

    }
}
