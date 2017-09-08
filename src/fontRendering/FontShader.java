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
}
