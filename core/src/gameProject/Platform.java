package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Platform extends Solid {
	public Platform(float x, float y, Stage s) { 
		super(x,y,40,20,s);
		loadTexture("JumpingJack/items/platform.png");
	}
}