package water;

import entities.Camera;
import entities.Light;
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
	 * Location of DuDv map variable
	 */
	private int location_dudvMap;

	/**
	 * Location of move factor variable
	 */
	private int location_moveFactor;

	/**
	 * Location of camera position variable
	 */
	private int location_cameraPosition;

	/**
	 * Location of normal map variable
	 */
	private int location_normalMap;

	/**
	 * Location of light color variable
	 */
	private int location_lightColour;

	/**
	 * Location of light position variable
	 */
	private int location_lightPosition;

	/**
	 * Location of depth map variable
	 */
	private int location_depthMap;

	/**
	 * Location of near plane variable
	 */
	private int location_near;

	/**
	 * Location of far plane variable
	 */
	private int location_far;

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
		location_projectionMatrix	= getUniformLocation("projectionMatrix");
		location_viewMatrix			= getUniformLocation("viewMatrix");
		location_modelMatrix		= getUniformLocation("modelMatrix");
		location_reflectionTexture	= getUniformLocation("reflectionTexture");
		location_refractionTexture	= getUniformLocation("refractionTexture");
		location_dudvMap			= getUniformLocation("dudvMap");
		location_moveFactor			= getUniformLocation("moveFactor");
		location_cameraPosition		= getUniformLocation("cameraPosition");
		location_normalMap			= getUniformLocation("normalMap");
		location_lightColour		= getUniformLocation("lightColour");
		location_lightPosition		= getUniformLocation("lightPosition");
		location_depthMap			= getUniformLocation("depthMap");
		location_near				= getUniformLocation("near");
		location_far				= getUniformLocation("far");
	}

	/**
	 * Binds reflection, refraction, and DuDv map variables to sampler2D (in vertex shader code - waterFragmentShader.glsl).
	 */
	public void connectTextureUnits()
	{
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
		super.loadInt(location_dudvMap, 2);
		super.loadInt(location_normalMap, 3);
		super.loadInt(location_depthMap, 4);
	}

	/**
	 * Loads light color and position.
	 * @param sun light
	 */
	public void loadLight(Light sun)
	{
		super.load3DVector(location_lightColour, sun.getColour());
		super.load3DVector(location_lightPosition, sun.getPosition());
	}

	/**
	 * Loads a float to the moveFactor variable by changing the DuDv map offset over time to simulate water movement.
	 * @param factor move factor value
	 */
	public void loadMoveFactor(float factor)
	{
		super.loadFloat(location_moveFactor, factor);
	}

	/**
	 * Loads a float to the near variable (in waterFragmentShader.glsl)
	 * @param near near plane value
	 */
	public void loadNearPlane(float near)
	{
		super.loadFloat(location_near, near);
	}

	/**
	 * Loads a float to the far variable (in waterFragmentShader.glsl)
	 * @param far far plane value
	 */
	public void loadFarPlane(float far)
	{
		super.loadFloat(location_far, far);
	}

	/**
	 * Loads a projection matrix to a uniform variable (in vertex shader code).
	 * @param projection projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection)
	{
		loadMatrix(location_projectionMatrix, projection);
	}

	/**
	 * Loads the view matrix and camera position to uniform variables (in vertex shader code).
	 * Loads camera position to uniform variable.
	 * @param camera camera
	 */
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.load3DVector(location_cameraPosition, camera.getPosition());
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
