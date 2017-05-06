package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;

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
		
		// Render a dragon
		RawModel dragonModel = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel dragonTextureModel = new TexturedModel(dragonModel, new ModelTexture(loader.loadTexture("white")));
		ModelTexture dragonTexture = dragonTextureModel.getTexture();
		dragonTexture.setShineDamper(10);
		dragonTexture.setReflectivity(1);
		Entity dragonEntity = new Entity(dragonTextureModel, new Vector3f(200, 0, 280), 0, 0, 0, 1);
		
		// Render a stall
		RawModel stallModel = OBJLoader.loadObjModel("stall", loader);
		TexturedModel stallTextureModel = new TexturedModel(stallModel, new ModelTexture(loader.loadTexture("stallTexture")));
		Entity stallEntity = new Entity(stallTextureModel, new Vector3f(180, 0, 260), 0, 180, 0, 1);
				
		TexturedModel treeModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader), new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grassModel = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
		grassModel.getTexture().setHasTransparency(true);;
		grassModel.getTexture().setUseFakeLighting(true);
		TexturedModel fernModel = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
		fernModel.getTexture().setHasTransparency(true);;

		
		// Render trees, grass and ferns
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 100; i++)
		{
			entities.add(new Entity(treeModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * 600), 0, 0, 0, 3));
			entities.add(new Entity(grassModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * 600), 0, 0, 0, 1));
			entities.add(new Entity(fernModel, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * 600), 0, 0, 0, 0.6f));
		}
		
		// Use grid positions with a negative z component if you want
		// the terrains to render in front of the camera
		// E.g. (0, -1) or (1, -1) etc.
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass")));
		
		// Render a light
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		
		// Create the camera
		Camera camera = new Camera(200, 5, 300);
		
		MasterRenderer renderer = new MasterRenderer();
		
		while (!Display.isCloseRequested()) { // loops until exit button pushed

			camera.move();

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
