package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 	Class used to create springboards on the map. The player can use them to jump over obstacles.
 */
public class Springboard extends BaseActor {
	public Springboard(float x, float y, Stage s) {
		super(x,y,s);
		loadAnimationFromSheet("JumpingJack/items/springboard.png", 1, 3, 0.2f, true);
	}    
}