package gameProject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Coin extends BaseActor {
	public Coin(float x, float y, Stage s) { 
		super(x,y,s);

		loadTexture("JumpingJack/items/coin.png");
		float random = MathUtils.random(30);
		addAction( Actions.forever( Actions.rotateBy(30 + random, 1) ) );
	}    
}