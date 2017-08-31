package normalMappingObjConverter;

/**
 * Responsible for storing data of a model with normal mapping implemented.
 *
 * Data includes:
 * 1. Vertices
 * 2. Texture Coordinates
 * 3. Normals
 * 4. Indices
 * 5. Furthest point
 *
 * @author Aaron Frazer
 */
public class ModelDataNM
{
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private float[] tangents;
    private int[] indices;

    /**
     * Furthest vertex of this model
     */
    private float furthestPoint;

    /**
     * Constructs a new data model with normal mapping.
     * @param vertices array of vertices
     * @param textureCoords array of texture coordinates
     * @param normals array of normals
     * @param tangents array of tangents
     * @param indices array of indices
     * @param furthestPoint furthest vertex
     */
    public ModelDataNM(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices, float furthestPoint)
    {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
        this.tangents = tangents;
    }

    /**
     * Returns this model's vertices.
     * @return array of vertices
     */
    public float[] getVertices()
    {
        return vertices;
    }

    /**
     * Returns this model's texture coordinates.
     * @return array of texture coordinates
     */
    public float[] getTextureCoords()
    {
        return textureCoords;
    }

    /**
     * Returns this model's normals.
     * @return array of normals
     */
    public float[] getNormals()
    {
        return normals;
    }

    /**
     * Returns this model's tangents
     * @return array of tangents
     */
    public float[] getTangents()
    {
        return tangents;
    }

    /**
     * Returns this model's indices.
     * @return array of indices
     */
    public int[] getIndices()
    {
        return indices;
    }

    /**
     * Returns the furthest vertex of this model.
     * @return furthest vertex
     */
    public float getFurthestPoint()
    {
        return furthestPoint;
    }

}