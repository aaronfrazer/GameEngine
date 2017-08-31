package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a light in the game.
 * @author Aaron Frazer
 */
public class Light
{
    /**
     * Position
     */
    private Vector3f position;

    /**
     * Color
     */
    private Vector3f colour;

    /**
     * Attenuation, default is 0 (no attenuation)
     * Attenuation - the gradual loss of intensity of any kind
     * of flux through a medium
     */
    private Vector3f attenuation = new Vector3f(1, 0, 0);

    /**
     * Creates a colored light in a position of the world.
     * @param position position of light
     * @param colour color of light
     */
    public Light(Vector3f position, Vector3f colour)
    {
        this.position = position;
        this.colour = colour;
    }

    /**
     * Creates an attenuated, colored light in a position of the world.
     * @param position location of light
     * @param colour color of light
     * @param attenuation attenuation of light
     */
    public Light(Vector3f position, Vector3f colour, Vector3f attenuation)
    {
        this.position = position;
        this.colour = colour;
        this.attenuation = attenuation;
    }

    /**
     * Returns the attenuation value of this light.
     * @return attenuation value
     */
    public Vector3f getAttenuation()
    {
        return attenuation;
    }

    /**
     * Returns the position of this light.
     * @return 3D coordinates
     */
    public Vector3f getPosition()
    {
        return position;
    }

    /**
     * Sets the position of this light.
     * @param position 3D vector
     */
    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    /**
     * Returns the color of this light.
     * @return color of light
     */
    public Vector3f getColour()
    {
        return colour;
    }

    /**
     * Sets the color of this light.
     * @param colour color of light
     */
    public void setColour(Vector3f colour)
    {
        this.colour = colour;
    }

    /**
     * Increases the color values of this light.
     * @param colorIncrease 3D vector of colors
     */
    public void increaseColor(Vector3f colorIncrease)
    {
        colorIncrease = new Vector3f(getColour().x + colorIncrease.x, getColour().y + colorIncrease.y, getColour().z + colorIncrease.z);
        setColour(new Vector3f(colorIncrease.x, colorIncrease.y, colorIncrease.z));
    }

    /**
     * Decreases the color values of this light.
     * @param colorDecrease 3D vector of colors
     */
    public void decreaseColor(Vector3f colorDecrease)
    {
        colorDecrease = new Vector3f(getColour().x - colorDecrease.x, getColour().y - colorDecrease.y, getColour().z - colorDecrease.z);
        setColour(new Vector3f(colorDecrease.x, colorDecrease.y, colorDecrease.z));
    }

}