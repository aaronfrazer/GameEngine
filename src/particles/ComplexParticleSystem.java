package particles;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;

/**
 * A complex particle system that is used for continually omitting particles at random
 * @author Aaron Frazer
 */
public class ComplexParticleSystem
{
    /**
     * Number of particles that are omitted per second
     */
    private float pps;

    /**
     * Average speed particles are omitted
     */
    private float averageSpeed;

    /**
     * Amount of gravity that is affecting particles
     */
    private float gravityComplient;

    /**
     * Average particle life
     */
    private float averageLifeLength;

    /**
     * Average scale of a particle
     */
    private float averageScale;

    /**
     * Particle speed error (between 0 and 1)
     */
    private float speedError;

    /**
     * Particle life error
     */
    private float lifeError;

    /**
     * Particle scale error
     */
    private float scaleError = 0;

    /**
     * Random rotation of particles
     */
    private boolean randomRotation = false;

    /**
     * Average direction in which particles are emitted
     */
    private Vector3f direction;

    /**
     * value between 0 and 1 indicating how far from the chosen direction particles can deviate
     */
    private float directionDeviation = 0;

    /**
     * Instance of random number class
     */
    private Random random = new Random();

    /**
     * Particle's texture
     */
    private ParticleTexture texture;

    /**
     * Creates a complex particle system
     * @param pps particles per second
     * @param speed speed at which particles are emitted
     * @param gravityComplient amount of gravity affecting particles
     * @param lifeLength particle life length
     * @param scale scale of particles
     */
    public ComplexParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength, float scale)
    {
        this.texture = texture;
        this.pps = pps;
        this.averageSpeed = speed;
        this.gravityComplient = gravityComplient;
        this.averageLifeLength = lifeLength;
        this.averageScale = scale;
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
        Vector3f velocity = null;
        if (direction != null)
        {
            velocity = generateRandomUnitVectorWithinCone(direction, directionDeviation);
        } else
        {
            velocity = generateRandomUnitVector();
        }
        velocity.normalise();
        velocity.scale(generateValue(averageSpeed, speedError));
//        float scale = generateValue(averageScale, scaleError);
        float scale = averageScale;
        float lifeLength = generateValue(averageLifeLength, lifeError);
        new Particle(texture, new Vector3f(center), velocity, gravityComplient, lifeLength, generateRotation(), scale);
    }

    /**
     * Generates an average value.
     * @param average average
     * @param errorMargin margin of error
     * @return new value
     */
    private float generateValue(float average, float errorMargin)
    {
        float offset = (random.nextFloat() - 0.5f) * 2f * errorMargin;
        return average + offset;
    }

    /**
     * Applies random rotation to a particle
     * @return random value (in degrees)
     */
    private float generateRotation()
    {
        if (randomRotation)
        {
            return random.nextFloat() * 360f;
        } else
        {
            return 0;
        }
    }

    /**
     * Generates a random vector within a cone.
     * @param coneDirection direction cone is facing
     * @param angle angle of cone
     * @return random vector
     */
    private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle)
    {
        float cosAngle = (float) Math.cos(angle);
        Random random = new Random();
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = cosAngle + (random.nextFloat() * (1 - cosAngle));
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));

        Vector4f direction = new Vector4f(x, y, z, 1);
        if (coneDirection.x != 0 || coneDirection.y != 0 || (coneDirection.z != 1 && coneDirection.z != -1))
        {
            Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0, 0, 1), null);
            rotateAxis.normalise();
            float rotateAngle = (float) Math.acos(Vector3f.dot(coneDirection, new Vector3f(0, 0, 1)));
            Matrix4f rotationMatrix = new Matrix4f();
            rotationMatrix.rotate(-rotateAngle, rotateAxis);
            Matrix4f.transform(rotationMatrix, direction, direction);
        } else if (coneDirection.z == -1)
        {
            direction.z *= -1;
        }
        return new Vector3f(direction);
    }

    /**
     * Generates a random vector.
     * @return random vector
     */
    private Vector3f generateRandomUnitVector()
    {
        float theta = (float) (random.nextFloat() * 2f * Math.PI);
        float z = (random.nextFloat() * 2) - 1;
        float rootOneMinusZSquared = (float) Math.sqrt(1 - z * z);
        float x = (float) (rootOneMinusZSquared * Math.cos(theta));
        float y = (float) (rootOneMinusZSquared * Math.sin(theta));
        return new Vector3f(x, y, z);
    }

    /**
     * Randomizes the rotation of particles.
     */
    public void randomizeRotation()
    {
        randomRotation = true;
    }

    /**
     * Sets the speed error.
     * @param error number between 0 and 1, where 0 means no error margin
     */
    public void setSpeedError(float error)
    {
        this.speedError = error * averageSpeed;
    }

    /**
     * Sets the life error.
     * @param error number between 0 and 1, where 0 means no error margin
     */
    public void setLifeError(float error)
    {
        this.lifeError = error * averageLifeLength;
    }

    /**
     * Sets the scale error.
     * @param error number between 0 and 1, where 0 means no error margin
     */
    public void setScaleError(float error)
    {
        this.scaleError = error * averageScale;
    }

    /**
     * Sets the direction in which particles are emitted.
     * @param direction average direction in which particles are emitted
     * @param deviation value between 0 and 1 indicating how far from the chosen direction particles can deviate
     */
    public void setDirection(Vector3f direction, float deviation)
    {
        this.direction = new Vector3f(direction);
        this.directionDeviation = (float) (deviation * Math.PI);
    }
}
