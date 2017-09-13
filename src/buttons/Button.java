package buttons;

import guis.GuiTexture;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.InputHelper;

import java.util.List;

/**
 * An abstract class that implements the IButton interface.
 * @author Aaron Frazer
 */
public abstract class Button implements IButton
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
     * Color of this button
     */
    private Vector4f color;

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
        this.color = new Vector4f(1, 1, 1, 1);
        this.isHidden = true;
        this.isHovering = false;
        this.guiTexture = new GuiTexture(loader.loadGameTexture(texture), position, scale);
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
                    onClick();
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
     * Shows this button on the screen
     * @param guiTextures list of GUI textures
     */
    public void show(List<GuiTexture> guiTextures)
    {
        startRender(guiTextures);
        isHidden = false;
    }

    /**
     * Hides this button from the screen.
     * @param guiTextures list of GUI textures
     */
    public void hide(List<GuiTexture> guiTextures)
    {
        stopRender(guiTextures);
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
     * Adds this button's GUI texture to the list of textures.
     * @param guiTextureList list of GUI textures
     */
    private void startRender(List<GuiTexture> guiTextureList)
    {
        guiTextureList.add(guiTexture);
    }

    /**
     * Removes this button's GUI texture from the list of textures.
     * @param guiTextureList list of GUI textures
     */
    private void stopRender(List<GuiTexture> guiTextureList)
    {
        guiTextureList.remove(guiTexture);
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
     * Returns the color of this button
     * @return button color
     */
    public Vector4f getColor()
    {
        return color;
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