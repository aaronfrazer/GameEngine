package shaders;

import org.lwjgl.util.vector.Matrix4f;

/**
 * A shader program used to create static models.
 * 
 * @author Aaron Frazer
 */
public class StaticShader extends ShaderProgram
{
	/**
	 * Filepath of vertex shader file.
	 */
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	
	/**
	 * Filepath of fragment shader file.
	 */
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

	/**
	 * Location of transformation matrix.
	 */
	private int location_transformationMatrix;
	
	/**
	 * Creates a static shader program.
	 */
	public StaticShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}
	
	/**
	 * Loads a transformation matrix a uniform variable (in vertex shader code).
	 * @param matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}
}
