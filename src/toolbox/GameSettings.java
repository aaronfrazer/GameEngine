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
	public static final float DAY_MAX_RED = 0.5444f, DAY_MAX_GREEN = 0.62f, DAY_MAX_BLUE = 0.69f;

    /**
     * Maximum fog color values for night hours (used in SkyboxRenderer).
     */
    public static final float NIGHT_MAX_RED = 0.01f, NIGHT_MAX_GREEN = 0.01f, NIGHT_MAX_BLUE = 0.01f;
}
