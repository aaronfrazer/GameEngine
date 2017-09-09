package fontRendering;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
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
     * Maps a font type to a list of texts
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
     * Renders a font.
     */
    public static void render()
    {
        renderer.render(texts);
    }

    /**
     * Loads a text and adds it to the screen.
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
            textBatch = new ArrayList<>();
            texts.put(font, textBatch);
        }
        textBatch.add(text);
    }

    /**
     * Removes text from the screen.
     * @param text text to be removed
     */
    public static void removeText(GUIText text)
    {
        System.out.println("All of the VAOs: " + loader.vaos);

        List<GUIText> textBatch = texts.get(text.getFont());
        textBatch.remove(text);
        if (textBatch.isEmpty()) // if list of texts is empty
        {
            // remove the list of texts from the hashmap
            texts.remove(text.getFont());

            // TODO: delete the text's VAO and related VBOs from memory if the text is never going to be used again
            // TODO: See Tutorial 32 @ 11:20
            if (loader.vaos.contains(text.getMesh()))
            {
                // remove the VAO from the list in the Loader class
//                loader.vaos.remove(text.getMesh());

                // delete the VAO and VBO
                GL20.glDisableVertexAttribArray(0);
                for (int vboID : loader.vaos)
                {
//                    System.out.println("Removed VBO ID: " + vboID);
//                    System.out.println("Removed VAO ID: " + text.getMesh());
                    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
                    GL15.glDeleteBuffers(vboID); // delete VBO
                    GL30.glBindVertexArray(0);
                    GL30.glDeleteVertexArrays(text.getMesh()); // delete VAO
                }
            }
        }
    }

    /**
     * Deletes a VAO and its associated VBOs from memory if the text is never
     * going to be used again.
     * @param vaoID VAO to be deleted
     */
    public static void deleteVaoFromCache(int vaoID)
    {
//        List<Integer> vbos = vaoCache.remove(vao);
//        for (int vbo : vbos)
//        {
//            GL15.glDeleteBuffers(vbo);
//        }
//        GL30.glDeleteVertexArrays(vao);
    }

    /**
     * Cleans up renderer when the game closes.
     */
    public static void cleanUp()
    {
        renderer.cleanUp();
    }
}
