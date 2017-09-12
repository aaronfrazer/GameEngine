package engineTester;

import cameras.*;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.ObjFileLoaderNM;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Keyboard;
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
import toolbox.InputHelper;
import toolbox.MousePicker;
import toolbox.VirtualClock;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sin;

public class MainGameLoop
{

    /**
     * Global variables accessed in these classes:
     * 1. lights - SkyboxRenderer
     */
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> normalMapEntities = new ArrayList<>();
    public static List<Terrain> terrains = new ArrayList<>();
    public static List<Light> lights = new ArrayList<>();
    public static List<GuiTexture> guiTextures = new ArrayList<>();
    public static List<WaterTile> waters = new ArrayList<>();

    public static void main(String[] args)
    {
        DisplayManager.createDisplay();
        Loader loader = new Loader();

        // *********** TERRAIN TEXTURE **********
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadGameTexture("grassTexture"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadGameTexture("mudTexture"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadGameTexture("grassFlowersTexture"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadGameTexture("pathTexture"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadGameTexture("blendMapCelShading"));
        // **************************************

        // ********** TERRAIN CREATION **********
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmapWater2");
        terrains.add(terrain);
        // **************************************

        //********** MODELS CREATION ************
        TexturedModel rocks = new TexturedModel(OBJFileLoader.loadOBJ("rocksModel", loader),
                new ModelTexture(loader.loadGameTexture("rocksTexture")));

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadGameTexture("fernTextureAtlas"));
        fernTextureAtlas.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fernModel", loader),
                fernTextureAtlas);
        fern.getTexture().setHasTransparency(true);

        TexturedModel pine = new TexturedModel(OBJFileLoader.loadOBJ("pineModel", loader),
                new ModelTexture(loader.loadGameTexture("pineTexture")));
        pine.getTexture().setHasTransparency(true);

        TexturedModel lamp = new TexturedModel(OBJFileLoader.loadOBJ("lampModel", loader),
                new ModelTexture(loader.loadGameTexture("lampTexture")));
        lamp.getTexture().setUseFakeLighting(true);

        RawModel personModel = OBJFileLoader.loadOBJ("personModel", loader);
        TexturedModel personTexturedModel = new TexturedModel(personModel, new ModelTexture(
                loader.loadGameTexture("personTexture")));
        // **************************************

        //********* NORMAL MAP MODELS CREATION ***********
        TexturedModel barrelModel = new TexturedModel(ObjFileLoaderNM.loadOBJ("barrelModel", loader),
                new ModelTexture(loader.loadGameTexture("barrelTexture")));
        barrelModel.getTexture().setNormalMap(loader.loadGameTexture("barrelNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);

        TexturedModel crateModel = new TexturedModel(ObjFileLoaderNM.loadOBJ("crateModel", loader),
                new ModelTexture(loader.loadGameTexture("crateTexture")));
        crateModel.getTexture().setNormalMap(loader.loadGameTexture("crateNormal"));
        crateModel.getTexture().setShineDamper(10);
        crateModel.getTexture().setReflectivity(0.5f);

        TexturedModel boulderModel = new TexturedModel(ObjFileLoaderNM.loadOBJ("boulderModel", loader),
                new ModelTexture(loader.loadGameTexture("boulderTexture")));
        boulderModel.getTexture().setNormalMap(loader.loadGameTexture("boulderNormal"));
        boulderModel.getTexture().setShineDamper(10);
        boulderModel.getTexture().setReflectivity(0.5f);
        //***************************************

        // ************* ENTITIES ***************
        Random random = new Random(5666778);
        for (int i = 0; i < 60; i++)
        {
            if (i % 3 == 0)
            {
                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x <= 50 || x >= 100) && (z >= -50 || z <= -100))
                {
                    float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(fern, 3, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
                }

            }
            if (i % 2 == 0)
            {

                float x = random.nextFloat() * 150;
                float z = random.nextFloat() * -150;
                if ((x <= 50 || x >= 100) && (z >= -50 || z <= -100))
                {
                    float y = terrain.getHeightOfTerrain(x, z);
                    entities.add(new Entity(pine, 1, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 0.8f));
                }
            }
        }
        entities.add(new Entity(rocks, new Vector3f(75, 4.6f, -75), 0, 0, 0, 75));
        Player player = new Player(personTexturedModel, new Vector3f(75, 5, -75), 0, 100, 0, 0.6f);
        entities.add(player);
        // **************************************

        // ******* NORMAL MAP ENTITIES **********
        Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f);
        Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f);
        Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f);
        normalMapEntities.add(entity);
        normalMapEntities.add(entity2);
        normalMapEntities.add(entity3);
        // **************************************

        // *********** LIGHT CREATION ***********
        Light sun = new Light(new Vector3f(10000, 10000, -10000), new Vector3f(1.3f, 1.3f, 1.3f));
        lights.add(sun);
        // **************************************

        MasterRenderer renderer = new MasterRenderer(loader);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        // ********** CAMERA CREATION ***********
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
        // **************************************

