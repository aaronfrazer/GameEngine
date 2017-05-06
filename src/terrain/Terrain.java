package terrain;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;

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
	private static final float SIZE = 800;
	
	/**
	 * Number of vertices on each side of terrain
	 */
	private static final int VERTEX_COUNT = 128;
	
	/**
	 * World coordinates
	 */
	private float x, z;
	
	/**
	 * Terrain model
	 */
	private RawModel model;
	
	/**
	 * Terrain texture
	 */
	private ModelTexture texture;
	
	/**
	 * Constructs a terrain.
	 * @param gridX - x coordinate
	 * @param gridZ - z coordinate
	 * @param loader - loader
	 * @param texture - texture of terrain
	 */
	public Terrain(int gridX, int gridZ, Loader loader, ModelTexture texture)
	{
		this.texture = texture;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader);
	}
	

	/**
	 * Generates a flat terrain.
	 * @param loader - loader
	 * @return model of terrain
	 */
	private RawModel generateTerrain(Loader loader){
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = 0;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer*3] = 0;
				normals[vertexPointer*3+1] = 1;
				normals[vertexPointer*3+2] = 0;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
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
	 * Returns the x coordinate of this terrain.
	 * @return x - x coordinate
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * Returns the z coordinate of this terrain.
	 * @return z - z coordinate
	 */
	public float getZ()
	{
		return z;
	}

	/**
	 * Returns the model of this terrain.
	 * @return model - terrain model
	 */
	public RawModel getModel()
	{
		return model;
	}

	/**
	 * Returns the texture of this terrain.
	 * @return texture - terrain texture
	 */
	public ModelTexture getTexture()
	{
		return texture;
	}
}
