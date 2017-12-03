package shadows;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

/**
 * A shader program used to render shadows.
 *
 * @author Aaron Frazer
 */
public class ShadowShader extends ShaderProgram
{
    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "shadows/shadowVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "shadows/shadowFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment programs
     */
    private int location_mvpMatrix;

    /**
     * Creates a shadow shader program.
     */
    protected ShadowShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_mvpMatrix = super.getUniformLocation("mvpMatrix");

    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "in_position");
        super.bindAttribute(1, "in_textureCoords");
    }

    /**
     * Loads a MVP matrix to a uniform variable (in vertex shader)
     * @param mvpMatrix MVP matrix
     */
    protected void loadMvpMatrix(Matrix4f mvpMatrix)
    {
        super.loadMatrix(location_mvpMatrix, mvpMatrix);
    }
}
