package renderEngine;

import cameras.CameraManager;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.TexturedModel;
import normalMappingRenderer.NormalMappingRenderer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
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
 * A class responsible for handling all rendering code in the game.
 * @author Aaron Frazer
 */
public class MasterRenderer
{
    /**
     * Field of view - the extent of the observable world
     * that is seen at any given moment
     */
    private static final float FOV = 70;

    /**
     * Near plane - closest location that will be rendered
     */
    private static final float NEAR_PLANE = 0.1f;

    /**
     * Far plane - farthest location that will be renederd
     */
    private static final float FAR_PLANE = 1000;

    /**
     * Projection matrix
     */
    private Matrix4f projectionMatrix;

    /**
     * Instance of a static shader
     */
    private StaticShader shader = new StaticShader();

    /**
     * Instance of a terrain shader program
     */
    private TerrainShader terrainShader = new TerrainShader();

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
     * Renderer for entities with normal mapping
     */
    private NormalMappingRenderer normalMapRenderer;

    /**
     * Hash map that contains all textured models and respective entities that
     * need to be rendered for a particular frame
     */
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    /**
     * Hash map that contains all textured models and respective entities
     * that use normal mapping
     */
    private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<>();

    /**
     * List of terrains to be rendered
     */
    private List<Terrain> terrains = new ArrayList<>();

    /**
     * Creates a master rendering program by initializing entity renderer and terrain renderer.
     * @param loader loader for sky box
     */
    public MasterRenderer(Loader loader)
    {
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
        normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
    }

    /**
     * Renders the entire scene of the game from scratch.  Called from MainGameLoop.
     * @param entities list of entities
     * @param normalMapEntities list of entities using normal mapping
     * @param terrains list of terrains
     * @param lights list of lights
     * @param cameraManager camera manager
     * @param picker mouse picker
     * @param clipPlane clipping plane
     */
    public void renderScene(Player player, List<Entity> entities, List<Entity> normalMapEntities, List<Terrain> terrains, List<Light> lights, CameraManager cameraManager, MousePicker picker, Vector4f clipPlane)
    {
//        cameraManager.update(cameraManager); // update current camera selected

//        if (picker != null)
//        {
//            picker.setCamera(cameraManager.getCurrentCamera());
//            picker.update();
//            Vector3f terrainPoint = picker.getCurrentTerrainPoint();
//            if (terrainPoint != null)
//            {
//                if (InputHelper.isButtonDown(0))
//                {
////					entities.get(0).setPosition(new Vector3f(terrainPoint.getX(), terrainPoint.getY() + 30f, terrainPoint.getZ())); // sunEntity
////					lights.get(0).setPosition(new Vector3f(terrainPoint.getX(), terrainPoint.getY() + 30f, terrainPoint.getZ())); // sunLight
//                }
//            }
////			System.out.println(picker.getCurrentRay());
//        }

        for (Terrain terrain : terrains)
        {
//            if (player != null)
//            {
//                if (terrain.isEntityInsideTerrain(player))
//                {
                    processTerrain(terrain);
//                    player.move(terrain);
//                }
//            } else
//            {
//                processTerrain(terrain);
//            }
        }
        for (Entity entity : entities)
        {
            processEntity(entity);
        }
        for (Entity entity : normalMapEntities)
        {
            processNormalMapEntity(entity);
        }
        render(lights, cameraManager.getCurrentCamera(), clipPlane);
    }

    /**
     * ThinMatrix's renderScene method.
     * @param lights list of lights
     * @param camera camera
     * @param clipPlane clipping plane
     */
    public void renderScene(List<Entity> entities, List<Entity> normalEntities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane)
    {
        for (Terrain terrain : terrains) {
            processTerrain(terrain);
        }
        for (Entity entity : entities) {
            processEntity(entity);
        }
        for(Entity entity : normalEntities){
            processNormalMapEntity(entity);
        }
        render(lights, camera, clipPlane);
    }

    /**
     * Renders all entities and lights in the scene.
     * @param lights list of lights
     * @param camera camera
     * @param clipPlane clipping plane
     */
    public void render(List<Light> lights, Camera camera, Vector4f clipPlane)
    {
        prepare();

        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(GameSettings.RED, GameSettings.GREEN, GameSettings.BLUE); // color of entities
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();

        normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);

        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColour(GameSettings.RED, GameSettings.GREEN, GameSettings.BLUE); // color of terrain
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        skyboxRenderer.render(camera, GameSettings.RED, GameSettings.GREEN, GameSettings.BLUE); // color of fog

        terrains.clear();
        entities.clear();
        normalMapEntities.clear();
    }

    /**
     * Renders a terrain to the screen.
     * @param terrain terrain to be added
     */
    private void processTerrain(Terrain terrain)
    {
        terrains.add(terrain);
    }

    /**
     * Processes an enityt by putting it into an associated hash map of it's model.
     * @param entity entity to be processed
     */
    private void processEntity(Entity entity)
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
     * Processes an entity that uses normal mapping.
     * @param entity normal mapped entity to be processed
     */
    private void processNormalMapEntity(Entity entity)
    {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = normalMapEntities.get(entityModel);
        if (batch != null)
        {
            batch.add(entity);
        } else
        {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            normalMapEntities.put(entityModel, newBatch);
        }
    }

    /**
     * Enables back face culling.
     * Does not render backside of model; used for optimization.
     */
    public static void enableCulling()
    {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Disables back face culling.
     */
    public static void disableCulling()
    {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    /**
     * Prepares OpenGL to render the game.
     * Called once every frame.
     */
    private void prepare()
    {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(GameSettings.RED, GameSettings.GREEN, GameSettings.BLUE, 1);
    }

    /**
     * Cleans up vertex and fragment shaders.
     * Called when game is closed.
     */
    public void cleanUp()
    {
        shader.cleanUp();
        terrainShader.cleanUp();
        normalMapRenderer.cleanUp();
    }

    /**
     * Creates a projection matrix.
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
     * @return projection matrix
     */
    public Matrix4f getProjectionMatrix()
    {
        return this.projectionMatrix;
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