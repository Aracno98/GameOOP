package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class BlockMove extends BaseActor {
	public BlockMove(float x, float y, float width, float height, Stage s) {
		super(x, y, s);
		setSize(width, height);
		setBoundaryRectangle();
	}
}
