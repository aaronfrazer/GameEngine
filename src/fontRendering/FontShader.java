package fontRendering;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;

/**
 * A shader program used to render fonts.
 *
 * @author Aaron Frazer
 */
public class FontShader extends ShaderProgram
{
    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "src/fontRendering/fontVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "src/fontRendering/fontFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment programs
     */
    private int location_colour;
    private int location_translation;
    private int location_width;
    private int location_edge;
    private int location_borderWidth;
    private int location_borderEdge;
    private int location_offset;
    private int location_outlineColour;

    /**
     * Creates a font shader program.
     */
    public FontShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_colour = super.getUniformLocation("colour");
        location_translation = super.getUniformLocation("translation");
        location_width = super.getUniformLocation("width");
        location_edge = super.getUniformLocation("edge");
        location_borderWidth = super.getUniformLocation("borderWidth");
        location_borderEdge = super.getUniformLocation("borderEdge");
        location_offset = super.getUniformLocation("offset");
        location_outlineColour = super.getUniformLocation("outlineColour");

    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "positions");
        super.bindAttribute(1, "textureCoords");
    }

    /**
     * Loads a color to a uniform variable (in fragment shader)
     * @param colour color
     */
    protected void loadColour(Vector3f colour)
    {
        super.load3DVector(location_colour, colour);
    }

    /**
     * Loads a translation to a uniform variable (in vertex shader)
     * @param translation translation
     */
    protected void loadTranslation(Vector2f translation)
    {
        super.load2DVector(location_translation, translation);
    }

    /**
     * Loads width to a uniform variable (in vertex shader).
     * @param width width of character (between 1 and 0)
     */
    public void loadWidth(float width)
    {
        super.loadFloat(location_width, width);
    }

    /**
     * Loads edge to a uniform variable (in vertex shader).
     * @param edge pixelation of edge (soft/hard edge)
     */
    public void loadEdge(float edge)
    {
        super.loadFloat(location_edge, edge);
    }

    /**
     * Loads borderWidth to a uniform variable (in vertex shader).
     * @param borderWidth outline width of character
     */
    public void loadBorderWidth(float borderWidth)
    {
        super.loadFloat(location_borderWidth, borderWidth);
    }

    /**
     * Loads borderEdge to a uniform variable (in vertex shader).
     * @param borderEdge edge transition distance
     */
    public void loadBorderEdge(float borderEdge)
    {
        super.loadFloat(location_borderEdge, borderEdge);
    }

    /**
     * Loads offset variable to a uniform variable (in vertex shader).
     * @param x x offset
     * @param y y offset
     */
    protected void loadOffset(float x, float y)
    {
        super.load2DVector(location_offset, new Vector2f(x, y));
    }

    /**
     * Loads outlineColour to a uniform variable (in vertex shader).
     * @param r red color value
     * @param g green color value
     * @param b blue color value
     */
    public void loadOutlineColour(float r, float g, float b)
    {
        super.load3DVector(location_outlineColour, new Vector3f(r, g, b));

    }
}
