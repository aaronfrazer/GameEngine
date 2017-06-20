package toolbox;

import renderEngine.DisplayManager;

import java.text.DecimalFormat;

/**
 * A virtual clock used in the game for keeping time.
 *
 * @author Aaron Frazer
 */
public class VirtualClock
{
	/**
	 * Continuous play time in milliseconds
	 */
	private static float time = 0;

	/**
	 * In game time
	 */
	private static double hours = 0, minutes = 0, seconds = 0;

	/**
	 * The factor of how fast/slow game clock runs.
	 * Eg: 1 = 1x faster, 2 = 2x faster, etc.
	 */
	private static double speedFactor = 30;

	/**
	 * Used to format time values
	 */
	private static DecimalFormat format = new DecimalFormat("0.#");

	/**
	 * Increases the time in the game (continuously called in the game loop).
	 * Time resets back to 0 ms when it surpasses 24000 ms.
	 */
	public static void update()
	{
		VirtualClock.time += DisplayManager.getFrameTimeSeconds() * 1000;
		VirtualClock.time %= 1440000 / speedFactor; // reset the clock every 1440 seconds (24 minutes)

		// 60 real seconds =  1 game hour
		hours = (getTimeInSecs() / 60) % 24;
		hours *= speedFactor;
		minutes = (hours * 60) % 60;
		seconds = (minutes * 60) % 60;



	}

	/**
	 * Returns current time in ms
	 * @return time in milliseconds
	 */
	public static float getTime()
	{
		return time;
	}

	/**
	 * Returns current time of game in seconds.
	 * @return time in seconds
	 */
	public static float getTimeInSecs()
	{
		return time / 1000;
	}

	/**
	 * Returns the current time (24 hr clock).
	 * @return string of time
	 */
	public static String getTimeString()
	{
		String str =
//						getTimeInSecs() + " real seconds = " +
						format.format(Math.floor(hours)) + ":" +
						format.format(Math.floor(minutes)) + ":" +
						format.format(Math.floor(seconds));

		return str;
	}

	/**
	 * Returns the hours from the game clock.
	 */
	public static double getHours()
	{
		return hours;
	}

	/**
	 * Returns the hours from the game clock.
	 */
	public static double getMins()
	{
		return minutes;
	}

	/**
	 * Returns the hours from the game clock.
	 */
	public static double getSecs()
	{
		return seconds;
	}
}
