package gameProject;

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

/**
 * 	Class used to handle the levels of the game.
 */
public abstract class LevelScreen extends BaseScreen {
	Robot jack;
	Enemy enemy;
	Zombie zombie;

	protected boolean gameOver;
	protected boolean death;
	protected int coins;
	protected int count;
	protected float tot_life;
	Label coinLabel;
	Label messageLabel;
	Table keyTable;
	Label lifeLabel;

	ArrayList<Color> keyList;
	ArrayList<LifeBar> lifeList;

	public abstract void initialize();

	public void update(float dt) {
		if (gameOver)
			return;

		for (BaseActor flag : BaseActor.getList(mainStage, Flag.class.getName())) {
			if (jack.overlaps(flag)) {
				messageLabel.setText("You Win!\nPress C to continue");
				messageLabel.setColor(Color.LIME);
				messageLabel.setVisible(true);
				jack.remove();
				gameOver = true;
			}
		}

		for (BaseActor door : BaseActor.getList(mainStage, Door.class.getName())) {
			if (jack.overlaps(door)) {
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
				coinLabel.setText("  " + coins);
				coin.remove();
			}
		}

		for (BaseActor health : BaseActor.getList(mainStage, Health.class.getName())) {
			if (jack.overlaps(health)) {
				if(jack.life < 150) {
					jack.life += 25;
					lifeLabel.setText("Life: " + (int) jack.life);
					health.remove();
				}	
			}
		}

		for (BaseActor voidFall : BaseActor.getList(mainStage, VoidFall.class.getName())) {
			if (jack.overlaps(voidFall)) {
				messageLabel.setText("GAME OVER!\nPress C to continue");
				messageLabel.setColor(Color.RED);

				messageLabel.setVisible(true);
				jack.life = 0;
				jack.remove();
				resetCharacterAnimation();
				lifeBarStatus();
				death = true;
				gameOver = true;
			}
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
			contactDamage(enemy);

		}

		for (BaseActor actor : BaseActor.getList(mainStage, Zombie.class.getName())) {
			zombie = (Zombie) actor;
			zombieAttack();
			contactDamage(zombie);
			for (BaseActor block : BaseActor.getList(mainStage, BlockMove.class.getName())) {
				if (zombie.overlaps(block)) {
					zombie.setMove();
				}

			}
		}

		// laser hit enemy
		for (BaseActor laserActor : BaseActor.getList(mainStage, Laser.class.getName())) {

			for (BaseActor actor : BaseActor.getList(mainStage, Enemy.class.getName())) {
				if (laserActor.overlaps(actor)) {
					Enemy enm = (Enemy) actor;
					Laser l = (Laser) laserActor;
					l.removeAction(l.first);
					l.setSpeed(0);

					if (!l.getHit())
						enm.life -= 25;
					l.setHit();
					if (enm.life <= 0) {
						enm.addAction((Actions.fadeOut(0.3f)));
						Timer.schedule(new Task() {
							@Override
							public void run() {
								enm.setPosition(-1000, -1000);
								enm.remove();
							}
						}, 0.3f);

					}
				}
			}

			for (BaseActor actor : BaseActor.getList(mainStage, Zombie.class.getName())) {

				if (laserActor.overlaps(actor)) {
					Zombie zomb = (Zombie) actor;
					Laser l = (Laser) laserActor;
					l.removeAction(l.first);
					l.setSpeed(0);

					if (!l.getHit())
						zomb.life -= 25;
					l.setHit();

					if (zomb.life <= 0) {
						zomb.addAction((Actions.fadeOut(0.3f)));
						Timer.schedule(new Task() {
							@Override
							public void run() {
								zomb.setPosition(-1000, -1000);
								zomb.remove();
							}
						}, 0.3f);
					}
				}
			}

			for (BaseActor actor : BaseActor.getList(mainStage, Solid.class.getName())) {
				Solid solid = (Solid) actor;
				if (laserActor.overlaps(solid)) {
					Laser l = (Laser) laserActor;
					l.removeAction(l.first);
					l.setSpeed(0);

				}
			}

		}

		// enemy hit jack
		for (BaseActor laser2Actor : BaseActor.getList(mainStage, Laser2.class.getName())) {
			if (laser2Actor.overlaps(jack)) {
				Laser2 l = (Laser2) laser2Actor;
				l.removeAction(l.first);
				l.setSpeed(0);

				if (!l.getHit())
					fireDamageJack();
				l.setHit();

			}
			for (BaseActor actor : BaseActor.getList(mainStage, Solid.class.getName())) {
				Solid solid = (Solid) actor;
				if (laser2Actor.overlaps(solid)) {
					laser2Actor.remove();
				}
			}

		}

		// CONTROL JACK LIFE
		controlLife();
	}

