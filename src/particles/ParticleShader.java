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
    private int location_modelViewMatrix;
    private int location_projectionMatrix;
    private int location_texOffset1;
    private int location_texOffset2;
    private int location_texCoordInfo;

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
        location_texOffset1 = super.getUniformLocation("texOffset1");
        location_texOffset2 = super.getUniformLocation("texOffset2");
        location_texCoordInfo = super.getUniformLocation("texCoordInfo");
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
     * Loads a projection matrix to a uniform variable (in vertex shader).
     * @param projectionMatrix projection matrix
     */
    protected void loadProjectionMatrix(Matrix4f projectionMatrix)
    {
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }

    /**
     * Loads offset values to uniform variable (in vertex shader).
     * @param offset1 first offset
     * @param offset2 second offset
     * @param numRows number of rows in X component
     * @param blend blend factor in Y component
     */
    protected void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numRows, float blend)
    {
        super.load2DVector(location_texOffset1, offset1);
        super.load2DVector(location_texOffset2, offset2);
        super.load2DVector(location_texCoordInfo, new Vector2f(numRows, blend));
    }
}
