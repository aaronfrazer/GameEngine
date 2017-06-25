package water;

/**
 * A 3D water tile used for rendering water.
 */
public class WaterTile
{
	/**
	 * Size of water tile
	 */
	public static final float TILE_SIZE = 60;

	/**
	 * Height of tile
	 */
	private float height;

	/**
	 * X and Y coords of tile
	 */
	private float x, z;

	/**
	 * Constructs a water tile.
	 * @param centerX center of tile X
	 * @param centerZ center of tile Z
	 * @param height height
	 */
	public WaterTile(float centerX, float centerZ, float height)
	{
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	/**
	 * Returns the height of this tile.
	 * @return height of tile
	 */
	public float getHeight()
	{
		return height;
	}

	/**
	 * Returns this tile's X coordinate.
	 * @return x coordinate
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * Returns this tile's Z coordinate.
	 * @return z coordinate
	 */
	public float getZ()
	{
		return z;
	}
}
