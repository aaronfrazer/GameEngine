
package postProcessing;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

/**
 * A class that represents an Frame Buffer Object.
 * This class is used for post-processing (tutorial 43).
 * TODO: Merge all three FBOs into a single class so that it can be used by all FBO-related activities
 * 1. water
 * 2. shadows
 * 3. post-processing
 * @author Aaron Frazer
 */
public class Fbo
{
    /**
     * Options for dept buffer attachments
     */
    public static final int NONE = 0;
    public static final int DEPTH_TEXTURE = 1;
    public static final int DEPTH_RENDER_BUFFER = 2;

    /**
     * Width/height of this FBO
     */
    private final int width;
    private final int height;

    private int frameBuffer;

    /**
     * Should this frame buffer be multisampled?
     */
    private boolean multisampleAndMultiTarget = false;

    private int colorTexture;
    private int depthTexture;

    private int depthBuffer;
    private int colorBuffer;
    private int colorBuffer2;

    /**
     * Creates an FBO with a specified width, height, and a depth buffer attachment.
     * @param width           width of FBO
     * @param height          height of FBO
     * @param depthBufferType int indicating type of depth buffer attachment that this FBO should use
     */
    public Fbo(int width, int height, int depthBufferType)
    {
        this.width = width;
        this.height = height;
        initializeFrameBuffer(depthBufferType);
    }

    /**
     * Creates an FBO with two multisampled color buffers.
     * @param width width of FBO
     * @param height height of FBO
     */
    public Fbo(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.multisampleAndMultiTarget = true;
        initializeFrameBuffer(DEPTH_RENDER_BUFFER);
    }

    /**
     * Deletes the frame buffer and its attachments when the game closes.
     */
    public void cleanUp()
    {
        GL30.glDeleteFramebuffers(frameBuffer);
        GL11.glDeleteTextures(colorTexture);
        GL11.glDeleteTextures(depthTexture);
        GL30.glDeleteRenderbuffers(depthBuffer);
        GL30.glDeleteRenderbuffers(colorBuffer);
        GL30.glDeleteRenderbuffers(colorBuffer2);
    }

    /**
     * Binds the frame buffer and sets it as the current render target.
     * Anything rendered after this will be rendered to this FBO and not to the screen.
     */
    public void bindFrameBuffer()
    {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Unbinds the frame buffer, setting the default frame buffer as the current
     * render target. Anything rendered after this will be rendered to the
     * screen and not this FBO.
     */
    public void unbindFrameBuffer()
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    /**
     * Binds the current FBO to be read from (not used in tutorial 43).
     */
    public void bindToRead()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
        GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
    }

    /**
     * Returns the ID of the texture containing the color buffer of this FBO.
     * @return texture ID
     */
    public int getColorTexture()
    {
        return colorTexture;
    }

    /**
     * Returns the texture containing this FBOs depth buffer.
     * @return depth buffer texture
     */
    public int getDepthTexture()
    {
        return depthTexture;
    }

    /**
     * Resolves an FBO into another FBO so that there is an anti-aliased image of the scene.
     * @param readBuffer attachment that will be read from
     * @param outputFbo output FBO that will be resolved into
     */
    public void resolveToFbo(int readBuffer, Fbo outputFbo)
    {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, outputFbo.frameBuffer);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
        GL11.glReadBuffer(readBuffer);
        GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, outputFbo.width, outputFbo.height,GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
        this.unbindFrameBuffer();
    }

    /**
     * Resolved this FBO to the screen.
     */
    public void resolveToScreen()
    {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
        GL11.glDrawBuffer(GL11.GL_BACK);
        GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, Display.getHeight(), Display.getWidth(),GL11.GL_COLOR_BUFFER_BIT, GL11.GL_NEAREST);
        this.unbindFrameBuffer();
    }

    /**
     * Creates an FBO along with a color buffer texture attachment and possibly a depth buffer.
     * @param type type of depth buffer attachment to be attached to this FBO
     */
    private void initializeFrameBuffer(int type)
    {
        createFrameBuffer();
        if (multisampleAndMultiTarget)
        {
            colorBuffer = createMultisampleColorAttachment(GL30.GL_COLOR_ATTACHMENT0);
            colorBuffer2 = createMultisampleColorAttachment(GL30.GL_COLOR_ATTACHMENT1);
        } else
        {
            createTextureAttachment();
        }
        if (type == DEPTH_RENDER_BUFFER)
        {
            createDepthBufferAttachment();
        } else if (type == DEPTH_TEXTURE)
        {
            createDepthTextureAttachment();
        }
        unbindFrameBuffer();
    }

    /**
     * Creates a new frame buffer object and sets the buffer to which drawing
     * will occur - colour attachment 0. This is the attachment where the colour
     * buffer texture is.
     */
    private void createFrameBuffer()
    {
        frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        determineDrawBuffers();
    }

    /**
     * Determines which draw buffers should be rendered.
     */
    private void determineDrawBuffers()
    {
        IntBuffer drawBuffers = BufferUtils.createIntBuffer(2);
        drawBuffers.put(GL30.GL_COLOR_ATTACHMENT0);
        if (this.multisampleAndMultiTarget)
        {
            drawBuffers.put(GL30.GL_COLOR_ATTACHMENT1);
        }
        drawBuffers.flip();
        GL20.glDrawBuffers(drawBuffers);
    }

    /**
     * Creates a texture and sets it as the colour buffer attachment for this FBO.
     */
    private void createTextureAttachment()
    {
        colorTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorTexture,
                0);
    }

    /**
     * Adds a depth buffer to the FBO in the form of a texture, which can later be sampled.
     */
    private void createDepthTextureAttachment()
    {
        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
    }

    /**
     * Adds a color buffer attachment to the FBO in the form of a render buffer.
     * This can't be used for samping in the shaders.
     */
    private int createMultisampleColorAttachment(int attachment)
    {
        int colorBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colorBuffer);
        GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, 4, GL11.GL_RGBA8, width, height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, attachment, GL30.GL_RENDERBUFFER, colorBuffer);

        return colorBuffer;
    }

    /**
     * Adds a depth buffer to the FBO in the form of a render buffer.
     * This can't be used for sampling in the shaders.
     */
    private void createDepthBufferAttachment()
    {
        depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        if (!multisampleAndMultiTarget)
        {
            GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width, height);
        } else
        {
            GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, 4, GL14.GL_DEPTH_COMPONENT24, width, height);
        }
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);
    }

}
