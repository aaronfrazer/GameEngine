package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * A virtual camera.
 * 
 * @author Aaron Frazer
 */
public class Camera
{
	/**
	 * Distance camera is from player
	 */
	private float distanceFromPlayer = 50;
	
	/**
	 * Angle of camera around player
	 */
	private float angleAroundPlayer = 0;
	
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
	 * Player the camera follows
	 */
	private Player player;
	
	/**
	 * Constructs a camera that follows a player.
	 * @param player - player object to be followed
	 */
	public Camera(Player player)
	{
		this.position = new Vector3f(0, 0, 0);
		this.player = player;
	}
	
	/**
	 * Constructs free-roaming camera.
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
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
		
//		if (Keyboard.isKeyDown(Keyboard.KEY_W))
//		{
//			position.z -= speed;
//		}
//		if (Keyboard.isKeyDown(Keyboard.KEY_S))
//		{
//			position.z += speed;
//		}
//		if (Keyboard.isKeyDown(Keyboard.KEY_D))
//		{
//			position.x += speed;
//		}
//		if (Keyboard.isKeyDown(Keyboard.KEY_A))
//		{
//			position.x -= speed;
//		}
//		
//		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
//		{
//			position.y += speed;
//		}
//		if (Keyboard.isKeyDown(Keyboard.KEY_E))
//		{
//			position.y -= speed;
//		}
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
	
	/**
	 * Calculates the X,Y,Z position of the camera in relation to the player.
	 * @param horizDistance - horizontal distance
	 * @param verticDistance - vertical distance
	 */
	private void calculateCameraPosition(float horizDistance, float verticDistance)
	{
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance;

	}
	
	/**
	 * Calculates the horizontal distance of the camera
	 * in relation to the player.
	 * @return horizontalDistance - horizontal camera distance
	 */
	private float calculateHorizontalDistance()
	{
		float horizontalDistance = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
		
		// TODO: May want to remove this piece of code
		if (horizontalDistance < 0)
			horizontalDistance = 0;
		//
		
		return horizontalDistance;
	}
	
	/**
	 * Calculates the vertical distance of the camera
	 * in relation to the player.
	 * @return verticalDistance - vertical camera distance
	 */
	private float calculateVerticalDistance()
	{
		float verticalDistance = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
		
		// TODO: May want to remove this piece of code
		if (verticalDistance < 0)
			verticalDistance = 0;
		//
		
		return verticalDistance;	
	}
	
	/**
	 * Calculates how far the camera is zoomed in relation to
	 * the player.
	 */
	private void calculateZoom()
	{
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	/**
	 * Calculates the pitch of the camera from the player.
	 */
	private void calculatePitch()
	{
		if (Mouse.isButtonDown(1))
		{
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
			
			// prevent pitch from looking away from player when hD/vD = 0.
			if (pitch < 0)
				pitch = 0;
			else if (pitch > 90)
				pitch = 90;
		}
	}
	
	/**
	 * Calculates the angle of the camera from the player.
	 */
	private void calculateAngleAroundPlayer()
	{
		if (Mouse.isButtonDown(0))
		{
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
