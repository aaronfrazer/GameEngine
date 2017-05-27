package engineTester;

import cameras.CameraManager;
import cameras.FirstPersonCamera;
import cameras.FreeRoamCamera;
import cameras.ThirdPersonCamera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.InputHelper;

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
	
	/**
	 * Runs the game.  Used to test multiple terrains.
	 * @param args - arguments
	 */
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
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
		ArrayList<Terrain> terrainList = new ArrayList<>();
		terrainList.add(terrain1);
		terrainList.add(terrain2);

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

		// Stall
		ModelData stallModelData = OBJFileLoader.loadOBJ("stallModel");
		RawModel stallRawModel = loader.loadToVAO(stallModelData.getVertices(), stallModelData.getTextureCoords(), stallModelData.getNormals(), stallModelData.getIndices());
		TexturedModel stallTexturedModel = new TexturedModel(stallRawModel, new ModelTexture(loader.loadTexture("stallTexture")));
		float stallX = 180, stallZ = 260;
		float stallY = terrain1.getHeightOfTerrain(stallX, stallZ);
		Vector3f stallCoords = new Vector3f(stallX, stallY, stallZ);
		Entity stallEntity = new Entity(stallTexturedModel, stallCoords, 0, 0, 0, 1);

        // Tree (many)
        ModelData treeModelData = OBJFileLoader.loadOBJ("treeModel");
        RawModel treeRawModel = loader.loadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(), treeModelData.getNormals(), treeModelData.getIndices());
        TexturedModel treeTexturedModel = new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("treeTexture")));

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
		//**************************************

		// Randomly generate trees
		List<Entity> entities = new ArrayList<>();
		Random random = new Random(676452);
		for (Terrain terrain : terrainList)
		{
			for (int i = 0; i < 400; i++)
			{
				if (i % 20 == 0)
				{
					float x = random.nextInt((int) Terrain.getSize() * 2) + terrain.getX();
					float z = random.nextInt((int) Terrain.getSize() * 2) + terrain.getZ();
					float y = terrain.getHeightOfTerrain(x, z);
					Entity treeEntity = new Entity(treeTexturedModel, new Vector3f(x,y,z), 0, 0, 0, 3f);
					entities.add(treeEntity);
//					System.out.println("Tree Enity Added: " + "(" + Math.floor(x) + ", " + Math.floor(y) + ", " + Math.floor(z) + ")");
				}
				if (i % 5 == 0)
				{
					float x = random.nextInt((int) Terrain.getSize() * 2) + terrain.getX();
					float z = random.nextInt((int) Terrain.getSize() * 2) + terrain.getZ();
					float y = terrain.getHeightOfTerrain(x, z);
					Entity grassEntity = new Entity(grassTexturedModel, new Vector3f(x,y,z), 0, 0, 0, 2f);
					entities.add(grassEntity);
				}
				if (i % 2 == 0)
				{
					float x = random.nextInt((int) Terrain.getSize() * 2) + terrain.getX();
					float z = random.nextInt((int) Terrain.getSize() * 2) + terrain.getZ();
					float y = terrain.getHeightOfTerrain(x, z);
					int rand = random.nextInt(4);
					Entity fernEntity = new Entity(fernTexturedModel, rand, new Vector3f(x,y,z), 0, 0, 0, 1f);
					entities.add(fernEntity);
				}
			}
		}

        //********** LIGHT CREATION **********
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
        //**************************************

		//********** PLAYER CREATION **********
		ModelData bunnyModelData = OBJFileLoader.loadOBJ("bluedevilModel");
		RawModel bunnyRawModel = loader.loadToVAO(bunnyModelData.getVertices(), bunnyModelData.getTextureCoords(), bunnyModelData.getNormals(), bunnyModelData.getIndices());
		TexturedModel bunnyTexturedModel = new TexturedModel(bunnyRawModel, new ModelTexture(loader.loadTexture("bluedevilTexture")));
		Player player = new Player(bunnyTexturedModel, new Vector3f(200, 0, 280), 0, 0, 0, 0.5f);
		//**************************************

		//********** CAMERA CREATION **********
		ThirdPersonCamera tpcamera = new ThirdPersonCamera(player);
		FirstPersonCamera fpcamera = new FirstPersonCamera(player);
		FreeRoamCamera frcamera = new FreeRoamCamera(new Vector3f(300, 10, 350));
		//**************************************
		
		MasterRenderer renderer = new MasterRenderer();
		
		CameraManager cameraManager = new CameraManager();
		cameraManager.addCamera(tpcamera);
		cameraManager.addCamera(frcamera);
		cameraManager.addCamera(fpcamera);
		cameraManager.setCurrentCamera(tpcamera);
		
		while (!Display.isCloseRequested()) { // loops until exit button pushed

		    InputHelper.update();
			
			cameraManager.update(cameraManager); // update current camera selected
			cameraManager.getCurrentCamera().move(); // move current camera
			
			for (Terrain terrain : terrainList)
			{
				if (terrain.isEntityInsideTerrain(player))
				{
					renderer.processTerrain(terrain);
					player.move(terrain);
				}
			}

            for (Entity entity : entities)
            {
//				entity.increaseRotation(0, 1, 0);
                renderer.processEntity(entity);
            }
			
			renderer.processEntity(player);
			renderer.processEntity(stallEntity);
			renderer.processEntity(dragonEntity);


			renderer.render(light, cameraManager.getCurrentCamera());
			
			// game logic
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
