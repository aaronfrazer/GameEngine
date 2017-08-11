package normalMappingObjConverter;

/**
 * Responsible for storing data of a model with normal mapping.
 */
public class ModelDataNM
{
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private float[] tangents;
    private int[] indices;
    private float furthestPoint;

    /**
     * Constructs a new data model using normal mapping.
     * @param vertices vertices of the model
     * @param textureCoords texture coordinates
     * @param normals normals of the model
     * @param tangents tangents of the model
     * @param indices indices of the model
     * @param furthestPoint furthest point of the model
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
     * Returns this model's tangents.
     * @return tangents - array of tangents
     */
    public float[] getTangents()
    {
        return tangents;
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