        MousePicker picker = new MousePicker(cameraManager.getCurrentCamera(), renderer.getProjectionMatrix(), terrain);

        //********** WATER RENDERING ************
        WaterFrameBuffers buffers = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
        WaterTile water = new WaterTile(75, -75, 0);
        waters.add(water);
        // **************************************

        //********** TEXT RENDERING *************
        TextMaster.init(loader);
        FontType font = new FontType(loader.loadFontTextureAtlas("candara"), new File("res/font/candara.fnt"));

        GUIText normalText = new GUIText("Some black and white text", 3, font, new Vector2f(0.0f, 0.0f), 1f, true);
        normalText.setColour(0, 0, 0);
        normalText.setDistanceFieldWidth(0.5f);
        normalText.setDistanceFieldEdge(0.1f);
        normalText.setBorderWidth(0);
        normalText.setBorderEdge(0.4f);
        normalText.setOffset(0.0f, 0.0f);
        normalText.setOutlineColour(1, 0, 1);
        normalText.add();

        // Purple with pink outline
        GUIText outlineText = new GUIText("Some text with an outline", 4, font, new Vector2f(0.0f, 0.2f), 1f, true);
        outlineText.setColour(0.5f, 0, 1);
        outlineText.setDistanceFieldWidth(0.5f);
        outlineText.setDistanceFieldEdge(0.1f);
        outlineText.setBorderWidth(0.4f);
        outlineText.setBorderEdge(0.5f);
        outlineText.setOffset(0.0f, 0.0f);
        outlineText.setOutlineColour(1, 0, 0.1f);
        outlineText.add();

        // Glowing effect
        GUIText glowText = new GUIText("A glowing bit of text!", 6, font, new Vector2f(0.0f, 0.4f), 1f, true);
        float borderEdge = 0.5f;
        glowText.setColour(0, 1, 0.5f);
        glowText.setDistanceFieldWidth(0.5f);
        glowText.setDistanceFieldEdge(0.1f);
        glowText.setBorderWidth(0.4f);
        glowText.setBorderEdge(borderEdge);
        glowText.setOffset(0.0f, 0.0f);
        glowText.setOutlineColour(1, 1, 0);
        glowText.add();

        // Dropshadow effect
        GUIText shadowText = new GUIText("Drop shadow!", 5, font, new Vector2f(0.0f, 0.6f), 1f, true);
        shadowText.setColour(0.5f, 0.1f, 0.3f);
        shadowText.setDistanceFieldWidth(0.5f);
        shadowText.setDistanceFieldEdge(0.1f);
        shadowText.setBorderWidth(0.3f);
        shadowText.setBorderEdge(0.5f);
        shadowText.setOffset(0.006f, 0.006f);
        shadowText.setOutlineColour(0, 0, 0);
        shadowText.add();

        // **************************************

        float time = 0;
        float CHANGE_SPEED = 0.5f;

        // ********* Game Loop Below ************
        while (!Display.isCloseRequested())
        {
            VirtualClock.update();
            InputHelper.update();
            cameraManager.update(cameraManager);

            Camera camera = cameraManager.getCurrentCamera();

            player.move(terrain);
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
//            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
            camera.getPosition().y += distance;
            camera.invertPitch();

            // render refraction texture
            buffers.bindRefractionFrameBuffer();
            renderer.renderScene(player, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, -1, 0, water.getHeight()));
//            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));

            // render to screen
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            buffers.unbindCurrentFrameBuffer();
            renderer.renderScene(player, entities, normalMapEntities, terrains, lights, cameraManager, picker, new Vector4f(0, -1, 0, 100000));
//            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun, waterShader);
            guiRenderer.render(guiTextures);

            TextMaster.render(); // render text on top of everything

            // Apply glowing text effect

            time += DisplayManager.getFrameTimeSeconds() * CHANGE_SPEED;
            time %= 1;
            float value = Math.abs((float) sin(time * Math.PI * 2));
            if (value > 0.6f) { value = 0.6f; }
            glowText.setBorderEdge(value);
            shadowText.setOffset(value*0.015f, value*0.015f);

//            // Remove/add text
//            if (InputHelper.isKeyPressed(Keyboard.KEY_Y))
//            {
//                if (text.isOnScreen())
//                {
//                    System.out.println("Removing text: " + text.getTextString());
//                    text.remove();
//                }
//                else
//                {
//                    System.out.println("Adding text: " + text.getTextString());
//                    text.add();
//                }
//            }
//
//            // Change text
//            if (InputHelper.isKeyPressed(Keyboard.KEY_N))
//            {
//                if (!text.isOnScreen())
//                {
//                    System.out.println("Updating text: " + "This is my new text!");
//                    text.update("This is my new text!");
//                }
//            }

            // Pink text

            // Glowing text

            DisplayManager.updateDisplay();
        }

        // ********* Clean Up Below *************
        TextMaster.cleanUp();
        buffers.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        // **************************************


        DisplayManager.closeDisplay();

    }


}