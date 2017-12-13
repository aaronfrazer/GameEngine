package bloom;

import shaders.ShaderProgram;

/**
 * A shader program used to create an bright filter that is applied to an image.
 * @author Aaron Frazer
 */
public class CombineShader extends ShaderProgram
{

    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "bloom/simpleVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "bloom/combineFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment programs
     */
    private int location_colourTexture;
    private int location_highlightTexture;

    /**
     * Creates a combine shader program.
     */
    protected CombineShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_colourTexture = super.getUniformLocation("colourTexture");
        location_highlightTexture = super.getUniformLocation("highlightTexture");
    }

    /**
     * Binds colored texture and highlight texture to sampler2D (in vertex shader - combineFragmentShader.glsl).
     */
    protected void connectTextureUnits()
    {
        super.loadInt(location_colourTexture, 0);
        super.loadInt(location_highlightTexture, 1);
    }
}
