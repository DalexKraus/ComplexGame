package at.dalex.grape.gamestatemanager;

import at.dalex.grape.entity.Entity;
import com.complex.entity.Player;
import org.joml.Matrix4f;
import java.util.ArrayList;

public class PlayState extends GameState {

	private Player player;
	private ArrayList<Entity> entities = new ArrayList<>();

	@Override
	public void init() {
		player = new Player(128, 128);
		entities.add(player);
	}

	@Override
	public void draw(Matrix4f projectionAndViewMatrix) {
		entities.forEach(entity -> entity.draw(projectionAndViewMatrix));
	}

	@Override
	public void update(double delta) {
		entities.forEach(entity -> entity.update(delta));
	}
}
