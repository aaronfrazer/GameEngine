package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Renders a model from a VAO.
 * 
 * @author Aaron Frazer
 */
public class Renderer
{
	/**
	 * Prepares OpenGL to render the game.
	 * Called once every frame.
	 */
	public void prepare()
	{
		GL11.glClearColor(1, 0, 0, 1); // red color
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	/**
	 * Renders a Raw Model.
	 */
	public void render(RawModel model)
	{
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
