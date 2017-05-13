package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

/**
 * Main game loop responsible for running our game.
 * 
 * @author Aaron Frazer
 */
public class MainGameLoop
{
	
	/**
	 * Runs the game.
	 * @param args - arguments
	 */
	public static void main(String[] args)
	{
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		//********** TERRAIN TEXTURES **********
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassTexture"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirtTexture"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowersTexture"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("pathTexture"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		//**************************************
		
		// Load a tree
		ModelData treeModelData = OBJFileLoader.loadOBJ("treeModel");
		RawModel treeRawModel = loader.loadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(), treeModelData.getNormals(), treeModelData.getIndices());
		TexturedModel treeTexturedModel = new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("treeTexture")));
		
		// Load a dragon
		ModelData dragonModelData = OBJFileLoader.loadOBJ("dragonModel");
		RawModel dragonRawModel = loader.loadToVAO(dragonModelData.getVertices(), dragonModelData.getTextureCoords(), dragonModelData.getNormals(), dragonModelData.getIndices());
		TexturedModel dragonTexturedModel = new TexturedModel(dragonRawModel, new ModelTexture(loader.loadTexture("brownTexture")));
		ModelTexture dragonModelTexture = dragonTexturedModel.getTexture();
		dragonModelTexture.setShineDamper(10);
		dragonModelTexture.setReflectivity(1);
		Entity dragonEntity = new Entity(dragonTexturedModel, new Vector3f(100, 0, 280), 0, 0, 0, 1);

		// Load a stall
		ModelData stallModelData = OBJFileLoader.loadOBJ("stallModel");
		RawModel stallRawModel = loader.loadToVAO(stallModelData.getVertices(), stallModelData.getTextureCoords(), stallModelData.getNormals(), stallModelData.getIndices());
		TexturedModel stallTexturedModel = new TexturedModel(stallRawModel, new ModelTexture(loader.loadTexture("stallTexture")));
		Entity stallEntity = new Entity(stallTexturedModel, new Vector3f(180, 0, 260), 0, 180, 0, 1);
		
		// Load a grass model
		ModelData grassModelData = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassRawModel = loader.loadToVAO(grassModelData.getVertices(), grassModelData.getTextureCoords(), grassModelData.getNormals(), grassModelData.getIndices());
		TexturedModel grassTexturedModel = new TexturedModel(grassRawModel, new ModelTexture(loader.loadTexture("grass")));
		grassTexturedModel.getTexture().setHasTransparency(true);
		grassTexturedModel.getTexture().setUseFakeLighting(true);
		
		// Load a fern model
		ModelData fernModelData = OBJFileLoader.loadOBJ("fernModel");
		RawModel fernRawModel = loader.loadToVAO(fernModelData.getVertices(), fernModelData.getTextureCoords(), fernModelData.getNormals(), fernModelData.getIndices());
		TexturedModel fernTexturedModel = new TexturedModel(fernRawModel, new ModelTexture(loader.loadTexture("fernTexture")));
		fernTexturedModel.getTexture().setHasTransparency(true);

		
		// Randomly generate trees, grass and ferns
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 100; i++)
		{
			entities.add(new Entity(treeTexturedModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * 600), 0, 0, 0, 3));
			entities.add(new Entity(grassTexturedModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * 600), 0, 0, 0, 1));
			entities.add(new Entity(fernTexturedModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * 600), 0, 0, 0, 0.6f));
		}
		
		// Use grid positions with a negative z component if you want
		// the terrains to render in front of the camera
		// E.g. (0, -1) or (1, -1) etc.
		Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(1, 0, loader, texturePack, blendMap);
		
		// Render a light
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		
		// Create the camera
		Camera camera = new Camera(200, 10, 300);
		
		MasterRenderer renderer = new MasterRenderer();
		
		// Create the player
//		RawModel bunnyModel = OBJLoader.loadObjModel("stanfordBunny", loader);
//		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
		
		ModelData bunnyModelData = OBJFileLoader.loadOBJ("stanfordBunny");
		RawModel bunnyRawModel = loader.loadToVAO(bunnyModelData.getVertices(), bunnyModelData.getTextureCoords(), bunnyModelData.getNormals(), bunnyModelData.getIndices());
		TexturedModel bunnyTexturedModel = new TexturedModel(bunnyRawModel, new ModelTexture(loader.loadTexture("brownTexture")));
		
		Player player = new Player(bunnyTexturedModel, new Vector3f(200, 0, 280), 0, 0, 0, 1);
		
		while (!Display.isCloseRequested()) { // loops until exit button pushed

			camera.move();
			player.move();
			renderer.processEntity(player);
			
			
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);

			renderer.processEntity(dragonEntity);
			renderer.processEntity(stallEntity);

			for (Entity entity : entities)
			{
//				entity.increaseRotation(0, 1, 0);
				renderer.processEntity(entity);
			}
			
			renderer.render(light, camera);
			// game logic
			
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
