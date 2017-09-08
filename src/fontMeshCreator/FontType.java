package fontMeshCreator;

import java.io.File;

/**
 * Represents a font. It holds the font's texture atlas as well as having the
 * ability to create the quad vertices for any text using this font.
 *
 * @author Aaron Frazer
 */
public class FontType
{
    /**
     * Font texture atlas ID
     */
    private int textureAtlas;

    /**
     * Text mesh creator based off font file for this font
     */
    private TextMeshCreator loader;

    /**
     * Creates a new font and loads up the data about each character from the font file.
     * @param textureAtlas ID of font's texture atlas
     * @param fontFile font file containing information about each character in texture atlas
     */
    public FontType(int textureAtlas, File fontFile)
    {
        this.textureAtlas = textureAtlas;
        this.loader = new TextMeshCreator(fontFile);
    }

    /**
     * Takes in an unloaded text and calculates all of the vertices for the quads
     * on which this text will be rendered. The vertex positions and texture
     * coordinates are calculated based on the information from the font file.
     * @param text unloaded text
     * @return information about the vertices of all the quads
     */
    public TextMeshData loadText(GUIText text)
    {
        return loader.createTextMesh(text);
    }

    /**
     * Returns the font's texture atlas ID
     * @return font texture atlas ID
     */
    public int getTextureAtlas()
    {
        return textureAtlas;
    }
}
