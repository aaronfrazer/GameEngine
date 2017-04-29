package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * A virtual camera
 * 
 * @author Aaron Frazer
 */
public class Camera
{
	/**
	 * Position of the camera
	 */
	private Vector3f position = new Vector3f(0, 0, 0);
	
	/**
	 * Pitch is up and down (rotation around x-axis)
	 */
	private float pitch;
	
	/**
	 * Yaw is left and right (rotation around y-axis)
	 */
	private float yaw;
	
	/**
	 * Roll is tilting (rotation around z-axis)
	 */
	private float roll;
	
	/**
	 * Constructor.
	 */
	public Camera()
	{
		
	}
	
	/**
	 * Moves the camera on key press.
	 * W - forward
	 * D - right
	 * A - left
	 */
	public void move()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.z -= 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			position.z += 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x += 0.02f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			position.x -= 0.02f;
		}
	}

	/**
	 * Returns the camera's position.
	 * @return position - camera's position vector
	 */
	public Vector3f getPosition()
	{
		return position;
	}

	/**
	 * Returns the camera's pitch.
	 * @return pitch - camera's pitch
	 */
	public float getPitch()
	{
		return pitch;
	}

	/**
	 * Returns the camera's yaw.
	 * @return yaw - camera's yaw
	 */
	public float getYaw()
	{
		return yaw;
	}

	/**
	 * Returns the camera's roll.
	 * @return roll - camera's roll
	 */
	public float getRoll()
	{
		return roll;
	}
}
