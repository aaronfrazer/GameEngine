package fontRendering;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for everything text related.  This is the master class of all
 * the text rendered in the game.
 *
 * @author Aaron Frazer
 */
public class TextMaster
{
    /**
     * Loader used to load text
     */
    private static Loader loader;

    /**
     * Each GUI list holds the font type used in that list
     */
    private static Map<FontType, List<GUIText>> texts = new HashMap<>();

    /**
     * Renders text
     */
    private static FontRenderer renderer;

    /**
     * Creates font renderer when game is first loaded.
     * @param theLoader loader
     */
    public static void init(Loader theLoader)
    {
        renderer = new FontRenderer();
        loader = theLoader;
    }

    /**
     * Loads up a text and adds it to the screen.
     * @param text text
     */
    public static void loadText(GUIText text)
    {
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vao, data.getVertexCount());
        List<GUIText> textBatch = texts.get(font);
        if (textBatch == null)
        {
            textBatch = new ArrayList<GUIText>();
            texts.put(font, textBatch);
        }
        textBatch.add(text);
    }

    /**
     * Removes text from the screen.
     * @param text text
     */
    public static void removeText(GUIText text)
    {
        List<GUIText> textBatch = texts.get(text.getFont());
        textBatch.remove(text);
        if (textBatch.isEmpty())
        {
            texts.remove(text.getFont());
            // TODO: delete the text's VAO and related VBOs from memory if the text is never going to be used again
            // TODO: See Tutorial 32 @ 11:20
            // remove the VAO from the list in the Loader class and
            // delete the VAO and VBO
        }
    }

    /**
     * Cleans up renderer when game is closed.
     */
    public static void cleanUp()
    {
        renderer.cleanUp();
    }
}
