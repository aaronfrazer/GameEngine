package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * An instance of a textured model in the 3D world.
 * @author Aaron Frazer
 */
public class Entity
{
    /**
     * Textured model
     */
    private TexturedModel model;

    /**
     * Position in the world
     */
    private Vector3f position;

    /**
     * Rotation
     */
    private float rotX, rotY, rotZ;

    /**
     * Scale
     */
    private float scale;

    /**
     * The texture in the texture atlas that this entity is using
     * Structure is as follows:
     * 0  1  2  3
     * 4  5  6  7
     * 8  9  10 11
     * 12 13 14 15
     */
    private int textureIndex = 0;

    /**
     * Creates an entity in a position of the world.
     * @param model textured model
     * @param position 3D coordinates
     * @param rotX x axis rotation
     * @param rotY y axis rotation
     * @param rotZ z axis rotation
     * @param scale scale
     */
    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale)
    {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     * Creates an entity in a position of the world with a texture atlas index to be
     * used as the texture.
     * @param model textured model
     * @param index texture atlas index
     * @param position 3D coordinates
     * @param rotX x axis rotation
     * @param rotY y axis rotation
     * @param rotZ z axis roation
     * @param scale scale
     */
    public Entity(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ, float scale)
    {
        this.textureIndex = index;
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    /**
     * Returns the X offset of the texture atlas.
     * @return X offset
     */
    public float getTextureXOffset()
    {
        int column = textureIndex % model.getTexture().getNumberOfRows();
        return (float) column / (float) model.getTexture().getNumberOfRows();
    }

    /**
     * Returns the Y offset of the texture atlas.
     * @return Y offset
     */
    public float getTextureYOffset()
    {
        int row = textureIndex / model.getTexture().getNumberOfRows();
        return (float) row / (float) model.getTexture().getNumberOfRows();
    }

    /**
     * Moves this entity in the world.
     * @param dx X value
     * @param dy Y value
     * @param dz Z value
     */
    public void increasePosition(float dx, float dy, float dz)
    {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    /**
     * Rotates this entity in the world.
     * @param dx X rotation
     * @param dy Y rotation
     * @param dz Z rotation
     */
    public void increaseRotation(float dx, float dy, float dz)
    {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    /**
     * Return's this entity's textured model.
     * @return textured model
     */
    public TexturedModel getModel()
    {
        return model;
    }

    /**
     * Sets this entity's textured model.
     * @param model textured model
     */
    public void setModel(TexturedModel model)
    {
        this.model = model;
    }

    /**
     * Returns this entity's position in the world.
     * @return 3D coordinates of entity position
     */
    public Vector3f getPosition()
    {
        return position;
    }

    /**
     * Sets this entity's position in the world.
     * @param position 3D position
     */
    public void setPosition(Vector3f position)
    {
        this.position = position;
    }

    /**
     * Returns this entity's X rotation value.
     * @return X rotation value
     */
    public float getRotX()
    {
        return rotX;
    }

    /**
     * Returns this entity's Y rotation value.
     * @return Y rotation value
     */
    public float getRotY()
    {
        return rotY;
    }

    /**
     * Returns this entity's Z rotation value.
     * @return Z rotation value
     */
    public float getRotZ()
    {
        return rotZ;
    }

    /**
     * Returns this entity's scale value.
     * @return scale
     */
    public float getScale()
    {
        return scale;
    }

    /**
     * Sets this entity's scale value.
     * @param scale scale
     */
    public void setScale(float scale)
    {
        this.scale = scale;
    }

}
