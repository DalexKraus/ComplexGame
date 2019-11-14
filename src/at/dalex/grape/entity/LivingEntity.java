package at.dalex.grape.entity;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import at.dalex.grape.graphics.Animation;
import at.dalex.grape.graphics.graphicsutil.ImageUtils;

public abstract class LivingEntity extends Entity {

	private int type;
	private int health;
	protected MoveDirection facingDirection = MoveDirection.DOWN;

	private boolean hasAnimation = false;
	private HashMap<String, Animation> animations = new HashMap<>();
	private String currentAnimation;

	public LivingEntity(double x, double y, int type, int health) {
		super(x, y);
		this.type = type;
		this.health = health;
	}

	@Override
	public void update(double delta) {
		if (hasAnimation) {
			animations.get(currentAnimation).update(delta);
		}
	}

	public boolean hasAnimation() {
		return hasAnimation;
	}

	public void setAnimation(String animation_key) {
		if (animations.containsKey(animation_key)) {
			this.currentAnimation = animation_key;
			hasAnimation = true;
		}
	}

	public void loadAnimation(String animation_key, String animationFile, int frameWidth, int frameHeight, int delay) {
		BufferedImage animationAtlas = ImageUtils.loadBufferedImage(animationFile);
		if (animationAtlas != null) {
			animations.put(animation_key, Animation.loadAnimation(animationAtlas, frameWidth, frameHeight, delay));
			hasAnimation = true;
		}
	}

	public Animation getCurrentAnimation() {
		return this.animations.get(currentAnimation);
	}

	public void damage(int damage) {
		health -= damage;
		if (health < 0) health = 0;
	}

	public int getHealth() {
		return this.health;
	}

	public int getType() {
		return this.type;
	}

	public MoveDirection getMoveDirection() {
		return this.facingDirection;
	}

}
