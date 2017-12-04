package postProcessing;

import org.lwjgl.opengl.GL11;

/**
 * Renders an image to the screen or to an FBO.
 * @author Aaron Frazer
 */
public class ImageRenderer
{
    /**
     * Frame Buffer Object
     */
    private Fbo fbo;

    /**
     * Creates an image renderer to render an image to an FBO.
     * @param width width of FBO
     * @param height height of FBO
     */
    protected ImageRenderer(int width, int height)
    {
        this.fbo = new Fbo(width, height, Fbo.NONE);
    }

    /**
     * Creates an image renderer to the screen.
     */
    protected ImageRenderer()
    {
    }

    /**
     * Renders a quad to either the screen or to another FBO.
     */
    protected void renderQuad()
    {
        if (fbo != null)
        {
            fbo.bindFrameBuffer();
        }
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        if (fbo != null)
        {
            fbo.unbindFrameBuffer();
        }
    }

    /**
     * Returns the output texture of this ImageRenderer's FBO.
     * @return color texture of FBO
     */
    protected int getOutputTexture()
    {
        return fbo.getColourTexture();
    }

    /**
     * Cleans up resources of this ImageRenderer's FBO.
     */
    protected void cleanUp()
    {
        if (fbo != null)
        {
            fbo.cleanUp();
        }
    }

}