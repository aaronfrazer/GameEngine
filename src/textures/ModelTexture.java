package textures;

/**
 * A model used for texture data.
 * 
 * @author Aaron Frazer
 */
public class ModelTexture
{
	/**
	 * Texture's ID
	 */
	private int textureID;
	
	/**
	 * Texture's shininess
	 */
	private float shineDamper = 1;
	
	/**
	 * Texture's reflectivity
	 */
	private float reflectivity = 0;
	
	/**
	 * Returns the shininess of the texture.
	 * @return shineDamper - shininess
	 */
	public float getShineDamper()
	{
		return shineDamper;
	}

	/**
	 * Sets the shininess of the texture.
	 * @param shineDamper
	 */
	public void setShineDamper(float shineDamper)
	{
		this.shineDamper = shineDamper;
	}

	/**
	 * Returns the reflectivity of the texture.
	 * @return reflectivity - reflectivity
	 */
	public float getReflectivity()
	{
		return reflectivity;
	}

	/**
	 * Sets the reflectivity of the texutre.
	 * @param reflectivity - reflectivity
	 */
	public void setReflectivity(float reflectivity)
	{
		this.reflectivity = reflectivity;
	}

	/**
	 * Constructor that sets the ID of the texture
	 * @param id - texture ID
	 */
	public ModelTexture(int id)
	{
		this.textureID = id;
	}
	
	/**
	 * Returns the texture's ID
	 * @return 
	 */
	public int getID()
	{
		return textureID;
	}
}
