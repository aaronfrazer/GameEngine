package buttons;

import guis.GuiTexture;
import org.lwjgl.util.vector.Vector2f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.InputHelper;

import java.util.List;

import static engineTester.MainGameLoop.guiTextures;

/**
 * An abstract class that implements the IButton interface.
 * Buttons will change colors depending on mouse hover/click.
 * @author Aaron Frazer
 */
public class ColorButton implements IButton
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
     * Is the mouse clicking this button?
     */
    private boolean isClicking;

    /**
     * Has the mouse been clicked previously and the cursor is off of it
     */
    private boolean hasBeenClicked;

    /**
     * Loader instance for this button
     */
    private Loader loader;

    /**
     * GUI textureIdle of this button
     */
    private GuiTexture guiTexture;

    /**
     * GUI texture of this button when it is hovered over
     */
    private GuiTexture guiTextureHovered;

    /**
     * GUI texture of this button when it is clicked on
     */
    private GuiTexture guiTexturePressed;

    /**
     * Texture file used for this button when idle (no hover or click)
     */
    private String textureIdle;

    /**
     * Texture file used when this button is hovered over
     */
    private String textureHovered;

    /**
     * Texture file used when this button is pressed
     */
    private String texturePressed;

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
     * @param position position of button
     * @param scale    scale of button
     */
    public ColorButton(Loader loader, Vector2f position, Vector2f scale)
    {
        this.textureIdle = "redButtonTexture";
        this.textureHovered = "yellowButtonTexture";
        this.texturePressed = "greenButtonTexture";
        this.position = position;
        this.scale = scale;
        this.isHidden = true;
        this.isHovering = false;
        this.loader = loader;
        this.guiTexture = new GuiTexture(loader.loadGameTexture(textureIdle), position, scale);
        this.guiTexturePressed = new GuiTexture(loader.loadGameTexture(texturePressed), position, scale);
        this.guiTextureHovered = new GuiTexture(loader.loadGameTexture(textureHovered), position, scale);
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
     * Event that occurs when this button is no longer being clicked.
     */
    public void stopClick()
    {
        playStopClickAnimation(0.08f);
    }

    /**
     * Event that occurs while the mouse is being hovered over this button.
     */
    public void whileHover()
    {
        playHoverAnimation(0.092f);
    }

    /**
     * Event that occurs when the mouse first makes contact with this button.
     */
    public void startHover()
    {
        playHoverAnimation(0.092f);
    }

    /**
     * Event that occurs when the mouse leaves this button.
     */
    public void stopHover()
    {
        playStopHoverAnimation();
    }

    /**
     * Calculates if the mouse is hovering over this button or if mouse is
     * clicked when hovering over this button.
     */
    public void update()
    {
        if (!isHidden)
        {
            Vector2f location;
            Vector2f scale;

            if (isClicking)
            {
                location = guiTexturePressed.getPosition();
                scale = guiTexturePressed.getScale();
            } else if (isHovering)
            {
                location = guiTextureHovered.getPosition();
                scale = guiTextureHovered.getScale();
            } else
            {
                location = guiTexture.getPosition();
                scale = guiTexture.getScale();
            }

            Vector2f mouseCoordinates = DisplayManager.getNormalizedMouseCoordinates();
            System.out.println("Has been clicked? " + hasBeenClicked);
            if (location.y + scale.y > -mouseCoordinates.y
            &&  location.y - scale.y < -mouseCoordinates.y
            &&  location.x + scale.x > mouseCoordinates.x
            &&  location.x - scale.x < mouseCoordinates.x)
            { // mouse is hovering
                if (!isHovering)
                { // mouse starting hover
                    isHovering = true;
                    startHover();
                }
                if (InputHelper.isButtonDown(0))
                { // mouse is pressed down on button
                    isClicking = true;
                    onClick();
                } else
                { // mouse is not pressed on button
                    stopClick();
                }
            } else
            { // mouse not hovering
                if (isHovering)
                { // mouse stopping hover
                    isHovering = false;
                    stopHover();
                }
            }
        }
    }

    /**
     * Plays the hover animation for this button.
     */
    public void playHoverAnimation(float scaleFactor)
    {
        System.out.println("startHoverAnimation");

        if (!guiTextures.contains(guiTextureHovered))
            guiTextures.add(guiTextureHovered);

        if (guiTextures.contains(guiTexture))
            guiTextures.remove(guiTexture);
    }

    /**
     * Plays an animation for this button when the mouse leaves the button.
     */
    public void playStopHoverAnimation()
    {
        System.out.println("stopHoverAnimation");

        if (!guiTextures.contains(guiTexture))
            guiTextures.add(guiTexture);

        if (guiTextures.contains(guiTextureHovered))
            guiTextures.remove(guiTextureHovered);

        if (guiTextures.contains(guiTexturePressed))
            guiTextures.remove(guiTexturePressed);
    }

    /**
     * Animation to play while mouse is clicking the button.
     * @param scaleFactor scale
     */
    public void playClickAnimation(float scaleFactor)
    {
        System.out.println("clickAnimation");

        if (guiTextures.contains(guiTexture))
            guiTextures.remove(guiTexture);

        if (!guiTextures.contains(guiTexturePressed))
            guiTextures.add(guiTexturePressed);
    }

    /**
     * Animation ot play while mouse has finished clicking the button.
     * @param scaleFactor scale
     */
    public void playStopClickAnimation(float scaleFactor)
    {
        System.out.println("stopClickAnimation");

        // remove green, add red

        if (!guiTextures.contains(guiTextureHovered))
            guiTextures.add(guiTextureHovered);

        if (guiTextures.contains(guiTexturePressed))
            guiTextures.remove(guiTexturePressed);

        if (guiTextures.contains(guiTexture))
            guiTextures.remove(guiTexture);
    }

    /**
     * Shows this button on the screen.
     * Adds this button's GUI textureIdle to the list of textures.
     * @param guiTextures list of GUI textures
     */
    public void show(List<GuiTexture> guiTextures)
    {
        guiTextures.add(guiTexture);
        isHidden = false;
    }

    /**
     * Hides this button from the screen.
     * Removes this button's GUI textureIdle from the list of textures.
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
     * Returns the textureIdle of this button.
     * @return button textureIdle
     */
    public String getTextureIdle()
    {
        return textureIdle;
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