package cameras;

import entities.Camera;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import toolbox.InputHelper;

/**
 * A free-roam camera not attached to an entity.
 * @author Aaron Frazer
 */
public class FreeRoamCamera extends Camera
{
	/**
	 * Camera panning speed
	 */
	private float speed;
	
	/**
	 * Constructs free-roam camera.
	 * @param position camera position
	 */
	public FreeRoamCamera(Vector3f position)
	{
		this.position = position;
		this.speed = 0.5f;
	}
	
	/**
	 * Moves the camera around the world via key presses.
	 */
	@Override
	public void move()
	{
		super.move();

		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			position.z -= speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			position.z += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			position.x += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			position.x -= speed;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Q))
			position.y += speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_E))
			position.y -= speed;

		// Look up/down controlled by right mouse button
		if (InputHelper.isButtonDown(0))
		{
			float pitchChange = Mouse.getDY() * 0.1f;
			float angleChange = Mouse.getDX() * 0.3f;
			pitch -= pitchChange;
			yaw -= angleChange;
		}
	}

}
