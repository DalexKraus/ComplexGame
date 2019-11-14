package at.dalex.grape.gamestatemanager;

import java.util.HashMap;
import java.util.UUID;

import org.joml.Matrix4f;

/**
 * This class manages all known game states.
 *
 * @author David Kraus
 * @since 1.0
 */
public class GameStateManager {

	private HashMap<UUID, GameState> gameStates = new HashMap<>();
	private UUID currentState;

	/**
	 * Draws the current {@link GameState}
	 * @param projectionAndViewMatrix The matrices with which the state should be drawn
	 */
	public void draw(Matrix4f projectionAndViewMatrix) {
		if (currentState != null) {
			gameStates.get(currentState).draw(projectionAndViewMatrix);
		}
	}

	/**
	 * Updates the {@link GameState}
	 * @param delta The time it took the last frame to render
	 */
	public void update(double delta) {
		if (currentState != null) {
			gameStates.get(currentState).update(delta);
		}
	}

	/**
	 * Registers a previously created {@link GameState},
	 * to easily switch between states.
	 * @param gameState
	 */
	//TODO: Rename method to "registerGameState"
	public void addGameState(GameState gameState) {
		gameStates.put(gameState.getId(), gameState);
	}

	/**
	 * Unergisters a previously registered {@link GameState}.
	 * @param gameState The registered {@link GameState}.
	 */
	//TODO: Rename to "unregisterGameState"
	public void removeGameState(GameState gameState) {
		removeGameState(gameState.getId());
	}

	/**
	 * Unregisters a {@link GameState} using it's id.
	 * @param gameStateId The registered {@link GameState}.
	 */
	//TODO: Rename to "unregisterGameState"
	public void removeGameState(UUID gameStateId) {
		if (gameStates.containsKey(gameStateId)) {
			gameStates.remove(gameStateId);
		}
	}

	/**
	 * Switch to a different {@link GameState}, by using it's id.
	 * @param gameStateId
	 */
	public void setState(UUID gameStateId) {
		if (gameStates.containsKey(gameStateId)) {
			gameStates.get(gameStateId).init();
			currentState = gameStateId;
		}
	}

	/**
	 * @return The id of the current {@link GameState}.
	 */
	public UUID getCurrentState() {
		return this.currentState;
	}

	/**
	 * Generates a new {@link GameState} id.
	 * @return The generated id
	 */
	public UUID genGameStateId() {
		boolean done = false;
		UUID id = null;
		while (!done) {
			id = UUID.randomUUID();
			if (!gameStates.containsKey(id)) done = true;
		}
		return id;
	}
}
