package guis;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;

/**
 * A shader program used to create static GUI models.
 *
 * @author Aaron Frazer
 */
public class GuiShader extends ShaderProgram
{
	/**
	 * Filepath of GUI vertex shader file
	 */
	private static final String VERTEX_FILE = "src/guis/guiVertexShader.glsl";

	/**
	 * Filepath of GUI fragment shader file
	 */
	private static final String FRAGMENT_FILE = "src/guis/guiFragmentShader.glsl";

	/**
	 * Location of  transformation matrix variable
	 */
	private int location_transformationMatrix;

	/**
	 * Constructs a new GUI Shader program.
	 */
	GuiShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
	 * Loads a transformation matrix to a uniform variable (in vertex shader code).
	 *
	 * @param matrix - transformation matrix
	 */
	void loadTransformation(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
	}

}