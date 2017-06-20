package skybox;

import entities.Camera;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;
import toolbox.GameSettings;
import toolbox.VirtualClock;
import engineTester.MainGameLoop;
import renderEngine.MasterRenderer;
import org.lwjgl.util.vector.Vector3f;

/**
 * A class responsible for rendering a skybox from a VAO.
 *
 * @author Aaron Frazer
 */
public class SkyboxRenderer
{
	/**
	 * Size of the skybox cube
	 */
	private static final float SIZE = 500f;

	/**
	 * Vertex positions of skybox cube
	 */
	private static final float[] VERTICES = {
			-SIZE,  SIZE, -SIZE,
			-SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE,  SIZE, -SIZE,
			-SIZE,  SIZE, -SIZE,

			-SIZE, -SIZE,  SIZE,
			-SIZE, -SIZE, -SIZE,
			-SIZE,  SIZE, -SIZE,
			-SIZE,  SIZE, -SIZE,
			-SIZE,  SIZE,  SIZE,
			-SIZE, -SIZE,  SIZE,

			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE,  SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,

			-SIZE, -SIZE,  SIZE,
			-SIZE,  SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE, -SIZE,  SIZE,
			-SIZE, -SIZE,  SIZE,

			-SIZE,  SIZE, -SIZE,
			SIZE,  SIZE, -SIZE,
			SIZE,  SIZE,  SIZE,
			SIZE,  SIZE,  SIZE,
			-SIZE,  SIZE,  SIZE,
			-SIZE,  SIZE, -SIZE,

			-SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE,  SIZE,
			SIZE, -SIZE, -SIZE,
			SIZE, -SIZE, -SIZE,
			-SIZE, -SIZE,  SIZE,
			SIZE, -SIZE,  SIZE
	};

	/**
	 * Texture file names
	 */
	private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};

	/**
	 * Texture file names of night skybox
	 */
	private static String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};

	/**
	 * Model of the skybox's cube
	 */
	private RawModel cube;

	/**
	 * Texture ID used for day cubemap
	 */
	private int texture;

	/**
	 * Texture ID used for night cubemap
	 */
	private int nightTexture;

	/**
	 * Skybox shader program
	 */
	private SkyboxShader shader;

	/**
	 * Creates a skybox renderer.
	 *
	 * @param loader - texture loader
	 * @param projectionMatrix - projection matrix (to be loaded to shader)
	 */
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix)
	{
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Renders a skybox and sets the color of fog.
	 * @param camera - camera
	 * @param r - red color value
	 * @param g - green color value
	 * @param b - blue color value
	 */
	public void render (Camera camera, float r, float g, float b)
	{
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadFogColour(r, g, b);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Binds 2 textures (first to 0, second to 1) and loads up the blend factor.
	 */
	private void bindTextures()
	{
		int texture1;
		int texture2;
		float blendFactor; // fade from day to night.  0 = day, 1 = night

		float nightStart = 0;
		float nightEnd = 5;
		float dayStart = 8;
		float dayEnd = 21;

		// TODO: Incorporate blendFactor so that the textures fade seamlessly
        // TODO: Test fog by changing nightStart, nightEnd, dayStart, dayEnd variables
		// TODO: Make lights blend seamlessly
		if (VirtualClock.getHours() >= nightStart && VirtualClock.getHours() < nightEnd) { // fog color:
			System.out.print("Night         | ");
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = 1;
			MainGameLoop.lights.get(0).setColour(new Vector3f(0.3f,0.3f,0.3f));
			MasterRenderer.RED = GameSettings.NIGHT_MAX_RED;
			MasterRenderer.GREEN = GameSettings.NIGHT_MAX_GREEN;
			MasterRenderer.BLUE = GameSettings.NIGHT_MAX_BLUE;
			System.out.print(" R: " + MasterRenderer.RED + " G: " + MasterRenderer.GREEN + " B: " + MasterRenderer.BLUE + " | ");
		} else if (VirtualClock.getHours() >= nightEnd && VirtualClock.getHours() < dayStart) {

			System.out.print("Night --> Day | ");
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (float) ((VirtualClock.getHours()*1000) - (nightEnd*1000))/((dayStart*1000) - (nightEnd*1000));
			MainGameLoop.lights.get(0).increaseColor(new Vector3f(0.0001f,0.0001f,0.0001f));

			// Add fog colors with each frame
            double factor = (120 * (dayStart - nightEnd) * 2); // 720 frames in 4 hours
            double intervalRed = (GameSettings.DAY_MAX_RED - GameSettings.NIGHT_MAX_RED) / factor;
            double intervalGreen = (GameSettings.DAY_MAX_GREEN - GameSettings.NIGHT_MAX_GREEN) / factor;
            double intervalBlue = (GameSettings.DAY_MAX_BLUE - GameSettings.NIGHT_MAX_BLUE) / factor;

            MasterRenderer.RED += intervalRed;
            MasterRenderer.GREEN += intervalGreen;
            MasterRenderer.BLUE += intervalBlue;

			System.out.print(" R: " + MasterRenderer.RED + " G: " + MasterRenderer.GREEN + " B: " + MasterRenderer.BLUE + " | ");

		} else if (VirtualClock.getHours() >= dayStart && VirtualClock.getHours() < dayEnd) {

            System.out.print("Day           | ");
			texture1 = texture;
			texture2 = texture;
			blendFactor = 0;
			MainGameLoop.lights.get(0).setColour(new Vector3f(1f,1f,1f));
			MasterRenderer.RED = GameSettings.DAY_MAX_RED;
			MasterRenderer.GREEN = GameSettings.DAY_MAX_GREEN;
			MasterRenderer.BLUE = GameSettings.DAY_MAX_BLUE;
			System.out.print(" R: " + MasterRenderer.RED + " G: " + MasterRenderer.GREEN + " B: " + MasterRenderer.BLUE + " | ");

		} else if (VirtualClock.getHours() >= dayEnd && VirtualClock.getHours() < 24) {

		    System.out.print("Day --> Night | ");
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (float) ((VirtualClock.getHours()*1000) - (dayEnd*1000))/((24*1000) - (dayEnd*1000));
//			blendFactor = (VirtualClock.getTime() - 21000)/(24000 - 21000);
			MainGameLoop.lights.get(0).decreaseColor(new Vector3f(0.0001f,0.0001f,0.0001f));

            // Add fog colors with each frame
            double factor = (120 * (24 - dayEnd) * 2); // 720 frames in 4 hours
            double intervalRed = (GameSettings.DAY_MAX_RED - GameSettings.NIGHT_MAX_RED) / factor;
            double intervalGreen = (GameSettings.DAY_MAX_GREEN - GameSettings.NIGHT_MAX_GREEN) / factor;
            double intervalBlue = (GameSettings.DAY_MAX_BLUE - GameSettings.NIGHT_MAX_BLUE) / factor;

			MasterRenderer.RED -= intervalRed;
			MasterRenderer.GREEN -= intervalGreen;
			MasterRenderer.BLUE -= intervalBlue;

			System.out.print(" R: " + MasterRenderer.RED + " G: " + MasterRenderer.GREEN + " B: " + MasterRenderer.BLUE + " | ");

		} else {
            System.err.println("Error in time: Time has gone over 24 hours: " + VirtualClock.getHours() + " hours");
			texture1 = texture;
			texture2 = texture;
			blendFactor = 0;
		}

		System.out.print("blend = " + ((int) ((blendFactor * 1000.0) + ((blendFactor < 0.0) ? -0.5 : 0.5))) / 1000.0 + " | " + VirtualClock.getTimeString() + "\n");

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
}
