package ex90_KoalaJack;

public class JumpingJackGame extends BaseGame {
	public void create() {        
		super.create();
		setActiveScreen( new MenuScreen() );
	}
}