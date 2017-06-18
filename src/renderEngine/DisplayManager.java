package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

/**
 * Manages the display.
 *
 * @author Aaron Frazer
 */
public class DisplayManager
{
	/**
	 * Width of the display
	 */
	private static final int WIDTH = 640; // 1080 is best

	/**
	 * Height of the display
	 */
	private static final int HEIGHT = 360; // 720 is best

	/**
	 * X window location
	 */
	private static final int WINDOW_X = 0;

	/**
	 * Y window location
	 */
	private static final int WINDOW_Y = 0;

	/**
	 * FPS the game is run at.
	 */
	private static final int FPS_CAP = 120;

	/**
	 * Time at the end of the last frame
	 */
	private static long lastFrameTime;

	/**
	 * Time taken to render the previous frame
	 */
	private static float delta;

	/**
	 * Creates and opens the display.
	 */
	public static void createDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);

		try
		{
			Display.setLocation(WINDOW_X, WINDOW_Y);
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Our Game Display!");
			Display.setResizable(true); // Resizable window
		} catch (LWJGLException e)
		{
			e.printStackTrace();
		}

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}

	/**
	 * Updates the display.
	 */
	public static void updateDisplay()
	{
		if (Display.wasResized())
		{
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}

		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}

	/**
	 * Returns the delta value.
	 *
	 * @return delta value
	 */
	public static float getFrameTimeSeconds()
	{
		return delta;
	}

	/**
	 * Closes the display.
	 */
	public static void closeDisplay()
	{
		Display.destroy();
	}

	/**
	 * Returns the current time in milliseconds.
	 *
	 * @return time
	 */
	private static long getCurrentTime()
	{
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
