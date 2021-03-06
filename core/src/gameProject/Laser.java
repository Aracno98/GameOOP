package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * 	Player character attack. Used to reduce enemy's life.
 */
public class Laser extends BaseActor {
	protected Action first;
	private boolean hit;

	public Laser(float x, float y, Stage s) {
		super(x, y, s);
		loadTexture("robot/Bullet_004.png");

		first = Actions.delay(2.5f);
		addAction(first);
		addAction(Actions.after(Actions.fadeOut(0.3f)));
		addAction(Actions.after(Actions.removeActor()));
		setSpeed(300);
		setMaxSpeed(350);
		setDeceleration(0);
		hit = false;
	}

	public void act(float dt) {
		super.act(dt);
		applyPhysics(dt);
		wrapAroundWorld();
	}

	public void setHit() {
		this.hit = true;
	}
	public boolean getHit() {
		return hit;
	}

}