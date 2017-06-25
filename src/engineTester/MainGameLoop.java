package engineTester;

import cameras.CameraManager;
import cameras.FirstPersonCamera;
import cameras.FreeRoamCamera;
import cameras.ThirdPersonCamera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
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
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game loop responsible for running our game.
 *
 * @author Aaron Frazer
 */
public class MainGameLoop
{

	public static List<Light> lights;
	public static List<Entity> entities;
	public static ArrayList<Terrain> terrains;
	public static List<WaterTile> waters;

	/**
	 * Runs the game.  Used to test multiple terrains.
	 *
	 * @param args - arguments
	 */
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		// *********** VARIABLES ***************
		entities = new ArrayList<>();
		terrains = new ArrayList<>();
		lights = new ArrayList<>();
		// *************************************

		//********** TERRAIN TEXTURES **********
		TerrainTexture grassTexture = new TerrainTexture(loader.loadTexture("grassTexture"));
		TerrainTexture sandTexture = new TerrainTexture(loader.loadTexture("sandTexture"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirtTexture"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowersTexture"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("pathTexture"));

		TerrainTexturePack texturePack1 = new TerrainTexturePack(grassTexture, rTexture, gTexture, bTexture);
		TerrainTexturePack texturePack2 = new TerrainTexturePack(sandTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		//**************************************

		// Create two terrains
		Terrain terrain1 = new Terrain(0, 0, loader, texturePack1, blendMap, "heightmap");
		Terrain terrain2 = new Terrain(0, -1, loader, texturePack2, blendMap, "heightmap");

		// Create an arraylist for terrains
		terrains.add(terrain1);
		terrains.add(terrain2);

		//********** ENTITY CREATION **********
		// Dragon
		ModelData dragonModelData = OBJFileLoader.loadOBJ("dragonModel");
		RawModel dragonRawModel = loader.loadToVAO(dragonModelData.getVertices(), dragonModelData.getTextureCoords(), dragonModelData.getNormals(), dragonModelData.getIndices());
		TexturedModel dragonTexturedModel = new TexturedModel(dragonRawModel, new ModelTexture(loader.loadTexture("brownTexture")));
		ModelTexture dragonModelTexture = dragonTexturedModel.getTexture();
		dragonModelTexture.setShineDamper(10);
		dragonModelTexture.setReflectivity(1);
		float dragonX = 100, dragonZ = 280;
		float dragonY = terrain1.getHeightOfTerrain(dragonX, dragonZ);
		Vector3f dragonCoords = new Vector3f(dragonX, dragonY, dragonZ);
		Entity dragonEntity = new Entity(dragonTexturedModel, dragonCoords, 0, 0, 0, 1);
		entities.add(dragonEntity);

		// Stall
		ModelData stallModelData = OBJFileLoader.loadOBJ("stallModel");
		RawModel stallRawModel = loader.loadToVAO(stallModelData.getVertices(), stallModelData.getTextureCoords(), stallModelData.getNormals(), stallModelData.getIndices());
		TexturedModel stallTexturedModel = new TexturedModel(stallRawModel, new ModelTexture(loader.loadTexture("stallTexture")));
		float stallX = 180, stallZ = 260;
		float stallY = terrain1.getHeightOfTerrain(stallX, stallZ);
		Vector3f stallCoords = new Vector3f(stallX, stallY, stallZ);
		Entity stallEntity = new Entity(stallTexturedModel, stallCoords, 0, 0, 0, 1);
		entities.add(stallEntity);

		// Tree (many)
		ModelData treeModelData = OBJFileLoader.loadOBJ("treeModel");
		RawModel treeRawModel = loader.loadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(), treeModelData.getNormals(), treeModelData.getIndices());
		TexturedModel treeTexturedModel = new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("treeTexture")));

		// Pine Trees (many)
		ModelData pineModelData = OBJFileLoader.loadOBJ("pineModel");
		RawModel pineRawModel = loader.loadToVAO(pineModelData.getVertices(), pineModelData.getTextureCoords(), pineModelData.getNormals(), pineModelData.getIndices());
		TexturedModel pineTexturedModel = new TexturedModel(pineRawModel, new ModelTexture(loader.loadTexture("pineTexture")));

		// Grass (many)
		ModelData grassModelData = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassRawModel = loader.loadToVAO(grassModelData.getVertices(), grassModelData.getTextureCoords(), grassModelData.getNormals(), grassModelData.getIndices());
		TexturedModel grassTexturedModel = new TexturedModel(grassRawModel, new ModelTexture(loader.loadTexture("grassbushTexture")));
		grassTexturedModel.getTexture().setHasTransparency(true);
		grassTexturedModel.getTexture().setUseFakeLighting(true);

		// Fern (many)
		ModelData fernModelData = OBJFileLoader.loadOBJ("fernModel");
		RawModel fernRawModel = loader.loadToVAO(fernModelData.getVertices(), fernModelData.getTextureCoords(), fernModelData.getNormals(), fernModelData.getIndices());
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fernTextureAtlas"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel fernTexturedModel = new TexturedModel(fernRawModel, fernTextureAtlas);
		fernTexturedModel.getTexture().setHasTransparency(true);

		// Lamps
		ModelData lampModelData = OBJFileLoader.loadOBJ("lampModel");
		RawModel lampRawModel = loader.loadToVAO(lampModelData.getVertices(), lampModelData.getTextureCoords(), lampModelData.getNormals(), lampModelData.getIndices());
		TexturedModel lampTexturedModel = new TexturedModel(lampRawModel, new ModelTexture(loader.loadTexture("lampTexture")));
		lampTexturedModel.getTexture().setUseFakeLighting(true);

		// Baseball
		ModelData baseballModelData = OBJFileLoader.loadOBJ("baseballModel");
		RawModel baseballRawModel = loader.loadToVAO(baseballModelData.getVertices(), baseballModelData.getTextureCoords(), baseballModelData.getNormals(), baseballModelData.getIndices());
		TexturedModel baseballTexturedModel = new TexturedModel(baseballRawModel, new ModelTexture(loader.loadTexture("brownTexture"))); // TODO: Add ability to create a load a texture from .mtl file
		float baseballX = 180, baseballZ = 260;
		float baseballY = terrain1.getHeightOfTerrain(baseballX, baseballZ);
		Vector3f baseballCoords = new Vector3f(baseballX, baseballY + 12, baseballZ);
		Entity baseballEntity = new Entity(baseballTexturedModel, baseballCoords, 0, 0, 0, 10);
		entities.add(baseballEntity);

		//**************************************

		// Randomly generate trees
		Random random = new Random(676452);
		for (Terrain terrain : terrains)
		{
			for (int i = 0; i < 400; i++)
			{
				float x = random.nextInt((int) Terrain.getSize() * 2) + terrain.getX();
				float z = random.nextInt((int) Terrain.getSize() * 2) + terrain.getZ();
				float y = terrain.getHeightOfTerrain(x, z);
				Entity pineEntity = new Entity(pineTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 1f);
				entities.add(pineEntity);

				if (i % 20 == 0)
				{
					x = random.nextInt((int) Terrain.getSize() * 2) + terrain.getX();
					z = random.nextInt((int) Terrain.getSize() * 2) + terrain.getZ();
					y = terrain.getHeightOfTerrain(x, z);
					Entity treeEntity = new Entity(treeTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 3f);
					entities.add(treeEntity);
//					System.out.println("Tree Enity Added: " + "(" + Math.floor(x) + ", " + Math.floor(y) + ", " + Math.floor(z) + ")");
				}
				if (i % 5 == 0)
				{
					x = random.nextInt((int) Terrain.getSize() * 2) + terrain.getX();
					z = random.nextInt((int) Terrain.getSize() * 2) + terrain.getZ();
					y = terrain.getHeightOfTerrain(x, z);
					Entity grassEntity = new Entity(grassTexturedModel, new Vector3f(x, y, z), 0, 0, 0, 2f);
					entities.add(grassEntity);
				}
				if (i % 2 == 0)
				{
					x = random.nextInt((int) Terrain.getSize() * 2) + terrain.getX();
					z = random.nextInt((int) Terrain.getSize() * 2) + terrain.getZ();
					y = terrain.getHeightOfTerrain(x, z);
					int rand = random.nextInt(4);
					Entity fernEntity = new Entity(fernTexturedModel, rand, new Vector3f(x, y, z), 0, 0, 0, 1f);
					entities.add(fernEntity);
				}

			}
		}

		//********** LIGHTS CREATION **********
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f))); // sun (no attenuation)

		float lampX = 200, lampZ = 290, lampY = terrain1.getHeightOfTerrain(lampX, lampZ);

		Vector3f lightCoords;
		Vector3f lampCoords = new Vector3f(lampX, lampY, lampZ);
		Vector3f redLight = new Vector3f(2, 0, 0);
		Vector3f yellowLight = new Vector3f(0, 2, 2);
		Vector3f greenLight = new Vector3f(2, 2, 0);
		Vector3f attenuation = new Vector3f(1, 0.01f, 0.002f);

		// Red Light
		Entity lampEntity = new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1);
		entities.add(0, lampEntity);
		lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
		lights.add(new Light(lightCoords, redLight, attenuation));

		// Yellow Light
		lampZ = lampZ + 30f;
		lampY = terrain1.getHeightOfTerrain(lampX, lampZ);
		lampCoords = new Vector3f(lampX, lampY, lampZ);
		entities.add(new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1));
		lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
		lights.add(new Light(lightCoords, yellowLight, attenuation));

		// Green Light
		lampZ = lampZ + 30f;
		lampY = terrain1.getHeightOfTerrain(lampX, lampZ);
		lampCoords = new Vector3f(lampX, lampY, lampZ);
		entities.add(new Entity(lampTexturedModel, lampCoords, 0, 0, 0, 1));
		lightCoords = new Vector3f(lampCoords.getX(), lampCoords.getY() + 12f, lampCoords.getZ());
		lights.add(new Light(lightCoords, greenLight, attenuation));

		// Free lights (no entities attached)
