package models;

import textures.ModelTexture;

/**
 * A textured model that contains a model and its texture
 * @author Aaron Frazer
 */
public class TexturedModel
{
    /**
     * Raw model
     */
    private RawModel rawModel;

    /**
     * Model texture
     */
    private ModelTexture texture;

    /**
     * Creates a textured model.
     * @param model 3D model
     * @param texture model texture
     */
    public TexturedModel(RawModel model, ModelTexture texture)
    {
        this.rawModel = model;
        this.texture = texture;
    }

    /**
     * Returns the raw model of this textured model.
     * @return raw model
     */
    public RawModel getRawModel()
    {
        return rawModel;
    }

    /**
     * Returns the model's texture
     * @return model texture
     */
    public ModelTexture getTexture()
    {
        return texture;
    }

}
