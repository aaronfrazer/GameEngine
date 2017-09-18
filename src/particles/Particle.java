package particles;

import entities.Player;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

/**
 * A single particle in the game.
 * @author Aaron Frazer
 */
public class Particle
{
    /**
     * 3D position in the world
     */
    private Vector3f position;

    /**
     * Speed and direction this particle is currently moving in
     */
    private Vector3f velocity;

    /**
     * How much this particle is affected by gravity (1 = heavy, 0 = light)
     */
    private float gravityEffect;

    /**
     * How long this particle should stay alive for
     */
    private float lifeLength;

    /**
     * Rotation of this particle
     */
    private float rotation;

    /**
     * Size of this particle
     */
    private float scale;

    /**
     * Amount of time this particle has been alive for
     */
    private float elapsedTime;

    /**
     * Creates a particle and adds it to the particle list in the particle master.
     * @param position position
     * @param velocity velocity
     * @param gravityEffect gravitational pull
     * @param lifeLength length of life
     * @param rotation rotation
     * @param scale scale
     */
    public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale)
    {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        ParticleMaster.addParticle(this);
    }

    /**
     * Updates this particle and returns true if this particle is alive.
     * This method is called every frame.
     * @return true if particle is still alive, false if particle is dead
     */
    protected boolean update()
    {
        velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);
        change.scale(DisplayManager.getFrameTimeSeconds());
        Vector3f.add(change, position, position);
        elapsedTime += DisplayManager.getFrameTimeSeconds();

        return elapsedTime < lifeLength;
    }

    /**
     * Returns this particle's position in the world
     * @return 3D position
     */
    public Vector3f getPosition()
    {
        return position;
    }

    /**
     * Returns this particle's rotation;
     * @return rotation
     */
    public float getRotation()
    {
        return rotation;
    }

    /**
     * Returns this particle's scale.
     * @return scale
     */
    public float getScale()
    {
        return scale;
    }
}
