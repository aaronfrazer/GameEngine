package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * An entity that contains a light inside it.
 *
 * TODO: Finish this class.  When finished you should be able to create an entity with a light source attached to it.  It should be called in a single constructor.
 *
 * @author Aaron Frazer
 */
public class LightEntity extends Entity
{
	/**
	 * Light for the entity
	 */
	private Light light;

	/**
	 * Creates an entity and a light in a position of the world.
	 *
	 * @param model       - textured model
	 * @param position    - position of entity
	 * @param rotX        - x axis rotation
	 * @param rotY        - y axis rotation
	 * @param rotZ        - z axis rotation
	 * @param scale       - scale
	 * @param color       - color of light
	 * @param attenuation - attenuation of light
	 */
	public LightEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Vector3f color, Vector3f attenuation)
	{
		super(model, position, rotX, rotY, rotZ, scale);

		Vector3f lightPosition = new Vector3f(position.getX(), position.getY() + 12, position.getZ());
		light = new Light(lightPosition, color, attenuation);
	}

	/**
	 * Returns the light attached to this entity.
	 */
	public Light getLight()
	{
		return light;
	}

	/**
	 * Sets the color of the light in the entity.
	 */
	public void setColor(Vector3f color)
	{
		light.setColour(color);
	}

	/**
	 * Sets the position of the entity
	 *
	 * @param position - position
	 */
	public void setPosition(Vector3f position)
	{
		light.setPosition(position);
	}
}
