package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * Manages the display.
 * 
 * @author Aaron Frazer
 */
public class DisplayManager
{
	/**
	 * The width of the display.
	 */
	private static final int WIDTH = 1280;
	
	/**
	 * The height of the display.
	 */
	private static final int HEIGHT = 720;
	
	/**
	 * The FPS we want the game to run at.
	 */
	private static final int FPS_CAP = 120;
	
	/**
	 * Creates and opens the display.
	 */
	public static void createDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3, 2);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
		
		try
		{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Our Game Display!");
		} catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	/**
	 * Updates the display.
	 */
	public static void updateDisplay()
	{
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	/**
	 * Closes the display.
	 */
	public static void closeDisplay()
	{
		Display.destroy();
	}
}
