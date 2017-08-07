package renderEngine;

import cameras.CameraManager;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrain.Terrain;
import toolbox.GameSettings;
import toolbox.MousePicker;

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
	 * Field of view is the extent the observable world that is seen at
	 * any given moment
	 */
	private static final float FOV = 70;

	/**
	 * Near plane - closest location that will be rendered
	 */
	private static final float NEAR_PLANE = 0.1f;

	/**
	 * Far plane - farthest location that will be rendered
	 */
	private static final float FAR_PLANE = 1000.0f;

	/**
	 * Projection matrix
	 */
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
	 * Skybox renderer
	 */
	private SkyboxRenderer skyboxRenderer;

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

	/**
	 * Creates a master renderer by initializing an entity renderer
	 * and a terrain renderer.
	 * @param loader - loader for skybox
	 */
	public MasterRenderer(Loader loader)
	{
		enableCulling();

		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
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
	 * @param lights list of lights
	 * @param camera camera
	 * @param clipPlane clip plane
	 */
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane)
	{
		prepare();

		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(GameSettings.RED, GameSettings.GREEN, GameSettings.BLUE); // Color of entities
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();

		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColour(GameSettings.RED, GameSettings.GREEN, GameSettings.BLUE); // Color of terrain
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();

		skyboxRenderer.render(camera, GameSettings.RED, GameSettings.GREEN, GameSettings.BLUE); // Color of fog

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
	 * Processes an entity by putting it into an associated hashmap of it's model.
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
		GL11.glClearColor(0.5f, 0.5f, 0.5f, 1);
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

	/**
	 * Returns the projection matrix.
	 * @return projectionMatrix
	 */
	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}

	/**
	 * Renders the entire scene of the game from scratch.
	 *
	 * @param entities list of entities
	 * @param terrains list of terrains
	 * @param lights list of lights
	 * @param cameraManager camera manager (with cameras)
	 * @param picker mouse picker
	 * @param clipPlane clip plane, where (x, y, z) = plane's normal and D = clipping distance
	 */
	public void renderScene(Player player, List<Entity> entities, ArrayList<Terrain> terrains, List<Light> lights, CameraManager cameraManager, MousePicker picker, Vector4f clipPlane)
	{
		cameraManager.update(cameraManager); // update current camera selected
		cameraManager.getCurrentCamera().move(); // move current camera

		if (picker != null)
		{
			picker.setCamera(cameraManager.getCurrentCamera());
			picker.update();
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if (terrainPoint != null)
			{
				if (Mouse.isButtonDown(0))
				{
//					entities.get(0).setPosition(new Vector3f(terrainPoint.getX(), terrainPoint.getY() + 30f, terrainPoint.getZ())); // sunEntity
//					lights.get(0).setPosition(new Vector3f(terrainPoint.getX(), terrainPoint.getY() + 30f, terrainPoint.getZ())); // sunLight
				}
			}
//			System.out.println(picker.getCurrentRay());
		}

		render(lights, cameraManager.getCurrentCamera(), clipPlane);

		for (Terrain terrain : terrains)
		{
			if (player != null)
			{
				if (terrain.isEntityInsideTerrain(player))
				{
					processTerrain(terrain);
					player.move(terrain);
				}
			} else
			{
				processTerrain(terrain);
			}
		}

		for (Entity entity : entities)
		{
//				entity.increaseRotation(0, 1, 0);
			processEntity(entity);
		}
	}

	/**
	 * Returns the near plane value.
	 * @return near plane
	 */
	public static float getNearPlane()
	{
		return NEAR_PLANE;
	}

	/**
	 * Returns the far plane value.
	 * @return far plane
	 */
	public static float getFarPlane()
	{
		return FAR_PLANE;
	}
}
