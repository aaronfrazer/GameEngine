package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
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
	private Vector3f position;
	
	/**
	 * Pitch is up and down (rotation around x-axis)
	 */
	private float pitch = 10;
	
	/**
	 * Yaw is left and right (rotation around y-axis)
	 */
	private float yaw;
	
	/**
	 * Roll is tilting (rotation around z-axis)
	 */
	private float roll;
	
	/**
	 * Camera panning speed
	 */
	private float speed;
	
	/**
	 * Constructs a camera.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param z - z coordinate
	 */
	public Camera(float x, float y, float z)
	{
		this.position = new Vector3f(x, y, z);
		this.speed = 0.5f;
	}
	
	/**
	 * Moves the camera on key press.
	 * W - forward
	 * S - backward
	 * D - right
	 * A - left
	 * Q - up
	 * E - down
	 */
	public void move()
	{	
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.z -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			position.z += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			position.x -= speed;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			position.y += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			position.y -= speed;
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
