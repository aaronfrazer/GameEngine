package postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import toolbox.GameSettings;

/**
 * Renders a textured quad and uses ContrastShader to change color in the quad.
 * @author Aaron Frazer
 */
public class ContrastChanger
{
    /**
     * Image renderer
     */
    private ImageRenderer renderer;

    /**
     * Contrast shader program
     */
    private ContrastShader shader;

    /**
     * Creates a contrast changer with a ContrastShader and ImageRenderer.
     */
    public ContrastChanger()
    {
        shader = new ContrastShader();
        renderer = new ImageRenderer();
    }

    /**
     * Renders a textured quad.
     * @param texture texture ID
     */
    public void render(int texture)
    {
        shader.start();
        shader.loadContrast(GameSettings.CONTRAST);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
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
}