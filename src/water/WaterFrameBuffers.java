package water;

import org.lwjgl.opengl.*;
import java.nio.ByteBuffer;

/**
 * Frame Buffer Objects for water frame buffer.
 * @author Aaron Frazer
 */
public class WaterFrameBuffers
{

	/**
	 * Reflection resolution of FBO
	 */
	protected static final int REFLECTION_WIDTH = 320;
	private static final int REFLECTION_HEIGHT = 180;

	/**
	 * Refraction resolution of FBO
	 */
	protected static final int REFRACTION_WIDTH = 1280;
	private static final int REFRACTION_HEIGHT = 720;

	private int reflectionFrameBuffer;
	private int reflectionTexture;
	private int reflectionDepthBuffer;

	private int refractionFrameBuffer;
	private int refractionTexture;
	private int refractionDepthTexture;

	/**
	 * Constructs two Frame Buffer Objects:
	 * 1. Color buffer and depth buffer texture attachment
	 * 2. Color buffer texture attachment and a depth buffer, render buffer attachment
	 * Called when loading the game.
	 */
	public WaterFrameBuffers()
	{
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}

	/**
	 * Deletes all frame buffers, attachments, and render buffer attachments.
	 * Called when closing the game.
	 */
	public void cleanUp()
	{
		GL30.glDeleteFramebuffers(reflectionFrameBuffer);
		GL11.glDeleteTextures(reflectionTexture);
		GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
		GL30.glDeleteFramebuffers(refractionFrameBuffer);
		GL11.glDeleteTextures(refractionTexture);
		GL11.glDeleteTextures(refractionDepthTexture);
	}

	/**
	 * Binds FBO to reflection frame buffer.
	 * NOTE: This method is called before rendering to this FBO.
	 */
	public void bindReflectionFrameBuffer()
	{
		bindFrameBuffer(reflectionFrameBuffer, REFLECTION_WIDTH, REFLECTION_HEIGHT);
	}

	/**
	 * Binds FBO to refraction frame buffer.
	 * NOTE: This method is called before rendering to this FBO.
	 */
	public void bindRefractionFrameBuffer()
	{
		bindFrameBuffer(refractionFrameBuffer, REFRACTION_WIDTH, REFRACTION_HEIGHT);
	}

	/**
	 * Switches to OpenGL's default frame buffer.
	 */
	public void unbindCurrentFrameBuffer()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	/**
	 * Returns this FBO's reflection texture.
	 * @return reflection texture
	 */
	public int getReflectionTexture()
	{
		return reflectionTexture;
	}

	/**
	 * Returns this FBO's refraction texture.
	 * @return refraction texture
	 */
	public int getRefractionTexture()
	{
		return refractionTexture;
	}

	/**
	 * Returns this FBO's refraction depth texture.
	 * @return refraction depth texture
	 */
	public int getRefractionDepthTexture()
	{
		return refractionDepthTexture;
	}

	/**
	 * Initializes the reflection frame buffer.
	 */
	private void initialiseReflectionFrameBuffer()
	{
		reflectionFrameBuffer = createFrameBuffer();
		reflectionTexture = createTextureAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}

	/**
	 * Initializes the refraction frame buffer.
	 */
	private void initialiseRefractionFrameBuffer()
	{
		refractionFrameBuffer = createFrameBuffer();
		refractionTexture = createTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}

	/**
	 * Binds a texture to Frame Buffer Object.
	 * @param frameBuffer frame buffer
	 * @param width width of FBO
	 * @param height height of FBO
	 */
	private void bindFrameBuffer(int frameBuffer, int width, int height)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); // make sure the texture isn't bound
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Creates a Frame Buffer Object and returns it.
	 * @return frame buffer object
	 */
	private int createFrameBuffer()
	{
		int frameBuffer = GL30.glGenFramebuffers(); // generate name for frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer); // create the framebuffer
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0); // render to color attachment 0

		return frameBuffer;
	}

	/**
	 * Adds a color buffer texture attachment to the currently bound
	 * Frame Buffer Object.
	 * @param width width of texture
	 * @param height height of texture
	 * @return texture
	 */
	private int createTextureAttachment(int width, int height)
	{
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0); // add texture attachment to currently bound FBO

		return texture;
	}

	/**
	 * Adds a depth buffer texture attachment to the currently bound
	 * Frame Buffer Object.
	 * @param width width of texture
	 * @param height height of texture
	 * @return texture
	 */
	private int createDepthTextureAttachment(int width, int height)
	{
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null); // set format to 32 bit depth texture
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);

		return texture;
	}

	/**
	 * Adds a depth buffer attachment that is NOT an attachment
	 * to the currently bound Frame Buffer Object.
	 * @param width width of attachment
	 * @param height height of attachment
	 * @return depth buffer
	 */
	private int createDepthBufferAttachment(int width, int height)
	{
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);

		return depthBuffer;
	}

}