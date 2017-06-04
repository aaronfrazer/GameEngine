package textures;

import java.nio.ByteBuffer;

/**
 * A data structure that holds decoded byte data of an image.
 */
public class TextureData
{
	/**
	 * Width of image
	 */
	private int width;

	/**
	 * Height of image
	 */
	private int height;

	/**
	 * Buffer of image
	 */
	private ByteBuffer buffer;

	/**
	 * Creates a new texture data structure that holds texture data.
	 *
	 * @param buffer - buffer of texture
	 * @param width  - width of texutre
	 * @param height - height of texture
	 */
	public TextureData(ByteBuffer buffer, int width, int height)
	{
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the width of the texture.
	 *
	 * @return width - texture's width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Returns the hieight of the texture.
	 *
	 * @return height - texture's height
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Returns the buffer of the texture.
	 *
	 * @return buffer - texutre's buffer
	 */
	public ByteBuffer getBuffer()
	{
		return buffer;
	}

}
