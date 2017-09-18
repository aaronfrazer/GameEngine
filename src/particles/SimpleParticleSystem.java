package particles;

import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

/**
 * Particle systems are responsible for continually omitting particles in a certain way.
 * @author Aaron Frazer
 */
public class SimpleParticleSystem
{
    /**
     * Number of particles that are emitted per second (particles per second)
     */
    private float pps;

    /**
     * Speed particles are emitted
     */
    private float speed;

    /**
     * Amount of gravity that is affecting particles
     */
    private float gravityComplient;

    /**
     * Length of particle life
     */
    private float lifeLength;

    /**
     * Creates a simple particle system.
     * @param pps particles per second
     * @param speed speed particles are emitted
     * @param gravityComplient amount of gravity
     * @param lifeLength particle life length
     */
    public SimpleParticleSystem(float pps, float speed, float gravityComplient, float lifeLength)
    {
        this.pps = pps;
        this.speed = speed;
        this.gravityComplient = gravityComplient;
        this.lifeLength = lifeLength;
    }

    /**
     * Generates particles.
     * Called once every frame.
     * @param systemCenter 3D point in the world where the particles are omitted from
     */
    public void generateParticles(Vector3f systemCenter)
    {
        float delta = DisplayManager.getFrameTimeSeconds();
        float particlesToCreate = pps * delta;
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for (int i = 0; i < count; i++)
        {
            emitParticle(systemCenter);
        }
        if (Math.random() < partialParticle)
        {
            emitParticle(systemCenter);
        }
    }

    /**
     * Emits a single particle from an origin's center in a random direction.
     * @param center center of origin
     */
    private void emitParticle(Vector3f center)
    {
        float dirX = (float) Math.random() * 2f - 1f;
        float dirZ = (float) Math.random() * 2f - 1f;
        Vector3f velocity = new Vector3f(dirX, 1, dirZ);
        velocity.normalise();
        velocity.scale(speed);
        new Particle(new Vector3f(center), velocity, gravityComplient, lifeLength, 0, 1);
    }
}