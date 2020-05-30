package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *	Class used to create platforms on the map. If down arrow is held while jump is pressed
 *	and the player is above a platform, then the player can fall down through it
 *	(this is managed by the KeyDown method of the LevelScreen class).
 */
public class Platform extends Solid {
	public Platform(float x, float y, Stage s) { 
		super(x,y,40,20,s);
		loadTexture("JumpingJack/items/platform.png");
	}
}