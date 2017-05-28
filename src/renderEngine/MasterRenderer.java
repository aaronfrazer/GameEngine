package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for handling all rendering code in the game.
 *
 * @author Aaron Frazer
 */
public class MasterRenderer
{
	/**
	 * Field of view is the exent the observable world that is seen at
	 * any given moment.
	 */
	private static final float FOV = 70;

	/**
	 * Near plane is the closest location that will be rendered.
	 */
	private static final float NEAR_PLANE = 0.1f;

	/**
	 * Far plane is the farthest location that will be rendered.
	 */
	private static final float FAR_PLANE = 1000;

	/**
	 * RGB color values for the sky
	 */
	private static final float RED = 0.5f,
			GREEN = 0.5f,
			BLUE = 0.5f;

	private Matrix4f projectionMatrix;

	/**
	 * Instance of a static shader
	 */
	private StaticShader shader = new StaticShader();

	/**
	 * Entity renderer
	 */
	private EntityRenderer renderer;

	/**
	 * Terrain renderer
	 */
	private TerrainRenderer terrainRenderer;

	/**
	 * Instance of a terrain shader program.
	 */
	private TerrainShader terrainShader = new TerrainShader();

	/**
	 * Hashmap that contains all textured models and respective entities that need to be
	 * rendered for a particular frame.
	 */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

	/**
	 * List of terrains to be rendered
	 */
	private List<Terrain> terrains = new ArrayList<>();

	public MasterRenderer()
	{
		enableCulling();

		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);

		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}

	/**
	 * Enables backface culling.
	 * Doesn't render backside of model (for optimization).
	 */
	static void enableCulling()
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	/**
	 * Disables backface culling.
	 */
	static void disableCulling()
	{
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	/**
	 * Renders all entities and lights in the scene.
	 *
	 * @param lights - list of lights
	 * @param camera - camera
	 */
	public void render(List<Light> lights, Camera camera)
	{
		prepare();

		shader.start();
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();

		terrainShader.start();
		terrainShader.loadSkyColour(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();

		entities.clear();
	}

	/**
	 * Renders a terrain to the screen.
	 *
	 * @param terrain - terrain to be added
	 */
	public void processTerrain(Terrain terrain)
	{
		terrains.add(terrain);
	}

	/**
	 * Processes an entity by putting it into a hashmap.
	 *
	 * @param entity - entity to be processed
	 */
	public void processEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null)
		{
			batch.add(entity);
		} else
		{
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	/**
	 * Cleans up vertex and fragment shaders.  Called when game is closed.
	 */
	public void cleanUp()
	{
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	/**
	 * Prepares OpenGL to render the game.
	 * Called once every frame.
	 */
	private void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1); // blue color
	}

	/**
	 * Creates a projeciton matrix (don't worry about how the math works
	 * in this method).
	 */
	private void createProjectionMatrix()
	{
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

}
