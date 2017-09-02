package normalMappingRenderer;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import toolbox.GameSettings;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

/**
 * A class responsible for rendering a model from VA using normal mapping.
 * @author Aaron Frazer
 */
public class NormalMappingRenderer
{
    /**
     * Normal mapping shader program
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
     * Renders a hash map of textured models and entities.
     * @param entities hash map of textureModels and entities to be rendered
     * @param clipPlane clipping plane
     * @param lights list of lights
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
     * Prepares a textured model to be rendered.
     * @param model model to be prepared
     */
    private void prepareTexturedModel(TexturedModel model)
    {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        ModelTexture texture = model.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if (texture.isHasTransparency())
        {
            MasterRenderer.disableCulling();
        }
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getNormalMap());
    }

    /**
     * Prepares an entity of a textured model.
     * @param entity instance of entity
     */
    private void prepareInstance(Entity entity)
    {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }

    /**
     * Loads clipping plane, skycolor, lights, and camera.
     * @param clipPlane clipping plane
     * @param lights list of lights
     * @param camera camera
     */
    private void prepare(Vector4f clipPlane, List<Light> lights, Camera camera)
    {
        shader.loadClipPlane(clipPlane);

        shader.loadDensity(GameSettings.FOG_DENSITY);
        shader.loadGradient(GameSettings.FOG_GRADIENT);
        shader.loadSkyColour(GameSettings.FOG_RED, GameSettings.FOG_GREEN, GameSettings.FOG_BLUE);
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);

        shader.loadLights(lights, viewMatrix);
        shader.loadViewMatrix(viewMatrix);
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
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }

    /**
     * Deletes resources from shader instance.
     */
    public void cleanUp()
    {
        shader.cleanUp();
    }
}
