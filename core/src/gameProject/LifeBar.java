package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 	Class used to manage the life bar of the player.
 */
public class LifeBar extends BaseActor {

	public LifeBar(float x, float y, Stage s) {
		super(x, y, s);
		loadTexture("JumpingJack/items/health/frame-1.png");
	}

}