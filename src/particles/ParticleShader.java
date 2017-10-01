package particles;

import org.lwjgl.util.vector.Matrix4f;

import org.lwjgl.util.vector.Vector2f;
import shaders.ShaderProgram;

/**
 * A shader program used to render particles.
 * @author Aaron Frazer
 */
public class ParticleShader extends ShaderProgram
{
    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "src/particles/particleVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "src/particles/particleFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment programs
     */
    private int location_numberOfRows;
    private int location_projectionMatrix;

    /**
     * Creates a particle shader program.
     */
    public ParticleShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "modelViewMatrix");
        super.bindAttribute(5, "texOffsets");
        super.bindAttribute(6, "blendFactor");
    }

    /**
     * Loads number of rows to a uniform variable (in vertex shader).
     * @param numberOfRows number of rows
     */
    protected void loadNumberOfRows(float numberOfRows)
    {
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    /**
     * Loads a projection matrix to a uniform variable (in vertex shader).
     * @param projectionMatrix projection matrix
     */
    protected void loadProjectionMatrix(Matrix4f projectionMatrix)
    {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }
}
