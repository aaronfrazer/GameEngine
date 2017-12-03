package water;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;
import toolbox.Maths;

/**
 * A shader program used to create water models.
 *
 * @author Aaron Frazer
 */
public class WaterShader extends ShaderProgram
{
    /**
     * Filepath of vertex shader
     */
    private final static String VERTEX_FILE = "water/waterVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private final static String FRAGMENT_FILE = "water/waterFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment programs
     */
    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvMap;
    private int location_moveFactor;
    private int location_cameraPosition;
    private int location_normalMap;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_skyColour;
    private int location_depthMap;
    private int location_near;
    private int location_far;
    private int location_density;
    private int location_gradient;

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
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_modelMatrix = super.getUniformLocation("modelMatrix");
        location_reflectionTexture = super.getUniformLocation("reflectionTexture");
        location_refractionTexture = super.getUniformLocation("refractionTexture");
        location_dudvMap = super.getUniformLocation("dudvMap");
        location_moveFactor = super.getUniformLocation("moveFactor");
        location_cameraPosition = super.getUniformLocation("cameraPosition");
        location_normalMap = super.getUniformLocation("normalMap");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColour = super.getUniformLocation("lightColour");
        location_skyColour = super.getUniformLocation("skyColour");
        location_depthMap = super.getUniformLocation("depthMap");
        location_near = super.getUniformLocation("near");
        location_far = super.getUniformLocation("far");
        location_density = super.getUniformLocation("density");
        location_gradient = super.getUniformLocation("gradient");
    }

    /**
     * Binds reflection, refraction, and DuDv map variables to sampler2D (in vertex shader - waterFragmentShader.glsl).
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
     * @param sun sun light
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
     * Loads a sky color to a uniform variable (in vertex shader).
     * @param r red value
     * @param g green value
     * @param b blue value
     */
    public void loadSkyColour(float r, float g, float b)
    {
        super.load3DVector(location_skyColour, new Vector3f(r, g, b));
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
     * Loads a model matrix to a uniform variable (in vertex shader).
     * @param modelMatrix model matrix
     */
    public void loadModelMatrix(Matrix4f modelMatrix)
    {
        loadMatrix(location_modelMatrix, modelMatrix);
    }

    /**
     * Loads density to a uniform variable (in vertex shader).
     * @param density density of fog
     */
    public void loadDensity(float density)
    {
        super.loadFloat(location_density, density);
    }

    /**
     * Loads gradient to a uniform variable (in vertex shader).
     * @param gradient gradient of fog
     */
    public void loadGradient(float gradient)
    {
        super.loadFloat(location_gradient, gradient);
    }

    /**
     * Loads a float to the near variable (in fragment shader).
     * @param near near plane value
     */
    public void loadNearPlane(float near)
    {
        super.loadFloat(location_near, near);
    }

    /**
     * Loads a float to the far variable (in fragment shader).
     * @param far far plane value
     */
    public void loadFarPlane(float far)
    {
        super.loadFloat(location_far, far);
    }


}
