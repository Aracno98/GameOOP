package ex90_KoalaJack;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Laser2 extends BaseActor {

	public Laser2(float x, float y, Stage s) {
		super(x, y, s);
		loadTexture("Images/fireball.png");
		addAction(Actions.delay(3));
		addAction(Actions.after(Actions.fadeOut(0.5f)));
		addAction(Actions.after(Actions.removeActor()));
		setSpeed(350);
		setMaxSpeed(350);
		setDeceleration(0);
	}

	public void act(float dt) {
		super.act(dt);
		applyPhysics(dt);
		wrapAroundWorld();
	}
}