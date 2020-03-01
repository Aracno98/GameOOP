package ex90_KoalaJack;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Wizard extends Enemy {
	
	public Wizard(float x, float y, Stage s) {
		super(x,y,s);
		loadAnimationFromSheet("Enemies/wizard_stand.png", 1, 10, 0.2f, true);
		
		setBoundaryPolygon(8);
	}
	
	public void act(float dt) {
		super.act(dt);
	}
}