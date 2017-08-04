package engineTester;

import cameras.CameraManager;
import cameras.FreeRoamCamera;
import cameras.LockOnCamera;
import entities.Camera;
import entities.Entity;
import entities.Light;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.GameSettings;
import toolbox.InputHelper;
import toolbox.MousePicker;
import toolbox.VirtualClock;
import water.WaterFrameBuffers;
import water.WaterRenderer;
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
     * Runs the demo for cel shading
     *
     * @param args arguments
     */
    public static void main(String[] args)
    {
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        // *********** VARIABLES ***************
        MainGameLoop.entities = new ArrayList<>();
        MainGameLoop.terrains = new ArrayList<>();
        MainGameLoop.lights = new ArrayList<>();
        List<Entity> entities = MainGameLoop.entities;
        ArrayList<Terrain> terrains = MainGameLoop.terrains;
        List<Light> lights = MainGameLoop.lights;
        // *************************************

        //*********** SETTINGS ************
        GameSettings.DAY_ONLY_MODE = true;
        GameSettings.FOG_ENABLED = true;
        //*********************************

        //********** TERRAIN TEXTURES **********
        TerrainTexture grassTexture = new TerrainTexture(loader.loadTexture("grassTexture"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirtTexture"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowersTexture"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("pathTexture"));

        TerrainTexturePack texturePack1 = new TerrainTexturePack(grassTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMapCelShading"));
        //**************************************

        //********** TERRAIN CREATION **********
        Terrain waterTerrain = new Terrain(0, 0, loader, texturePack1, blendMap, "heightmap");
        terrains.add(waterTerrain);
        //**************************************

        //********** LIGHT CREATION **********
        Light sunLight = new Light(new Vector3f(50, 10000, 50), new Vector3f(1, 1, 1));
        lights.add(sunLight); // sun (no attenuation)
        //************************************

        //********** ENTITY CREATION ***********
        ModelData pineModelData = OBJFileLoader.loadOBJ("bobbleTreeModel");
        RawModel pineRawModel = loader.loadToVAO(pineModelData.getVertices(), pineModelData.getTextureCoords(), pineModelData.getNormals(), pineModelData.getIndices());
        TexturedModel pineTexturedModel = new TexturedModel(pineRawModel, new ModelTexture(loader.loadTexture("bobbleTreeTexture")));

        Random random = new Random(676452);
        for (Terrain terrain : MainGameLoop.terrains)
        {
            for (int i = 0; i < 100; i++)
            {
                float x = random.nextInt((int) Terrain.getSize());
                float z = random.nextInt((int) Terrain.getSize());
                float y = terrain.getHeightOfTerrain(x, z);
                System.out.println(y);
                if (y > 32) // only render trees above water
                {
                    Entity pineEntity = new Entity(pineTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 5);
                    entities.add(pineEntity);
                }
            }
        }
        //**************************************

        //********** CAMERA CREATION **********
        FreeRoamCamera frcamera = new FreeRoamCamera(new Vector3f(50, 75, 170));
        CameraManager cameraManager = new CameraManager();
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
        MainGameLoop.waters = new ArrayList<>();
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
        MainGameLoop.waters.add(new WaterTile(50, 52, 36));

        GuiTexture refractionGui = new GuiTexture(fbos.getRefractionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        GuiTexture reflectionGui = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//		guiTextures.add(refractionGui);
//		guiTextures.add(reflectionGui);
        //*****************************************

        // TODO: Add a method to Terrain that will get the highest/lowest part on the terrain

        while (!Display.isCloseRequested()) { // loops until exit button pushed

            VirtualClock.update();
            InputHelper.update();

            Camera camera = cameraManager.getCurrentCamera();
            WaterTile water = MainGameLoop.waters.get(0);

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            // Render reflection frame buffer
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            camera.invertRoll();
            renderer.renderScene(null, entities, terrains, lights, cameraManager, picker, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
            camera.getPosition().y += distance;
            camera.invertPitch();
            camera.invertRoll();

            // Render refraction frame buffer
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(null, entities, terrains, lights, cameraManager, picker, new Vector4f(0, -1, 0, water.getHeight() + 1f));

            // Render to screen
            fbos.unbindCurrentFrameBuffer();
            renderer.renderScene(null, entities, terrains, lights, cameraManager, picker,
                    new Vector4f(0, 0, 0, 0));

            // Game logic


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
