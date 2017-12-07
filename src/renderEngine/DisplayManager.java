package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;

/**
 * A class used to manage the display for the game.
 */
public class DisplayManager
{
    /**
     * Width of display
     */
    private static final int WIDTH = 1280; // 1280 is best

    /**
     * Height of display
     */
    private static final int HEIGHT = 720; // 720 is best

    /**
     * X and Y window location
     */
    private static final int WINDOW_X = 0, WINDOW_Y = 0;

    /**
     * FPS game is run at
     */
    public static final int FPS_CAP = 60; // maybe increase to 120

    /**
     * Time at the end of the last frame
     */
    private static long lastFrameTime;

    /**
     * Time taken to render the previous time
     */
    private static float delta;

    /**
     * Creates and opens a display.
     */
    public static void createDisplay()
    {
        ContextAttribs attribs = new ContextAttribs(3, 3)
                .withForwardCompatible(true)
                .withProfileCore(true);
        try
        {
            Display.setLocation(WINDOW_X, WINDOW_Y); // Location of window
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat().withDepthBits(24), attribs);
            Display.setTitle("Our First Display!");
            GL11.glEnable(GL13.GL_MULTISAMPLE);
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
     * Closes the display.
     */
    public static void closeDisplay()
    {
        Display.destroy();
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
     * Returns the current time in milliseconds
     *
     * @return time in ms
     */
    private static long getCurrentTime()
    {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    public static Vector2f getNormalizedMouseCoordinates()
    {
        float normalizedX = -1.0f + 2.0f * Mouse.getX() / Display.getWidth();
        float normalizedY = -(1.0f - 2.0f * Mouse.getY() / Display.getHeight());

        return new Vector2f(normalizedX, normalizedY);
    }

}
