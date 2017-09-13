package buttons;

import guis.GuiTexture;

import java.util.List;

/**
 * An button interface that contains events and animations.
 */
public interface IButton
{
    /*******************
     * Events
     *******************/

    /**
     * Event when button is clicked.
     */
    void onClick();

    /**
     * Event while mouse is hovering over button.
     */
    void whileHover();

    /**
     * Event when mouse first makes contact with button.
     */
    void startHover();

    /**
     * Event when mouse goes from being over button to not over button.
     */
    void stopHover();

    /**
     * Updates button on mouse hover/click.
     */
    void update();


    /*******************
     * Animations
     *******************/

    /**
     * Animation that plays when the mouse is hovered over the button.
     * @param scaleFactor scale
     */
    void playHoverAnimation(float scaleFactor);

    /**
     * Animation that plays when the button is clicked.
     * @param scaleFactor scale
     */
    void playClickAnimation(float scaleFactor);

    /**
     * Hides the button
     * @param guiTextures list of GUI textures
     */
    void hide(List<GuiTexture> guiTextures);

    /**
     * Shows the button.
     * @param guiTextures list of GUI textures
     */
    void show(List<GuiTexture> guiTextures);

    /**
     * Hides and then shows the button.
     * @param guiTextures list of GUI textures
     */
    void reopen(List<GuiTexture> guiTextures);
}