//		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
//		lights.add(light);
//		lights.add(new Light(new Vector3f(-200, 10, -200), new Vector3f(10, 0, 0)));
//		lights.add(new Light(new Vector3f(200, 10, 200), new Vector3f(0, 0, 10)));

		//************************************

		//********** PLAYER CREATION **********
		ModelData personModelData = OBJFileLoader.loadOBJ("personModel");
		RawModel personRawModel = loader.loadToVAO(personModelData.getVertices(), personModelData.getTextureCoords(), personModelData.getNormals(), personModelData.getIndices());
		TexturedModel personTexturedModel = new TexturedModel(personRawModel, new ModelTexture(loader.loadTexture("personTexture")));
		Player player = new Player(personTexturedModel, new Vector3f(570, 0, 500), 0, 0, 0, 0.5f);
		entities.add(player);
		//**************************************

		//********** CAMERA CREATION **********
		ThirdPersonCamera tpcamera = new ThirdPersonCamera(player);
		FirstPersonCamera fpcamera = new FirstPersonCamera(player);
		FreeRoamCamera frcamera = new FreeRoamCamera(new Vector3f(300, 10, 350));
		//**************************************

		CameraManager cameraManager = new CameraManager();
		cameraManager.addCamera(tpcamera);
		cameraManager.addCamera(frcamera);
		cameraManager.addCamera(fpcamera);
		cameraManager.setCurrentCamera(tpcamera);

		MasterRenderer renderer = new MasterRenderer(loader);

		//********** GUI TEXTURES **********
		// TODO: Show the game clock on the display
		List<GuiTexture> guis = new ArrayList<>();
		GuiTexture gui1 = new GuiTexture(loader.loadTexture("socuwanTexture"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
//		guis.add(gui1);
		GuiTexture gui2 = new GuiTexture(loader.loadTexture("thinmatrixTexture"), new Vector2f(0.3f, 0.75f), new Vector2f(0.4f, 0.4f));
//		guis.add(gui2);

		GuiRenderer guiRenderer = new GuiRenderer(loader);
		//**********************************

		//********** MOUSE PICKER **********
		// TODO: Make mouse picker work for all cameras
		MousePicker picker = new MousePicker(tpcamera, renderer.getProjectionMatrix(), terrains.get(0));
		//***********************************

		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
		waters = new ArrayList<WaterTile>();
		waters.add(new WaterTile(565, 538, -2));

		while (!Display.isCloseRequested()) { // loops until exit button pushed

			VirtualClock.update();
			InputHelper.update();

			// 3D rendering
			System.out.println(player.getPosition());
			renderer.renderScene(player, entities, terrains, lights, cameraManager, picker);

			waterRenderer.render(waters, cameraManager.getCurrentCamera());

			// 2D rendering, done separately
			guiRenderer.render(guis);

			// Game logic

			DisplayManager.updateDisplay();
		}

		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
