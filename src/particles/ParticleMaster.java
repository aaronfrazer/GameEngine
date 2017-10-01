package particles;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Responsible for managing all particles in the scene and keeps them updated.
 * @author Aaron Frazer
 */
public class ParticleMaster
{
    /**
     * List of particles in the scene
     * Each list of particles is associated with a texture that is being used
     */
    private static Map<ParticleTexture, List<Particle>> particles = new HashMap<>();

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
     * Iterates through all of the lists of particles.  For each list of particles it iterates through all of the particles and updates
     * each of the particles (removing any if necessary).
     */
    public static void update(Camera camera)
    {
        // Create new iterator to iterate through each list of particles in the hashmap
        Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
        while(mapIterator.hasNext())
        {
            Entry<ParticleTexture, List<Particle>> entry = mapIterator.next();
            List<Particle> list = entry.getValue();

            Iterator<Particle> iterator = list.iterator();
            while(iterator.hasNext())
            {
                Particle p = iterator.next();
                boolean stillAlive = p.update(camera);
                if (!stillAlive)
                {
                    iterator.remove();
                    if (list.isEmpty())
                    {
                        mapIterator.remove();
                    }
                }
            }

            // Only sort particles which use alpha blending
            if (!entry.getKey().useAdditiveBlending())
            {
                InsertionSort.sortHighToLow(list);
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
        List<Particle> list = particles.get(particle.getTexture());
        if (list == null)
        {
            list = new ArrayList<>();
            particles.put(particle.getTexture(), list);
        }
        list.add(particle);
    }
}