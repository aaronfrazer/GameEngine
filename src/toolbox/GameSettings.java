package toolbox;

/**
 * Static class used to store settings that can be tweaked throughout the game.
 *
 * @author Aaron Frazer
 */
public class GameSettings
{
	/**
	 * Amount of mipmapping for entities.
	 */
	public static final float MIPMAPPING = -0.8f;

	/**
	 * Maximum fog color values for daylight hours (used in SkyboxRenderer).
	 */
	public static final float DAY_FOG_MAX_RED = 0.5444f, DAY_FOG_MAX_GREEN = 0.62f, DAY_FOG_MAX_BLUE = 0.69f;

    /**
     * Maximum fog color values for night hours (used in SkyboxRenderer).
     */
    public static final float NIGHT_FOG_MAX_RED = 0.01f, NIGHT_FOG_MAX_GREEN = 0.01f, NIGHT_FOG_MAX_BLUE = 0.01f;

	/**
	 * Fog enabled? (used in SkyboxRenderer)
	 */
	public static boolean FOG_ENABLED = false;

	/**
	 * Brightness of the sun during nighttime/daytime hours. (used in SkyboxRenderer)
	 */
	public static final float NIGHT_LIGHT_CONST_HOURS = 0.3f, DAY_LIGHT_CONST_HOURS = 1.0f;

	/**
	 * Enable/disable night and day mode
	 */
	public static boolean DAY_ONLY_MODE = true;

	/**
	 * Enable/disable cel shading
	 */
	public static boolean CEL_SHADING = false;

	/**
	 * Fog color
	 */
	public static float RED = 0.5444f, GREEN = 0.62f, BLUE = 0.69f;
}
