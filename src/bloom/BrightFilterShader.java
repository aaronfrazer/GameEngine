package bloom;

import shaders.ShaderProgram;

/**
 * A shader program used to create bright filters of an image.
 * @author Aaron Frazer
 */
public class BrightFilterShader extends ShaderProgram
{
    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "bloom/simpleVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "bloom/brightFilterFragmentShader.glsl";

    /**
     * Creates a bright filter shader program.
     */
    public BrightFilterShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations()
    {
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
    }
}