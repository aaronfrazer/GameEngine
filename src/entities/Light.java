package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a light in the game.
 *
 * @author Aaron Frazer
 */
public class Light
{
	/**
	 * Position of the light
	 */
	private Vector3f position;

	/**
	 * Color of the light
	 */
	private Vector3f colour;

	/**
	 * Attenuation of the light, default is 0 (no attenuation)
	 * Attenuation - the gradual loss in intensity of any kind of flux
	 * though a medium
	 */
	private Vector3f attenuation = new Vector3f(1, 0, 0);

	/**
	 * Creates a colored light in a position of the world.
	 *
	 * @param position - location of light
	 * @param colour   - color of light
	 */
	public Light(Vector3f position, Vector3f colour)
	{
		this.position = position;
		this.colour = colour;
	}

	/**
	 * Creates an attenuated, colored light in a position of the world.
	 *
	 * @param position    - location of light
	 * @param colour      - color of light
	 * @param attenuation - attenuation of light
	 */
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation)
	{
		this.position = position;
		this.colour = colour;
		this.attenuation = attenuation;
	}

	/**
	 * Returns the attenuation value of the light.
	 *
	 * @return attenuation - light attenuation
	 */
	public Vector3f getAttenuation()
	{
		return attenuation;
	}

	/**
	 * Returns the position of the light.
	 *
	 * @return position - light position
	 */
	public Vector3f getPosition()
	{
		return position;
	}

	/**
	 * Sets the position of the light.
	 *
	 * @param position - light position
	 */
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	/**
	 * Returns the color of the light.
	 *
	 * @return position - light position
	 */
	public Vector3f getColour()
	{
		return colour;
	}

	/**
	 * Sets the color of the light.
	 *
	 * @param colour - light color
	 */
	public void setColour(Vector3f colour)
	{
		this.colour = colour;
	}


}
