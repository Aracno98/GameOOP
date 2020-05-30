package gameProject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 *	Class used to create coins on the map. Coins are collected by the player character.
 */
public class Coin extends BaseActor {
	public Coin(float x, float y, Stage s) { 
		super(x,y,s);

		loadTexture("JumpingJack/items/coin.png");
		float random = MathUtils.random(30);
		addAction( Actions.forever( Actions.rotateBy(30 + random, 1) ) );
		Action pulse = Actions.sequence(
				Actions.scaleTo(1.1f, 1.1f, 0.5f),
				Actions.scaleTo(1.0f, 1.0f, 0.5f) );

		addAction( Actions.forever(pulse) );
	}    
}