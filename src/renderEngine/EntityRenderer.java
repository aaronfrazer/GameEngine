package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.GameSettings;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

/**
 * A class responsible for rendering a model from a VAO.
 * @author Aaron Frazer
 */
public class EntityRenderer
{
    /**
     * Instance of static shader
     */
    private StaticShader shader;

    /**
     * Creates a projection matrix.  This method is only run once.
     * @param shader shader program
     * @param projectionMatrix 4D projection matrix
     */
    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix)
    {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    /**
     * Renders a hash map of textured models and entities.
     * @param entities hash map of textured models and entities
     */
    public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f toShadowSpace)
    {
        shader.loadToShadowSpaceMatrix(toShadowSpace);
        if (GameSettings.WIREFRAME_ENABLED)
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        else
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

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
    }

    /**
     * Prepares a textured model.
     * @param model model
     */
    private void prepareTexturedModel(TexturedModel model)
    {
        RawModel rawModel = model.getRawModel();

        // Bind raw model
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = model.getTexture();

        // Load number of rows from texture atlas
        shader.loadNumberOfRows(texture.getNumberOfRows());

        if (texture.isHasTransparency())
        {
            MasterRenderer.disableCulling();
        }

        // Load fake variable lighting
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());

        // Load shine settings
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

        // Bind model's texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());

        // Load specular map to model's texture
        shader.loadUseSpecularMap(texture.hasSpecularMap());
        if (texture.hasSpecularMap())
        {
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getSpecularMap());
        }
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
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }

}
