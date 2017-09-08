package fontMeshCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of text during the loading of a text.
 *
 * @author Aaron Frazer
 */
public class Line
{
    /**
     * Screen-space maximum length of this line
     */
    private double maxLength;

    /**
     * Screen-space this line uses
     */
    private double spaceSize;

    /**
     * Current length of this line
     */
    private double currentLineLength = 0;

    /**
     * List of words
     */
    private List<Word> words = new ArrayList<>();

    /**
     * Creates an empty line.
     * @param spaceWidth screen-space width of a space character
     * @param fontSize size of font being used.
     * @param maxLength maximum line length
     */
    protected Line(double spaceWidth, double fontSize, double maxLength)
    {
        this.spaceSize = spaceWidth * fontSize;
        this.maxLength = maxLength;
    }

    /**
     * Attempt to add a word to the line. If the line can fit the word without reaching
     * the maximum line length, then the word is added and the line length increased.
     * @param word word to add
     * @return {@code true} word has successfully been added to the line
     */
    protected boolean attemptToAddWord(Word word)
    {
        double additionalLength = word.getWordWidth();
        additionalLength += !words.isEmpty() ? spaceSize : 0;
        if (currentLineLength + additionalLength <= maxLength)
        {
            words.add(word);
            currentLineLength += additionalLength;
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Returns the maximum length of this line
     * @return max length
     */
    protected double getMaxLength()
    {
        return maxLength;
    }

    /**
     * Returns the current screen-space length of this line.
     * @return line length
     */
    protected double getLineLength()
    {
        return currentLineLength;
    }

    /**
     * Returns the list of works in this line.
     * @return list  of words
     */
    protected List<Word> getWords()
    {
        return words;
    }
}