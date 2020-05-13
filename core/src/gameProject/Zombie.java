package gameProject;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Zombie extends BaseActor {

	private Animation<TextureRegion> walk;
	private Animation<TextureRegion> attack;


	protected boolean move;
	protected float speed;
	private int direction;
	protected float life;

	public Zombie(float x, float y, Stage s) {
		super(x, y, s);

		String[] walkFileNames = { "zombie/Walk (1).png", "zombie/Walk (2).png", "zombie/Walk (3).png",
				"zombie/Walk (4).png", "zombie/Walk (5).png", "zombie/Walk (6).png", "zombie/Walk (7).png",
				"zombie/Walk (8).png", "zombie/Walk (9).png", "zombie/Walk (10).png" };

		walk = loadAnimationFromFiles(walkFileNames, 0.15f, true);
		setBoundaryPolygon(8);
		String[] attackFileNames = { "zombie/Attack (1).png", "zombie/Attack (2).png", "zombie/Attack (3).png",
				"zombie/Attack (4).png", "zombie/Attack (5).png", "zombie/Attack (6).png", "zombie/Attack (7).png",
				"zombie/Attack (8).png" };
		attack = loadAnimationFromFiles(attackFileNames, 0.095f, true);

		speed = 3f;
		direction = 1;
		move = true;
		life = 50;

	}

	public void act(float dt) {
		super.act(dt);

		if (move) {
			moveBy(speed, 0);
		}

	}

	public void attack() {
		move = false;
		setAnimation(attack);
	}

	public void walk() {
		setAnimation(walk);
		move = true;
	}

	public void setMove() {
		speed = -speed;
		direction = -direction;
		setScaleX(direction);
	}

}
