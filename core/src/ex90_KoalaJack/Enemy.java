package ex90_KoalaJack;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Enemy extends BaseActor {
	protected Animation stand;
	protected Animation attack;

	private float fire_time;
	protected float life;

	public Enemy(float x, float y, Stage s) {
		super(x, y, s);

		stand = loadAnimationFromSheet("Enemies/Idle/Idle - Sprite Sheet.png", 2, 6, 0.07f, true);
		setBoundaryPolygon(8);

		attack = loadAnimationFromSheet("Enemies/Shoot/Shoot - Sprite Sheet.png", 2, 8, 0.04f, true);

		setScaleX(1);

		life = 100;
		fire_time = 0;
	}

	public void act(float dt) {
		super.act(dt);

		fire_time -= dt;

	}

	public void attack() {

		if (getStage() == null)
			return;
		setAnimation(attack);
		if (fire_time <= 0) {
			fire_time = 0.7f;
			Laser2 laser = new Laser2(0, 0, this.getStage());
			laser.centerAtActor(this);
			laser.setScaleX(this.getScaleX());
			laser.setPosition(laser.getX() + laser.getScaleX() * 50, laser.getY() + 10);
			laser.velocityVec.x = laser.getSpeed() * laser.getScaleX();
		
			
		}
	}

	public void stand() {
		setAnimation(stand);

	}

}
