package skybox;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;

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
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.glsl";

	/**
	 * Location of projection matrix variable
	 */
	private int location_projectionMatrix;

	/**
	 * Location of view matrix variable
	 */
	private int location_viewMatrix;

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
	 * stays in a fixed position when moving the camera.
	 *
	 * @param camera
	 */
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		super.loadMatrix(location_viewMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
	}

}
