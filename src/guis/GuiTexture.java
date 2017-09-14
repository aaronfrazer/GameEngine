package guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * A class that represents a GUI textureID.
 * @author Aaron Frazer
 */
public class GuiTexture
{
    /**
     * ID of textureID
     */
    private int textureID;

    /**
     * Position on the display
     */
    private Vector2f position;

    /**
     * X and Y scale
     */
    private Vector2f scale;

    /**
     * Creates a GUI textureID.
     * @param textureID textureID ID
     * @param position 2D vector position
     * @param scale scale
     */
    public GuiTexture(int textureID, Vector2f position, Vector2f scale)
    {
        this.textureID = textureID;
        this.position = position;
        this.scale = scale;
    }

    /**
     * Returns this GUI textureID's ID.
     * @return textureID ID
     */
    public int getTextureID()
    {
        return textureID;
    }

    /**
     * Returns the position of this GUI textureID.
     * @return 2D coordinates of textureID's position
     */
    public Vector2f getPosition()
    {
        return position;
    }

    /**
     * Returns the scale of this GUI textureID.
     * @return scale
     */
    public Vector2f getScale()
    {
        return scale;
    }

    /**
     * Sets the position of this GUI textureID.
     * @param position position
     */
    public void setPosition(Vector2f position)
    {
        this.position = position;
    }

    /**
     * Sets the scale of this GUI textureID.
     * @param scale scale
     */
    public void setScale(Vector2f scale)
    {
        this.scale = scale;
    }
}
