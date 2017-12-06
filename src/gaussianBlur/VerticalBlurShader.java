package gaussianBlur;

import shaders.ShaderProgram;

/**
 * A shader program used to create a vertical blur on the screen.
 * @author Aaron Frazer
 */
public class VerticalBlurShader extends ShaderProgram
{
    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "gaussianBlur/verticalBlurVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "gaussianBlur/blurFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment programs
     */
    private int location_targetHeight;

    /**
     * Creates a vertical blur shader program.
     */
    protected VerticalBlurShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Loads targetHeight to a uniform variable (in fragment shader).
     * @param height height of FBO
     */
    protected void loadTargetHeight(float height)
    {
        super.loadFloat(location_targetHeight, height);
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_targetHeight = super.getUniformLocation("targetHeight");
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
    }
}
