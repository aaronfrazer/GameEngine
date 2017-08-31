package cameras;

import entities.Camera;
import entities.Player;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import toolbox.InputHelper;

/**
 * A first-person camera used by a player to view the world.
 * @author Aaron Frazer
 */
public class FirstPersonCamera extends Camera
{
	/**
	 * Sensitivity of camera
	 */
	 private float sensitivity = 1f;
	
	/**
	 * Player the camera is viewing through
	 */
	private Player player;
	
	/**
	 * Constructs a first-person camera.
	 * @param player player
	 */
	public FirstPersonCamera(Player player)
	{
		super();
		position = new Vector3f(0, 0, 0);
		this.player = player;
	}
	
	/**
	 * Moves the first-person camera around the player.
	 */
	@Override
	public void move()
	{
		super.move();

		calculateCameraPosition(player);
		yaw = (180 - player.getRotY());
		mouseMovements();
	}
	
	/**
	 * Sets the camera position to the X,Y,Z coordinates of the player.
	 */
	private void calculateCameraPosition(Player player)
	{
		position.x = player.getPosition().x;
		position.z = player.getPosition().z;
		position.y = player.getPosition().y + 7f;
	}
	
	/**
	 * Calculates the camera's yaw, pitch, and roll by taking mouse inputs.
	 */
	private void mouseMovements()
	{
		// Look up/down
		if (InputHelper.isButtonDown(1))
		{
			float pitchChange = Mouse.getDY() * sensitivity;
			pitch -= pitchChange;
			
			// Camera can only look up and down from player
			if (pitch < -90)
				pitch = -90;
			else if (pitch > 90)
				pitch = 90;
			
			System.out.println("Pitch is " + pitch);
		}
	}
}
