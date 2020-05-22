package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Lock extends Solid {
	public Lock(float x, float y, Stage s) {
		super(x,y,40,40,s);
		loadTexture("JumpingJack/items/lock.png");
	}
}