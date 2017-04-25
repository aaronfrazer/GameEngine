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
