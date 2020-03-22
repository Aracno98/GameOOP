package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class VoidFall extends BaseActor {
	public VoidFall(float x, float y, float width, float height, Stage s) {
		super(x, y, s);
		setSize(width, height);
		setBoundaryRectangle();
	}
}