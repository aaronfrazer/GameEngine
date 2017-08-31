package textures;

/**
 * A model used for texture data.
 * @author Aaron Frazer
 */
public class ModelTexture
{
    /**
     * ID of this texture
     */
    private int textureID;

    /**
     * Normal map of this texture
     */
    private int normalMap;

    /**
     * Texture's shininess
     */
    private float shineDamper = 1;

    /**
     * Texture's reflectivity
     */
    private float reflectivity = 0;

    /**
     * Is the model transparent?
     */
    private boolean hasTransparency = false;

    /**
     * Does this model implement fake lighting?
     * Fake lighting is used for objects that need help with lighting
     */
    private boolean useFakeLighting = false;

    /**
     * Number of rows in texture atlas
     * Set to 1 for a single texture (no atlas)
     */
    private int numberOfRows = 1;

    /**
     * Constructs a model texture by setting the ID of the texture.
     * @param texture texture ID
     */
    public ModelTexture(int texture)
    {
        this.textureID = texture;
    }

    /**
     * Returns the texture's ID
     * @return texture ID
     */
    public int getTextureID()
    {
        return textureID;
    }

    /**
     * Returns the normal map associated with this model texture.
     * @return normalMap normal map
     */
    public int getNormalMap()
    {
        return normalMap;
    }

    /**
     * Sets the normal map of this texture.
     * @param normalMap normal map
     */
    public void setNormalMap(int normalMap)
    {
        this.normalMap = normalMap;
    }

    /**
     * Returns the shininess of the texture.
     * @return shininess
     */
    public float getShineDamper()
    {
        return shineDamper;
    }

    /**
     * Sets the shininess of the texture.
     * @param shineDamper shine damper
     */
    public void setShineDamper(float shineDamper)
    {
        this.shineDamper = shineDamper;
    }

    /**
     * Returns the reflectivity of the texture.
     * @return reflectivity
     */
    public float getReflectivity()
    {
        return reflectivity;
    }

    /**
     * Sets the reflectivity of the texture.
     * @param reflectivity reflectivity
     */
    public void setReflectivity(float reflectivity)
    {
        this.reflectivity = reflectivity;
    }

    /**
     * Returns true if this model is transparent.
     * @return true if model is transparent
     */
    public boolean isHasTransparency()
    {
        return hasTransparency;
    }

    /**
     * Sets the transparency of this model.
     * @param hasTransparency true if has transparency
     */
    public void setHasTransparency(boolean hasTransparency)
    {
        this.hasTransparency = hasTransparency;
    }

    /**
     * Returns true if this model uses fake lighting
     * @return true if using fake lighting
     */
    public boolean isUseFakeLighting()
    {
        return useFakeLighting;
    }

    /**
     * Sets the fake lighting of this model
     * @param useFakeLighting true if using fake lighting
     */
    public void setUseFakeLighting(boolean useFakeLighting)
    {
        this.useFakeLighting = useFakeLighting;
    }

    /**
     * Returns the number of rows used by the texture atlas for this texture.
     * @return number of rows
     */
    public int getNumberOfRows()
    {
        return numberOfRows;
    }

    /**
     * Sets the number of rows used by the texture atlas for this texture.
     * @param numberOfRows number of rows
     */
    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
