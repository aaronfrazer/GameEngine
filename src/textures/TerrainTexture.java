package textures;

/**
 * Represents a terrain texture.
 *
 * @author Aaron Frazer
 */
public class TerrainTexture
{
	/**
	 * ID of terrain texture
	 */
	private int textureID;

	/**
	 * Constructs a terrain texture.
	 *
	 * @param textureID - terrain texture's ID
	 */
	public TerrainTexture(int textureID)
	{
		this.textureID = textureID;
	}

	/**
	 * Returns this terrain texture's ID.
	 *
	 * @return textureID - texture's ID
	 */
	public int getTextureID()
	{
		return textureID;
	}
}
