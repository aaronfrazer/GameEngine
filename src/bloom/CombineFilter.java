package bloom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;

/**
 * A post-processing filter responsible for combining image and blurred highlight texture.
 * @author Aaron Frazer
 */
public class CombineFilter
{
    /**
     * Image renderer
     */
    private ImageRenderer renderer;

    /**
     * Combine shader program
     */
    private CombineShader shader;

    public CombineFilter()
    {
        shader = new CombineShader();
        shader.start();
        shader.connectTextureUnits();
        shader.stop();
        renderer = new ImageRenderer();
    }

    /**
     * Renders a textured quad with the original image and highlight texture.
     * @param colourTexture image
     * @param highlightTexture highlighted texture
     */
    public void render(int colourTexture, int highlightTexture)
    {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, highlightTexture);
        renderer.renderQuad();
        shader.stop();
    }

    /**
     * Cleans up resources in the renderer and shader.
     */
    public void cleanUp()
    {
        renderer.cleanUp();
        shader.cleanUp();
    }

    /**
     * Returns the output texture of the ImageRenderer's FBO.
     * @return output texture
     */
    public int getOutputTexture()
    {
        return renderer.getOutputTexture();
    }
}