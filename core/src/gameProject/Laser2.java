package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Laser2 extends BaseActor {
	protected Action first;
	private boolean hit;

	public Laser2(float x, float y, Stage s) {
		super(x, y, s);
		loadTexture("Images/fireball.png");
		first = Actions.delay(2.5f);
		addAction(first);
		addAction(Actions.after(Actions.fadeOut(0.5f)));
		addAction(Actions.after(Actions.removeActor()));
		setSpeed(200);
		setMaxSpeed(250);
		setDeceleration(0);
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