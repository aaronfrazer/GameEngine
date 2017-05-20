package terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

/**
 * Represents a terrain in the game.
 * 
 * @author Aaron Frazer
 */
public class Terrain
{
	/**
	 * Size of terrain
	 */
	public static final float SIZE = 800;
	
	/**
	 * Maximum height of terrain
	 */
	private static final float MAX_HEIGHT = 40;
	
	/**
	 * Maximum color value that a pixel on the height map can have
	 */
	private static final float MAX_PIXEL_COLOUR = 256 * 256 * 256;
	
	/**
	 * World coordinates
	 */
	private float x, y;
	
	/**
	 * Height of each vertex on terrain
	 */
	private float[][] heights;
	
	/**
	 * Terrain model
	 */
	private RawModel model;
	
	/**
	 * Terrain texture pack that contains 4 textures it will be using
	 */
	private TerrainTexturePack texturePack;
	
	/**
	 * Blend map of terrain of where 4 textures should be rendered
	 */
	private TerrainTexture blendMap;
	
	/**
	 * Constructs a terrain.
	 * @param gridX - x coordinate
	 * @param gridZ - z coordinate
	 * @param loader - loader
	 * @param texture - texture of terrain
	 * @param heightMap - height map filename
	 */
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap)
	{
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.y = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	/**
	 * Generates a terrain using a height map.
	 * @param loader - loader
	 * @param heightMap - name of height map
	 * @return model of terrain
	 */
	private RawModel generateTerrain(Loader loader, String heightMap){
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("res/heightmaps/" + heightMap + ".png"));
		} catch (IOException e) { e.printStackTrace(); }
		
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++)
		{
			for(int j=0;j<VERTEX_COUNT;j++)
			{
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				
				Vector3f normal = calculateNormal(j, i, image);
				
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++)
		{
			for(int gx=0;gx<VERTEX_COUNT-1;gx++)
			{
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
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
	 * Returns a normal of a pixel.
	 * @param x - x coordinate of vertex
	 * @param y - y coordinate of vertex
	 * @param image - buffered image of heightmap
	 * @return normal - normal as a Vector3f
	 */
	private Vector3f calculateNormal(int x, int y, BufferedImage image)
	{
		float heightL = getHeight(x - 1, y, image);
		float heightR = getHeight(x + 1, y, image);
		float heightD = getHeight(x, y - 1, image);
		float heightU = getHeight(x, y + 1, image);
		
		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		
		return normal;
	}
	
	/**
	 * Returns the height of terrain for a given (x,y) coordinate.
	 * @param worldX
	 * @param worldY
	 * @return answer - height of terrain
	 */
	public float getHeightOfTerrain(float worldX, float worldY)
	{
		float terrainX = worldX - this.x;
		float terrainY = worldY - this.y;
		float gridSquareSize = SIZE / ((float)heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridY = (int) Math.floor(terrainY / gridSquareSize);
		if (gridX >= heights.length - 1 || gridY >= heights.length - 1 || gridX < 0 || gridY < 0)
		{
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float yCoord = (terrainY % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1-yCoord)) 
		{
			answer = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridY], 0), new Vector3f(1,
							heights[gridX + 1][gridY], 0), new Vector3f(0,
							heights[gridX][gridY + 1], 1), new Vector2f(xCoord, yCoord));
		} else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridY], 0), new Vector3f(1,
							heights[gridX + 1][gridY + 1], 1), new Vector3f(0,
							heights[gridX][gridY + 1], 1), new Vector2f(xCoord, yCoord));
		}
		
		return answer;
	}
	
	/**
	 * Returns the height represented by a pixel on the height map.
	 * @param x - x coordinate of pixel
	 * @param y - y coordinate of pixel
	 * @param image - buffered image
	 * @return
	 */
	private float getHeight(int x, int y, BufferedImage image)
	{
		if (x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight())
		{
			return 0;
		}
		
		float height = image.getRGB(x, y);
		
		height += MAX_PIXEL_COLOUR / 2f;
		height /= MAX_PIXEL_COLOUR / 2f;
		height *= MAX_HEIGHT;
		
		return height;
	}
	
	/**
	 * Returns the x coordinate of this terrain.
	 * @return x - x coordinate
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * Returns this terrain's texture pack.
	 * @return texturePack - texture pack
	 */
	public TerrainTexturePack getTexturePack()
	{
		return texturePack;
	}

	/**
	 * Returns this terrain's blend map.
	 * @return blendMap - blend map
	 */
	public TerrainTexture getBlendMap()
	{
		return blendMap;
	}


	/**
	 * Returns the z coordinate of this terrain.
	 * @return z - z coordinate
	 */
	public float getZ()
	{
		return y;
	}

	/**
	 * Returns the model of this terrain.
	 * @return model - terrain model
	 */
	public RawModel getModel()
	{
		return model;
	}
	
}
