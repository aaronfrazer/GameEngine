package entities;

import org.lwjgl.util.vector.Vector3f;

/**
 * A virtual camera that can be used in a variety of ways.
 * @author Aaron Frazer
 */
public abstract class Camera
{
    /**
     * Position of camera
     */
    protected Vector3f position;

    /**
     * Pitch is up and down (rotation around x-axis)
     */
    protected float pitch;

    /**
     * Yaw is left and right (rotation around y-axis)
     */
    protected float yaw;

    /**
     * Roll is tilting (rotation around z-axis)
     */
    private float roll;

    /**
     * Moves the camera.
     */
    public void move() { }

    /**
     * Returns the camera's position.
     * @return position - camera's position vector
     */
    public Vector3f getPosition()
    {
        return position;
    }

    /**
     * Returns the camera's pitch.
     * @return pitch - camera's pitch
     */
    public float getPitch()
    {
        return pitch;
    }

    /**
     * Returns the camera's yaw.
     * @return yaw - camera's yaw
     */
    public float getYaw()
    {
        return yaw;
    }

    /**
     * Returns the camera's roll.
     * @return roll - camera's roll
     */
    public float getRoll()
    {
        return roll;
    }

    /**
     * Invert the pitch of the camera.
     * This is used for water reflection.
     */
    public void invertPitch()
    {
        this.pitch = -pitch;
    }

    /**
     * Inverts the roll of the camera.
     * This is used for water reflection
     */
    public void invertRoll()
    {
        this.roll = -roll;
    }


}
