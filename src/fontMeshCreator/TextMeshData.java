package fontMeshCreator;

/**
 * Stores vertex data for all quads on which a text will be rendered.
 * @author Aaron Frazer
 */
public class TextMeshData
{
    /**
     * Positions of vertices
     */
    private float[] vertexPositions;

    /**
     * Texture coordinates
     */
    private float[] textureCoords;

    /**
     * Creates vertex data for all quads on the text to be rendered
     * @param vertexPositions vertices
     * @param textureCoords texture coordinates
     */
    protected TextMeshData(float[] vertexPositions, float[] textureCoords)
    {
        this.vertexPositions = vertexPositions;
        this.textureCoords = textureCoords;
    }

    /**
     * Returns vertex positions of the text.
     * @return vertices
     */
    public float[] getVertexPositions()
    {
        return vertexPositions;
    }

    /**
     * Returns texture coordinates of the text.
     * @return texture coordinates
     */
    public float[] getTextureCoords()
    {
        return textureCoords;
    }

    /**
     * Returns the number of vertices.
     * @return number of vertices
     */
    public int getVertexCount()
    {
        return vertexPositions.length / 2;
    }
}