package bloom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;

/**
 * A post-processing filter responsible for extracting bright parts of an image.
 * @author Aaron Frazer
 */
public class BrightFilter
{
    /**
     * Image renderer
     */
    private ImageRenderer renderer;

    /**
     * Bright filter shader program
     */
    private BrightFilterShader shader;

    /**
     * Creates a new FBO with the given dimensions.
     * @param width width of FBO
     * @param height height of FBO
     */
    public BrightFilter(int width, int height)
    {
        shader = new BrightFilterShader();
        renderer = new ImageRenderer(width, height);
    }

    /**
     * Renders a textured quad.
     * @param texture texture ID
     */
    public void render(int texture)
    {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.stop();
    }

    /**
     * Returns the output texture of the ImageRenderer's FBO.
     * @return output texture
     */
    public int getOutputTexture()
    {
        return renderer.getOutputTexture();
    }

    /**
     * Cleans up resources in the renderer and shader.
     */
    public void cleanUp()
    {
        renderer.cleanUp();
        shader.cleanUp();
    }
}