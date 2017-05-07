package objConverter;

/**
 * Responsible for storing data of a model.
 * Data includes:
 * 1. Vertices
 * 2. Texture Coordinates
 * 3. Normals
 * 4. Indices
 * 5. Furthest point
 * 
 * @author Aaron Frazer
 */
public class ModelData
{
	/**
	 * Array of vertices of this model
	 */
	private float[] vertices;
	
	/**
	 * Array of texture coordinates of this model
	 */
	private float[] textureCoords;
	
	/**
	 * Array of normals of this model
	 */
	private float[] normals;
	
	/**
	 * Array of indices of this model
	 */
	private int[] indices;
	
	/**
	 * Furthest vertex of this model
	 */
	private float furthestPoint;

	/**
	 * Constructs a new data model.
	 * @param vertices
	 * @param textureCoords
	 * @param normals
	 * @param indices
	 * @param furthestPoint
	 */
	public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, float furthestPoint)
	{
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.furthestPoint = furthestPoint;
	}

	/**
	 * Returns this model's vertices.
	 * @return vertices - array of vertices
	 */
	public float[] getVertices()
	{
		return vertices;
	}

	/**
	 * Returns this model's texture coordinates.
	 * @return textureCoords - array of texture coordinates
	 */
	public float[] getTextureCoords()
	{
		return textureCoords;
	}

	/**
	 * Returns this model's normals.
	 * @return normals - array of normals
	 */
	public float[] getNormals()
	{
		return normals;
	}

	/**
	 * Returns this model's indices.
	 * @return indices - array of indices
	 */
	public int[] getIndices()
	{
		return indices;
	}

	/**
	 * Returns the furthest vertex of this model.
	 * @return furthestPoint - furthest vertex
	 */
	public float getFurthestPoint()
	{
		return furthestPoint;
	}

}
