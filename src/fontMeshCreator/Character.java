package fontMeshCreator;

/**
 * Simple data structure class holding information about a certain glyph in a font texture atlas.
 * All sizes are for a font-size of 1.
 *
 * @author Aaron Frazer
 */
public class Character
{
    /**
     * ASCII value
     */
    private int id;

    /**
     * X and Y coordinates for top left corner of character in texture atlas
     */
    private double xTextureCoord;
    private double yTextureCoord;

    /**
     * Maximum distance for X and Y coordinates
     */
    private double xMaxTextureCoord;
    private double yMaxTextureCoord;

    /**
     * X and Y distance from the cursor to the left/top edge of character's quad
     */
    private double xOffset;
    private double yOffset;

    /**
     * Width and height of character's quad in screen space
     */
    private double sizeX;
    private double sizeY;

    /**
     * Number of pixels cursor should advance after adding this character
     */
    private double xAdvance;

    /**
     * Creates a character.
     * @param id ASCII value
     * @param xTextureCoord x texture coordinate for the top left corner
     * @param yTextureCoord y texture coordinate for the top left corner
     * @param xTexSize width
     * @param yTexSize height
     * @param xOffset x distance from cursor to the left edge of character's quad
     * @param yOffset y distance from cursor to the top edge of character's quad
     * @param sizeX width of character's quad in screen space
     * @param sizeY height of character's quad in screen space
     * @param xAdvance distance cursor should advance after adding this character
     */
    protected Character(int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize,
                        double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance)
    {
        this.id = id;
        this.xTextureCoord = xTextureCoord;
        this.yTextureCoord = yTextureCoord;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.xMaxTextureCoord = xTexSize + xTextureCoord;
        this.yMaxTextureCoord = yTexSize + yTextureCoord;
        this.xAdvance = xAdvance;
    }

    /**
     * Returns the ASCII value of this character.
     * @return ASCII value
     */
    protected int getId()
    {
        return id;
    }

    /**
     * Returns X coordinate for top left corner of this character.
     * @return X coordinate
     */
    protected double getxTextureCoord()
    {
        return xTextureCoord;
    }

    /**
     * Returns Y coordinate for top left corner of this character.
     * @return Y coordinate
     */
    protected double getyTextureCoord()
    {
        return yTextureCoord;
    }

    /**
     * Returns the maximum X coordinate distance of this character.
     * @return max distance for X coordinate
     */
    protected double getXMaxTextureCoord()
    {
        return xMaxTextureCoord;
    }

    /**
     * Returns the maximum Y coordinate distance of this character.
     * @return max distance for Y coordinate
     */
    protected double getYMaxTextureCoord()
    {
        return yMaxTextureCoord;
    }

    /**
     * Returns the X distance from the cursor to the left edge of this character
     * @return X distance
     */
    protected double getxOffset()
    {
        return xOffset;
    }

    /**
     * Returns the Y distance from the cursor to the top edge of this character
     * @return Y distance
     */
    protected double getyOffset()
    {
        return yOffset;
    }

    /**
     * Returns the width of this character's quad in screen space
     * @return width of character
     */
    protected double getSizeX()
    {
        return sizeX;
    }

    /**
     * Returns the height of this character's quad in screen space
     * @return height of character
     */
    protected double getSizeY()
    {
        return sizeY;
    }

    /**
     * Returns the distance the cursor will advance after adding this character
     * @return cursor distance
     */
    protected double getxAdvance()
    {
        return xAdvance;
    }

}
