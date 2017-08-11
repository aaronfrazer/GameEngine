package cameras;

import entities.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import toolbox.InputHelper;

/**
 * A camera that can be used to lock onto a location.  Once it is locked onto
 * a location, it is able to pan around the location and zoom in and out.
 *
 * TODO: Mouse picking entities does not work
 *
 * @author Aaron Frazer
 */
public class LockOnCamera extends Camera
{
    /**
     * Distance camera is from location
     */
    private float distanceFromLocation = 100;

    /**
     * Angle of camera around location
     */
    public float angleAroundLocation = 0;

    /**
     * Player the camera is following
     */
    private Vector3f location;

    /**
     * Constructs a camera that is looking at a specified location in the world.
     *
     * @param position position of camera
     * @param location location camera is looking at
     */
    public LockOnCamera(Vector3f position, Vector3f location)
    {
        this.position = position;
        this.location = location;
        this.pitch = 10f;
    }

    /**
     * Moves the camera around the fixed location.
     */
    @Override
    public void move()
    {
        super.move();

        calculateZoom();
        calculatePosition();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

        this.yaw = 180 - (angleAroundLocation);
    }

    /**
     * Calculates the X and Y positions of the camera in relation to the location.
     *
     * @param horizDistance  horizontal distance
     * @param verticDistance vertical distance
     */
    private void calculateCameraPosition(float horizDistance, float verticDistance)
    {
        float theta = angleAroundLocation;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = location.getX() - offsetX;
        position.z = location.getZ() - offsetZ;
        position.y = location.getY() + verticDistance;
    }

    /**
     * Calculates the horizontal distance of the camera in relation to the location.
     *
     * @return horizontalDistance - horizontal camera distance
     */
    private float calculateHorizontalDistance()
    {
        float horizontalDistance = (float) (distanceFromLocation * Math.cos(Math.toRadians(pitch)));

        // Camera stops zooming horizontally when it hits player
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
        float verticalDistance = (float) (distanceFromLocation * Math.sin(Math.toRadians(pitch)));

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
        distanceFromLocation -= zoomLevel;
    }

    /**
     * Calculates the pitch and angle around the player.
     */
    private void calculatePosition()
    {
        if (InputHelper.isButtonDown(0))
        {
            float pitchChange = Mouse.getDY() * 0.1f;
            float angleChange = Mouse.getDX() * 0.3f;
            pitch -= pitchChange;
            angleAroundLocation -= angleChange;

            // prevent pitch from looking away from player when hD/vD = 0.
//            if (pitch <= 0)
//                pitch = 0;
//            else if (pitch > 90)
//                pitch = 90;
        }
    }

    /**
     * Spins the camera around the location it is locked onto on the (x, z) axis.
     * @param angleChange - camera speed
     */
    public void changeAngle(float angleChange)
    {
        angleAroundLocation -= angleChange;
    }
}
