package cameras;

import entities.Camera;
import org.lwjgl.input.Keyboard;
import toolbox.InputHelper;

import java.util.ArrayList;

/**
 * Responsible for managing all cameras in the game.
 * 
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
	 * @return currentCamera - current camera
	 */
	public Camera getCurrentCamera()
	{
		return currentCamera;
	}

	/**
	 * Sets the current camera in use.
	 * @param currentCamera - current camera
	 */
	public void setCurrentCamera(Camera currentCamera)
	{
		this.currentCamera = currentCamera;
	}

	/**
	 * Sets the current camera in use by using the index of array list.
	 * @param i - index
	 */
	private void setCurrentCamera(int i)
	{
		this.currentCamera = cameraList.get(i);	
	}
	
	/**
	 * Adds a Camera to the list of cameras.
	 * @param camera - camera to be added
	 */
	public void addCamera(Camera camera)
	{
		cameraList.add(camera);
	}

	/**
	 * Updates which camera is currently selected by polling key presses from the user.
	 * 
	 * To be called in the main game loop.
	 */
	public void update(CameraManager cm)
	{
		if (InputHelper.isKeyPressed(Keyboard.KEY_1))
		{
			System.out.println("Switched to 1st person camera");
			cm.setCurrentCamera(0);
		}
		
		if (InputHelper.isKeyPressed(Keyboard.KEY_2))
		{
			System.out.println("Switched to free roam camera");
			cm.setCurrentCamera(1);
		}
		
		if (InputHelper.isKeyPressed(Keyboard.KEY_3))
		{
			System.out.println("Switched to 3rd person camera");
			cm.setCurrentCamera(2);
		}
		
		if (InputHelper.isKeyPressed(Keyboard.KEY_4))
		{
			System.out.println("Switched to lock on camera");
			cm.setCurrentCamera(3);
		}

		// TODO: Add birds eye camera
	}

}
