package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

/**
 * Responsible for rendering a model from a VAO.
 * 
 * @author Aaron Frazer
 */
public class Renderer
{
	/**
	 * Field of view is the exent the observable world that is seen at
	 * any given moment.
	 */
	private static final float FOV = 70;
	
	/**
	 * Near plane is the closest location that will be rendered.
	 */
	private static final float NEAR_PLANE = 0.1f;
	
	/**
	 * Far plane is the farthest location that will be rendered.
	 */
	private static final float FAR_PLANE = 1000;
	
	/**
	 * Projection matrix is a square matrix that gives a vector
	 * space projection from a subspace.
	 */
	private Matrix4f projectionMatrix;
	
	/**
	 * Instance of static shader.
	 */
	private StaticShader shader;
	
	/**
	 * Constructor that creates a projection matrix.
	 * The projection matrix is to be set up only once.
	 * This method should be only run once.
	 * @param shader - shader program
	 */
	public Renderer(StaticShader shader)
	{
		this.shader = shader;
		
		// Don't render backside of model (for optimization)
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	/**
	 * Prepares OpenGL to render the game.
	 * Called once every frame.
	 */
	public void prepare()
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.5f, 0.0f, 0.0f, 1); // red color
	}
	
	/**
	 * Renders a hashmap of textured models and entities.
	 * @param entities - hashmap of texturedModels and entities to be rendered
	 */
	public void render(Map<TexturedModel, List<Entity>> entities)
	{
		for (TexturedModel model : entities.keySet())
		{
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			
			for (Entity entity : batch)
			{
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			unbindTexturedModel();
		}
	}
	
	/**
	 * Prepares a textured model.
	 * @param model - model to be prepared
	 */
	private void prepareTexturedModel(TexturedModel model)
	{
		RawModel rawModel = model.getRawModel();
		// Bind raw model
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		
		ModelTexture texture = model.getTexture();
		
		// Load shine settings
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		// Bind model's texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	/**
	 * Unbinds attributed of a textured model.
	 */
	private void unbindTexturedModel()
	{
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Prepares an entity of a textured model.
	 * @param entity - instance of entity
	 */
	private void prepareInstance(Entity entity)
	{
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	/**
	 * Creates a projeciton matrix (don't worry about how the math works
	 * in this method).
	 */
	private void createProjectionMatrix()
	{
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }
}
