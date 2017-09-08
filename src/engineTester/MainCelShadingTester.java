package engineTester;

import cameras.CameraManager;
import cameras.FreeRoamCamera;
import entities.Camera;
import entities.Entity;
import entities.Light;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxShader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.GameSettings;
import toolbox.InputHelper;
import toolbox.MousePicker;
import toolbox.VirtualClock;
import water.WaterFrameBuffers;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main class used to test cel shading.
 *
 * @author Aaron Frazer
 */
public class MainCelShadingTester
{

    /**
     * Global variables pulled from MainGameLoop.
     */
    private static List<Entity> entities = MainGameLoop.entities;
    private static List<Entity> normalMapEntities = MainGameLoop.normalMapEntities;
    private static List<Terrain> terrains = MainGameLoop.terrains;
    private static List<Light> lights = MainGameLoop.lights;
    private static List<WaterTile> waters = MainGameLoop.waters;

    /**
     * Runs the demo for cel shading
     *
     * @param args arguments
     */
    public static void main(String[] args)
    {
        DisplayManager.createDisplay();
        Loader loader = new Loader();

        //*********** SETTINGS ************
        GameSettings.DAY_ONLY_MODE = true;
        GameSettings.FOG_ENABLED = true;
        Terrain.SIZE = 2000;
        //*********************************

        //********** TERRAIN TEXTURES **********
        TerrainTexture grassTexture = new TerrainTexture(loader.loadGameTexture("celGrassTexture"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadGameTexture("dirtTexture"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadGameTexture("celGrassTexture"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadGameTexture("pathTexture"));

        TerrainTexturePack texturePack1 = new TerrainTexturePack(grassTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadGameTexture("blendMapCelShading"));
        //**************************************

        //********** TERRAIN CREATION **********
        Terrain terrain1 = new Terrain(0, 0, loader, texturePack1, blendMap, "heightmap");
        terrains.add(terrain1);
        //**************************************

        //********** LIGHT CREATION **********
        Light sunLight = new Light(new Vector3f(50, 10000, 50), new Vector3f(1, 1, 1));
        lights.add(sunLight); // sun (no attenuation)
        //************************************

        //********** ENTITY CREATION ***********
        RawModel bobbleTreeModel = OBJFileLoader.loadOBJ("bobbleTreeModel", loader);
        TexturedModel bobbleTreeTexturedModel = new TexturedModel(bobbleTreeModel, new ModelTexture(
                loader.loadGameTexture("bobbleTreeTexture")));

        RawModel rockRawModel = OBJFileLoader.loadOBJ("toonRocksModel", loader);
        TexturedModel rockTexturedModel = new TexturedModel(rockRawModel, new ModelTexture(
                loader.loadGameTexture("toonRocksTexture")));

        Random random = new Random(676452);
        for (Terrain terrain : terrains)
        {
            for (int i = 0; i < 100; i++)
            {
                float x = random.nextInt((int) Terrain.getSize());
                float z = random.nextInt((int) Terrain.getSize());
                float y = terrain.getHeightOfTerrain(x, z);
                Entity pineEntity = new Entity(bobbleTreeTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 1);
                entities.add(pineEntity);

                x = random.nextInt((int) Terrain.getSize());
                z = random.nextInt((int) Terrain.getSize());
                y = terrain.getHeightOfTerrain(x, z);
                Entity rockEntity = new Entity(rockTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 3);
                entities.add(rockEntity);
            }
        }
        //**************************************

        //********** CAMERA CREATION **********
        CameraManager cameraManager = new CameraManager();
        FreeRoamCamera frcamera = new FreeRoamCamera(new Vector3f(50, 75, 170));
        cameraManager.addCamera(frcamera);
        cameraManager.setCurrentCamera(frcamera);
        //*************************************

        MasterRenderer renderer = new MasterRenderer(loader);

        //********** GUI TEXTURES **********
        List<GuiTexture> guiTextures = new ArrayList<>();
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        //**********************************

        //********** MOUSE PICKER **********
        // TODO: Make mouse picker work for all cameras
        MousePicker picker = new MousePicker(frcamera, renderer.getProjectionMatrix(), terrains.get(0));
        //**********************************

        //********** WATER RENDERING **********
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
//        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
        waters.add(new WaterTile(50, 52, 36));

//        GuiTexture refractionGui = new GuiTexture(fbos.getRefractionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//        GuiTexture reflectionGui = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//		guiTextures.add(refractionGui);
//		guiTextures.add(reflectionGui);
        //*****************************************

        while (!Display.isCloseRequested()) { // loops until exit button pushed

            VirtualClock.update();
            InputHelper.update();
            cameraManager.update(cameraManager);

            Camera camera = cameraManager.getCurrentCamera();
            camera.move();

            WaterTile water = waters.get(0);

            if (InputHelper.isKeyPressed(Keyboard.KEY_C))
            {
                if (GameSettings.CEL_SHADING)
                {
                    GameSettings.CEL_SHADING = false;
                    Random rand = new Random();
                    GameSettings.FOG_RED = rand.nextFloat();
                    GameSettings.FOG_GREEN = rand.nextFloat();
                    GameSettings.FOG_BLUE = rand.nextFloat();
                    StaticShader.FRAGMENT_FILE = "src/shaders/fragmentShader.glsl";
                    TerrainShader.FRAGMENT_FILE = "src/shaders/terrainFragmentShader.glsl";
                    SkyboxShader.FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.glsl";
                    renderer = new MasterRenderer(loader);
                    System.out.println("Cel shading is OFF");
                } else
                {
                    GameSettings.CEL_SHADING = true;
                    GameSettings.FOG_RED = 1.0f;
                    GameSettings.FOG_GREEN = 0.3f;
                    GameSettings.FOG_BLUE = 0.7f;
                    StaticShader.FRAGMENT_FILE = "src/shaders/celFragmentShader.glsl";
                    TerrainShader.FRAGMENT_FILE = "src/shaders/celTerrainFragmentShader.glsl";
                    SkyboxShader.FRAGMENT_FILE = "src/skybox/celSkyboxFragmentShader.glsl";
                    renderer = new MasterRenderer(loader);
                    System.out.println("Cel shading is ON");
                }
            }

            if (InputHelper.isKeyPressed(Keyboard.KEY_R))
            {
                GameSettings.WIREFRAME_ENABLED = !GameSettings.WIREFRAME_ENABLED;
            }

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            // Render reflection frame buffer
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            camera.invertRoll();
            renderer.renderScene(null, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
            camera.getPosition().y += distance;
            camera.invertPitch();
            camera.invertRoll();

            // Render refraction frame buffer
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(null, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, -1, 0, water.getHeight() + 1f));

            // Render to screen
            fbos.unbindCurrentFrameBuffer();
            renderer.renderScene(null, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, 0, 0, 0));

            DisplayManager.updateDisplay();
        }

        fbos.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
