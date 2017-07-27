package shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * A generic shader program that contains attributes and methods
 * that every shader program should implement.
 * 
 * @author Aaron Frazer
 */
public abstract class ShaderProgram
{
	/**
	 * ID of the program.
	 */
	private int programID;
	
	/**
	 * ID of the program's vertexShader.
	 */
	private int vertexShaderID;
	
	/**
	 * ID of the program's fragementShader.
	 */
	private int fragmentShaderID;
	
	/**
	 * FloatBuffer used to load matrices.
	 */
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	/**
	 * Creates a shader program.
	 * @param vertexFile - vertex file path
	 * @param fragmentFile - fragment file path
	 */
	public ShaderProgram(String vertexFile, String fragmentFile)
	{
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	/**
	 * Starts the shader program.
	 */
	public void start()
	{
		GL20.glUseProgram(programID);
	}
	
	/**
	 * Stops the shader program.
	 */
	public void stop()
	{
		GL20.glUseProgram(0);
	}
	
	/**
	 * Deletes fragment and vertex shaders from this shader program.
	 */
	public void cleanUp()
	{
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteProgram(vertexShaderID);
		GL20.glDeleteProgram(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * Returns all uniform variable locations of shader program.
	 */
	protected abstract void getAllUniformLocations();
	
	/**
	 * Returns the location of a uniform variable.
	 * @param uniformName - name of uniform variable (in shader code)
	 * @return location of uniform variable
	 */
	protected int getUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	/**
	 * Binds an attribute to a variable name.
	 * @param attribute - number of attribute list
	 * @param variableName - name of variable the attribute will be bound to
	 */
	protected void bindAttribute(int attribute, String variableName)
	{
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Loads a float into a uniform location.
 	 * @param location - position the float is loaded to
	 * @param value - float value
	 */
	protected void loadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * Loads an int into a uniform location.
 	 * @param location - position the int is loaded to
	 * @param value - int value
	 */
	protected void loadInt(int location, int value)
	{
		GL20.glUniform1i(location, value);
	}

	/**
	 * Loads a 2D vector into a uniform location.
	 * @param location - position the 2D vector is loaded to
	 * @param vector - 2D vector
	 */
	protected void load2DVector(int location, Vector2f vector)
	{
		GL20.glUniform2f(location, vector.x, vector.y);
	}

	/**
	 * Loads a 3D vector into a uniform location.
	 * @param location - position the 3D vector is loaded to
	 * @param vector - 3D vector
	 */
	protected void load3DVector(int location, Vector3f vector)
	{
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Loads a 4D vector into a uniform location.
	 * @param location - position the 3D vector is loaded to
	 * @param vector - 3D vector
	 */
	protected void load4DVector(int location, Vector4f vector)
	{
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}

	/**
	 * Loads a Vector3f into a uniform location.
	 * @param location - position the boolean is loaded to
	 * @param value - 0 or 1
	 */
	protected void loadBoolean(int location, boolean value)
	{
		GL20.glUniform1f(location, value ? 1 : 0);
	}
	
	/**
	 * Loads a matrix into a uniform location.
	 * @param location - position the matrix is loaded to
	 * @param matrix - matrix
	 */
	protected void loadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	/**
	 * Binds inputs in the shader program to one of the attributes in the VAO.
	 */
	protected abstract void bindAttributes();
	
	/**
	 * Loads up a shader source code file.
	 * @param file - file name of the shader file
	 * @param type - type of shader (vertext or fragment)
	 * @return shaderID - ID of the created shader
	 */
	 private static int loadShader(String file, int type)
	 { 
		 StringBuilder shaderSource = new StringBuilder();
		 
		 try {
			 BufferedReader reader = new BufferedReader(new FileReader(file));
			 String line;
			 while( (line = reader.readLine()) != null){
				 shaderSource.append(line).append("\n");
			 }
			 reader.close();
		  } catch(IOException e) {
			  e.printStackTrace();
			  System.exit(-1);
		  }
		 
		 int shaderID = GL20.glCreateShader(type);
		 GL20.glShaderSource(shaderID, shaderSource);
		 GL20.glCompileShader(shaderID);
		 
		 if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE)
		 {
			 System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			 System.err.println("Could not compile shader: " + file);
			 System.exit(-1);
		 }
		 
		 return shaderID;
	 }
}
