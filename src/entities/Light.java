package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * A light
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
	 * Creates a light.
	 * @param position - location of light
	 * @param colour - color of light
	 */
	public Light(Vector3f position, Vector3f colour)
	{
		this.position = position;
		this.colour = colour;
	}
	
	/**
	 * Returns the position of the light.
	 * @return position - light position
	 */
	public Vector3f getPosition()
	{
		return position;
	}

	/**
	 * Sets the position of the light.
	 * @param position - light position
	 */
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}
	
	/**
	 * Returns the color of the light.
	 * @return position - light position
	 */
	public Vector3f getColour()
	{
		return colour;
	}
	
	/**
	 * Sets the color of the light.
	 * @param position - light position
	 */
	public void setColour(Vector3f colour)
	{
		this.colour = colour;
	}
	
	
}
