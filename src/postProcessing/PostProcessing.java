package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import renderEngine.Loader;

/**
 * Represents post-processing in the game.  Post-processing is applied after an image
 * has been rendered to the FBO and post-processing effects are applied to the FBO.
 * @author Aaron Frazer
 */
public class PostProcessing
{
    private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
    private static RawModel quad;

    /**
     * Contrast changer
     */
    private static ContrastChanger contrastChanger;

    /**
     * Creates a 2D quad that fills up the display.
     * @param loader loader
     */
    public static void init(Loader loader)
    {
        quad = loader.loadToVAO(POSITIONS, 2);
        contrastChanger = new ContrastChanger();
    }

    /**
     * Applies post-processing effect between start() and end() calls.
     * @param colourTexture color texture ID
     */
    public static void doPostProcessing(int colourTexture)
    {
        start();
        contrastChanger.render(colourTexture);
        end();
    }

    /**
     * Cleans up post-processing resources.
     */
    public static void cleanUp()
    {
        contrastChanger.cleanUp();
    }

    /**
     * Starts the post-processing pipeline by binding the quads' VAO.
     */
    private static void start()
    {
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Ends the post-processing pipeline by unbiding the quads' VAO.
     */
    private static void end()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }


}
