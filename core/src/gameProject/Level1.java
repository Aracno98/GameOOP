package gameProject;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.audio.Music;

public class Level1 extends LevelScreen {
	private float audioVolume;
	private Music instrumental;
	
	
	public void initialize() {
		TilemapActor tma = new TilemapActor("Levels/map01R.tmx", mainStage);

		for (MapObject obj : tma.getRectangleList("Solid")) {
			MapProperties props = obj.getProperties();
			new Solid((float) props.get("x"), (float) props.get("y"), (float) props.get("width"),
					(float) props.get("height"), mainStage);
		}

		for (MapObject obj : tma.getRectangleList("Void")) {
			MapProperties props = obj.getProperties();
			new VoidFall((float) props.get("x"), (float) props.get("y"), (float) props.get("width"),
					(float) props.get("height"), mainStage);
		}

		for (MapObject obj : tma.getRectangleList("BlockMove")) {
			MapProperties props = obj.getProperties();
			new BlockMove((float) props.get("x"), (float) props.get("y"), (float) props.get("width"),
					(float) props.get("height"), mainStage);
		}

		MapObject startPoint = tma.getRectangleList("start").get(0);
		MapProperties startProps = startPoint.getProperties();
		jack = new Robot((float) startProps.get("x"), (float) startProps.get("y"), mainStage);

		for (MapObject obj : tma.getTileList("Flag")) {
			MapProperties props = obj.getProperties();
			new Flag((float) props.get("x"), (float) props.get("y"), mainStage);
		}

		for (MapObject obj : tma.getTileList("Coin")) {
			MapProperties props = obj.getProperties();
			new Coin((float) props.get("x"), (float) props.get("y"), mainStage);
		}

		for (MapObject obj : tma.getTileList("Health")) {
			MapProperties props = obj.getProperties();
			new Health((float) props.get("x"), (float) props.get("y"), mainStage);
		}

		for (MapObject obj : tma.getTileList("Springboard")) {
			MapProperties props = obj.getProperties();
			new Springboard((float) props.get("x"), (float) props.get("y"), mainStage);
		}

		for (MapObject obj : tma.getTileList("Platform")) {
			MapProperties props = obj.getProperties();
			new Platform((float) props.get("x"), (float) props.get("y"), mainStage);
		}

		for (MapObject obj : tma.getTileList("Key")) {
			MapProperties props = obj.getProperties();
			Key key = new Key((float) props.get("x"), (float) props.get("y"), mainStage);
			String color = (String) props.get("color");
			if (color.equals("red"))
				key.setColor(Color.RED);
			else if (color.equals("blue"))
				key.setColor(Color.BLUE);
			else if (color.equals("yellow"))
				key.setColor(Color.YELLOW);
			else // default color
				key.setColor(Color.WHITE);
		}

		for (MapObject obj : tma.getTileList("Lock")) {
			MapProperties props = obj.getProperties();
			Lock lock = new Lock((float) props.get("x"), (float) props.get("y"), mainStage);
			String color = (String) props.get("color");
			if (color.equals("red"))
				lock.setColor(Color.RED);
			else if (color.equals("blue"))
				lock.setColor(Color.BLUE);
			else if (color.equals("yellow"))
				lock.setColor(Color.YELLOW);
			else // default
				lock.setColor(Color.WHITE);
		}

		/*for (MapObject obj : tma.getRectangleList("Wizard")) {
			MapProperties props = obj.getProperties();
			new Wizard((float) props.get("x"), (float) props.get("y"), mainStage);
		}*/

		for (MapObject obj : tma.getRectangleList("Zombie")) {
			MapProperties zomProps = obj.getProperties();
			new Zombie((float) zomProps.get("x"), (float) zomProps.get("y"), mainStage);
		}

		for (MapObject obj : tma.getRectangleList("Enemy")) {
			MapProperties enProps = obj.getProperties();
			Enemy e = new Enemy((float) enProps.get("x"), (float) enProps.get("y"), mainStage);
			float scale = Float.parseFloat((String) enProps.get("scale"));
			e.setScaleX(scale);
		}

		jack.toFront();

		gameOver = false;
		coins = 0;
		time = 150;

		coinLabel = new Label("  " + coins, BaseGame.labelStyle);
		coinLabel.setColor(Color.GOLD);
		keyTable = new Table();
		timeLabel = new Label("Time: " + (int) time, BaseGame.labelStyle);
		timeLabel.setColor(Color.LIGHT_GRAY);
		messageLabel = new Label("Message", BaseGame.labelStyle);
		messageLabel.setVisible(false);
		lifeLabel = new Label("Life: " + (int) jack.life, BaseGame.labelStyle);

		BaseActor coin_bar = new BaseActor(0, 0, mainStage);
		coin_bar.loadTexture("JumpingJack/items/frame-1.png");

		lifeList = new ArrayList<LifeBar>();

		uiTable.pad(20);
		uiTable.add(coin_bar);
		uiTable.add(coinLabel);
		uiTable.add(keyTable).expandX();
		uiTable.add(timeLabel);
		for (int i = 0; i < jack.life / 25; i++) {
			LifeBar l = new LifeBar(0, 0, mainStage);
			lifeList.add(l);
			uiTable.add(l);
		}

		uiTable.row();
		uiTable.add(messageLabel).colspan(9).expandY();

		keyList = new ArrayList<Color>();

		instrumental = Gdx.audio.newMusic(Gdx.files.internal("Musics/SMB.mp3"));
		audioVolume = 1.00f;

		instrumental.setLooping(true);
		instrumental.setVolume(audioVolume);
		instrumental.play();

		count = 0;
		tot_life = jack.life;
	}

	public void update(float dt) {
		super.update(dt);

		for (BaseActor voidFall : BaseActor.getList(mainStage, VoidFall.class.getName())) {
			if (jack.overlaps(voidFall)) {
				messageLabel.setText("GAME OVER!");
				messageLabel.setColor(Color.RED);
				// messageLabel.setPosition(Gdx.graphics.getWidth()/2,
				// Gdx.graphics.getHeight()/2);
				
				messageLabel.setVisible(true);
				jack.life = 0;
				jack.remove();
				resetCharacterAnimation();
				lifeBarStatus();
				gameOver = true;
			}
		}

		lifeBarStatus();

	}



	public boolean keyDown(int keyCode) {
		super.keyDown(keyCode);

		if (gameOver) {
			if (keyCode == Keys.C) {
				this.instrumental.dispose();
				JumpingJackGame.setActiveScreen(new Level2());
			}

			return false;
		}

		// mute and unmute the audio
		if (keyCode == Keys.S) {
			audioVolume = 1 - audioVolume;
			instrumental.setVolume(audioVolume);
		}

		return false;
	}
}
