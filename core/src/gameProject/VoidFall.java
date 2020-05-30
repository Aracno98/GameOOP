package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 	Class used as an obstacle to the player. When the player overlaps an VoidFall object,
 * 	a Game Over message is displayed.
 */
public class VoidFall extends BaseActor {
	public VoidFall(float x, float y, float width, float height, Stage s) {
		super(x, y, s);
		setSize(width, height);
		setBoundaryRectangle();
	}
}