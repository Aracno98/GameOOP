package gameProject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;

/**
 * Class that displays the title screen of the game
 */
public class MenuScreen extends BaseScreen {
	public void initialize() {
		BaseActor back = new BaseActor(0,0, mainStage);
		back.loadTexture( "JumpingJack/Background3.jpg" );
		back.setSize(1200,900);

		TextButton startButton = new TextButton( "Start", BaseGame.textButtonStyle );
		startButton.addListener(
				(Event e) ->
				{
					if ( !(e instanceof InputEvent) ||
							!((InputEvent)e).getType().equals(Type.touchDown) )
						return false;
					JumpingJackGame.setActiveScreen( new Level1() );
					return false;
				}
				);

		TextButton quitButton = new TextButton( "Quit", BaseGame.textButtonStyle );
		quitButton.addListener(
				(Event e) ->
				{
					if ( !(e instanceof InputEvent) ||
							!((InputEvent)e).getType().equals(Type.touchDown) )
						return false;
					Gdx.app.exit();
					return false;
				}
				);

		uiTable.add(startButton);
		uiTable.add(quitButton);
	}

	public void update(float dt) {}

	public boolean keyDown(int keyCode) {
		if (Gdx.input.isKeyPressed(Keys.ENTER))
			JumpingJackGame.setActiveScreen( new Level1() );
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();
		return false;
	}
}