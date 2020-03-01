package ex90_KoalaJack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class LevelScreen extends BaseScreen {
	Koala jack;

	boolean gameOver;
	int coins;
	float time;

	Label coinLabel;
	Label timeLabel;
	Label messageLabel;
	Table keyTable;

	ArrayList<Color> keyList;

	public abstract void initialize();

	public void update(float dt) {
		if ( gameOver )
			return;

		for (BaseActor voidFall : BaseActor.getList(mainStage, VoidFall.class.getName())) {
			if ( jack.overlaps(voidFall) ) {
				messageLabel.setText("You Lose!");
				messageLabel.setColor(Color.RED);
				messageLabel.setVisible(true);
				jack.remove();
				gameOver = true;
			}
		}

		for (BaseActor flag : BaseActor.getList(mainStage, Flag.class.getName())) {
			if ( jack.overlaps(flag) ) {   
				messageLabel.setText("You Win!\nPress C to continue");
				messageLabel.setColor(Color.LIME);
				messageLabel.setVisible(true);
				jack.remove();
				gameOver = true;
			}
		}

		for (BaseActor coin : BaseActor.getList(mainStage, Coin.class.getName())) {
			if ( jack.overlaps(coin) ) {
				coins++;
				coinLabel.setText("Coins: " + coins);
				coin.remove();
			}
		}

		time -= dt;
		timeLabel.setText("Time: " + (int)time);

		for (BaseActor timer : BaseActor.getList(mainStage, Timer.class.getName())) {
			if ( jack.overlaps(timer) ) {   
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
			if ( jack.belowOverlaps(springboard) && jack.isFalling() ) {
				jack.spring();
			}
		}

		for (BaseActor actor : BaseActor.getList(mainStage, Solid.class.getName())) {
			Solid solid = (Solid)actor;

			if ( solid instanceof Platform ) {
				// disable the solid when jumping up through
				if ( jack.isJumping() && jack.overlaps(solid) )
					solid.setEnabled(false);

				// when jumping, after passing through, re-enable the solid
				if ( jack.isJumping() && !jack.overlaps(solid) )
					solid.setEnabled(true);

				// disable the solid when jumping down through: code is in keyDown method

				// when falling, after passing through, re-enable the solid
				if ( jack.isFalling() && !jack.overlaps(solid) && !jack.belowOverlaps(solid) )
					solid.setEnabled(true);
			}

			if ( solid instanceof Lock && jack.overlaps(solid) ) {
				Color lockColor = solid.getColor();
				if ( keyList.contains(lockColor) ) {
					solid.setEnabled(false);
					solid.addAction( Actions.fadeOut(0.5f) );
					solid.addAction( Actions.after( Actions.removeActor() ) );
				}
			}

			if ( jack.overlaps(solid) && solid.isEnabled() ) {
				Vector2 offset = jack.preventOverlap(solid);

				if (offset != null) {
					// collided in X direction
					if ( Math.abs(offset.x) > Math.abs(offset.y) )
						jack.velocityVec.x = 0;
					else // collided in Y direction
						jack.velocityVec.y = 0;
				}
			}
		}

		for (BaseActor key : BaseActor.getList(mainStage, Key.class.getName())) {
			if ( jack.overlaps(key) ) {
				Color keyColor = key.getColor();
				key.remove();

				BaseActor keyIcon =  new BaseActor(0,0,uiStage);
				keyIcon.loadTexture("JumpingJack/key-icon.png");
				keyIcon.setColor(keyColor);
				keyTable.add(keyIcon);

				keyList.add(keyColor);         
			}
		}

		for (BaseActor fire : BaseActor.getList(mainStage, Laser.class.getName())) {
			for (BaseActor wiz : BaseActor.getList(mainStage, Wizard.class.getName())) {
				if ( fire.overlaps(wiz) ) {
					fire.remove();
					coins += 3;
					coinLabel.setText("Coins: " + coins);
					wiz.setAnimation(wiz.loadAnimationFromSheet("Enemies/wizard_death.png", 1, 10, 0.5f, false));
					wiz.remove();
				}
				
				if ( jack.overlaps(wiz) ) {
					messageLabel.setText("You Lose!");
					messageLabel.setColor(Color.RED);
					messageLabel.setVisible(true);
					jack.remove();
					gameOver = true;
				}
			}
		}
	}

	public boolean keyDown(int keyCode) {
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			Gdx.app.exit();
		if (Gdx.input.isKeyPressed(Keys.X))
			jack.shoot();
		return false;
	}
}