package fontRendering;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import toolbox.GameSettings;

import java.util.List;
import java.util.Map;

/**
 * Responsible for rendering font the the screen.
 *
 * @author Aaron Frazer
 */
public class FontRenderer
{
    /**
     * Font shader
     */
    private FontShader shader;

    /**
     * Creates a font renderer.
     */
    public FontRenderer()
    {
        shader = new FontShader();
    }

    /**
     * Cleans up resources used by the font shader.
     */
    public void cleanUp()
    {
        shader.cleanUp();
    }

    /**
     * Renders a font.
     * @param texts hashmap of texts that need to be rendered
     */
    public void render(Map<FontType, List<GUIText>> texts)
    {
        prepare();
        for (FontType font : texts.keySet())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTextureAtlas());
            for (GUIText text : texts.get(font))
            {
                renderText(text);
            }
        }
        endRendering();
    }

    /**
     * Prepares the font for rendering by starting the font shader.
     */
    private void prepare()
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        shader.start();
    }

    /**
     * Renders a text.
     * @param text text
     */
    private void renderText(GUIText text)
    {
        GL30.glBindVertexArray(text.getMesh());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        shader.loadColour(text.getColour());
        shader.loadTranslation(text.getPosition());

        shader.loadWidth(GameSettings.FONT_WIDTH);
        shader.loadEdge(GameSettings.FONT_EDGE);
        shader.loadBorderWidth(GameSettings.FONT_BORDER_WIDTH);
        shader.loadBorderEdge(GameSettings.FONT_BORDER_EDGE);
        shader.loadOffset(GameSettings.FONT_OFFSET_X, GameSettings.FONT_OFFSET_Y);
        shader.loadOutlineColour(GameSettings.FONT_OUTLINE_COLOR_RED, GameSettings.FONT_OUTLINE_COLOR_GREEN, GameSettings.FONT_OUTLINE_COLOR_BLUE);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    /**
     * Ends font rendering by stopping the font shader.
     */
    private void endRendering()
    {
        shader.stop();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
}