package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 	Class used to create obstacles to the player. Locks can be opened with
 * 	the corresponding keys.
 */
public class Lock extends Solid {
	public Lock(float x, float y, Stage s) {
		super(x,y,40,40,s);
		loadTexture("JumpingJack/items/lock.png");
	}
}