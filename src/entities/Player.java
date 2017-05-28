package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrain.Terrain;
import toolbox.InputHelper;

/**
 * An entity that represents a player.
 * 
 * @author Aaron Frazer
 */
public class Player extends Entity
{
	/**
	 * Player run speed measured in units per second
	 */
	private static final float RUN_SPEED = 20;
	
	/**
	 * Player sprint speed measured in units per second
	 */
	private static final float SPRINT_SPEED = 100;
	
	/**
	 * Player turn speed measured in degrees per second
	 */
	private static final float TURN_SPEED = 160;
	
	/**
	 * Gravity affecting the player
	 */
	private static float GRAVITY = -50;
	
	/**
	 * Player jump power
	 */
	private static final float JUMP_POWER = 30;
	
	/**
	 * Current speed of player
	 */
	private float currentSpeed = 0;

	/**
	 * Current turn speed of player
	 */
	private float currentTurnSpeed;
	
	/**
	 * Upward speed of player
	 */
	private float upwardsSpeed = 0;
	
	/**
	 * Is the player in the air?
	 */
	private boolean isInAir = false;
	
	/**
	 * Constructs  a player.
	 * @param model - textured model
	 * @param position - vector3f position
	 * @param rotX - x rotation
	 * @param rotY - y rotation
	 * @param rotZ - z rotation
	 * @param scale - scale
	 */
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale)
	{
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	/**
	 * Moves this player on a terrain.
	 * To be called inside the main game loop.
	 * @param terrain - terrain player is standing on
	 */
	public void move(Terrain terrain)
	{
		checkInputs();
		
		// Turning
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		// Moving
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		
		// Falling
		upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
		// Colision detection for falling
		if (super.getPosition().y < terrainHeight)
		{
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
		
		if (super.getPosition().x < terrain.getX())
		{
			super.getPosition().x = terrain.getX();
		}

		if (super.getPosition().z < terrain.getZ())
		{
			super.getPosition().z = terrain.getZ();
		}

		if (super.getPosition().x > Terrain.SIZE)
		{
			super.getPosition().x = Terrain.SIZE - 0.001f;
		}

		if (super.getPosition().z > Terrain.SIZE)
		{
			super.getPosition().z = Terrain.SIZE - 0.001f;
		}
	}

	/**
	 * Makes the player jump.
	 */
	private void jump()
	{
		if (!isInAir)
		{
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	/**
	 * Checks keyboard inputs to set speed and turn speed.
	 */
	private void checkInputs()
	{
		if (InputHelper.isKeyDown(Keyboard.KEY_W))
		{
			if (InputHelper.isKeyDown(Keyboard.KEY_LSHIFT))
				this.currentSpeed = SPRINT_SPEED;
			else
				this.currentSpeed = RUN_SPEED;
			
		} else if (InputHelper.isKeyDown(Keyboard.KEY_S))
		{
			if (InputHelper.isKeyDown(Keyboard.KEY_LSHIFT))
				this.currentSpeed = -SPRINT_SPEED / 2;
			else
				this.currentSpeed = -RUN_SPEED;
			
		} else
		{
			this.currentSpeed = 0;
		}
		
		if (InputHelper.isKeyDown(Keyboard.KEY_D))
		{
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (InputHelper.isKeyDown(Keyboard.KEY_A))
		{
			this.currentTurnSpeed = TURN_SPEED;
		} else
		{
			this.currentTurnSpeed = 0;
		}
	
		if (InputHelper.isKeyDown(Keyboard.KEY_SPACE))
		{
			jump();
		}		
	}
}
