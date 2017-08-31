package guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * A class that represents a GUI texture.
 * @author Aaron Frazer
 */
public class GuiTexture
{
    /**
     * ID of texture
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
     * Creates a GUI texture.
     * @param texture texture ID
     * @param position 2D vector position
     * @param scale scale
     */
    public GuiTexture(int texture, Vector2f position, Vector2f scale)
    {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    /**
     * Returns this GUI texture's ID.
     * @return texture ID
     */
    public int getTexture()
    {
        return texture;
    }

    /**
     * Returns the position of this GUI texture.
     * @return 2D coordinates of texture's position
     */
    public Vector2f getPosition()
    {
        return position;
    }

    /**
     * Returns the scale of this GUI texture.
     * @return scale
     */
    public Vector2f getScale()
    {
        return scale;
    }

}
