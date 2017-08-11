package cameras;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Player;
import toolbox.InputHelper;

/**
 * A third-person camera that is always attached to a player.
 *
 * @author Aaron Frazer
 */
public class ThirdPersonCamera extends Camera
{
    /**
     * Distance camera is from player
     */
    private float distanceFromPlayer = 50;

    /**
     * Angle of camera around player
     */
    private float angleAroundPlayer = 0;

    /**
     * Player the camera is following
     */
    private Player player;

    /**
     * Constructs a third person camera around a player.
     *
     * @param player - player
     */
    public ThirdPersonCamera(Player player)
    {
        super();
        position = new Vector3f(0, 0, 0);
        this.player = player;
//        this.pitch = 10f;
    }

    /**
     * Moves the third-person camera around the player.
     */
    @Override
    public void move()
    {
        super.move();

        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    /**
     * Calculates the X,Y,Z position of the camera in relation to the player.
     *
     * @param horizDistance  horizontal distance
     * @param verticDistance vertical distance
     */
    private void calculateCameraPosition(float horizDistance, float verticDistance)
    {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance;
    }

    /**
     * Calculates the horizontal distance of the camera in relation to the player.
     *
     * @return horizontalDistance - horizontal camera distance
     */
    private float calculateHorizontalDistance()
    {
        float horizontalDistance = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));

        // Camera stops zooming vertically when it hits player
        if (horizontalDistance < 0)
            horizontalDistance = 0;

        return horizontalDistance;
    }

    /**
     * Calculates the vertical distance of the camera in relation to the player.
     *
     * @return verticalDistance vertical camera distance
     */
    private float calculateVerticalDistance()
    {
        float verticalDistance = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));

        // Camera stops zooming vertically when it hits player
        if (verticalDistance < 0)
            verticalDistance = 0;

        return verticalDistance;
    }

    /**
     * Calculates how far the camera is zoomed in relation to the player.
     */
    private void calculateZoom()
    {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    /**
     * Calculates the pitch of the camera from the player.
     */
    private void calculatePitch()
    {
        if (InputHelper.isButtonDown(1))
        {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;

            // prevent pitch from looking away from player when hD/vD = 0.
            if (pitch <= 0)
                pitch = 0;
            else if (pitch > 90)
                pitch = 90;

            System.out.println("Pitch Change: " + pitchChange);
        }
    }

    /**
     * Calculates the angle of the camera from the player.
     */
    private void calculateAngleAroundPlayer()
    {
		if (InputHelper.isButtonDown(0))
		{
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
    }

}
