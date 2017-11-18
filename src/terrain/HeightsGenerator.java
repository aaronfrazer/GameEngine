package terrain;

import java.util.Random;

/**
 * Responsible for generating random terrains.
 */
public class HeightsGenerator
{
    /**
     * Height of the terrain
     */
    private static final float AMPLITUDE = 75f;

    /**
     * Number of noise functions used
     */
    private static final int OCTAVES = 3;

    /**
     * Roughness of terrain
     */
    private static final float ROUGHNESS = 0.3f;

    /**
     * Random number generator
     */
    private Random random = new Random();

    /**
     * Seed used for random number generation
     */
    private int seed;
    private int xOffset = 0;
    private int zOffset = 0;

    /**
     * Creates a new height generator.
     */
    public HeightsGenerator()
    {
        this.seed = random.nextInt(1000000000);
    }

    /**
     * Creates a new height generator.  Use this method with multiple terrains.
     * NOTE: Only works with POSITIVE gridX and gridZ values
     * @param gridX X coordinate of terrain size
     * @param gridZ Z coordniate of terrain size
     * @param vertexCount number of vertices on terrain
     * @param seed seed
     */
    public HeightsGenerator(int gridX, int gridZ, int vertexCount, int seed)
    {
        this.seed = seed;
        xOffset = gridX * (vertexCount - 1);
        zOffset = gridZ * (vertexCount - 1);
    }

    /**
     * Generates the height of a vertex and returns it's height.
     * @param x X coordinate
     * @param z Z coordinate
     * @return height
     */
    public float generateHeight(int x, int z)
    {
        float total = 0;
        float d = (float) Math.pow(2, OCTAVES - 1);
        for (int i = 0; i < OCTAVES; i++)
        {
            float freq = (float) (Math.pow(2, i) / d);
            float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
            total += getInterpolatedNoise((x + xOffset) * freq, (z + zOffset) * freq) * amp;
        }
        return total;
    }

    /**
     * Performs smooth averaging to simulate cosine interpolation for a point.
     * @param x X coordinate
     * @param z Z coordinate
     * @return interpolated height of (X, Z) position
     */
    private float getInterpolatedNoise(float x, float z)
    {
        int intX = (int) x;
        int intZ = (int) z;
        float fracX = x - intX;
        float fracZ = z - intZ;

        float v1 = getSmoothNoise(intX, intZ);
        float v2 = getSmoothNoise(intX + 1, intZ);
        float v3 = getSmoothNoise(intX, intZ + 1);
        float v4 = getSmoothNoise(intX + 1, intZ + 1);
        float i1 = interpolate(v1, v2, fracX);
        float i2 = interpolate(v3, v4, fracX);
        return interpolate(i1, i2, fracZ);
    }

    /**
     * Performs cosine interpolation on two float values.
     * @param a float value
     * @param b float value
     * @param blend blend factor
     * @return cosine interpolated value
     */
    private float interpolate(float a, float b, float blend)
    {
        double theta = blend * Math.PI;
        float f = (float) (1f - Math.cos(theta)) * 0.5f;
        return a * (1f - f) + b * f;
    }

    /**
     * Returns a smooth average output for X and Z inputs.
     * @param x X coordinate
     * @param z Z coordinate
     * @return smooth average of
     */
    private float getSmoothNoise(int x, int z)
    {
        float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1)
                + getNoise(x + 1, z + 1)) / 16f;
        float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1)
                + getNoise(x, z + 1)) / 8f;
        float center = getNoise(x, z) / 4f;
        return corners + sides + center;
    }

    /**
     * Generates a random number between 1 and -1 based on inputs.
     * Similar inputs will always generate the same random number.
     * @param x X coordinate
     * @param z Z coordinate
     * @return random number between 1 and -1
     */
    private float getNoise(int x, int z)
    {
        random.setSeed(x * 49632 + z * 325176 + seed);
        return random.nextFloat() * 2f - 1f;
    }

}