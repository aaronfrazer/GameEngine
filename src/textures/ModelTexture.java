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
	 * Does model have transparency?
	 */
	private boolean hasTransparency = false;
	
	/**
	 * Does model implement fake lighting?
	 * Fake lighting is used for objects that need help
	 * with lighting.
	 */
	private boolean useFakeLighting = false;

	/**
	 * Number of rows in texture atlas
	 * Set to 1 for a single texture (no atlas)
	 */
	private int numberOfRows = 1;

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
	
	/**
	 * Returns true if this model is transparent.
	 * @return hasTrancparency
	 */
	public boolean isHasTransparency()
	{
		return hasTransparency;
	}

	/**
	 * Sets the transcparency of this model.
	 * @param hasTransparency
	 */
	public void setHasTransparency(boolean hasTransparency)
	{
		this.hasTransparency = hasTransparency;
	}
	
	/**
	 * Returns true if this model uses fake lighting
	 * @return useFakeLighting
	 */
	public boolean isUseFakeLighting()
	{
		return useFakeLighting;
	}

	/**
	 * Sets the fake lighting of this model
	 * @param useFakeLighting
	 */
	public void setUseFakeLighting(boolean useFakeLighting)
	{
		this.useFakeLighting = useFakeLighting;
	}

	/**
	 * Returns the number of rows used by the texture atlas for this texture.
	 * @return numberOfRows
	 */
	public int getNumberOfRows()
	{
		return numberOfRows;
	}

	/**
	 * Sets the number of rows used by the texture atlas for this texture.
	 * @param numberOfRows
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
}
