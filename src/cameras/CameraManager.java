package cameras;

import entities.Camera;
import org.lwjgl.input.Keyboard;
import toolbox.InputHelper;

import java.util.ArrayList;

/**
 * Responsible for managing all cameras in the game.
 * @author Aaron Frazer
 */
public class CameraManager
{
	/**
	 * Array list of cameras
	 */
	private ArrayList<Camera> cameraList = new ArrayList<>();
		
	/**
	 * Current camera in use
	 */
	private Camera currentCamera;

	/**
	 * Returns the current camera being used.
	 * @return current camera
	 */
	public Camera getCurrentCamera()
	{
		return currentCamera;
	}

	/**
	 * Sets the current camera in use.
	 * @param currentCamera current camera
	 */
	public void setCurrentCamera(Camera currentCamera)
	{
		this.currentCamera = currentCamera;
	}

	/**
	 * Sets the current camera in use by using the index of array list.
	 * @param i index
	 */
	private void setCurrentCamera(int i)
	{
		if ((i+1) > (cameraList.size()))
		{
			System.err.println("Camera out of bounds");
			return;
		} else if (cameraList.get(i) instanceof FirstPersonCamera)
		{
			System.out.println("Switched to 1st Person camera");
		} else if (cameraList.get(i) instanceof FreeRoamCamera)
		{
			System.out.println("Switched to Free Roam camera");
		} else if (cameraList.get(i) instanceof ThirdPersonCamera)
		{
			System.out.println("Switched to Third Person camera");
		} else if (cameraList.get(i) instanceof LockOnCamera)
		{
			System.out.println("Switched to Lock On camera");
		}

		this.currentCamera = cameraList.get(i);
	}
	
	/**
	 * Adds a camera to the list of cameras.
	 * @param camera camera to be added
	 */
	public void addCamera(Camera camera)
	{
		cameraList.add(camera);
	}

	/**
	 * Updates which camera is currently selected by polling key presses from the user.
	 * Called in the main game loop.
	 */
	public void update(CameraManager cm)
	{
		if (InputHelper.isKeyPressed(Keyboard.KEY_1))
			cm.setCurrentCamera(0);
		
		if (InputHelper.isKeyPressed(Keyboard.KEY_2))
			cm.setCurrentCamera(1);
		
		if (InputHelper.isKeyPressed(Keyboard.KEY_3))
			cm.setCurrentCamera(2);
		
		if (InputHelper.isKeyPressed(Keyboard.KEY_4))
			cm.setCurrentCamera(3);

		// TODO: Add birds eye camera
	}

}
