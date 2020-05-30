package gameProject;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *	Blocks used to handle enemy movements
 */
public class BlockMove extends BaseActor {
	public BlockMove(float x, float y, float width, float height, Stage s) {
		super(x, y, s);
		setSize(width, height);
		setBoundaryRectangle();
	}
}
