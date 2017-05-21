package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

/**
 * An instance of a textured model. Contains a textured model 
 * with a position, rotation, and scale that the model is rendered
 * in the 3D world.
 * 
 * @author Aaron Frazer
 */
public class Entity
{
	/**
	 * Textured model of the entity
	 */
	private TexturedModel model;
	
	/**
	 * Position of the entity
	 */
	private Vector3f position;
	
	/**
	 * Rotation of the entity
	 */
	private float rotX, rotY, rotZ;
	
	/**
	 * Scale of the entity
	 */
	private float scale;

	/**
	 * Creates an entity.
	 * @param model - textured model
	 * @param position - vector
	 * @param rotX - x axis rotation
	 * @param rotY - y axis rotation
	 * @param rotZ - z axis rotation
	 * @param scale - scale
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
	 * Moves this entity in the world.
	 * @param dx - x value
	 * @param dy - y value
	 * @param dz - z value
	 */
	public void increasePosition(float dx, float dy, float dz)
	{
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	/**
	 * Rotates this entity in the world.
	 * @param dx - x rotation
	 * @param dy - y rotation
	 * @param dz - z rotation
	 */
	public void increaseRotation(float dx, float dy, float dz)
	{
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;		
	}
	
	/**
	 * Returns this entity's textured model.
	 * @return model - textured model
	 */
	public TexturedModel getModel()
	{
		return model;
	}

	/**
	 * Sets this entity's textured model
	 * @param model - textured model
	 */
	public void setModel(TexturedModel model)
	{
		this.model = model;
	}

	/**
	 * Returns this entity's position as a Vector3f object.
	 * @return position - position of entity
	 */
	public Vector3f getPosition()
	{
		return position;
	}

	/**
	 * Sets the position of the entity
	 * @param position - position
	 */
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	/**
	 * Returns the entity's x rotation value.
	 * @return rotX - x value
	 */
	public float getRotX()
	{
		return rotX;
	}

	/**
	 * Sets the entity's x rotation value.
	 * @param rotX - x value
	 */
	public void setRotX(float rotX)
	{
		this.rotX = rotX;
	}

	/**
	 * Returns the entity's y rotation value.
	 * @return roty - y value
	 */
	public float getRotY()
	{
		return rotY;
	}

	/**
	 * Sets the entity's y rotation value.
	 * @param rotY - y value
	 */
	public void setRotY(float rotY)
	{
		this.rotY = rotY;
	}

	/**
	 * Returns the entity's z rotation value.
	 * @return rotZ - z value
	 */
	public float getRotZ()
	{
		return rotZ;
	}

	/**
	 * Sets the entity's z rotation value.
	 * @param rotZ - z value
	 */
	public void setRotZ(float rotZ)
	{
		this.rotZ = rotZ;
	}

	/**
	 * Returns this entity's scale value.
	 * @return scale - scale
	 */
	public float getScale()
	{
		return scale;
	}

	/**
	 * Sets this entity's scale value.
	 * @param scale - scale
	 */
	public void setScale(float scale)
	{
		this.scale = scale;
	}
	
	
}
