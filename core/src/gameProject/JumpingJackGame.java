package gameProject;

public class JumpingJackGame extends BaseGame {
	public void create() {        
		super.create();
		setActiveScreen( new MenuScreen() );
	}
}