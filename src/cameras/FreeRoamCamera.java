package cameras;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;

public class FreeRoamCamera extends Camera
{
	/**
	 * Camera panning speed
	 */
	private float speed;
	
	/**
	 * Constructs free-roaming camera.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 */
	public FreeRoamCamera(Vector3f position)
	{
		this.position = position;
		this.speed = 0.5f;
	}
	
	/**
	 * Moves the camera around the world via keypresses.
	 */
	@Override
	public void move()
	{
		super.move();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			position.z -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			position.z += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			position.x += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			position.x -= speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_K))
		{
			position.y += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_L))
		{
			position.y -= speed;
		}
	}

}
