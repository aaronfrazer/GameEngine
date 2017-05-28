package models;

import textures.ModelTexture;

/**
 * A textured model that contains both a model and a model's texture.
 *
 * @author Aaron Frazer
 */
public class TexturedModel
{
	/**
	 * Raw model.
	 */
	private RawModel rawModel;
	
	/**
	 * Texture the model is textured with.
	 */
	private ModelTexture texture;
	
	/**
	 * Constructs a textured model.
	 * @param model - the model
	 * @param texture - the texture of the model
	 */
	public TexturedModel(RawModel model, ModelTexture texture)
	{
		this.rawModel = model;
		this.texture = texture;
	}

	/**
	 * Returns the raw model.
	 * @return rawModel - the raw model
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * Returns the model's texture.
	 * @return texture - the texture of the model
	 */
	public ModelTexture getTexture() {
		return texture;
	}
}
