package normalMappingRenderer;

import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shaders.ShaderProgram;

import java.util.List;

/**
 * A shader program used to create entities with normal mapping.
 *
 * The program takes in a vertex shader or fragment shader (GLSL file)
 * and is able to access variables in the Java program.
 *
 * @author Aaron Frazer
 */
public class NormalMappingShader extends ShaderProgram
{
    /**
     * Maximum number of lights allowed on an entity at one time
     * NOTE: Increasing this value will make rendering slower
     */
    private static final int MAX_LIGHTS = 4;

    /**
     * Filepath of vertex shader
     */
    private static final String VERTEX_FILE = "normalMappingRenderer/normalMapVertexShader.glsl";

    /**
     * Filepath of fragment shader
     */
    private static final String FRAGMENT_FILE = "normalMappingRenderer/normalMapFragmentShader.glsl";

    /**
     * Location of uniform variables in vertex/fragment shader programs
     */
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPositionEyeSpace[];
    private int location_lightColour[];
    private int location_attenuation[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;
    private int location_numberOfRows;
    private int location_offset;
    private int location_plane;
    private int location_modelTexture;
    private int location_normalMap;
    private int location_density;
    private int location_gradient;
    private int location_specularMap;
    private int location_usesSpecularMap;

    /**
     * Creates a normal mapping shader program.
     */
    public NormalMappingShader()
    {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes()
    {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
        super.bindAttribute(3, "tangent");
    }

    @Override
    protected void getAllUniformLocations()
    {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skyColour = super.getUniformLocation("skyColour");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset = super.getUniformLocation("offset");
        location_plane = super.getUniformLocation("plane");
        location_modelTexture = super.getUniformLocation("modelTexture");
        location_normalMap = super.getUniformLocation("normalMap");
        location_density = super.getUniformLocation("density");
        location_gradient = super.getUniformLocation("gradient");
        location_specularMap = super.getUniformLocation("specularMap");
        location_usesSpecularMap = super.getUniformLocation("usesSpecularMap");

        location_lightPositionEyeSpace = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++)
        {
            location_lightPositionEyeSpace[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
            location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    /**
     * Loads model's texture and normal map into uniform variable (in vertex shader).
     */
    protected void connectTextureUnits()
    {
        super.loadInt(location_modelTexture, 0);
        super.loadInt(location_normalMap, 1);
        super.loadInt(location_specularMap, 2);
    }

    /**
     * Loads useSpecularMap variable into a uniform variable (in vertex shader code).
     * @param useMap true if specular map is to be used
     */
    public void loadUseSpecularMap(boolean useMap)
    {
        super.loadBoolean(location_usesSpecularMap, useMap);
    }

    /**
     * Loads a transformation matrix to a uniform variable (in vertex shader code).
     * @param matrix transformation matrix
     */
    protected void loadTransformationMatrix(Matrix4f matrix)
    {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    /**
     * Loads a projection matrix to a uniform variable (in vertex shader code).
     * @param projection projection matrix
     */
    protected void loadProjectionMatrix(Matrix4f projection)
    {
        super.loadMatrix(location_projectionMatrix, projection);
    }

    /**
     * Loads a view matrix to a uniform variable (in vertex shader code).
     * @param viewMatrix view matrix
     */
    protected void loadViewMatrix(Matrix4f viewMatrix)
    {
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    /**
     * Loads light properties to all uniform variables for lights in the shader program.
     * @param lights list of lights
     */
    protected void loadLights(List<Light> lights, Matrix4f viewMatrix)
    {
        for (int i = 0; i < MAX_LIGHTS; i++)
        {
            if (i < lights.size())
            {
                super.load3DVector(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
                super.load3DVector(location_lightColour[i], lights.get(i).getColour());
                super.load3DVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else
            {
                super.load3DVector(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
                super.load3DVector(location_lightColour[i], new Vector3f(0, 0, 0));
                super.load3DVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
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
     * Loads a texture's properties to a uniform variable (in vertex shader code).
     * @param damper texture's damper
     * @param reflectivity textures' reflectivity
     */
    protected void loadShineVariables(float damper, float reflectivity)
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
    protected void loadSkyColour(float r, float g, float b)
    {
        super.load3DVector(location_skyColour, new Vector3f(r, g, b));
    }

    /**
     * Loads numberOfRows to a uniform variable (in vertex shader).
     * @param numberOfRows number of rows in texture atlas
     */
    protected void loadNumberOfRows(int numberOfRows)
    {
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    /**
     * Loads offset variable to a uniform variable (in vertex shader).
     * @param x x offset
     * @param y y offset
     */
    protected void loadOffset(float x, float y)
    {
        super.load2DVector(location_offset, new Vector2f(x, y));
    }

    /**
     * Loads a clip plane to a uniform variable (in vertex shader).
     * @param plane clipping plane
     */
    protected void loadClipPlane(Vector4f plane)
    {
        super.load4DVector(location_plane, plane);
    }

    /**
     * Returns the position of eye space coordinates
     * @param light light
     * @param viewMatrix view matrix
     * @return 3D coordinates of eye space
     */
    private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix)
    {
        Vector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.x, position.y, position.z, 1f);
        Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);

        return new Vector3f(eyeSpacePos);
    }

}
