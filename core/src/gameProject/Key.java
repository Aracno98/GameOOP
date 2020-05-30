package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 *	Class used to create keys on the map. Key are used to open locks when the player overlaps them.
 */
public class Key extends BaseActor {
	public Key(float x, float y, Stage s) { 
		super(x,y,s);
		loadTexture("JumpingJack/items/key.png");

		rotateBy(10);

		Action tilt = Actions.sequence(
				Actions.rotateBy(-20, 0.5f),
				Actions.rotateBy(20, 0.5f) );

		addAction( Actions.forever(tilt) );
	}    
}