package textures;

/**
 * Contains 4 terrain textures that are rendered onto a terrain.
 * 
 * @author Aaron Frazer
 */
public class TerrainTexturePack
{
	/**
	 * Background terrain texture
	 */
	private TerrainTexture backgroundTexture;
	
	/**
	 * Red terrain texture
	 */
	private TerrainTexture rTexture;
	
	/**
	 * Green terrain texture
	 */
	private TerrainTexture gTexture;
	
	/**
	 * Blue terrain texture
	 */
	private TerrainTexture bTexture;
	
	/**
	 * Constructs a terrain texture pack.
	 * @param backgroundTexture - background texture
	 * @param rTexture - red texture
	 * @param gTexture - green texture
	 * @param bTexture - blue texture
	 */
	public TerrainTexturePack(TerrainTexture backgroundTexture, TerrainTexture rTexture, TerrainTexture gTexture, TerrainTexture bTexture)
	{
		super();
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	/**
	 * Returns this terrain texture pack's background texture.
	 * @return backgroundTexture - background texture
	 */
	public TerrainTexture getBackgroundTexture()
	{
		return backgroundTexture;
	}

	/**
	 * Returns this terrain texture pack's red texture.
	 * @return rTexture - red texture
	 */
	public TerrainTexture getrTexture()
	{
		return rTexture;
	}

	/**
	 * Returns this terrain texture pack's green texture.
	 * @return gTexture - green texture
	 */
	public TerrainTexture getgTexture()
	{
		return gTexture;
	}

	/**
	 * Returns this terrain texture pack's blue texture.
	 * @return bTexture - blue texture
	 */
	public TerrainTexture getbTexture()
	{
		return bTexture;
	}
	
}
