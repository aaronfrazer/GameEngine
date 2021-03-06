package postProcessing;

import bloom.BrightFilter;
import bloom.CombineFilter;
import gaussianBlur.HorizontalBlur;
import gaussianBlur.VerticalBlur;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import renderEngine.Loader;
import toolbox.GameSettings;

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
     * Contrast changer stage
     */
    private static ContrastChanger contrastChanger;

    /**
     * Bright filter to apply bloom effect
     */
    private static BrightFilter brightFilter;

    /**
     * Horizontal blur stage
     */
    private static HorizontalBlur hBlur;

    /**
     * Vertical blur stage
     */
    private static VerticalBlur vBlur;

    /**
     * Second horizontal blur stage
     */
    private static HorizontalBlur hBlur2;

    /**
     * Second vertical blur stage
     */
    private static VerticalBlur vBlur2;

    /**
     * Combine filter
     */
    private static CombineFilter combineFilter;

    /**
     * Creates a 2D quad that fills up the display.
     * @param loader loader
     */
    public static void init(Loader loader)
    {
        quad = loader.loadToVAO(POSITIONS, 2);
        contrastChanger = new ContrastChanger();
        brightFilter = new BrightFilter(Display.getWidth()/2, Display.getHeight()/2);
        hBlur = new HorizontalBlur(Display.getWidth() / 8, Display.getHeight() / 8);
        vBlur = new VerticalBlur(Display.getWidth() / 8, Display.getHeight() / 8);
        hBlur2 = new HorizontalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
        vBlur2 = new VerticalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
        combineFilter = new CombineFilter();
    }

    /**
     * Applies post-processing effect between start() and end() calls.
     * @param colourTexture color texture ID
*      @param brightTexture bright texture ID
     */
    public static void doPostProcessing(int colourTexture, int brightTexture)
    {
        start();

        if (GameSettings.GAUSSIAN_BLUR)
        {
            hBlur2.render(colourTexture);
            vBlur2.render(hBlur2.getOutputTexture());
            hBlur.render(vBlur2.getOutputTexture());
            vBlur.render(hBlur.getOutputTexture());
            contrastChanger.render(vBlur.getOutputTexture());
        } else {
            if (GameSettings.BLOOM_EFFECT)
            { // bloom effect
                hBlur2.render(brightTexture);
                vBlur2.render(hBlur2.getOutputTexture());
                hBlur.render(vBlur2.getOutputTexture());
                vBlur.render(hBlur.getOutputTexture());
                contrastChanger.render(vBlur.getOutputTexture());
                combineFilter.render(colourTexture, vBlur.getOutputTexture());
            } else
            { // no effects
                contrastChanger.render(colourTexture);
            }
        }

        end();
    }

    /**
     * Cleans up post-processing resources.
     */
    public static void cleanUp()
    {
        contrastChanger.cleanUp();
        brightFilter.cleanUp();
        hBlur.cleanUp();
        vBlur.cleanUp();
        hBlur2.cleanUp();
        vBlur2.cleanUp();
        combineFilter.cleanUp();
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
