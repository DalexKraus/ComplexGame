package at.dalex.grape.gamestatemanager;

import java.util.UUID;
import org.joml.Matrix4f;
import at.dalex.grape.GrapeEngine;

/**
 * This class is the base of every game state.
 *
 * A game state can be defined as a specific state of the game,
 * e.g. main menu, intro, credits, play or death state.
 *
 * @author David Kraus
 * @since 1.0
 */
public abstract class GameState {

	private UUID gameStateId;

	/**
	 * Creates a new GameState
	 */
	public GameState() {
		gameStateId = GrapeEngine.getEngine().getGameStateManager().genGameStateId();
	}

	/**
	 * Initializes the GameState
	 */
	public abstract void init();

	/**
	 * Draws the GameState
	 * @param projectionAndViewMatrix The matrices with which the state should be drawn
	 */
	public abstract void draw(Matrix4f projectionAndViewMatrix);

	/**
	 * Updates the game state
	 * @param delta The time it took the last frame to render
	 */
	public abstract void update(double delta);

	/**
	 * @return The unique id of this game state.
	 */
	public UUID getId() {
		return this.gameStateId;
	}
}