	public void enemyAttack() {
		if (jack.isDead())
			return;
		float diffx = enemy.getX() - jack.getX();
		float diffy = enemy.getY() - jack.getY();
		if (Math.abs(diffx) < 420 && Math.abs(diffy) < 10) {
			if ((diffx > 0 && enemy.getScaleX() == -1) || (diffx < 0 && enemy.getScaleX() == 1)) {
				if (enemy.getScaleX() == -1 && enemy.hasAttacked == false) {
					enemy.moveBy(-30, 0);
					enemy.hasAttacked = true;
				}

				enemy.attack();
			} else {
				if (enemy.getScaleX() == -1 && enemy.hasAttacked == true) {
					enemy.moveBy(+30, 0);
					enemy.hasAttacked = false;
				}
				enemy.stand();
			}
		} else {
			if (enemy.getScaleX() == -1 && enemy.hasAttacked == true) {
				enemy.moveBy(+30, 0);
				enemy.hasAttacked = false;
			}
			enemy.stand();
		}
	}

	public void zombieAttack() {
		if (jack.isDead())
			return;
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

	public void contactDamage(BaseActor other) {
		if (jack.overlaps(other)) {
			if (jack.life <= 0)
				controlLife();
			else {
				count++;
				if (count == 48) {
					jack.life = jack.life - 25;
					if (jack.life <= 0)
						jack.life = 0;
					lifeLabel.setText("Life: " + (int) jack.life);
					count = 0;
				}
			}
		}
	}

	public void fireDamageJack() {
		if (!gameOver)
			controlLife();
		if (jack.life > 0) {
			jack.life = jack.life - 25;
			if (jack.life <= 0)
				jack.life = 0;
			lifeLabel.setText("Life: " + (int) jack.life);
		}
	}

	public void controlLife() {
		if (jack.life <= 0) {
			jack.dead();
			resetCharacterAnimation();
			Timer.schedule(new Task() {
				@Override
				public void run() {

					messageLabel.setText("Game Over\nPress C to continue");
					messageLabel.setColor(Color.RED);
					messageLabel.setVisible(true);
					jack.remove();
					jack.setPosition(-1000, -1000);

					death = true;
					gameOver = true;
				}
			}, 3.0f);
		}
	}

	public void lifeBarStatus() {
		if (jack.life == 0) {
			float r = 0;
			float k = tot_life / 25 - 1;
			while (r <= k) {
				LifeBar x = lifeList.get((int) k);
				x.setVisible(false);
				k--;
			}
		} else {
			float r = jack.life / 25 - 1;
			float k = tot_life / 25 - 1;
			while (k != r && r < k) {
				LifeBar x = lifeList.get((int) k);
				x.setVisible(false);
				k--;
			}
			for (int i = 0; i <= r; i++) {
				LifeBar x = lifeList.get((int) i);
				x.setVisible(true);
			}
		}
	}

	public void resetCharacterAnimation() {
		for (BaseActor actor : BaseActor.getList(mainStage, Enemy.class.getName())) {
			enemy = (Enemy) actor;
			enemy.stand();
		}

		for (BaseActor actor : BaseActor.getList(mainStage, Zombie.class.getName())) {
			zombie = (Zombie) actor;
			zombie.speed = 0;
			zombie.setAnimationPaused(true);
		}
	}

	public boolean keyDown(int keyCode) {
		if (gameOver)
			return false;

		if (keyCode == Keys.SPACE) {
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

}
