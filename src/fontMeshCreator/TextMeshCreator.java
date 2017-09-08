package fontMeshCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a structure for character data.
 *
 * @author Aaron Frazer
 */
public class TextMeshCreator
{
    /**
     * Height of line
     */
    protected static final double LINE_HEIGHT = 0.03f;

    /**
     * ASCII code for space character
     */
    protected static final int SPACE_ASCII = 32;

    /**
     * Data about the font file
     */
    private MetaFile metaData;

    /**
     * Creates a new structure for the data by reading in a file.
     * @param metaFile file
     */
    protected TextMeshCreator(File metaFile)
    {
        metaData = new MetaFile(metaFile);
    }

    /**
     * Creates a structure for the text and creates quad vertices for the text.
     * @param text text to be rendered
     * @return data about the structure of characters
     */
    protected TextMeshData createTextMesh(GUIText text)
    {
        List<Line> lines = createStructure(text);
        TextMeshData data = createQuadVertices(text, lines);

        return data;
    }

    /**
     * Creates a structure for the text.
     * @param text text to be rendered
     * @return lines of text
     */
    private List<Line> createStructure(GUIText text)
    {
        char[] chars = text.getTextString().toCharArray();
        List<Line> lines = new ArrayList<>();
        Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
        Word currentWord = new Word(text.getFontSize());
        for (char c : chars)
        {
            int ascii = (int) c;
            if (ascii == SPACE_ASCII)
            {
                boolean added = currentLine.attemptToAddWord(currentWord);
                if (!added)
                {
                    lines.add(currentLine);
                    currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
                    currentLine.attemptToAddWord(currentWord);
                }
                currentWord = new Word(text.getFontSize());
                continue;
            }
            Character character = metaData.getCharacter(ascii);
            currentWord.addCharacter(character);
        }
        completeStructure(lines, currentLine, currentWord, text);

        return lines;
    }

    /**
     * Completes the structure of the text.
     * @param lines list of lines
     * @param currentLine current line
     * @param currentWord current word
     * @param text text to be rendered
     */
    private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GUIText text)
    {
        boolean added = currentLine.attemptToAddWord(currentWord);
        if (!added)
        {
            lines.add(currentLine);
            currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
            currentLine.attemptToAddWord(currentWord);
        }
        lines.add(currentLine);
    }

    /**
     * Creates quad vertices for the text.
     * @param text text to be rendered
     * @param lines list of lines
     * @return data about the text
     */
    private TextMeshData createQuadVertices(GUIText text, List<Line> lines)
    {
        text.setNumberOfLines(lines.size());
        double curserX = 0f;
        double curserY = 0f;
        List<Float> vertices = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        for (Line line : lines)
        {
            if (text.isCentered())
            {
                curserX = (line.getMaxLength() - line.getLineLength()) / 2;
            }
            for (Word word : line.getWords())
            {
                for (Character letter : word.getCharacters())
                {
                    addVerticesForCharacter(curserX, curserY, letter, text.getFontSize(), vertices);
                    addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
                            letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
                    curserX += letter.getxAdvance() * text.getFontSize();
                }
                curserX += metaData.getSpaceWidth() * text.getFontSize();
            }
            curserX = 0;
            curserY += LINE_HEIGHT * text.getFontSize();
        }

        return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
    }

    /**
     * Add vertices for a characters.
     * @param curserX cursor position X
     * @param curserY cursor position Y
     * @param character character
     * @param fontSize font size
     * @param vertices list of vertices
     */
    private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize, List<Float> vertices)
    {
        double x = curserX + (character.getxOffset() * fontSize);
        double y = curserY + (character.getyOffset() * fontSize);
        double maxX = x + (character.getSizeX() * fontSize);
        double maxY = y + (character.getSizeY() * fontSize);
        double properX = (2 * x) - 1;
        double properY = (-2 * y) + 1;
        double properMaxX = (2 * maxX) - 1;
        double properMaxY = (-2 * maxY) + 1;
        addVertices(vertices, properX, properY, properMaxX, properMaxY);
    }

    /**
     * Adds vertices to the list for each X and Y value.
     * @param vertices list of vertices
     * @param x X value
     * @param y Y value
     * @param maxX maximum X value
     * @param maxY maximum Y value
     */
    private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY)
    {
        vertices.add((float) x);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) maxY);
        vertices.add((float) maxX);
        vertices.add((float) y);
        vertices.add((float) x);
        vertices.add((float) y);
    }

    /**
     * Adds texture coordinates to texture coordinates array list.
     * @param texCoords lsit of texture coordinates
     * @param x X value
     * @param y Y value
     * @param maxX maximum X value
     * @param maxY maximum Y value
     */
    private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY)
    {
        texCoords.add((float) x);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) maxY);
        texCoords.add((float) maxX);
        texCoords.add((float) y);
        texCoords.add((float) x);
        texCoords.add((float) y);
    }

    /**
     * Converts a list of floats to an array of floats.
     * @param listOfFloats list of floats
     * @return array of floats
     */
    private static float[] listToArray(List<Float> listOfFloats)
    {
        float[] array = new float[listOfFloats.size()];
        for (int i = 0; i < array.length; i++)
        {
            array[i] = listOfFloats.get(i);
        }

        return array;
    }
}