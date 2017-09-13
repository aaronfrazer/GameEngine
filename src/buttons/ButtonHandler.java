package buttons;

import guis.GuiTexture;
import renderEngine.Loader;

import java.util.HashMap;
import java.util.List;

/**
 * A class used to manage buttons.
 */
public class ButtonHandler
{
    /**
     * Loader instance
     */
    private Loader loader;

    /**
     * List of GUI textures
     */
    private List<GuiTexture> guiTextureList;

    /**
     * Hash map of buttons and their associated IDs
     */
    private HashMap<Integer, Button> buttons = new HashMap<>();

    /**
     * Creates a button handler.
     * @param loader loader
     * @param guiList list of GUI textures
     */
    public ButtonHandler(Loader loader, List<GuiTexture> guiList)
    {
        this.loader = loader;
        this.guiTextureList = guiList;
    }

    /**
     * Updates all button animations.
     */
    public void update()
    {
        for (Button button : buttons.values())
            button.update();
    }

    /**
     * Adds a button to the list of buttons.
     * @param id button ID
     * @param button button
     */
    public void registerButton(int id, Button button)
    {
        if (!buttons.containsKey(button))
            buttons.put(id, button);
    }

    /**
     * Returns the list of GUI textures.
     * @return list of GUI textrures
     */
    public List<GuiTexture> getGuiTextureList()
    {
        return guiTextureList;
    }

    /**
     * Returns the hashmap buttons
     * @return hashmap of buttons
     */
    public HashMap<Integer, Button> getButtons()
    {
        return buttons;
    }

    /**
     * Returns a button from the hashmap of buttons.
     * @param id button ID
     * @return button
     */
    public Button getButton(int id)
    {
        return buttons.get(id);
    }

    /**
     * Returns the loader instance.
     * @return loader
     */
    public Loader getLoader()
    {
        return loader;
    }

}
