package fontMeshCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one word in the text.
 *
 * @author Aaron Frazer
 */
public class Word
{
    /**
     * Width
     */
    private double width = 0;

    /**
     * Font size
     */
    private double fontSize;

    /**
     * List of characters in this word
     */
    private List<Character> characters = new ArrayList<>();

    /**
     * Create an empty word.
     * @param fontSize font size word's text
     */
    protected Word(double fontSize)
    {
        this.fontSize = fontSize;
    }

    /**
     * Adds a character to the end of this word and increases the screen-space width of this word.
     * @param character character to be added
     */
    protected void addCharacter(Character character)
    {
        characters.add(character);
        width += character.getxAdvance() * fontSize;
    }

    /**
     * Returns the list of characters in this word.
     * @return list of characters in the word
     */
    protected List<Character> getCharacters()
    {
        return characters;
    }

    /**
     * Returns the width of the word in terms of screen size.
     * @return word width
     */
    protected double getWordWidth()
    {
        return width;
    }
}