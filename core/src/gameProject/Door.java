package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Door extends BaseActor {
	public Door(float x, float y, float width, float height, Stage s) {
		super(x, y, s);
		setSize(width, height);
		setBoundaryRectangle();
	}
}
