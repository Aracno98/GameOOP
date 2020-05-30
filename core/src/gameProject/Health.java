package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 *	Class used to create hearts on the map. Hearts increase player's life.
 */
public class Health extends BaseActor {
	public Health(float x, float y, Stage s) {
		super(x,y,s);

		String[] lifeFileNames = { "JumpingJack/items/health/frame-1.png", "JumpingJack/items/health/frame-2.png","JumpingJack/items/health/frame-3.png",
				"JumpingJack/items/health/frame-4.png","JumpingJack/items/health/frame-5.png","JumpingJack/items/health/frame-6.png",
				"JumpingJack/items/health/frame-7.png","JumpingJack/items/health/frame-8.png"};
		loadAnimationFromFiles(lifeFileNames, 0.5f, true);

		Action pulse = Actions.sequence(
				Actions.scaleTo(1.1f, 1.1f, 0.5f),
				Actions.scaleTo(1.0f, 1.0f, 0.5f) );

		addAction( Actions.forever(pulse) );
	}    
}