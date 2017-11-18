package fontMeshCreator;

import fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a piece of text in the game.
 *
 * @author Aaron Frazer
 */
public class GUIText
{
    /**
     * String of text
     */
    private String textString;

    /**
     * Size of text
     * Default size is 1
     */
    private float fontSize;

    /**
     * VAO mesh of quads that text will be rendered onto
     */
    private int textMeshVao;

    /**
     * Number of vertices
     */
    private int vertexCount;

    /**
     * Width of the virtual page in terms of screen width (1 is full screen width, 0.5 is half the width of the screen, etc.)
     * Text cannot go off the edge of the page, so if the text is longer than this length it will go onto the next line.
     * When text is centered it is centered into the middle of the line, based on this line length value.
     */
    private float lineMaxSize;

    /**
     * Number of lines the text goes over if it is long
     */
    private int numberOfLines;

    /**
     * Is text centered?
     */
    private boolean centerText;

    /**
     * Is text currently on screen?
     */
    private boolean isOnScreen;

    /**
     * Position on the screen where top left corner of text will be rendered
     * Top left corner of the screen is (0, 0) and bottom right is (1, 1)
     */
    private Vector2f position;

    /**
     * Font type for this text
     */
    private FontType font;

    /**
     * Color of text
     */
    private Vector3f colour;

    /**
     * Width of character (between 0 and 1
     */
    private float distanceFieldWidth;

    /**
     * Pixelation of edge (soft/hard edge)
     */
    private float distanceFieldEdge;

    /**
     * Outline of character
     */
    private float borderWidth;

    /**
     * Edge transition distance
     */
    private float borderEdge;

    /**
     * Dropshadow effect
     */
    private Vector2f offset;

    /**
     * Outline color of text
     */
    private Vector3f outlineColour;

    /**
     * Creates a text, loads the text's quads into a VAO, and adds the text to the screen.
     * @param text text
     * @param fontSize font size of text
     * @param font font
     * @param position position where top left corner of text should be rendered
     * @param maxLineLength line length
     * @param centered  true if text is centered
     */
    public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered)
    {
        this.textString = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        this.lineMaxSize = maxLineLength;
        this.centerText = centered;
        this.isOnScreen = true;
        this.colour = new Vector3f(0f, 0f, 0f);
        this.offset = new Vector2f(0f, 0f);
        this.outlineColour = new Vector3f(0f, 0f, 0f);
//        TextMaster.loadText(this);
    }

    /**
     * Adds this text to the screen.
     */
    public void add()
    {
        TextMaster.loadText(this);
        isOnScreen = true;
    }

    /**
     * Replaces this text with a new string of text.
     */
    public void update(String newString)
    {
        remove();
        this.textString = newString;
        TextMaster.loadText(this);
    }

    /**
     * Removes this text from the screen.
     */
    public void remove()
    {
        TextMaster.removeText(this);
        isOnScreen = false;
    }

    /**
     * Returns the string of this text.
     * @return string
     */
    public String getTextString()
    {
        return textString;
    }

    /**
     * Returns the font size of this text.
     * A font size of 1 is normal.
     * @return font size
     */
    protected float getFontSize()
    {
        return fontSize;
    }

    /**
     * Returns the ID of this text's VAO which contains all vertex data for quads on which the text will be rendered.
     * @return VAO ID
     */
    public int getMesh()
    {
        return textMeshVao;
    }

    /**
     * Returns the total number of vertices of all this text's quads.
     * @return number of text's vertices
     */
    public int getVertexCount()
    {
        return this.vertexCount;
    }

    /**
     * Returns the maximum line length of this text
     * @return max line length
     */
    protected float getMaxLineSize()
    {
        return lineMaxSize;
    }

    /**
     * Returns the number of lines of text.  This is determined when the text is loaded,
     * based on the length of the text and the max line length that is set.
     * @return number of lines of text
     */
    public int getNumberOfLines()
    {
        return numberOfLines;
    }

    /**
     * Returns true if the text should be centered
     * @return {@code true} text is centered
     */
    protected boolean isCentered()
    {
        return centerText;
    }

    /**
     * Returns true if the text is currently displayed on the screen.
     * @return {@code true} text is on screen
     */
    public boolean isOnScreen()
    {
        return isOnScreen;
    }

    /**
     * Returns the color of this text
     * @return text color
     */
    public Vector3f getColour()
    {
        return colour;
    }

    /**
     * Returns the top-left corner of the text in screen-space.
     * @return text position
     */
    public Vector2f getPosition()
    {
        return position;
    }

    /**
     * Returns the font used by this text.
     * @return font
     */
    public FontType getFont()
    {
        return font;
    }

    /**
     * Sets the VAO and vertex count for this text.
     * @param vao VAO containing all the vertex data for the quads on which the text will be rendered.
     * @param verticesCount total number of vertices in all of the quads.
     */
    public void setMeshInfo(int vao, int verticesCount)
    {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
    }

    /**
     * Sets the number of lines that this text covers.
     * This method is only used in loading.
     * @param number number of lines
     */
    protected void setNumberOfLines(int number)
    {
        this.numberOfLines = number;
    }

    /**
     * Sets the color of this text.
     * @param r red value, between 0 and 1
     * @param g green value, between 0 and 1
     * @param b blue value, between 0 and 1
     */
    public void setColour(float r, float g, float b)
    {
        colour.set(r, g, b);
    }

    /**********************************
     * Text effects getters/setters
     **********************************/

    public float getDistanceFieldWidth()
    {
        return distanceFieldWidth;
    }

    public float getDistanceFieldEdge()
    {
        return distanceFieldEdge;
    }

    public float getBorderWidth()
    {
        return borderWidth;
    }

    public float getBorderEdge()
    {
        return borderEdge;
    }

    public Vector2f getOffset()
    {
        return offset;
    }

    public Vector3f getOutlineColour()
    {
        return outlineColour;
    }

    public void setDistanceFieldWidth(float distanceFieldWidth)
    {
        this.distanceFieldWidth = distanceFieldWidth;
    }

    public void setDistanceFieldEdge(float distanceFieldEdge)
    {
        this.distanceFieldEdge = distanceFieldEdge;
    }

    public void setBorderWidth(float borderWidth)
    {
        this.borderWidth = borderWidth;
    }

    public void setBorderEdge(float borderEdge)
    {
        this.borderEdge = borderEdge;
    }

    public void setOffset(float x, float y)
    {
        offset.set(x, y);
    }

    public void setOutlineColour(float r, float g, float b)
    {
        outlineColour.set(r, g, b);
    }
}