package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

import java.util.List;

/**
 * A shader program used to create static models.
 *
 * The program takes in a vertex shader or fragment shader (GLSL file)
 * and is able to access variables in the Java program.
 *
 * @author Aaron Frazer
 */
public class StaticShader extends ShaderProgram
{
    /**
     * Maximum number of lights allowed on an entity at one time
     * NOTE: Increasing this value will make rendering slower
     */
    private static final int MAX_LIGHTS = 4;

    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "src/shaders/vertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    public static String FRAGMENT_FILE = "src/shaders/fragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment shader programs
     */
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColour[];
    private int location_attenuation[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_skyColour;
    private int location_numberOfRows;
    private int location_offset;
    private int location_plane;
    private int location_density;
    private int location_gradient;
    private int location_toShadowMapSpace;
    private int location_shadowMap;
    private int location_shadowDistance;
    private int location_transitionDistance;
    private int location_mapSize;

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
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_skyColour = super.getUniformLocation("skyColour");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset = super.getUniformLocation("offset");
        location_plane = super.getUniformLocation("plane");
        location_density = super.getUniformLocation("density");
        location_gradient = super.getUniformLocation("gradient");
        location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
        location_shadowMap = super.getUniformLocation("shadowMap");
        location_shadowDistance = super.getUniformLocation("shadowDistance");
        location_transitionDistance = super.getUniformLocation("transitionDistance");
        location_mapSize = super.getUniformLocation("mapSize");

        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++)
        {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    /**
     * Loads entity shader variables to their respective uniform variables in the vertex shader.
     */
    public void connectTextureUnits()
    {
        super.loadInt(location_shadowMap, 5);
    }

    /**
     * Loads useFakeLighting variable into a uniform variable (in vertex shader code).
     * @param useFake true if fake lighting is to be used
     */
    public void loadFakeLightingVariable(boolean useFake)
    {
        super.loadBoolean(location_useFakeLighting, useFake);
    }

    /**
     * Loads numberOfRows to a uniform variable (in vertex shader).
     * @param numberOfRows number of rows in texture atlas
     */
    public void loadNumberOfRows(int numberOfRows)
    {
        super.loadFloat(location_numberOfRows, numberOfRows);
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
     * Loads offset variable to a uniform variable (in vertex shader).
     * @param x x offset
     * @param y y offset
     */
    public void loadOffset(float x, float y)
    {
        super.load2DVector(location_offset, new Vector2f(x, y));
    }

    /**
     * Loads a clip plane to a uniform variable (in vertex shader).
     * @param plane clipping plane
     */
    public void loadClipPlane(Vector4f plane)
    {
        super.load4DVector(location_plane, plane);
    }

    /**
     * Loads a transformation matrix to a uniform variable (in vertex shader code).
     * @param matrix transformation matrix
     */
    public void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    /**
     * Loads a projection matrix to a uniform variable (in vertex shader code).
     * @param projection projection matrix
     */
    public void loadProjectionMatrix(Matrix4f projection)
    {
        super.loadMatrix(location_projectionMatrix, projection);
    }

    /**
     * Loads a texture's properties to a uniform variable (in vertex shader code).
     * @param damper texture's damper
     * @param reflectivity textures' reflectivity
     */
    public void loadShineVariables(float damper, float reflectivity)
    {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
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
     * Loads a view matrix to a uniform variable (in vertex shader code.
     * @param camera camera
     */
    public void loadViewMatrix(Camera camera)
    {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    /**
     * Loads toShadowMapSpaceMatrix to a uniform variable (in vertex shdaer).
     * @param matrix shadow map matrix
     */
    public void loadToShadowSpaceMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_toShadowMapSpace, matrix);
    }

    /**
     * Loads shadowDistance to a uniform variable (in vertex shader).
     * @param shadowDistance shadow distance
     */
    public void loadShadowDistance(float shadowDistance)
    {
        super.loadFloat(location_shadowDistance, shadowDistance);
    }

    /**
     * Loads transitionDistance to a uniform variable (in vertex shader).
     * @param transitionDistance transition distance
     */
    public void loadTransitionDistance(float transitionDistance)
    {
        super.loadFloat(location_transitionDistance, transitionDistance);
    }

    /**
     * Loads mapSize to a uniform variable (in vertex shader).
     * @param mapSize shadow map size
     */
    public void loadMapSize(float mapSize)
    {
        super.loadFloat(location_mapSize, mapSize);
    }

    /**
     * Loads light properties to all uniform variables for lights in the shader program.
     * @param lights list of lights
     */
    public void loadLights(List<Light> lights)
    {
        for (int i = 0; i < MAX_LIGHTS; i++)
        {
            if (i < lights.size())
            {
                super.load3DVector(location_lightPosition[i], lights.get(i).getPosition());
                super.load3DVector(location_lightColour[i], lights.get(i).getColour());
                super.load3DVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else
            {
                super.load3DVector(location_lightPosition[i], new Vector3f(0, 0, 0));
                super.load3DVector(location_lightColour[i], new Vector3f(0, 0, 0));
                super.load3DVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

}
