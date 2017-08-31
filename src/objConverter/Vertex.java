package objConverter;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * A vertex is a point where two or more lines meet.
 * @author Aaron Frazer
 */
public class Vertex
{

    private static final int NO_INDEX = -1;

    /**
     * Vertex position
     */
    private Vector3f position;

    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Vertex duplicateVertex = null;
    private int index;
    private float length;
    private List<Vector3f> tangents = new ArrayList<>();
    private Vector3f averagedTangent = new Vector3f(0, 0, 0);

    /**
     * Creates a vertex.
     * @param index indexed value
     * @param position 3D position
     */
    public Vertex(int index, Vector3f position)
    {
        this.index = index;
        this.position = position;
        this.length = position.length();
    }

    /**
     * Adds a tangent to this vertex.
     * @param tangent tangent
     */
    public void addTangent(Vector3f tangent)
    {
        tangents.add(tangent);
    }

    /**
     * Averages the list of tangents.
     */
    public void averageTangents()
    {
        if (tangents.isEmpty())
        {
            return;
        }
        for (Vector3f tangent : tangents)
        {
            Vector3f.add(averagedTangent, tangent, averagedTangent);
        }
        averagedTangent.normalise();
    }

    /**
     * Returns true if this vertex is set.
     * @return true if set
     */
    public boolean isSet()
    {
        return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
    }

    /**
     * Compares this vertex's texture and normal.
     * @param textureIndexOther texture index
     * @param normalIndexOther normal index
     * @return true if texture and normal are the same
     */
    public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther)
    {
        return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
    }

    /**
     * Returns the position of this vertex.
     * @return 3D position
     */
    public Vector3f getPosition()
    {
        return position;
    }

    /**
     * Returns the texture index ID of this vertex.
     * @return texutre index ID
     */
    public int getTextureIndex()
    {
        return textureIndex;
    }

    /**
     * Returns the normal index ID of this vertex.
     * @return normal index ID
     */
    public int getNormalIndex()
    {
        return normalIndex;
    }

    /**
     * Returns the average tangent of this vertex.
     * @return 3D tangent
     */
    public Vector3f getAverageTangent()
    {
        return averagedTangent;
    }

    /**
     * Returns the duplicate vertex of this vertex.
     * @return duplicate vertex
     */
    public Vertex getDuplicateVertex()
    {
        return duplicateVertex;
    }

    /**
     * Returns the index of this tangent.
     * @return index value
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Returns the length of this tangent.
     * @return length of tangent
     */
    public float getLength()
    {
        return length;
    }

    /**
     * Sets the texture index of this vertex.
     * @param textureIndex texture index
     */
    public void setTextureIndex(int textureIndex)
    {
        this.textureIndex = textureIndex;
    }

    /**
     * Sets the normal index of this vertex.
     * @param normalIndex normal index
     */
    public void setNormalIndex(int normalIndex)
    {
        this.normalIndex = normalIndex;
    }

    /**
     * Sets the duplicate vertex of this vertex.
     * @param duplicateVertex duplicate vertex
     */
    public void setDuplicateVertex(Vertex duplicateVertex)
    {
        this.duplicateVertex = duplicateVertex;
    }

}
