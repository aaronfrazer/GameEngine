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
    public Fbo fbo;

    /**
     * Creates an image renderer to render an image to an FBO.
     * @param width width of FBO
     * @param height height of FBO
     */
    public ImageRenderer(int width, int height)
    {
        this.fbo = new Fbo(width, height, Fbo.NONE);
    }

    /**
     * Creates an image renderer to the screen.
     */
    public ImageRenderer()
    {
    }

    /**
     * Renders a quad to either the screen or to another FBO.
     */
    public void renderQuad()
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
    public int getOutputTexture()
    {
        return fbo.getColorTexture();
    }

    /**
     * Cleans up resources of this ImageRenderer's FBO.
     */
    public void cleanUp()
    {
        if (fbo != null)
        {
            fbo.cleanUp();
        }
    }

}