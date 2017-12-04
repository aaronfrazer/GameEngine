package toolbox;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Static class used to store settings that can be tweaked throughout the game.
 *
 * @author Aaron Frazer
 */
public class GameSettings
{
    /**
     * Enable/disable wireframe
     */
    public static boolean WIREFRAME_ENABLED = false;

    /**
     * Fog enabled?
     */
    public static boolean FOG_ENABLED = true;

    /**
     * Enable/disable sky box
     */
    public static boolean SKYBOX_ENABLED = true;

    /**
     * Enable/disable night and day mode
     */
    public static boolean DAY_ONLY_MODE = true;

    /**
     * Enable/disable cel shading
     */
    public static boolean CEL_SHADING = false;

    /**
     * Amount of mipmapping for entities.
     */
    public static final float MIPMAPPING = 0;

    /**
     * Fog/background color(seen only when sky box is disabled)
     */
    public static float FOG_RED = 0.5444f, FOG_GREEN = 0.62f, FOG_BLUE = 0.69f;
//    public static float FOG_RED = 0.1f, FOG_GREEN = 0.4f, FOG_BLUE = 0.2f;

    /**
     * Fog density
     */
    public static float FOG_DENSITY = 0.007f;

    /**
     * Fog gradient
     */
    public static float FOG_GRADIENT = 1.5f;

    /**
     * Maximum fog color values for daylight hours
     */
    public static final float DAY_FOG_MAX_RED = 0.5444f, DAY_FOG_MAX_GREEN = 0.62f, DAY_FOG_MAX_BLUE = 0.69f;

    /**
     * Maximum fog color values for night hours
     */
    public static final float NIGHT_FOG_MAX_RED = 0.01f, NIGHT_FOG_MAX_GREEN = 0.01f, NIGHT_FOG_MAX_BLUE = 0.01f;

    /**
     * Brightness of the sun during nighttime/daytime hours
     */
    public static final float NIGHT_LIGHT_CONST_HOURS = 0.3f, DAY_LIGHT_CONST_HOURS = 1.0f;

    /**
     * Distance from camera at which shadows are displayed
     * Note: increasing this value will lower the resolution of shadows
     */
    public static final float SHADOW_DISTANCE = 150.0f;

    /**
     * Transition period for shadows to appear
     */
    public static final float TRANSITION_DISTANCE = 10.0f;

    /**
     * Resolution of shadows
     * Note: increasing this value will affect performance
     */
    public static final int SHADOW_MAP_SIZE = 4096;

    /**
     * Contrast of the screen.
     */
    public static final float CONTRAST = 0.0f;
}
