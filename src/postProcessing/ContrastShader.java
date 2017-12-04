package postProcessing;

import shaders.ShaderProgram;

/**
 * A shader program used to create contrast on the screen.
 * @author Aaron Frazer
 */
public class ContrastShader extends ShaderProgram
{
    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "postProcessing/contrastVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "postProcessing/contrastFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment programs
     */
    private int location_contrast;

    /**
     * Creates a contrast shader program.
     */
    public ContrastShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_contrast = super.getUniformLocation("contrast");
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
    }

    /**
     * Loads contrast to a uniform variable (in fragment shader).
     * @param contrast contrast of screen
     */
    public void loadContrast(float contrast)
    {
        super.loadFloat(location_contrast, contrast);
    }
}