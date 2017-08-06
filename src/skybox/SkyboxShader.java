package skybox;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;

import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

/**
 * A shader program used to create skyboxes.
 *
 * @author Aaron Frazer
 */
public class SkyboxShader extends ShaderProgram
{
	/**
	 * Filepath of vertex shader file
	 */
	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.glsl";

	/**
	 * Filepath of fragment shader file
	 */
	public static String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.glsl";

	/**
	 * Rotation speed of skybox (degrees per second)
	 */
	private static final float ROTATE_SPEED = 1f;

	/**
	 * Current rotation of skybox (in degrees)
	 */
	private float rotation = 0;

	/**
	 * Location of projectionMatrix variable
	 */
	private int location_projectionMatrix;

	/**
	 * Location of viewMatrix variable
	 */
	private int location_viewMatrix;

	/**
	 * Location of fogColour variable
	 */
	private int location_fogcolour;

	/**
	 * Location of cubeMap variable
	 */
	private int location_cubeMap;

	/**
	 * Location of cubeMap2 variable
	 */
	private int location_cubeMap2;

	/**
	 * Location of blendFactor variable
	 */
	private int location_blendFactor;

	/**
	 * Creates a skybox shader program.
	 */
	public SkyboxShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	/**
	 * Loads a projection matrix to a uniform variable in vertex shader code.
	 *
	 * @param matrix - projection matrix
	 */
	public void loadProjectionMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	/**
	 * Creates a view matrix for the skybox.  Makes sure that the skybox
	 * stays in a fixed position when moving the camera.  Rotates the skybox
	 * depending on rotation speed.
	 *
	 * @param camera
	 */
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}

	/**
	 * Loads the fog colour to a uniform variable in the vertex shader.
	 * @param r - red color value
	 * @param g - green color value
	 * @param b - blue color value
	 */
	public void loadFogColour(float r, float g, float b)
	{
		super.load3DVector(location_fogcolour, new Vector3f(r, g, b));
	}

	/**
	 * Loads cubemap variables to their respective uniform variables in
	 * the vertex shader.
	 */
	public void connectTextureUnits()
	{
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}

	/**
	 * Loads a blend factor to a uniform variable in the vertex shader.
	 * @param blend - blend factor
	 */
	public void loadBlendFactor(float blend)
	{
		super.loadFloat(location_blendFactor, blend);
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogcolour = super.getUniformLocation("fogColour");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
		location_blendFactor = super.getUniformLocation("blendFactor");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
	}

}
