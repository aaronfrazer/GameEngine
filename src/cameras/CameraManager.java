package cameras;

import java.util.ArrayList;

import entities.Camera;

/**
 * Responsible for managing all cameras in the game.
 * 
 * @author Aaron Frazer
 */
public class CameraManager
{
	/**
	 * Arraylist of cameras
	 */
	private ArrayList<Camera> cameraList = new ArrayList<Camera>();
	
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
	 * Adds a Camera to the list of cameras.
	 * @param camera - camera to be added
	 */
	public void addCamera(Camera camera)
	{
		cameraList.add(camera);
	}

	/**
	 * Sets the camera to the next one in the list.
	 */
	public void nextCamera()
	{
		
	}
	
	
	

}
