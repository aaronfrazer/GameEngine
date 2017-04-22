package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;

/**
 * Main game loop to run our game.
 * 
 * @author Aaron Frazer
 */
public class MainGameLoop
{

	public static void main(String[] args)
	{
		DisplayManager.createDisplay();
		
		while (!Display.isCloseRequested()) { // loops until exit button pushed
			// game logic
			// rendering
			DisplayManager.updateDisplay();
		}
		
		DisplayManager.closeDisplay();
	}

}
