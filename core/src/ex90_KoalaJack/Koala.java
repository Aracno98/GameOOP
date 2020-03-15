package ex90_KoalaJack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Koala extends BaseActor {
	private Animation<TextureRegion> stand;
	private Animation<TextureRegion> walk;
	private Animation<TextureRegion> jump;
	private Animation<TextureRegion> fire;
	private Animation<TextureRegion> jfall;
	private Animation<TextureRegion> dead;
	private Animation<TextureRegion> run_shoot;

	private float walkAcceleration;
	private float walkDeceleration;
	private float maxHorizontalSpeed;
	private float gravity;
	private float maxVerticalSpeed;

	private float fire_animtime; // used for fire animation time
	private float fire_time;
	private boolean d;// used for set animation of dead

	protected float life;

	private float jumpSpeed;
	public BaseActor belowSensor;

	public Koala(float x, float y, Stage s) {
		super(x, y, s);
		String[] standFileNames = { "robot/Idle (1).png", "robot/Idle (2).png", "robot/Idle (3).png",
				"robot/Idle (4).png", "robot/Idle (5).png", "robot/Idle (6).png", "robot/Idle (7).png",
				"robot/Idle (8).png", "robot/Idle (9).png", "robot/Idle (10).png" };
		stand = loadAnimationFromFiles(standFileNames, 0.2f, true);

		String[] walkFileNames = { "robot/Run (1).png", "robot/Run (2).png", "robot/Run (3).png", "robot/Run (4).png",
				"robot/Run (5).png", "robot/Run (6).png", "robot/Run (7).png", "robot/Run (8).png" };
		walk = loadAnimationFromFiles(walkFileNames, 0.2f, true);

		maxHorizontalSpeed = 300;
		walkAcceleration = 400;
		walkDeceleration = 1000;
		gravity = 700;
		maxVerticalSpeed = 1000;

		setBoundaryPolygon(15);

		String[] fireFileNames = { "robot/Shoot (1).png", "robot/Shoot (2).png", "robot/Shoot (3).png",
		"robot/Shoot (4).png" };

		fire = loadAnimationFromFiles(fireFileNames, 0.5f, true);

		String[] jumpFileNames = { "robot/Jump (1).png", "robot/Jump (2).png", "robot/Jump (3).png",
				"robot/Jump (4).png", };
		jump = loadAnimationFromFiles(jumpFileNames, 0.18f, true);

		String[] jfallFileNames = { "robot/Jump (8).png", "robot/Jump (9).png" };
		jfall = loadAnimationFromFiles(jfallFileNames, 0.29f, true);
		jumpSpeed = 450;
		
		String[] deadFileNames = { "robot/Dead (1).png", "robot/Dead (2).png", "robot/Dead (3).png",
				"robot/Dead (4).png", "robot/Dead (5).png", "robot/Dead (6).png", "robot/Dead (7).png",
				"robot/Dead (8).png", "robot/Dead (9).png", "robot/Dead (10).png" };
		dead = loadAnimationFromFiles(deadFileNames, 0.75f, false);

		String[] runshootFileNames = { "robot/run_shoot/RunShoot (1).png", "robot/run_shoot/RunShoot (2).png",
				"robot/run_shoot/RunShoot (3).png", "robot/run_shoot/RunShoot (4).png",
				"robot/run_shoot/RunShoot (5).png", "robot/run_shoot/RunShoot (6).png",
				"robot/run_shoot/RunShoot (7).png", "robot/run_shoot/RunShoot (8).png" };
		run_shoot = loadAnimationFromFiles(runshootFileNames, 0.25f, true);
	
		// set up the below sensor
		belowSensor = new BaseActor(0, 0, s);
		belowSensor.loadTexture("JumpingJack/white.png");
		belowSensor.setSize(this.getWidth() - 8, 8);
		belowSensor.setBoundaryRectangle();
		belowSensor.setVisible(false);

		this.fire_animtime = 0f;
		this.fire_time = 0f;
		this.d = false;
		this.life = 100f;
	}

	public void act(float dt) {
		super.act(dt);

		// get keyboard input

		if (Gdx.input.isKeyPressed(Keys.LEFT) && d == false)
			accelerationVec.add(-walkAcceleration, 0);

		if (Gdx.input.isKeyPressed(Keys.RIGHT) && d == false)
			accelerationVec.add(walkAcceleration, 0);

		// decelerate when not accelerating
		if (!Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT)) {
			float decelerationAmount = walkDeceleration * dt;

			float walkDirection;

			if (velocityVec.x > 0)
				walkDirection = 1;
			else
				walkDirection = -1;

			float walkSpeed = Math.abs(velocityVec.x);

			walkSpeed -= decelerationAmount;

			if (walkSpeed < 0)
				walkSpeed = 0;

			velocityVec.x = walkSpeed * walkDirection;

		}

		if (fire_animtime > 0)
			fire_animtime = fire_animtime - dt;
		else
			fire_animtime = 0;

		fire_time -= dt;

		// apply gravity
		accelerationVec.add(0, -gravity);

		velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);

		velocityVec.x = MathUtils.clamp(velocityVec.x, -maxHorizontalSpeed, maxHorizontalSpeed);

		moveBy(velocityVec.x * dt, velocityVec.y * dt);

		// reset acceleration
		accelerationVec.set(0, 0);

		// move the below sensor below the koala
		belowSensor.setPosition(getX() + 4, getY() - 8);

		// manage animations
		if (this.isOnSolid()) {
			if (d)
				setAnimation(dead);
			else {
				if (fire_animtime > 0 && velocityVec.x == 0)
					setAnimation(fire);
				// if(timer > 0 && velocityVec.x != 0)
				// setAnimation(run_shoot);
				else {
					belowSensor.setColor(Color.GREEN);
					if (velocityVec.x == 0)
						setAnimation(stand);
					else {
						if (fire_animtime > 0)
							setAnimation(run_shoot);
						else
							setAnimation(walk);
					}
				}
			}
		} else {

			belowSensor.setColor(Color.RED);
			if (this.isFalling())
				setAnimation(jfall);
			else
				setAnimation(jump);

		}

		if (velocityVec.x > 0) // face right
			setScaleX(1);

		if (velocityVec.x < 0) // face left
			setScaleX(-1);

		alignCamera();
		boundToWorld();
	}

	public boolean belowOverlaps(BaseActor actor) {
		return belowSensor.overlaps(actor);
	}

	public boolean isOnSolid() {
		for (BaseActor actor : BaseActor.getList(getStage(), Solid.class.getName())) {
			Solid solid = (Solid) actor;
			if (belowOverlaps(solid) && solid.isEnabled())
				return true;
		}

		return false;
	}

	public void jump() {
		if (d == false)
			velocityVec.y = jumpSpeed;
	}

	public boolean isFalling() {
		return (velocityVec.y < 0);
	}

	public void spring() {
		velocityVec.y = 1.5f * jumpSpeed;
	}

	public boolean isJumping() {
		return (velocityVec.y > 0);
	}

	public void shoot() {
		if (getStage() == null)
			return;
		if (d == false && fire_time <= 0) {
			fire_animtime = 0.8f;
			fire_time = 0.3f;
			Laser laser = new Laser(0, 0, this.getStage());
			laser.centerAtActor(this);
			laser.setScaleX(this.getScaleX());
			laser.setPosition(laser.getX() + laser.getScaleX() * 35, laser.getY());
			laser.velocityVec.x = laser.getSpeed() * laser.getScaleX();
		}
	}

	public void dead() {
		velocityVec.x = 0;
		d = true;
	}

	public boolean isDead() {
		return d;
	}

}