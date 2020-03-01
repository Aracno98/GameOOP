package ex90_KoalaJack;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Laser extends BaseActor {
	public Laser(float x, float y, Stage s) {
		super(x,y,s);
		loadTexture("Images/fireball.png");
		
		addAction( Actions.delay(0.5f) );
		addAction( Actions.after( Actions.fadeOut(0.5f) ) );
		addAction( Actions.after( Actions.removeActor() ) );
		
		setSpeed(400);
		setMaxSpeed(400);
		setDeceleration(0);
	}
	
	public void act(float dt) {
		super.act(dt);
		applyPhysics(dt);
	}
}