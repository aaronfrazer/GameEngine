package guis;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;
import toolbox.Maths;

import java.util.List;

/**
 * A class responsible for rendering GUIs.
 * @author Aaron Frazer
 */
public class GuiRenderer
{
    /**
     * Quad VAO used to render GUI texture
     */
    private final RawModel quad;

    /**
     * GUI shader program
     */
    private GuiShader shader;

    /**
     * Creates a GUI renderer to render a quad model and creates
     * a new GUI shader program.
     * @param loader loader object
     */
    public GuiRenderer(Loader loader)
    {
        float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions, 2);
        shader = new GuiShader();
    }

    /**
     * Renders a list of GUI textures.
     * @param guis list of GUI textures
     */
    public void render(List<GuiTexture> guis)
    {
        shader.start();

        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        // Enable alpha blending (transparency)
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Enable depth testing (for GUIs on top of each other)
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (GuiTexture gui : guis)
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTextureID());
            Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
            shader.loadTransformation(matrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        shader.stop();
    }

    /**
     * Cleans up GUI shader.
     * Called when game is closed.
     */
    public void cleanUp()
    {
        shader.cleanUp();
    }
}
