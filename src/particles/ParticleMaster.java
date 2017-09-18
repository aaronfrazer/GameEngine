package particles;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Responsible for managing all particles in the scene and keeps them updated.
 * @author Aaron Frazer
 */
public class ParticleMaster
{
    /**
     * List of particles in the scene
     */
    private static List<Particle> particles = new ArrayList<>();

    /**
     * Particle renderer
     */
    private static ParticleRenderer renderer;

    /**
     * Initializes particle renderer.
     * @param loader loader
     * @param projectionMatrix projection matrix
     */
    public static void init(Loader loader, Matrix4f projectionMatrix)
    {
        renderer = new ParticleRenderer(loader, projectionMatrix);
    }

    /**
     * Updates all particles in the scene.
     */
    public static void update()
    {
        Iterator<Particle> iterator = particles.iterator();
        while(iterator.hasNext())
        {
            Particle p = iterator.next();
            boolean stillAlive = p.update();
            if (!stillAlive)
            {
                iterator.remove();
            }
        }
    }

    /**
     * Renders all particles in the scene.
     * @param camera camera
     */
    public static void renderParticles(Camera camera)
    {
        renderer.render(particles, camera);
    }

    /**
     * Cleans up resources in the particle renderer.
     */
    public static void cleanUp()
    {
        renderer.cleanUp();
    }

    /**
     * Adds a particle to the list of particles in the scene.
     * @param particle particle to be added
     */
    public static void addParticle(Particle particle)
    {
        particles.add(particle);
    }
}
