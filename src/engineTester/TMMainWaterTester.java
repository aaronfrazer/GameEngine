package engineTester;

import cameras.*;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.ObjFileLoaderNM;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
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
 * Main class used to test rendering system (the one used by ThinMatrix)
 */
public class TMMainWaterTester
{

    public static void main(String[] args)
    {
        DisplayManager.createDisplay();
        Loader loader = new Loader();

        // *********** MAIN VARIABLES ***************
        MainGameLoop.entities = new ArrayList<>();
        MainGameLoop.normalMapEntities = new ArrayList<>();
        MainGameLoop.terrains = new ArrayList<>();
        MainGameLoop.lights = new ArrayList<>();
        MainGameLoop.waters = new ArrayList<>();
        MainGameLoop.guiTextures = new ArrayList<>();

        List<Entity> entities = MainGameLoop.entities;
        List<Entity> normalMapEntities = MainGameLoop.normalMapEntities;
        ArrayList<Terrain> terrains = MainGameLoop.terrains;
        List<Light> lights = MainGameLoop.lights;
        List<WaterTile> waters = MainGameLoop.waters;
        List<GuiTexture> guiTextures = MainGameLoop.guiTextures;

        MasterRenderer renderer = new MasterRenderer(loader);
        GuiRenderer guiRenderer = new GuiRenderer(loader);
        // *************************************

        // ********* GAME SETTINGS *************
        GameSettings.DAY_ONLY_MODE = true;
        GameSettings.FOG_ENABLED = true;
        Terrain.SIZE = 150;
        // *************************************

        // ******** TERRAIN TEXTURES ***********
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassTexture"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mudTexture"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowersTexture"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("pathTexture"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMapCelShading"));
        // *************************************

        // ********* ENTITY CREATION ***********
        TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJModel("rocksModel", loader),
                new ModelTexture(loader.loadTexture("rocksTexture")));

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fernTexture"));
        fernTextureAtlas.setNumberOfRows(2);

        TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJModel("fernModel", loader),
                fernTextureAtlas);
        fern.getTexture().setHasTransparency(true);

        TexturedModel bobble = new TexturedModel(OBJFileLoader.loadOBJModel("pineModel", loader),
                new ModelTexture(loader.loadTexture("pineTexture")));
        bobble.getTexture().setHasTransparency(true);

        TexturedModel lamp = new TexturedModel(OBJFileLoader.loadOBJModel("lampModel", loader),
                new ModelTexture(loader.loadTexture("lampTexture")));
        lamp.getTexture().setUseFakeLighting(true);
        // *************************************

        // ************ NORMAL MAP ENTITY CREATION ******************
        TexturedModel barrelModel = new TexturedModel(ObjFileLoaderNM.loadOBJ("barrelModel", loader),
                new ModelTexture(loader.loadTexture("barrelTexture")));
        barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);

        TexturedModel crateModel = new TexturedModel(ObjFileLoaderNM.loadOBJ("crateModel", loader),
                new ModelTexture(loader.loadTexture("crateTexture")));
        crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
        crateModel.getTexture().setShineDamper(10);
        crateModel.getTexture().setReflectivity(0.5f);

        TexturedModel boulderModel = new TexturedModel(ObjFileLoaderNM.loadOBJ("boulderModel", loader),
                new ModelTexture(loader.loadTexture("boulderTexture")));
        boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);
        // ***********************************************************

        // ******** TERRAIN GENERATION *********
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmapWater2");
        terrains.add(terrain);
        // *************************************

        // ************ ENTITY GENERATION *******************
        Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
        Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
        Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
        normalMapEntities.add(entity);
        normalMapEntities.add(entity2);
        normalMapEntities.add(entity3);

        Random random = new Random(5666778);
        for (int i = 0; i < 60; i++)
        {
            if (i % 3 == 0)
            {
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x < 50 || x > 100) && (z > -50 || z < -100))
                {
                    float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
                }
            }
            if (i % 2 == 0)
            {
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x < 50 || x > 100) && (z > -50 || z < -100))
                {
                    float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(bobble, 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
                }
            }
        }
        entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));
        // **************************************************


        // ************ LIGHTS ***********
        Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);
        // *******************************

        // *********** PLAYER CREATION ****************
        RawModel bunnyModel = OBJFileLoader.loadOBJModel("personModel", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(
                loader.loadTexture("personTexture")));
        Player player = new Player(stanfordBunny, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
        entities.add(player);
        // ********************************************

        // *************** CAMERA CREATION ****************
        CameraManager cameraManager = new CameraManager();
        FirstPersonCamera fpCamera = new FirstPersonCamera(player);
        FreeRoamCamera frCamera = new FreeRoamCamera(new Vector3f(50, 75, 170));
        ThirdPersonCamera tpCamera = new ThirdPersonCamera(player);
        LockOnCamera loCamera = new LockOnCamera(new Vector3f(50, 75, 170), terrains.get(0).getCenter());
        cameraManager.addCamera(fpCamera);
        cameraManager.addCamera(frCamera);
        cameraManager.addCamera(tpCamera);
        cameraManager.addCamera(loCamera);
        cameraManager.setCurrentCamera(tpCamera);
        // ************************************************

        // *************** MOUSE PICKER ****************
        MousePicker picker = new MousePicker(fpCamera, renderer.getProjectionMatrix(), terrains.get(0));
        // ************************************************

        //*************** WATER RENDERING *******************
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        WaterTile water = new WaterTile(75, -75, 0);
        waters.add(water);
        //***************************************************


        while (!Display.isCloseRequested())
        {
            VirtualClock.update();
            InputHelper.update();

            Camera camera = cameraManager.getCurrentCamera();

            player.move(terrains.get(0));
            camera.move();
            picker.update();
            entity.increaseRotation(0, 1, 0);
            entity2.increaseRotation(0, 1, 0);
            entity3.increaseRotation(0, 1, 0);
            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            // render reflection texture
            buffers.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(player, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, 1, 0, -water.getHeight() + 1));
            camera.getPosition().y += distance;
            camera.invertPitch();

            // render refraction texture
            buffers.bindRefractionFrameBuffer();
            renderer.renderScene(player, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, -1, 0, water.getHeight()));

            // render to screen
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            buffers.unbindCurrentFrameBuffer();
            renderer.renderScene(player, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun);
            guiRenderer.render(guiTextures);

            DisplayManager.updateDisplay();
        }

        buffers.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}