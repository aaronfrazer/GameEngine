package particles;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import models.RawModel;
import renderEngine.Loader;
import toolbox.Maths;

/**
 * Responsible for rendering particles.
 * @author Aaron Frazer
 */
public class ParticleRenderer
{
    /**
     * Particle vertices
     */
    private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};

    /**
     * Maximum number of particles that can be in the scene at one time
     */
    private static final int MAX_INSTANCES = 10000;

    /**
     * Amount of data(floats) for each particle
     */
    private static final int INSTANCE_DATA_LENGTH = 21;

    /**
     * Float buffer that is used to store data in VBO
     */
    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);

    /**
     * Quad of particle
     */
    private RawModel quad;

    /**
     * Particle shader
     */
    private ParticleShader shader;

    /**
     * Instance of loader
     */
    private Loader loader;

    /**
     * ID of VBO
     */
    private int vboID;

    /**
     * Position in float array we are currently writing to
     */
    private int pointer = 0;

    /**
     * Creates a particle renderer by creating a new particle shader and loading up projection matrix.
     * @param loader           loader
     * @param projectionMatrix projection matrix
     */
    protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix)
    {
        this.loader = loader;
        this.vboID = loader.createEmptyVbo(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
        quad = loader.loadToVAO(VERTICES, 2);

        loader.addInstancedAttribute(quad.getVaoID(), vboID, 1, 4, INSTANCE_DATA_LENGTH, 0); // column A
        loader.addInstancedAttribute(quad.getVaoID(), vboID, 2, 4, INSTANCE_DATA_LENGTH, 4); // column B
        loader.addInstancedAttribute(quad.getVaoID(), vboID, 3, 4, INSTANCE_DATA_LENGTH, 8); // column C
        loader.addInstancedAttribute(quad.getVaoID(), vboID, 4, 4, INSTANCE_DATA_LENGTH, 12); // column D
        loader.addInstancedAttribute(quad.getVaoID(), vboID, 5, 4, INSTANCE_DATA_LENGTH, 16); // 4D texture offsets
        loader.addInstancedAttribute(quad.getVaoID(), vboID, 6, 1, INSTANCE_DATA_LENGTH, 20); // blend factors

        shader = new ParticleShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /**
     * Renders a list of particles.
     * @param particles hashmap of lists of particles to be rendered
     * @param camera camera
     */
    protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera)
    {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        prepare();

        for (ParticleTexture texture : particles.keySet())
        {
            bindTexture(texture);
            List<Particle> particleList = particles.get(texture);
            pointer = 0;
            float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
            for (Particle particle : particleList)
            {
                updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix, vboData);
                updateTexCoordInfo(particle, vboData);
            }
            loader.updateVbo(vboID, vboData, buffer);
            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
        }
        finishRendering();
    }

    private void bindTexture(ParticleTexture texture)
    {
        // choose between alpha or additive blending
        if (texture.useAdditiveBlending())
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // additive blending (good for fire/magic effects)
        else
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // alpha blending (good for smoke / falling leaves)

        // bind texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

        shader.loadNumberOfRows(texture.getNumberOfRows());
    }

    /**
     * Updates a particles model view matrix.
     * Model view matrix = particle's transformation matrix * view matrix of scene.
     * @param position particle position
     * @param rotation particle rotation
     * @param scale particle scale
     * @param viewMatrix view matrix of scene
     * @param vboData VBO data
     */
    private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix, float[] vboData)
    {
        Matrix4f modelMatrix = new Matrix4f();
        Matrix4f.translate(position, modelMatrix, modelMatrix);

        // Set the rotation 3x3 part of the model matrix to the
        // transpose of the 3x3 rotation part of the view matrix
        modelMatrix.m00 = viewMatrix.m00;
        modelMatrix.m01 = viewMatrix.m10;
        modelMatrix.m02 = viewMatrix.m20;
        modelMatrix.m10 = viewMatrix.m01;
        modelMatrix.m11 = viewMatrix.m11;
        modelMatrix.m12 = viewMatrix.m21;
        modelMatrix.m20 = viewMatrix.m02;
        modelMatrix.m21 = viewMatrix.m12;
        modelMatrix.m22 = viewMatrix.m22;

        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);

        storeMatrixData(modelViewMatrix, vboData);
    }

    /**
     * Stores columns of model view matrix data into VBO data array.
     * @param matrix model view matrix to be stored
     * @param vboData float array of data
     */
    private void storeMatrixData(Matrix4f matrix, float[] vboData)
    {
        vboData[pointer++] = matrix.m00;
        vboData[pointer++] = matrix.m01;
        vboData[pointer++] = matrix.m02;
        vboData[pointer++] = matrix.m03;
        vboData[pointer++] = matrix.m10;
        vboData[pointer++] = matrix.m11;
        vboData[pointer++] = matrix.m12;
        vboData[pointer++] = matrix.m13;
        vboData[pointer++] = matrix.m20;
        vboData[pointer++] = matrix.m21;
        vboData[pointer++] = matrix.m22;
        vboData[pointer++] = matrix.m23;
        vboData[pointer++] = matrix.m30;
        vboData[pointer++] = matrix.m31;
        vboData[pointer++] = matrix.m32;
        vboData[pointer++] = matrix.m33;
    }

    /**
     * Cleans up resources used by the particle shader.
     */
    protected void cleanUp()
    {
        shader.cleanUp();
    }

    /**
     * Stores texture data (offsets and blend factor) into a float array.
     * @param particle particle
     * @param data float array of data
     */
    private void updateTexCoordInfo(Particle particle, float[] data)
    {
        data[pointer++] = particle.getTexOffset1().x;
        data[pointer++] = particle.getTexOffset1().y;
        data[pointer++] = particle.getTexOffset2().x;
        data[pointer++] = particle.getTexOffset2().y;
        data[pointer++] = particle.getBlend();
    }

    /**
     * Prepares particles for rendering by starting the particle shader and
     * enabling vertex attributes.
     */
    private void prepare()
    {
        shader.start();

        // Bind quad to VAO
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);

        // Enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // alpha blending (good for smoke / falling leaves)
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // additive blending (good for fire/magic effects)

        // Stop particles from being rendered to depth buffer
        GL11.glDepthMask(false);
    }

    /**
     * Ends particle rendering by stopping the particle shader and
     * disabling vertex attributes.
     */
    private void finishRendering()
    {
        GL11.glDepthMask(true);

        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL20.glDisableVertexAttribArray(4);
        GL20.glDisableVertexAttribArray(5);
        GL20.glDisableVertexAttribArray(6);

        GL30.glBindVertexArray(0);

        shader.stop();
    }
}