package engineTester;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

/**
 * Main game loop to run our game.
 * 
 * @author Aaron Frazer
 */
public class MainGameLoop
{

	public static void main(String[] args)
	{
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		// TODO: All of this can be removed. It's just used for debugging.
	    System.out.println("OS name " + System.getProperty("os.name"));
	    System.out.println("OS version " + System.getProperty("os.version"));
	    System.out.println("LWJGL version " + org.lwjgl.Sys.getVersion());
	    System.out.println("OpenGL version " + glGetString(GL_VERSION));
	    
		StaticShader shader = new StaticShader();
		
		float[] vertices = {
				-0.5f,  0.5f, 0f, // V0
				-0.5f, -0.5f, 0f, // V1
				 0.5f, -0.5f, 0f, // V2
				 0.5f,  0.5f, 0f  // V3
		};
		  
		int[] indices = {
				0,1,3, // Top left triangle (V0, V1, V3)
				3,1,2  // Bottom right triangle (V3, V1, V2)
		};
		
		RawModel model = loader.loadToVAO(vertices, indices);
		
		while (!Display.isCloseRequested()) { // loops until exit button pushed
			
			renderer.prepare();
			shader.start();
			// game logic
			renderer.render(model);
			shader.stop();
			
			DisplayManager.updateDisplay();
			
			
		}
		
		shader.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
