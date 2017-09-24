package particles;

/**
 * A class that represents a particle's texture.
 * A texture can use either additive blending or alpha blending.
 * Additive blending is good for fire or magic effects
 * Alpha blending is good for smoke or falling leaves
 * @author Aaron Frazer
 */
public class ParticleTexture
{
    /**
     * ID of loaded texture
     */
    private int textureID;

    /**
     * Number of rows in texture atlas
     */
    private int numberOfRows;

    /**
     * True if using additive blending, false if using alpha blending
     */
    private boolean additive;

    /**
     * Creates a particle texture.
     * @param textureID texture's ID
     * @param numberOfRows number of rows in texture atlas
     */
    public ParticleTexture(int textureID, int numberOfRows, boolean additive)
    {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
        this.additive = additive;
    }

    /**
     * Returns this particle texture's texture ID.
     * @return ID of texture
     */
    protected int getTextureID()
    {
        return textureID;
    }

    /**
     * Returns the number of rows in this particle texture's texture atlas.
     * @return number of rows in texture atlas
     */
    protected int getNumberOfRows()
    {
        return numberOfRows;
    }

    /**
     * Returns true if this texture should use additive blending. Returns
     * false if this texture should use alpha blending.
     * @return true if using additive, false if using alpha
     */
    public boolean useAdditiveBlending()
    {
        return additive;
    }
}