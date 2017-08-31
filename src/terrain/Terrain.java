package terrain;

import entities.Entity;
import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Represents a terrain.
 */
public class Terrain
{
    /**
     * Size of terrain
     */
    public static float SIZE = 150;

    /**
     * Maximum height of terrain
     */
    private static final float MAX_HEIGHT = 40;

    /**
     * Maximum color value that a pixel on the height map can have
     */
    private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;

    /**
     * Terrain coordinates
     */
    private float x, z;

    /**
     * Terrain model
     */
    private RawModel model;

    /**
     * Terrain texture pack that contains 4 textures it will be using
     */
    private TerrainTexturePack texturePack;

    /**
     * Blend map of terrain where 4 textures are rendered
     */
    private TerrainTexture blendMap;

    /**
     * Height of each vertex on terrain
     */
    private float[][] heights;

    /**
     * Constructs a terrain.
     *
     * @param gridX x coordinate
     * @param gridZ z coordinate
     * @param loader loader
     * @param texturePack texture pack of terrain
     * @param blendMap blend map of terrain
     * @param heightMap height map filename
     */
    public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap)
    {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain(loader, heightMap);
    }

    /**
     * Generates a model of a terrain using a height map.
     *
     * @param loader loader
     * @param heightMap name of height map
     * @return model of terrain
     */
    @SuppressWarnings("ConstantConditions")
    private RawModel generateTerrain(Loader loader, String heightMap)
    {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/heightmaps/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int VERTEX_COUNT = image.getHeight();

        int count = VERTEX_COUNT * VERTEX_COUNT;
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT)];
        int vertexPointer = 0;
        for (int i = 0; i < VERTEX_COUNT; i++)
        {
            for (int j = 0; j < VERTEX_COUNT; j++)
            {
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j, i, image);
                vertices[vertexPointer * 3 + 1] = height;
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++)
        {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++)
            {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    /**
     * Returns the normal of a pixel.
     * @param x x coordinate of vertex
     * @param z z coordinate of vertex
     * @param image - buffered image of heightmap
     * @return normal - normal as a Vector3f
     */
    private Vector3f calculateNormal(int x, int z, BufferedImage image)
    {
        float heightL = getHeight(x - 1, z, image);
        float heightR = getHeight(x + 1, z, image);
        float heightD = getHeight(x, z - 1, image);
        float heightU = getHeight(x, z + 1, image);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();

        return normal;
    }

    /**
     * Checks if an entity is inside the x and z coordinates of this terrain.
     * @param entity - entity to be checked
     * @return true if entity is inside
     */
    public boolean isEntityInsideTerrain(Entity entity)
    {
        if (getX() <= entity.getPosition().x) // if terrain.x < player.x
        {
            if (getX() + Terrain.getSize() > entity.getPosition().x) // if terrain.x
            {
                if (getZ() <= entity.getPosition().z)
                {
                    if (getZ() + Terrain.getSize() > entity.getPosition().z)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns the position at the center of this terrain.
     * @return center position
     */
    public Vector3f getCenter()
    {
        return new Vector3f(SIZE/2, 0, -SIZE/2);
    }

    /**
     * Returns the model of this terrain.
     *
     * @return model of terrain
     */
    public RawModel getModel()
    {
        return model;
    }

    /**
     * Returns the texture pack of this terrain.
     *
     * @return texture pack
     */
    public TerrainTexturePack getTexturePack()
    {
        return texturePack;
    }
    /**
     * Returns the blend map of this terrain.
     *
     * @return blend map
     */
    public TerrainTexture getBlendMap()
    {
        return blendMap;
    }

    /**
     * Returns the height represented by a pixel on the height map.
     *
     * @param x - x coordinate of pixel
     * @param z z coordinate of pixel
     * @param image - buffered image
     * @return height of terrain at particular point
     */
    private float getHeight(int x, int z, BufferedImage image)
    {
        if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight())
            return 0;

        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOUR / 2f;
        height /= MAX_PIXEL_COLOUR / 2f;
        height *= MAX_HEIGHT;

        return height;
    }

    /**
     * Returns the height of the terrain at a particular (x, z) coordinate.
     *
     * @param worldX x coordinate
     * @param worldZ z coordinate
     * @return height of terrain at particular point
     */
    public float getHeightOfTerrain(float worldX, float worldZ)
    {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0)
        {
            return 0;
        }

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float answer;

        if (xCoord <= (1 - zCoord))
        {
            answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                    heights[gridX + 1][gridZ], 0), new Vector3f(0,
                    heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else
        {
            answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                    heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                    heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }

        return answer;
    }

    /**
     * Returns the size of the terrain.
     * @return SIZE - terrain size
     */
    public static float getSize()
    {
        return SIZE;
    }

    /**
     * Returns the x coordinate of this terrain.
     *
     * @return x coordinate
     */
    public float getX()
    {
        return x;
    }

    /**
     * Returns the z coordinate of this terrain.
     *
     * @return z coordinate
     */
    public float getZ()
    {
        return z;
    }

}