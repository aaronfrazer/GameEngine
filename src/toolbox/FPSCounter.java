package toolbox;

import fontMeshCreator.GUIText;

import static org.lwjgl.Sys.getTime;

/**
 * A class that calculates FPS in the game.
 * @author Aaron Frazer
 */
public class FPSCounter
{
    private static int startTime;

    private static int frameTimes = 0;

    private static short frames = 0;

    /**
     * Starts counting FPS.
     */
    public static void startCounter()
    {
        startTime = (int) getTime();
    }

    /**
     * Stop counting FPS and display it to console.
     * @param fpsText GUI text to be updated
     */
    public static final void stopCounter(GUIText fpsText)
    {
        int endTime = (int) getTime();
        frameTimes = frameTimes + endTime - startTime;
        ++frames;

        if (frameTimes >= 1000)
        {
            fpsText.update(Long.toString(frames));
//            System.out.println("FPS: " + Long.toString(frames));
            frames = 0;
            frameTimes = 0;
        }
    }
}