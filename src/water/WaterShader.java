package water;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;

/**
 * A shader program used to create water models.
 */
public class WaterShader extends ShaderProgram
{

	/**
	 * Filepath of vertex shader file.
	 */
	private final static String VERTEX_FILE = "src/water/waterVertexShader.glsl";

	/**
	 * Filepath of fragment shader file.
	 */
	private final static String FRAGMENT_FILE = "src/water/waterFragmentShader.glsl";

	/**
	 * Location of model matrix variable
	 */
	private int location_modelMatrix;

	/**
	 * Location of view matrix variable
	 */
	private int location_viewMatrix;

	/**
	 * Location of projection matrix variable
	 */
	private int location_projectionMatrix;

	/**
	 * Location of reflection texture variable
	 */
	private int location_reflectionTexture;

	/**
	 * Location of refraction texture variable
	 */
	private int location_refractionTexture;


	/**
	 * Creates a water shader program.
	 */
	public WaterShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes()
	{
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_reflectionTexture = getUniformLocation("reflectionTexture");
		location_refractionTexture = getUniformLocation("refractionTexture");
	}

	/**
	 * Binds reflection and refraction variables to sampler2D (in vertex shader code - waterFragmentShader.glsl).
	 */
	public void connectTextureUnits()
	{
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
	}

	/**
	 * Loads a projection matrix to a uniform variable (in vertex shader code).
	 * @param projection - projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection)
	{
		loadMatrix(location_projectionMatrix, projection);
	}

	/**
	 * Loads a view matrix to a uniform variable (in vertex shader code).
	 * @param camera - camera
	 */
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	/**
	 * Loads a model matrix to a uniform variable (in vertex shader code).
	 * @param modelMatrix - model matrix
	 */
	public void loadModelMatrix(Matrix4f modelMatrix)
	{
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
