package particles;

import org.lwjgl.util.vector.Matrix4f;

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
    private int location_modelViewMatrix;
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
        location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
    }

    /**
     * Loads a model view matrix to a uniform variable (in vertex shader).
     * @param modelView model matrix
     */
    protected void loadModelViewMatrix(Matrix4f modelView)
    {
        super.loadMatrix(location_modelViewMatrix, modelView);
    }

    /**
     * Loads a projection matrix to a uniform variable (in vertex shader code).
     * @param projectionMatrix projection matrix
     */
    protected void loadProjectionMatrix(Matrix4f projectionMatrix)
    {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }

}
