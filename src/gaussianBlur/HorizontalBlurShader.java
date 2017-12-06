package gaussianBlur;

import shaders.ShaderProgram;

/**
 * A shader program used to create a horizontal blur on the screen.
 * @author Aaron Frazer
 */
public class HorizontalBlurShader extends ShaderProgram
{
    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "gaussianBlur/horizontalBlurVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "gaussianBlur/blurFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment programs
     */
    private int location_targetWidth;

    /**
     * Creates a horizontal blur shader program.
     */
    protected HorizontalBlurShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    /**
     * Loads targetWidth to a uniform variable (in fragment shader).
     * @param width width of FBO
     */
    protected void loadTargetWidth(float width)
    {
        super.loadFloat(location_targetWidth, width);
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_targetWidth = super.getUniformLocation("targetWidth");
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
    }

}
