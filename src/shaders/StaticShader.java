package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolbox.Maths;

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
	 * Location of projection matrix.
	 */
	private int location_projectionMatrix;
	
	/**
	 * Location of view matrix.
	 */
	private int location_viewMatrix;
	
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
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	/**
	 * Loads a transformation matrix to a uniform variable (in vertex shader code).
	 * @param matrix - transformation matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	/**
	 * Loads a projection matrix to a unifrom variable (in vertex shader code).
	 * @param projection - projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection)
	{
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	/**
	 * Loads a view matrix to a unifrom variable (in vertex shader code).
	 * @param camera - camera
	 */
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
}
