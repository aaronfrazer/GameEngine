package buttons;

import guis.GuiTexture;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.InputHelper;

import java.util.List;

/**
 * An abstract class that implements the IButton interface.
 * @author Aaron Frazer
 */
public class Button implements IButton
{
    /**
     * Is this button hidden?
     */
    private boolean isHidden;

    /**
     * Is the mouse hovering over this button?
     */
    private boolean isHovering;

    /**
     * GUI texture of this button
     */
    private GuiTexture guiTexture;

    /**
     * Texture file used for this button
     */
    private String texture;

    /**
     * Coordinates of this button
     */
    private Vector2f position;

    /**
     * Scale of this button
     */
    private Vector2f scale;

    /**
     * Creates a button.
     * @param loader   loader
     * @param texture  filepath of texture
     * @param position position of button
     * @param scale    scale of button
     */
    public Button(Loader loader, String texture, Vector2f position, Vector2f scale)
    {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
        this.isHidden = true;
        this.isHovering = false;
        this.guiTexture = new GuiTexture(loader.loadGameTexture(texture), position, scale);
    }

    /**
     * Event that occurs when this button is clicked.
     */
    public void onClick()
    {
        if (!isHidden)
        {
            playClickAnimation(0.08f);
        }
    }

    /**
     * Event that occurs while the mouse is being hovered over this button.
     */
    public void whileHover()
    {
//        System.out.println("Mouse is hovering over button");
        playHoverAnimation(0.092f);
    }

    /**
     * Event that occurs when the mouse first makes contact with this button.
     */
    public void startHover()
    {
//        System.out.println("Button was hovered over");
        playHoverAnimation(0.092f);
    }

    /**
     * Event that occurs when the mouse leaves this button.
     */
    public void stopHover()
    {
//        System.out.println("Mouse has stopped hovering over button");
        setScale(new Vector2f(0.2f, 0.2f)); // reset scale
    }

    /**
     * Calculates if the mouse is hovering over this button or if mouse is
     * clicked when hovering over this button.
     */
    public void update()
    {
        if (!isHidden)
        {
            Vector2f location = guiTexture.getPosition();
            Vector2f scale = guiTexture.getScale();
            Vector2f mouseCoordinates = DisplayManager.getNormalizedMouseCoordinates();
            if (location.y + scale.y > -mouseCoordinates.y
                    && location.y - scale.y < -mouseCoordinates.y
                    && location.x + scale.x > mouseCoordinates.x
                    && location.x - scale.x < mouseCoordinates.x)
            { // mouse is inside of button
                whileHover();
                if (!isHovering)
                {
                    isHovering = true;
                    startHover();
                }
                if (InputHelper.isButtonDown(0))
                {
                    onClick(); // mouse is pressed down on button
                    System.out.println("mouse pressed");
                } else {
                    // mouse is not pressed down on button
//                    guiTexture = texture;
                    System.out.println("mouse hovering");
                }

            } else // mouse is outside of button
            {
                if (isHovering)
                {
                    isHovering = false;
                    stopHover();
                }
                guiTexture.setScale(this.scale);
            }
        }
    }

    /**
     * Plays the hover animation for this button.
     */
    public void playHoverAnimation(float scaleFactor)
    {
        guiTexture.setScale(new Vector2f(scale.x + scaleFactor, scale.y + scaleFactor));
    }

    /**
     * Scales this button when it is clicked.
     * @param scaleFactor scale
     */
    public void playClickAnimation(float scaleFactor)
    {
        guiTexture.setScale(new Vector2f(scale.x - (scaleFactor * 2), scale.y - (scaleFactor * 2)));
    }

    /**
     * Shows this button on the screen.
     * Adds this button's GUI texture to the list of textures.
     * @param guiTextures list of GUI textures
     */
    public void show(List<GuiTexture> guiTextures)
    {
        guiTextures.add(guiTexture);
        isHidden = false;
    }

    /**
     * Hides this button from the screen.
     * Removes this button's GUI texture from the list of textures.
     * @param guiTextures list of GUI textures
     */
    public void hide(List<GuiTexture> guiTextures)
    {
        guiTextures.remove(guiTexture);
        isHidden = true;
    }

    /**
     * Hides and shows this button on the screen.
     * @param guiTextures list of GUI textures
     */
    public void reopen(List<GuiTexture> guiTextures)
    {
        hide(guiTextures);
        show(guiTextures);
    }

    /**
     * Returns true if the button is hidden.
     * @return true if hidden, false if shown
     */
    public boolean isHidden()
    {
        return isHidden;
    }

    /**
     * Returns the texture of this button.
     * @return button texture
     */
    public String getTexture()
    {
        return texture;
    }

    /**
     * Returns the 2D coordinates of this button.
     * @return button position
     */
    public Vector2f getPosition()
    {
        return position;
    }

    /**
     * Returns the scale of this button.
     * @return button scale
     */
    public Vector2f getScale()
    {
        return scale;
    }

    /**
     * Sets the position of this button.
     * @param position button position
     */
    public void setPosition(Vector2f position)
    {
        guiTexture.setPosition(position);
    }

    /**
     * Sets the scale of this button.
     * @param scale button scale
     */
    public void setScale(Vector2f scale)
    {
        guiTexture.setScale(scale);
    }
}