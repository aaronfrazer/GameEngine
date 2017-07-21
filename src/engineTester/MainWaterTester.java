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
 * Main class used to test water rendering.
 *
 * @author Aaron Frazer
 */
public class MainWaterTester
{

    /**
     * Runs the demo for water rendering
     *
     * @param args - arguments
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

        GameSettings.DAY_ONLY_MODE = true;
        GameSettings.FOG_ENABLED = true;
        Terrain.SIZE = 100;

        //********** TERRAIN TEXTURES **********
        TerrainTexture grassTexture = new TerrainTexture(loader.loadTexture("grassTexture"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirtTexture"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowersTexture"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("pathTexture"));

        TerrainTexturePack texturePack1 = new TerrainTexturePack(grassTexture, rTexture, gTexture, bTexture);
        TerrainTexturePack texturePackUnder1 = new TerrainTexturePack(rTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMapBlack"));
        //**************************************

        //********** TERRAIN CREATION **********
        Terrain waterTerrain = new Terrain(0, 0, loader, texturePackUnder1, blendMap, "heightmapWater");
        terrains.add(waterTerrain);
        //**************************************

        //********** ENTITY CREATION ***********
        ModelData pineModelData = OBJFileLoader.loadOBJ("pineModel");
        RawModel pineRawModel = loader.loadToVAO(pineModelData.getVertices(), pineModelData.getTextureCoords(), pineModelData.getNormals(), pineModelData.getIndices());
        TexturedModel pineTexturedModel = new TexturedModel(pineRawModel, new ModelTexture(loader.loadTexture("pineTexture")));

        // Lamps
        ModelData lampModelData = OBJFileLoader.loadOBJ("lampModel");
        RawModel lampRawModel = loader.loadToVAO(lampModelData.getVertices(), lampModelData.getTextureCoords(), lampModelData.getNormals(), lampModelData.getIndices());
        TexturedModel lampTexturedModel = new TexturedModel(lampRawModel, new ModelTexture(loader.loadTexture("lampTexture")));
        lampTexturedModel.getTexture().setUseFakeLighting(true);

        // Baseball
        ModelData baseballModelData = OBJFileLoader.loadOBJ("baseballModel");
        RawModel baseballRawModel = loader.loadToVAO(baseballModelData.getVertices(), baseballModelData.getTextureCoords(), baseballModelData.getNormals(), baseballModelData.getIndices());
        TexturedModel baseballTexturedModel = new TexturedModel(baseballRawModel, new ModelTexture(loader.loadTexture("brownTexture"))); // TODO: Add ability to create a load a texture from .mtl file
        float baseballX = 50, baseballZ = 100, baseballY = 50;
//        float baseballY = waterTerrain.getHeightOfTerrain(baseballX, baseballZ);
        Vector3f baseballCoords = new Vector3f(baseballX, baseballY, baseballZ);
        System.out.println(waterTerrain.getCenter());
        Entity baseballEntity = new Entity(baseballTexturedModel, baseballCoords, 0, 0, 0, 50);
        entities.add(baseballEntity);
        //**************************************

        //********** LIGHT CREATION **********
        Light sunLight = new Light(new Vector3f(baseballCoords), new Vector3f(1f, 1f, 1f));
        lights.add(sunLight); // sun (no attenuation)

        float lampX = 10, lampZ = 0, lampY = waterTerrain.getHeightOfTerrain(lampX, lampZ);

        Vector3f lightCoords;
        Vector3f lampCoords = new Vector3f(lampX, lampY, lampZ);
        Vector3f redLight = new Vector3f(2, 0, 0);
        Vector3f yellowLight = new Vector3f(0, 2, 2);
        Vector3f greenLight = new Vector3f(2, 2, 0);
        Vector3f attenuation = new Vector3f(1, 0.01f, 0.002f);

        // Red Light
        Entity lampRedEntity = new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1);
        entities.add(lampRedEntity);
        lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
//        lights.add(new Light(lightCoords, redLight, attenuation));

        // Yellow Light

        lampZ = lampZ + 30f;
        lampY = waterTerrain.getHeightOfTerrain(lampX, lampZ);
        lampCoords = new Vector3f(lampX, lampY, lampZ);
        Entity lampYellowEntity = new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1);
        entities.add(lampYellowEntity);
        lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
//        lights.add(new Light(lightCoords, yellowLight, attenuation));

        // Green Light
        lampZ = lampZ + 30f;
        lampY = waterTerrain.getHeightOfTerrain(lampX, lampZ);
        lampCoords = new Vector3f(lampX, lampY, lampZ);
        Entity lampGreenEntity = new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1);
        entities.add(lampGreenEntity);
        lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
//        lights.add(new Light(lightCoords, greenLight, attenuation));
        //************************************

        // Randomly generate trees
        Random random = new Random(676452);
        for (Terrain terrain : MainGameLoop.terrains)
        {
            for (int i = 0; i < 20; i++)
            {
                float x = random.nextInt((int) Terrain.getSize() * 2) + terrain.getX();
                float z = random.nextInt((int) Terrain.getSize() * 2) + terrain.getZ();
                float y = terrain.getHeightOfTerrain(x, z);
                Entity pineEntity = new Entity(pineTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 1f);
                entities.add(pineEntity);
            }
        }

        //********** CAMERA CREATION **********
        FreeRoamCamera frcamera = new FreeRoamCamera(new Vector3f(50, 75, 170));
        LockOnCamera locamera = new LockOnCamera(new Vector3f(500, 75, 500), new Vector3f(50, 50, 36));
        CameraManager cameraManager = new CameraManager();
        cameraManager.addCamera(frcamera);
        cameraManager.addCamera(locamera);
        cameraManager.setCurrentCamera(locamera);
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

        //********** WATER RENDERING NEW **********
        MainGameLoop.waters = new ArrayList<>();
        //*****************************************

        //********** WATER RENDERING OLD **********
//        WaterFrameBuffers waterBuffers = new WaterFrameBuffers();
//        WaterShader waterShader = new WaterShader();
//        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), waterBuffers);
//        MainGameLoop.waters = new ArrayList<>();
//        MainGameLoop.waters.add(new WaterTile(50, 52, 36));

//		GuiTexture refraction = new GuiTexture(waterBuffers.getRefractionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//		GuiTexture reflection = new GuiTexture(waterBuffers.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//		guiTextures.add(refraction);
//		guiTextures.add(reflection);
        //*************************************


        float newColor = 0.1f;

        while (!Display.isCloseRequested()) { // loops until exit button pushed

            VirtualClock.update();
            InputHelper.update();

            Camera camera = cameraManager.getCurrentCamera();
//            WaterTile water = MainGameLoop.waters.get(0);

            renderer.renderScene(null, entities, terrains, lights, cameraManager, picker);

//            System.out.println(lights.get(0).getPosition().getY());

//            // Render scene to reflection frame buffer
//            waterBuffers.bindReflectionFrameBuffer();
//            // TODO: Not sure if this camera inversion should be here?
//            float distance = 2 * (camera.getPosition().y - water.getHeight());
//            camera.getPosition().y -= distance;
//            camera.invertPitch();
//            renderer.renderScene(null, MainGameLoop.entities, MainGameLoop.terrains, MainGameLoop.lights, cameraManager, picker, new Vector4f(0, 1, 0, -water.getHeight()));
//            camera.getPosition().y += distance;
//            camera.invertPitch();
//
//            // Render scene to refraction frame buffer
//            waterBuffers.bindRefractionFrameBuffer();
//            renderer.renderScene(null, MainGameLoop.entities, MainGameLoop.terrains, MainGameLoop.lights, cameraManager, picker, new Vector4f(0, -1, 0, water.getHeight()));
//            waterBuffers.unbindCurrentFrameBuffer();
//
//            // Render scene to screen
//            waterBuffers.unbindCurrentFrameBuffer();
//            renderer.renderScene(null, MainGameLoop.entities, MainGameLoop.terrains, MainGameLoop.lights, cameraManager, picker, new Vector4f(0, 0, 0, 0)); // Don't clip anything
//            waterRenderer.render(MainGameLoop.waters, camera);
//            guiRenderer.render(guiTextures); // 2D rendering

            // Game logic

            // Simulate camera spinning around terrain
            locamera.changeAngle(0.1f);

            DisplayManager.updateDisplay();
        }

//        waterBuffers.cleanUp();
//        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
