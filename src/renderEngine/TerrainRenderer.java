package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import shaders.TerrainShader;
import terrain.Terrain;
import textures.TerrainTexturePack;
import toolbox.Maths;

/**
 * Responsible for rendering terrain in the game.
 * 
 * @author Aaron Frazer
 */
public class TerrainRenderer
{
	/**
	 * Shader program
	 */
	private TerrainShader shader;
	
	/**
	 * Constructs a terrain renderer
	 * @param shader - shader program
	 * @param projectionMatrix - projection matrix
	 */
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}
	
	public void render(List<Terrain> terrains)
	{
		for (Terrain terrain : terrains)
		{
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			unbindTexturedModel();
		}
	}
	
	/**
	 * Prepares a terrain by binding model and texture to VAO attributes.
	 * @param terrain - terrain model to be prepared
	 */
	private void prepareTerrain(Terrain terrain)
	{
		RawModel rawModel = terrain.getModel();
		// Bind raw model
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		bindTextures(terrain);
		
		// Load shine settings
		// TODO: Add shine to terrains
		shader.loadShineVariables(1, 0);
	}
	
	/**
	 * Binds 4 terrain textures and blend map to 5 separate texture units.
	 * @param terrain - terrain 
	 */
	private void bindTextures(Terrain terrain)
	{
		TerrainTexturePack texturePack = terrain.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
	}
	
	/**
	 * Unbinds attributes of a terrain.
	 */
	private void unbindTexturedModel()
	{
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Loads a model matrix for a terrain.
	 * @param terrain - instance of terrain
	 */
	private void loadModelMatrix(Terrain terrain)
	{
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
