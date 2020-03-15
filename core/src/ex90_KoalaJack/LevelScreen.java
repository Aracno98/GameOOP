package ex90_KoalaJack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class LevelScreen extends BaseScreen {
	Koala jack;
	Enemy enemy;
	Zombie zombie;

	boolean gameOver;
	int coins;
	float time;
	int count;

	Label coinLabel;
	Label timeLabel;
	Label messageLabel;
	Table keyTable;
	Label lifeLabel;

	ArrayList<Color> keyList;

	public abstract void initialize();

	public void update(float dt) {
		if (gameOver)
			return;

		for (BaseActor voidFall : BaseActor.getList(mainStage, VoidFall.class.getName())) {
			if (jack.overlaps(voidFall)) {
				messageLabel.setText("You Lose!");
				messageLabel.setColor(Color.RED);
				messageLabel.setVisible(true);
				jack.remove();
				gameOver = true;
			}
		}

		for (BaseActor flag : BaseActor.getList(mainStage, Flag.class.getName())) {
			if (jack.overlaps(flag)) {
				messageLabel.setText("You Win!\nPress C to continue");
				messageLabel.setColor(Color.LIME);
				messageLabel.setVisible(true);
				jack.remove();
				gameOver = true;
			}
		}

		for (BaseActor coin : BaseActor.getList(mainStage, Coin.class.getName())) {
			if (jack.overlaps(coin)) {
				coins++;
				coinLabel.setText("Coins: " + coins);
				coin.remove();
			}
		}

		time -= dt;
		timeLabel.setText("Time: " + (int) time);

		for (BaseActor timer : BaseActor.getList(mainStage, Timer2.class.getName())) {
			if (jack.overlaps(timer)) {
				time += 30;
				timer.remove();
			}
		}

		if (time <= 0) {
			messageLabel.setText("Time Up - Game Over");
			messageLabel.setColor(Color.RED);
			messageLabel.setVisible(true);
			jack.remove();
			gameOver = true;
		}

		for (BaseActor springboard : BaseActor.getList(mainStage, Springboard.class.getName())) {
			if (jack.belowOverlaps(springboard) && jack.isFalling()) {
				jack.spring();
			}
		}

		for (BaseActor actor : BaseActor.getList(mainStage, Solid.class.getName())) {
			Solid solid = (Solid) actor;

			if (solid instanceof Platform) {
				// disable the solid when jumping up through
				if (jack.isJumping() && jack.overlaps(solid))
					solid.setEnabled(false);

				// when jumping, after passing through, re-enable the solid
				if (jack.isJumping() && !jack.overlaps(solid))
					solid.setEnabled(true);

				// disable the solid when jumping down through: code is in keyDown method

				// when falling, after passing through, re-enable the solid
				if (jack.isFalling() && !jack.overlaps(solid) && !jack.belowOverlaps(solid))
					solid.setEnabled(true);
			}

			if (solid instanceof Lock && jack.overlaps(solid)) {
				Color lockColor = solid.getColor();
				if (keyList.contains(lockColor)) {
					solid.setEnabled(false);
					solid.addAction(Actions.fadeOut(0.5f));
					solid.addAction(Actions.after(Actions.removeActor()));
				}
			}

			if (jack.overlaps(solid) && solid.isEnabled()) {
				Vector2 offset = jack.preventOverlap(solid);

				if (offset != null) {
					// collided in X direction
					if (Math.abs(offset.x) > Math.abs(offset.y))
						jack.velocityVec.x = 0;
					else // collided in Y direction
						jack.velocityVec.y = 0;
				}
			}
		}

		for (BaseActor key : BaseActor.getList(mainStage, Key.class.getName())) {
			if (jack.overlaps(key)) {
				Color keyColor = key.getColor();
				key.remove();

				BaseActor keyIcon = new BaseActor(0, 0, uiStage);
				keyIcon.loadTexture("JumpingJack/key-icon.png");
				keyIcon.setColor(keyColor);
				keyTable.add(keyIcon);

				keyList.add(keyColor);
			}
		}


		for (BaseActor actor : BaseActor.getList(mainStage, Enemy.class.getName())) {
			enemy = (Enemy) actor;
			enemyAttack();

		}

		for (BaseActor actor : BaseActor.getList(mainStage, Zombie.class.getName())) {
			zombie = (Zombie) actor;
			zombieAttack();
			zombieDamage(zombie);
		}

		// laser hit enemy
		for (BaseActor laserActor : BaseActor.getList(mainStage, Laser.class.getName())) {

			for (BaseActor actor : BaseActor.getList(mainStage, Enemy.class.getName())) {
				if (laserActor.overlaps(actor)) {
					Enemy enm = (Enemy) actor;
					laserActor.remove();
					enm.life -= 25;
					if (enm.life <= 0) {
						enm.setPosition(-1000, -1000);
						enm.remove();
					}
				}
			}
			for (BaseActor actor : BaseActor.getList(mainStage, Zombie.class.getName())) {

				if (laserActor.overlaps(actor)) {
					Zombie zomb = (Zombie) actor;
					laserActor.remove();
					zomb.life -= 25;
					if (zomb.life <= 0) {
						zomb.setPosition(-1000, -1000);
						zomb.remove();
					}
				}
			}

			for (BaseActor actor : BaseActor.getList(mainStage, Solid.class.getName())) {
				Solid solid = (Solid) actor;
				if (laserActor.overlaps(solid)) {
					laserActor.remove();
				}
			}

		}

		// enemy hit jack
		for (BaseActor laser2Actor : BaseActor.getList(mainStage, Laser2.class.getName())) {
			if (laser2Actor.overlaps(jack)) {
				fireDamageJack();
				laser2Actor.remove();
			}
			for (BaseActor actor : BaseActor.getList(mainStage, Solid.class.getName())) {
				Solid solid = (Solid) actor;
				if (laser2Actor.overlaps(solid)) {
					laser2Actor.remove();
				}
			}

		}

		/*
		 * for (BaseActor fire : BaseActor.getList(mainStage, Laser.class.getName())) {
		 * for (BaseActor wiz : BaseActor.getList(mainStage, Wizard.class.getName())) {
		 * if ( fire.overlaps(wiz) ) { fire.remove(); coins += 3;
		 * coinLabel.setText("Coins: " + coins);
		 * wiz.setAnimation(wiz.loadAnimationFromSheet("Enemies/wizard_death.png", 1,
		 * 10, 0.5f, false)); wiz.remove(); }
		 * 
		 * if ( jack.overlaps(wiz) ) { messageLabel.setText("You Lose!");
		 * messageLabel.setColor(Color.RED); messageLabel.setVisible(true);
		 * jack.remove(); gameOver = true; } } }
		 */
		//CONTROL JACK LIFE
		controlLife();
	}

	public boolean keyDown(int keyCode) {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();
		if (gameOver)
			return false;
		if (keyCode == Keys.SPACE) {
			// if down arrow is held while jump is pressed and koala is above a platform,
			// then the koala can fall down through it.
			if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				for (BaseActor actor : BaseActor.getList(mainStage, Platform.class.getName())) {
					Platform platform = (Platform) actor;
					if (jack.belowOverlaps(platform)) {
						platform.setEnabled(false);
					}
				}
			} else if (jack.isOnSolid()) {
				jack.jump();
			}
		}
		if (keyCode == Keys.SHIFT_LEFT) {
			jack.shoot();

		}
		return false;
	}

	public void enemyAttack() {

		float diffx = enemy.getX() - jack.getX();
		float diffy = enemy.getY() - jack.getY();
		if (Math.abs(diffx) < 600 && Math.abs(diffy) < 10) {
			if ((diffx > 0 && enemy.getScaleX() == -1) || (diffx < 0 && enemy.getScaleX() == 1))
				enemy.attack();
			else
				enemy.stand();
		} else
			enemy.stand();
	}

	public void zombieAttack() {

		float diffx = zombie.getX() - jack.getX();
		float diffy = zombie.getY() - jack.getY();
		if (Math.abs(diffx) < 30 && Math.abs(diffy) < 6) {
			if ((diffx > 0 && zombie.getScaleX() == -1) || (diffx < 0 && zombie.getScaleX() == 1))
				zombie.attack();
			else
				zombie.walk();
		} else
			zombie.walk();

	}

	public void zombieDamage(BaseActor other) {
		if (jack.overlaps(other)) {
			if(jack.life <= 0)
				controlLife();
			else {
				count++;
				if (count == 48) {
					jack.life = jack.life - 25 ;
					if (jack.life <= 0)
						jack.life = 0;
					lifeLabel.setText("Life: " + (int) jack.life);
					count = 0;
				}
			}
		}

	}


	public void fireDamageJack() {
		if(!gameOver)
			controlLife();
		if(jack.life>0) {
			jack.life = jack.life - 25;
			if (jack.life <= 0)
				jack.life = 0;
			lifeLabel.setText("Life: " + (int) jack.life);
		}
	}

	public void controlLife() {
		if (jack.life <= 0) {
			jack.dead();
			Timer.schedule(new Task() {
				@Override
				public void run() {
					messageLabel.setText("Game Over");
					messageLabel.setColor(Color.RED);
					messageLabel.setVisible(true);
					jack.remove();
					jack.setPosition(-1000, -1000);
					gameOver = true;
				}
			}, 3.0f);
		}
	}
}
