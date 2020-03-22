package ex90_KoalaJackDesktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import gameProject.JumpingJackGame;

public class Launcher {
	public static void main (String[] args) {
		Game myGame = new JumpingJackGame();
		new LwjglApplication( myGame, "Jumping Jack", 800, 640 );
	}
}