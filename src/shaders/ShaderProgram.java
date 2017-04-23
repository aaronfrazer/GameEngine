package shaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;

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
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		bindAttributes();
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
	 * Deletes fragment and vertex shaders.
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
	 * Binds inputs in the shader program to one of the attributs in the VAO.
	 */
	protected abstract void bindAttributes();
	
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
			 System.err.println("Could not compile shader!");
			 System.exit(-1);
		 }
		 
		 return shaderID;
	 }
}
