package water;

import entities.Camera;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import toolbox.Maths;

import java.util.List;

/**
 * A class responsible for rendering water in the game.
 */
public class WaterRenderer
{
	/**
	 * 3D model of the water (just a quad)
	 */
	private RawModel quad;

	/**
	 * Water shader program
	 */
	private WaterShader shader;

	/**
	 * Water frame buffer object
	 */
	private WaterFrameBuffers fbos;

	/**
	 * Constructs a water renderer.
	 *
	 * @param loader           - loader
	 * @param shader           - water shader program
	 * @param projectionMatrix - projection matrix
	 * @param fbos - water frame buffer object
	 */
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos)
	{
		this.shader = shader;
		this.fbos = fbos;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	/**
	 * Renders water.
	 *
	 * @param water  - list of water tiles
	 * @param camera - camera
	 */
	public void render(List<WaterTile> water, Camera camera)
	{
		prepareRender(camera);
		for (WaterTile tile : water)
		{
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}

	/**
	 * Prepares water by loading the camera into the view matrix, binding
	 * quad texture to VAO attributes, and binds reflection/refraction
	 * textures to their respective texture units.
	 *
	 * @param camera - cameramodel to be prepared
	 */
	private void prepareRender(Camera camera)
	{
		shader.start();
		shader.loadViewMatrix(camera);

		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
	}

	/**
	 * Unbinds attributes of water.
	 */
	private void unbind()
	{
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Loads a quad into a VAO.
	 *
	 * @param loader - loader to load 3D model
	 */
	private void setUpVAO(Loader loader)
	{
		// Just x and z vertex positions here, y is set to 0 in v.shader
		float[] vertices = {-1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1};
		quad = loader.loadToVAO(vertices, 2);
	}

}
