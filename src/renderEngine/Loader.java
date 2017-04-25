package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

/**
 * Loads 3D models into memory by storing positional data about the
 * model in a VAO.
 * 
 * @author Aaron Frazer
 */
public class Loader
{
	/**
	 * An arraylist of VAOs.
	 */
	private List<Integer> vaos = new ArrayList<Integer>();
	
	/**
	 * An arraylist of VBOs.
	 */
	private List<Integer> vbos = new ArrayList<Integer>();
	
	/**
	 * An arraylist of texture IDs.
	 */
	private List<Integer> textures = new ArrayList<Integer>();
	
	/**
	 * Loads positions into a VAO and returns information about VAO
	 * as a Raw Model object.
	 * @param positions - positions of the 
	 * @param textureCoords - texture coordinates
	 * @param indices - indices ofthe VBO
	 * @return Raw Model object
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, int[] indices)
	{
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	

	/**
	 * Loads up a texture into OpenGL.
	 * @param fileName - filepath of texture
	 * @return textureID - the loaded texture's ID
	 */
	public int loadTexture(String fileName)
	{
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
	}
	
	/**
	 * Deletes all VAOs, VBOs, and Textures.
	 */
	public void cleanUp()
	{
		for (int vao:vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture:textures)
		{
			GL11.glDeleteTextures(texture);
		}
	}
	
	
	/**
	 * Creates an empty VAO.
	 * @return vaoID - ID of the created VAO
	 */
	private int createVAO()
	{
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	/**
	 * Stores the data into an attribute list of a VAO.
	 * @param attributeNumber - number of attribute list
	 * @param coordinateSize - size of coordinate (2D or 3D)
	 * @param data - data to be stored
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Unbinds the VAO.
	 * This method is called when VAO is no longer in use.
	 */
	private void unbindVAO()
	{
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Loads an indices buffer and binds it to a VAO.
	 * @param indices - 
	 */
	private void bindIndicesBuffer(int[] indices)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Converts an array of ints into a FloatBuffer object.
	 * @param data - array of ints
	 * @return buffer - FloatBuffer object
	 */
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Converts an array of floats into a FloatBuffer object.
	 * @param data - array of floats
	 * @return buffer - FloatBuffer object
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
