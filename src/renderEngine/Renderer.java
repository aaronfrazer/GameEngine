package renderEngine;

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
import toolbox.Maths;

/**
 * Renders a model from a VAO.
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
	 * Constructor that creates projection matrix.
	 * The projection matrix is set up only once.  Therefore this
	 * method should be only run one time!
	 * @param shader - shader program
	 */
	public Renderer(StaticShader shader)
	{
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
		GL11.glClearColor(0.0f, 0.3f, 0.0f, 1); // dark green color
	}
	
	/**
	 * Renders an entity.
	 * @param entity - entity
	 * @param shader - entity's transformation
	 */
	public void render(Entity entity, StaticShader shader)
	{
		TexturedModel model = entity.getModel();
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
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
