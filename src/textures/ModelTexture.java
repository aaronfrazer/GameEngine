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
     * Specular map of this texture
     */
    private int specularMap;

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
     * Does this model have a specular map?
     */
    private boolean hasSpecularMap = false;

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
     * Returns the shininess of the texture.
     * @return shininess
     */
    public float getShineDamper()
    {
        return shineDamper;
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
     * Returns the number of rows used by the texture atlas for this texture.
     * @return number of rows
     */
    public int getNumberOfRows()
    {
        return numberOfRows;
    }

    /**
     * Returns the specular map of this model texture.
     * @return specular map ID
     */
    public int getSpecularMap()
    {
        return specularMap;
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
     * Returns true if this model uses fake lighting
     * @return true if using fake lighting
     */
    public boolean isUseFakeLighting()
    {
        return useFakeLighting;
    }

    /**
     * Returns true if this model texture is using a specular map.
     * @return true if using specular map
     */
    public boolean hasSpecularMap()
    {
        return hasSpecularMap;
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
     * Sets the shininess of the texture.
     * @param shineDamper shine damper
     */
    public void setShineDamper(float shineDamper)
    {
        this.shineDamper = shineDamper;
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
     * Sets the transparency of this model.
     * @param hasTransparency true if has transparency
     */
    public void setHasTransparency(boolean hasTransparency)
    {
        this.hasTransparency = hasTransparency;
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
     * Sets the number of rows used by the texture atlas for this texture.
     * @param numberOfRows number of rows
     */
    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    /**
     * Sets specular/glowing information map of this model texture.
     * Red = specular
     * Green = glowing
     * @param specMap ID of specular map
     */
    public void setExtraInfoMap(int specMap)
    {
        this.specularMap = specMap;
        this.hasSpecularMap = true;
    }
}
