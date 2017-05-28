package guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * Represents a GUI texture.
 *
 * @author Aaron Frazer
 */
public class GuiTexture
{
	/**
	 * ID of the Texture
	 */
	private int texture;

	/**
	 * Position on the display
	 */
	private Vector2f position;

	/**
	 * X and Y scale
	 */
	private Vector2f scale;

	/**
	 * Constructs a new GUI Texture.
	 *
	 * @param texture  - Texture ID
	 * @param position - 2D vector position
	 * @param scale    - scale
	 */
	public GuiTexture(int texture, Vector2f position, Vector2f scale)
	{
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}

	/**
	 * Returns the texture's ID.
	 *
	 * @return texture - Texture ID
	 */
	public int getTexture()
	{
		return texture;
	}

	/**
	 * Returns the position of the GUI Texture.
	 *
	 * @return position - position
	 */
	public Vector2f getPosition()
	{
		return position;
	}

	/**
	 * Returns the scale of the GUI Texture.
	 *
	 * @return scale - scale
	 */
	public Vector2f getScale()
	{
		return scale;
	}

}
