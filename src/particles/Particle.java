package particles;

import entities.Camera;
import entities.Player;
import org.lwjgl.util.vector.Vector2f;
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
     * Distance of particle from camera
     */
    private float distance;

    /**
     * Particle's texture
     */
    private ParticleTexture texture;

    /**
     * Current stage in texture atlas
     */
    private Vector2f texOffset1 = new Vector2f();

    /**
     * Next stage in texture atlas
     */
    private Vector2f texOffset2 = new Vector2f();

    /**
     * Blend factor
     */
    private float blend;

    /**
     * Creates a particle and adds it to the particle list in the particle master.
     * @param texture texture
     * @param position position
     * @param velocity velocity
     * @param gravityEffect gravitational pull
     * @param lifeLength length of life
     * @param rotation rotation
     * @param scale scale
     */
    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale)
    {
        this.texture = texture;
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
    protected boolean update(Camera camera)
    {
        velocity.y += Player.GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
        Vector3f change = new Vector3f(velocity);
        change.scale(DisplayManager.getFrameTimeSeconds());
        Vector3f.add(change, position, position);
        distance = Vector3f.sub(camera.getPosition(), position, null).lengthSquared();
        updateTextureCoordInfo();
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

    /**
     * Returns this particle's texture.
     * @return texture
     */
    public ParticleTexture getTexture()
    {
        return texture;
    }

    /**
     * Returns this particle's distance from the camera.
     * @return distance from camera
     */
    public float getDistance()
    {
        return distance;
    }

    /**
     * Returns the current stage of this particle in the texture atlas.
     * @return current stage coordinates
     */
    public Vector2f getTexOffset1()
    {
        return texOffset1;
    }

    /**
     * Returns the next stage of this particle in the texture atlas.
     * @return next stage coordinates
     */
    public Vector2f getTexOffset2()
    {
        return texOffset2;
    }

    /**
     * Returns this particle's blend factor value.
     * @return blend factor
     */
    public float getBlend()
    {
        return blend;
    }

    /**
     * Updates texture offsets and blend value.
     * This method is called every frame.
     */
    private void updateTextureCoordInfo()
    {
        float lifeFactor = elapsedTime / lifeLength; // between 0 and 1
        int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows(); // number of stages in texture atlas
        float atlasProgression = lifeFactor * stageCount;
        int index1 = (int) Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
        this.blend = atlasProgression % 1;
        setTextureOffset(texOffset1, index1);
        setTextureOffset(texOffset2, index2);
    }

    /**
     * Converts and index into an offset
     * @param offset offset
     * @param index index
     */
    private void setTextureOffset(Vector2f offset, int index)
    {
       int column = index % texture.getNumberOfRows();
       int row = index / texture.getNumberOfRows();
       offset.x = (float) column / texture.getNumberOfRows();
       offset.y = (float) row / texture.getNumberOfRows();
    }
}