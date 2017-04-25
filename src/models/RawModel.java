package models;

/**
 * A 3D model stored in memory.
 * 
 * @author Aaron Frazer
 */
public class RawModel
{
	/**
	 * ID of VAO.
	 */
	private int vaoID;
	
	/**
	 * Number of vertices in this 3D model.
	 */
	private int vertexCount;
	
	/**
	 * Constructor that initializes a Raw Model.
	 * @param vaoID - ID of VAO
	 * @param vertexCount - number of vertices
	 */
	public RawModel(int vaoID, int vertexCount)
	{
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	/**
	 * Returns the ID of the VAO.
	 * @return vaoID - VAO ID
	 */
	public int getVaoID()
	{
		return vaoID;
	}

	/**
	 * Returns the number of vertices.
	 * @return vertexCount - number of vertices
	 */
	public int getVertexCount()
	{
		return vertexCount;
	}
}
