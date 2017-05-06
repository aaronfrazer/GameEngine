package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

/**
 * Responsible for handling all rendering code in the game.
 * 
 * @author Aaron Frazer
 */
public class MasterRenderer
{
	/**
	 * Instance of static shader
	 */
	private StaticShader shader = new StaticShader();
	
	/**
	 * Instance of renderer
	 */
	private Renderer renderer = new Renderer(shader);
	
	/**
	 * Hashmap that contains all textured models and respective entities that need to be
	 * rendered for a particular frame
	 */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	/**
	 * Renders all entities in the scene.
	 * @param sun - light
	 * @param camera - camera
	 */
	public void render(Light sun, Camera camera)
	{
		renderer.prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		entities.clear();
	}
	
	/**
	 * Processes an entity by putting it into a hashmap.
	 * @param entity - entity to be processed
	 */
	public void processEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null)
		{
			batch.add(entity);
		} else 
		{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	/**
	 * Cleans up the shader.  Called when game gets closed.
	 */
	public void cleanUp()
	{
		shader.cleanUp();
	}
}
