package gaussianBlur;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;

/**
 * Renders a textured quad and uses a HorizontalBlurShader to create a horizontal blur effect
 * for the quad.
 * @author Aaron Frazer
 */
public class HorizontalBlur
{
    /**
     * Image renderer
     */
    private ImageRenderer renderer;

    /**
     * Horizontal blur shader program
     */
    private HorizontalBlurShader shader;

    /**
     * Creates a new FBO with the given dimensions.
     * @param targetFboWidth width of FBO
     * @param targetFboHeight height of FBO
     */
    public HorizontalBlur(int targetFboWidth, int targetFboHeight)
    {
        shader = new HorizontalBlurShader();
        shader.start();
        shader.loadTargetWidth(targetFboWidth);
        shader.stop();
        renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
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