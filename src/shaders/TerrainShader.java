package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolbox.Maths;

/**
 * A shader program used to create terrain models.
 * 
 * @author Aaron Frazer
 */
public class TerrainShader extends ShaderProgram
{
	/**
	 * Filepath of vertex shader file.
	 */
	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.glsl";
	
	/**
	 * Filepath of fragment shader file.
	 */
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.glsl";

	/**
	 * Location of transformation matrix variable
	 */
	private int location_transformationMatrix;
	
	/**
	 * Location of projection matrix variable
	 */
	private int location_projectionMatrix;
	
	/**
	 * Location of view matrix variable
	 */
	private int location_viewMatrix;
	
	/**
	 * Location of the light position variable
	 */
	private int location_lightPosition;
	
	/**
	 * Location of the light color variable
	 */
	private int location_lightColour;
	
	/**
	 * Location of texture's shine damper variable
	 */
	private int location_shineDamper;
	
	/**
	 * Location of texture's reflectivity variable
	 */
	private int location_reflectivity;
	
	/**
	 * Location of sky colour variable
	 */
	private int location_skyColour;
	
	/**
	 * Location of background texture variable
	 */
	private int location_backgroundTexture;
	
	/**
	 * Location of red texture variable
	 */
	private int location_rTexture;
	
	/**
	 * Location of green texture variable
	 */
	private int location_gTexture;
	
	/**
	 * Location of blue texture variable
	 */
	private int location_bTexture;
	
	/**
	 * Location of blend map variable
	 */
	private int location_blendMap;
	
	/**
	 * Creates a static shader program.
	 */
	public TerrainShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations()
	{
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyColour = super.getUniformLocation("skyColour");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
	}
	
	/**
	 * Loads up an int to each sample2Ds to indicate which texture unit they should be referencing.
	 */
	public void connectTextureUnits()
	{
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	
	/**
	 * Loads a sky colour value to a uniform variable (in vertex shader code).
	 * @param r - red
	 * @param g - green
	 * @param b - blue
	 */
	public void loadSkyColour(float r, float g, float b)
	{
		super.load3DVector(location_skyColour, new Vector3f(r, g, b));
	}
	
	/**
	 * Loads a texture's properties to a uniform variable (in vertex shader code).
	 * @param damper - texture's damper
	 * @param reflectivity - texture's reflectivity
	 */
	public void loadShineVariables(float damper, float reflectivity)
	{
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
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
	 * Loads light properties to a uniform variables.
	 * @param light - light
	 */
	public void loadLight(Light light)
	{
		super.load3DVector(location_lightPosition, light.getPosition());
		super.load3DVector(location_lightColour, light.getColour());
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
