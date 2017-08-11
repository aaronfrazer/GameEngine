package normalMappingRenderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import toolbox.GameSettings;
import toolbox.Maths;

/**
 * A class responsible for rendering a model from a VAO using normal mapping.
 *
 * @author Aaron Frazer
 */
public class NormalMappingRenderer
{
    /**
     * Instance of shader that uses normal mapping
     */
    private NormalMappingShader shader;

    /**
     * Creates a projection matrix.
     * @param projectionMatrix 4D projection matrix
     */
    public NormalMappingRenderer(Matrix4f projectionMatrix)
    {
        this.shader = new NormalMappingShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    /**
     * Renders a hashmap of textured models and entities.
     * @param entities hashmap of texturedModels and entities to be rendered
     * @param clipPlane clipping plane
     * @param lights lights
     * @param camera camera
     */
    public void render(Map<TexturedModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights, Camera camera)
    {
        shader.start();
        prepare(clipPlane, lights, camera);
        for (TexturedModel model : entities.keySet())
        {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch)
            {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
        shader.stop();
    }

    /**
     * Deletes resources from shader instance.
     */
    public void cleanUp()
    {
        shader.cleanUp();
    }

    /**
     * Prepares a textured model.
     * @param model model to be prepared
     */
    private void prepareTexturedModel(TexturedModel model)
    {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = model.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if (texture.isHasTransparency())
        {
            MasterRenderer.disableCulling();
        }
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    /**
     * Unbinds attributes of a textured model.
     */
    private void unbindTexturedModel()
    {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    /**
     * Prepares an entity of a textured model.
     * @param entity instance of entity
     */
    private void prepareInstance(Entity entity)
    {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
                entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }

    private void prepare(Vector4f clipPlane, List<Light> lights, Camera camera)
    {
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(GameSettings.RED, GameSettings.GREEN, GameSettings.BLUE);
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);

        shader.loadLights(lights, viewMatrix);
        shader.loadViewMatrix(viewMatrix);
    }
}
