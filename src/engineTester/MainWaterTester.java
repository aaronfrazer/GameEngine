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
import normalMappingObjConverter.ObjFileLoaderNM;
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
//    public static void main(String[] args)
//    {
//        DisplayManager.createDisplay();
//        Loader loader = new Loader();
//
//        // *********** VARIABLES ***************
//        MainGameLoop.entities = new ArrayList<>();
//        MainGameLoop.normalMapEntities = new ArrayList<>();
//        MainGameLoop.terrains = new ArrayList<>();
//        MainGameLoop.lights = new ArrayList<>();
//
//        List<Entity> entities = MainGameLoop.entities;
//        List<Entity> normalMapEntities = MainGameLoop.normalMapEntities;
//        ArrayList<Terrain> terrains = MainGameLoop.terrains;
//        List<Light> lights = MainGameLoop.lights;
//        // *************************************
//
//        GameSettings.DAY_ONLY_MODE = true;
//        GameSettings.FOG_ENABLED = true;
//        Terrain.SIZE = 100;
//
//        //********** TERRAIN TEXTURES **********
//        TerrainTexture grassTexture = new TerrainTexture(loader.loadTexture("grassTexture"));
//        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirtTexture"));
//        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowersTexture"));
//        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("pathTexture"));
//
//        TerrainTexturePack texturePack1 = new TerrainTexturePack(grassTexture, rTexture, gTexture, bTexture);
//        TerrainTexturePack texturePackUnder1 = new TerrainTexturePack(grassTexture, rTexture, gTexture, bTexture);
//        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMapBlack"));
//        //**************************************
//
//        //********** TERRAIN CREATION **********
//        Terrain waterTerrain = new Terrain(0, 0, loader, texturePackUnder1, blendMap, "heightmapWater");
//        terrains.add(waterTerrain);
//        //**************************************
//
//        //********** ENTITY CREATION ***********
//        ModelData pineModelData = OBJFileLoader.loadOBJModel("pineModel");
//        RawModel pineRawModel = loader.loadToVAO(pineModelData.getVertices(), pineModelData.getTextureCoords(), pineModelData.getNormals(), pineModelData.getIndices());
//        TexturedModel pineTexturedModel = new TexturedModel(pineRawModel, new ModelTexture(loader.loadTexture("pineTexture")));
//
//        // Lamps
//        ModelData lampModelData = OBJFileLoader.loadOBJModel("lampModel");
//        RawModel lampRawModel = loader.loadToVAO(lampModelData.getVertices(), lampModelData.getTextureCoords(), lampModelData.getNormals(), lampModelData.getIndices());
//        TexturedModel lampTexturedModel = new TexturedModel(lampRawModel, new ModelTexture(loader.loadTexture("lampTexture")));
//        lampTexturedModel.getTexture().setUseFakeLighting(true);
//
//        // Baseball
//        ModelData baseballModelData = OBJFileLoader.loadOBJModel("baseballModel");
//        RawModel baseballRawModel = loader.loadToVAO(baseballModelData.getVertices(), baseballModelData.getTextureCoords(), baseballModelData.getNormals(), baseballModelData.getIndices());
//        TexturedModel baseballTexturedModel = new TexturedModel(baseballRawModel, new ModelTexture(loader.loadTexture("brownTexture"))); // TODO: Add ability to create a load a texture from .mtl file
//        float baseballX = 50, baseballY = 100, baseballZ = 50;
////        float baseballY = waterTerrain.getHeightOfTerrain(baseballX, baseballZ);
//        Vector3f baseballCoords = new Vector3f(baseballX, baseballY, baseballZ);
//        Entity baseballEntity = new Entity(baseballTexturedModel, baseballCoords, 0, 0, 0, 50);
//        entities.add(baseballEntity);
//
//        // Barrel with normal mapping
//        RawModel barrelRawModel = ObjFileLoaderNM.loadOBJ("barrelModel", loader);
////        RawModel barrelRawModel = loader.loadToVAO(barrelModelData.getVertices(), barrelModelData.getTextureCoords(), barrelModelData.getNormals(), barrelModelData.getIndices());
//        TexturedModel barrelTexturedModel = new TexturedModel(barrelRawModel, new ModelTexture(loader.loadTexture("barrelTexture")));
//        barrelTexturedModel.getTexture().setShineDamper(10);
//        barrelTexturedModel.getTexture().setReflectivity(0.5f);
//        Entity barrelEntity = new Entity(barrelTexturedModel, new Vector3f(10, 0, 0), 0, 0, 90, 20);
//        normalMapEntities.add(barrelEntity);
//        //**************************************
//
//        //********** LIGHT CREATION **********
//        Light sunLight = new Light(new Vector3f(50, 10000, 50), new Vector3f(1, 1, 1));
//
//        lights.add(sunLight); // sun (no attenuation)
//
//        float lampX = 10, lampZ = 0, lampY = waterTerrain.getHeightOfTerrain(lampX, lampZ);
//
//        Vector3f lightCoords;
//        Vector3f lampCoords = new Vector3f(lampX, lampY, lampZ);
//        Vector3f redLight = new Vector3f(2, 0, 0);
//        Vector3f yellowLight = new Vector3f(0, 2, 2);
//        Vector3f greenLight = new Vector3f(2, 2, 0);
//        Vector3f attenuation = new Vector3f(1, 0.01f, 0.002f);
//
//        // Red Light
//        Entity lampRedEntity = new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1);
//        entities.add(lampRedEntity);
//        lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
////        lights.add(new Light(lightCoords, redLight, attenuation));
//
//        // Yellow Light
//
//        lampZ = lampZ + 30f;
//        lampY = waterTerrain.getHeightOfTerrain(lampX, lampZ);
//        lampCoords = new Vector3f(lampX, lampY, lampZ);
//        Entity lampYellowEntity = new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1);
//        entities.add(lampYellowEntity);
//        lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
////        lights.add(new Light(lightCoords, yellowLight, attenuation));
//
//        // Green Light
//        lampZ = lampZ + 30f;
//        lampY = waterTerrain.getHeightOfTerrain(lampX, lampZ);
//        lampCoords = new Vector3f(lampX, lampY, lampZ);
//        Entity lampGreenEntity = new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1);
//        entities.add(lampGreenEntity);
//        lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
////        lights.add(new Light(lightCoords, greenLight, attenuation));
//        //************************************
//
//        // Randomly generate trees
//        Random random = new Random(676452);
//        for (Terrain terrain : MainGameLoop.terrains)
//        {
//            for (int i = 0; i < 10; i++)
//            {
//                float x = random.nextInt((int) Terrain.getSize());
//                float z = random.nextInt((int) Terrain.getSize());
//                float y = terrain.getHeightOfTerrain(x, z);
//                System.out.println(y);
//                if (y > 32) // only render trees above water
//                {
//                    Entity pineEntity = new Entity(pineTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 2);
//                    entities.add(pineEntity);
//                }
//            }
//        }
//
//        //********** CAMERA CREATION **********
//        FreeRoamCamera frcamera = new FreeRoamCamera(new Vector3f(50, 75, 170));
//        LockOnCamera locamera = new LockOnCamera(new Vector3f(50, 75, 170), new Vector3f(50, 50, 36));
//        CameraManager cameraManager = new CameraManager();
//        cameraManager.addCamera(frcamera);
//        cameraManager.addCamera(locamera);
//        cameraManager.setCurrentCamera(locamera);
//        //*************************************
//
//        MasterRenderer renderer = new MasterRenderer(loader);
//
//        //********** GUI TEXTURES **********
//        List<GuiTexture> guiTextures = new ArrayList<>();
//        GuiRenderer guiRenderer = new GuiRenderer(loader);
//        //**********************************
//
//        //********** MOUSE PICKER **********
//        // TODO: Make mouse picker work for all cameras
//        MousePicker picker = new MousePicker(frcamera, renderer.getProjectionMatrix(), terrains.get(0));
//        //**********************************
//
//        //********** WATER RENDERING **********
//        MainGameLoop.waters = new ArrayList<>();
//        WaterFrameBuffers fbos = new WaterFrameBuffers();
//        WaterShader waterShader = new WaterShader();
//        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
//        MainGameLoop.waters.add(new WaterTile(50, 52, 36));
//
//        GuiTexture refractionGui = new GuiTexture(fbos.getRefractionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//		GuiTexture reflectionGui = new GuiTexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
////		guiTextures.add(refractionGui);
////		guiTextures.add(reflectionGui);
//        //*****************************************
//
//        // TODO: Add a method to Terrain that will get the highest/lowest part on the terrain
//
//        while (!Display.isCloseRequested()) { // loops until exit button pushed
//
//            VirtualClock.update();
//            InputHelper.update();
//
//            Camera camera = cameraManager.getCurrentCamera();
//            WaterTile water = MainGameLoop.waters.get(0);
//
//            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
//
//            // Render reflection frame buffer
//            fbos.bindReflectionFrameBuffer();
//            float distance = 2 * (camera.getPosition().y - water.getHeight());
//            camera.getPosition().y -= distance;
//            camera.invertPitch();
//            camera.invertRoll();
//            renderer.renderScene(null, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
//            camera.getPosition().y += distance;
//            camera.invertPitch();
//            camera.invertRoll();
//
//            // Render refraction frame buffer
//            fbos.bindRefractionFrameBuffer();
//            renderer.renderScene(null, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, -1, 0, water.getHeight() + 1f));
//
//            // Render to screen
//            fbos.unbindCurrentFrameBuffer();
//            renderer.renderScene(null, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, 0, 0, 0));
//            waterRenderer.render(MainGameLoop.waters, camera, sunLight);
//            guiRenderer.render(guiTextures);
//
//            // Game logic
//
//            // Simulate camera spinning around terrain
//            locamera.changeAngle(0.1f);
//
//            DisplayManager.updateDisplay();
//        }
//
//        fbos.cleanUp();
//        waterShader.cleanUp();
//        guiRenderer.cleanUp();
//        renderer.cleanUp();
//        loader.cleanUp();
//        DisplayManager.closeDisplay();
//    }

}
