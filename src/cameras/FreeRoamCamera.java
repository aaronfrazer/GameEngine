package cameras;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import toolbox.InputHelper;

/**
 * A free-roam camera that is not attached to an entity.
 * 
 * @author Aaron Frazer
 */
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_I))
		{
			position.z -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_K))
		{
			position.z += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_L))
		{
			position.x += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_J))
		{
			position.x -= speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_U))
		{
			position.y += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_O))
		{
			position.y -= speed;
		}
		
		// Look up/down
		if (InputHelper.isButtonDown(1))
		{
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			
			// prevent pitch from looking away from player when hD/vD = 0.
			if (pitch < 0)
				pitch = 0;
			else if (pitch > 90)
				pitch = 90;
		}
		
		// Look left/right
		if (InputHelper.isButtonDown(0))
		{
			float angleChange = Mouse.getDX() * 0.3f;
			yaw -= angleChange;
		}
	}

}
