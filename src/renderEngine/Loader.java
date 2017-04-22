package renderEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
	 * Loads positions into a VAO and returns information about VAO
	 * as a Raw Model object.
	 * @param positions - positions of the 
	 * @return Raw Model object
	 */
	public RawModel loadToVAO(float[] positions)
	{
		int vaoID = createVAO();
		storeDataInAttributeList(0, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / 3);
	}
	
	/**
	 * Deletes all VAOs and VBOs.
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
	 * @param data - data to be stored
	 */
	private void storeDataInAttributeList(int attributeNumber, float[] data)
	{
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
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
	 * Converts float array of data into a float buffer.
	 * @param data
	 * @return
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
