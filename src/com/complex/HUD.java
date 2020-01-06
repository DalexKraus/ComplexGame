package com.complex;

import at.dalex.grape.entity.ITickable;
import at.dalex.grape.graphics.DisplayManager;
import at.dalex.grape.graphics.Graphics;
import at.dalex.grape.graphics.Image;
import at.dalex.grape.graphics.ImageUtils;
import com.complex.entity.Player;
import org.joml.Matrix4f;

import java.io.File;

public class HUD implements ITickable {

    private Player playerInstance;
    private Image healthText;
    private Image healthbar_green;

    public HUD(Player playerInstance) {
        this.playerInstance = playerInstance;
        this.healthText = ImageUtils.loadImage(new File("textures/hud/health.png"));
        this.healthbar_green = ImageUtils.loadImage(new File("textures/hud/healthbar_green.png"));
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void draw(Matrix4f projectionAndViewMatrix) {
        Graphics.enableBlending(true);
        int healthbar_xOffset = 160;
        int healthbar_yOffset = DisplayManager.windowHeight - 64;
        int size = 32;

        int healthTextY = healthbar_yOffset - (healthText.getHeight() / 4);
        Graphics.drawImage(healthText, 32, healthTextY, healthText.getWidth(), healthText.getHeight(), projectionAndViewMatrix);

        int numBars = playerInstance.getHealth() / 5;
        for (int i = 0; i < numBars; i++) {
            int xOffset = i * (size / 4);
            Graphics.drawImage(healthbar_green, healthbar_xOffset + xOffset, healthbar_yOffset, size, size, projectionAndViewMatrix);
        }
        Graphics.enableBlending(false);
    }
}
