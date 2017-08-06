package skybox;

import entities.Camera;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.DisplayManager;
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
	private int dayTexture;

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
	 * @param loader - dayTexture loader
	 * @param projectionMatrix - projection matrix (to be loaded to shader)
	 */
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix)
	{
		cube = loader.loadToVAO(VERTICES, 3);
		dayTexture = loader.loadCubeMap(TEXTURE_FILES);
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
	public void render(Camera camera, float r, float g, float b)
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
		float nightEnd = 2;
		float dayStart = 4;
		float dayEnd = 21;

		if (!GameSettings.DAY_ONLY_MODE)
		{
			// TODO: Fix fog variables for day/night times
			if (VirtualClock.getHours() >= nightStart && VirtualClock.getHours() < nightEnd)
			{
//			System.out.print("Night         | ");

				// Texture
				texture1 = nightTexture;
				texture2 = nightTexture;
				blendFactor = 1;

				// Light
				MainGameLoop.lights.get(0).setColour(new Vector3f(GameSettings.NIGHT_LIGHT_CONST_HOURS, GameSettings.NIGHT_LIGHT_CONST_HOURS, GameSettings.NIGHT_LIGHT_CONST_HOURS));

				// Fog
				GameSettings.RED = GameSettings.NIGHT_FOG_MAX_RED;
				GameSettings.GREEN = GameSettings.NIGHT_FOG_MAX_GREEN;
				GameSettings.BLUE = GameSettings.NIGHT_FOG_MAX_BLUE;

			} else if (VirtualClock.getHours() >= nightEnd && VirtualClock.getHours() < dayStart)
			{
//			System.out.print("Night --> Day | ");

				// Texture
				texture1 = nightTexture;
				texture2 = dayTexture;
				blendFactor = (float) ((VirtualClock.getHours()*1000) - (nightEnd*1000))/((dayStart*1000) - (nightEnd*1000));

				// Light
				float factor = (DisplayManager.FPS_CAP * (dayStart - nightEnd) * 2); // x FPS in y hours
				float interval = (GameSettings.DAY_LIGHT_CONST_HOURS - GameSettings.NIGHT_LIGHT_CONST_HOURS) / factor;
				MainGameLoop.lights.get(0).increaseColor(new Vector3f(interval, interval, interval));

				// Fog
				double intervalRed = (GameSettings.DAY_FOG_MAX_RED - GameSettings.NIGHT_FOG_MAX_RED) / factor;
				double intervalGreen = (GameSettings.DAY_FOG_MAX_GREEN - GameSettings.NIGHT_FOG_MAX_GREEN) / factor;
				double intervalBlue = (GameSettings.DAY_FOG_MAX_BLUE - GameSettings.NIGHT_FOG_MAX_BLUE) / factor;
				GameSettings.RED += intervalRed;
				GameSettings.GREEN += intervalGreen;
				GameSettings.BLUE += intervalBlue;

			} else if (VirtualClock.getHours() >= dayStart && VirtualClock.getHours() < dayEnd)
			{
//            System.out.print("Day           | ");

				// Texture
				texture1 = dayTexture;
				texture2 = dayTexture;
				blendFactor = 0;

				// Light
				MainGameLoop.lights.get(0).setColour(new Vector3f(GameSettings.DAY_LIGHT_CONST_HOURS,GameSettings.DAY_LIGHT_CONST_HOURS,GameSettings.DAY_LIGHT_CONST_HOURS));

				// Fog
				GameSettings.RED = GameSettings.DAY_FOG_MAX_RED;
				GameSettings.GREEN = GameSettings.DAY_FOG_MAX_GREEN;
				GameSettings.BLUE = GameSettings.DAY_FOG_MAX_BLUE;

			} else if (VirtualClock.getHours() >= dayEnd && VirtualClock.getHours() < 24)
			{
//		    System.out.print("Day --> Night | ");

				//Texture
				texture1 = dayTexture;
				texture2 = nightTexture;
				blendFactor = (float) ((VirtualClock.getHours()*1000) - (dayEnd*1000))/((24*1000) - (dayEnd*1000));

				// Light
				float factor = (DisplayManager.FPS_CAP * (24 - dayEnd) * 2); // x FPS in y hours
				float interval = (GameSettings.DAY_LIGHT_CONST_HOURS - GameSettings.NIGHT_LIGHT_CONST_HOURS) / factor;
				MainGameLoop.lights.get(0).decreaseColor(new Vector3f(interval,interval,interval));

				// Fog
				double intervalRed = (GameSettings.DAY_FOG_MAX_RED - GameSettings.NIGHT_FOG_MAX_RED) / factor;
				double intervalGreen = (GameSettings.DAY_FOG_MAX_GREEN - GameSettings.NIGHT_FOG_MAX_GREEN) / factor;
				double intervalBlue = (GameSettings.DAY_FOG_MAX_BLUE - GameSettings.NIGHT_FOG_MAX_BLUE) / factor;
				GameSettings.RED -= intervalRed;
				GameSettings.GREEN -= intervalGreen;
				GameSettings.BLUE -= intervalBlue;

			} else
			{
				System.err.println("Error in time: Time has gone over 24 hours: " + VirtualClock.getHours() + " hours");
				texture1 = dayTexture;
				texture2 = dayTexture;
				blendFactor = 0;
			}
		} else
		{
			texture1 = dayTexture;
			texture2 = dayTexture;
			blendFactor = 0;
		}

//		System.out.print(" R: " + MasterRenderer.RED + " G: " + MasterRenderer.GREEN + " B: " + MasterRenderer.BLUE + " | ");

		if (GameSettings.FOG_ENABLED == false)
		{
			GameSettings.RED = 1.0f;
			GameSettings.GREEN = 1.0f;
			GameSettings.BLUE = 1.0f;
		}

//		System.out.print("blend = " + ((int) ((blendFactor * 1000.0) + ((blendFactor < 0.0) ? -0.5 : 0.5))) / 1000.0 + " | " + VirtualClock.getTimeString() + "\n");

//		System.out.print("lights = " + MainGameLoop.lights.get(0).getColour() + " | " + VirtualClock.getTimeString() + "\n");

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
}
