package fontMeshCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.Display;

/**
 * Provides functionality for getting the values from a font file.
 *
 * @author Aaron Frazer
 */
public class MetaFile
{
    /**
     * Padding on sides
     */
    private static final int PAD_TOP = 0;
    private static final int PAD_LEFT = 1;
    private static final int PAD_BOTTOM = 2;
    private static final int PAD_RIGHT = 3;

    /**
     * Desired padding
     */
    private static final int DESIRED_PADDING = 8;

    /**
     * String splitter (space character)
     */
    private static final String SPLITTER = " ";

    /**
     * Number separator (comma character)
     */
    private static final String NUMBER_SEPARATOR = ",";

    /**
     * Aspect ratio of font file
     */
    private double aspectRatio;

    /**
     * Pixel size
     */
    private double verticalPerPixelSize;
    private double horizontalPerPixelSize;

    /**
     * Width of space character
     */
    private double spaceWidth;

    /**
     * Array of padding
     */
    private int[] padding;

    /**
     * Amount of padding
     */
    private int paddingWidth;
    private int paddingHeight;

    /**
     * Hash map of characters
     */
    private Map<Integer, Character> metaData = new HashMap<>();

    /**
     * Buffered reader
     */
    private BufferedReader reader;

    /**
     * Hash map of string values
     */
    private Map<String, String> values = new HashMap<>();

    /**
     * Opens a font file in preparation for reading.
     * @param file font file
     */
    protected MetaFile(File file)
    {
        this.aspectRatio = (double) Display.getWidth() / (double) Display.getHeight();
        openFile(file);
        loadPaddingData();
        loadLineSizes();
        int imageWidth = getValueOfVariable("scaleW");
        loadCharacterData(imageWidth);
        close();
    }

    /**
     * Reads the next line and stores the variable values.
     * @return {@code true} file hasn't been reached.
     */
    private boolean processNextLine()
    {
        values.clear();
        String line = null;
        try
        {
            line = reader.readLine();
        } catch (IOException e1)
        {
        }
        if (line == null)
        {
            return false;
        }
        for (String part : line.split(SPLITTER))
        {
            String[] valuePairs = part.split("=");
            if (valuePairs.length == 2)
            {
                values.put(valuePairs[0], valuePairs[1]);
            }
        }
        return true;
    }

    /**
     * Closes the font file after it has been read.
     */
    private void close()
    {
        try
        {
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens a font file to prepare it for reading.
     * @param file font file.
     */
    private void openFile(File file)
    {
        try
        {
            reader = new BufferedReader(new FileReader(file));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Couldn't read font meta file!");
        }
    }

    /**
     * Calculates how much padding is used around each character in the texture atlas.
     */
    private void loadPaddingData()
    {
        processNextLine();
        this.padding = getValuesOfVariable("padding");
        this.paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT];
        this.paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM];
    }

    /**
     * Loads information about the line height for this font in pixels, and uses this as a
     * way to find the conversion rate between pixels in the texture atlas and screen-space.
     */
    private void loadLineSizes()
    {
        processNextLine();
        int lineHeightPixels = getValueOfVariable("lineHeight") - paddingHeight;
        verticalPerPixelSize = TextMeshCreator.LINE_HEIGHT / (double) lineHeightPixels;
        horizontalPerPixelSize = verticalPerPixelSize / aspectRatio;
    }

    /**
     * Loads data about each character and stores the data in the Character class.
     * @param imageWidth width of texture atlas in pixels
     */
    private void loadCharacterData(int imageWidth)
    {
        processNextLine();
        processNextLine();
        while (processNextLine())
        {
            Character c = loadCharacter(imageWidth);
            if (c != null)
            {
                metaData.put(c.getId(), c);
            }
        }
    }

    /**
     * Loads all the data about one character in the texture atlas and converts
     * it from 'pixels' to 'screen-space' before storing. The effects of
     * padding are also removed from the data.
     * @param imageSize size of texture atlas in pixels
     * @return character data
     */
    private Character loadCharacter(int imageSize)
    {
        int id = getValueOfVariable("id");
        if (id == TextMeshCreator.SPACE_ASCII)
        {
            this.spaceWidth = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize;
            return null;
        }
        double xTex = ((double) getValueOfVariable("x") + (padding[PAD_LEFT] - DESIRED_PADDING)) / imageSize;
        double yTex = ((double) getValueOfVariable("y") + (padding[PAD_TOP] - DESIRED_PADDING)) / imageSize;
        int width = getValueOfVariable("width") - (paddingWidth - (2 * DESIRED_PADDING));
        int height = getValueOfVariable("height") - ((paddingHeight) - (2 * DESIRED_PADDING));
        double quadWidth = width * horizontalPerPixelSize;
        double quadHeight = height * verticalPerPixelSize;
        double xTexSize = (double) width / imageSize;
        double yTexSize = (double) height / imageSize;
        double xOff = (getValueOfVariable("xoffset") + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPerPixelSize;
        double yOff = (getValueOfVariable("yoffset") + (padding[PAD_TOP] - DESIRED_PADDING)) * verticalPerPixelSize;
        double xAdvance = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize;

        return new Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance);
    }

    /**
     * Returns an integer value of a variable with a certain name.
     * @param variable name of variable.
     * @return value of variable
     */
    private int getValueOfVariable(String variable)
    {
        return Integer.parseInt(values.get(variable));
    }

    /**
     * Returns an array of ints associated with a variable.
     * @param variable name of variable.
     * @return The int array of values associated with the variable.
     */
    private int[] getValuesOfVariable(String variable)
    {
        String[] numbers = values.get(variable).split(NUMBER_SEPARATOR);
        int[] actualValues = new int[numbers.length];
        for (int i = 0; i < actualValues.length; i++)
        {
            actualValues[i] = Integer.parseInt(numbers[i]);
        }

        return actualValues;
    }

    /**
     * Returns a character according to it's ASCII value.
     * @param ascii ASCII value
     * @return character
     */
    protected Character getCharacter(int ascii)
    {
        return metaData.get(ascii);
    }

    /**
     * Returns the width of the space character
     * @return space width
     */
    protected double getSpaceWidth()
    {
        return spaceWidth;
    }
}