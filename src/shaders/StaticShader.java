package shaders;

/**
 * A shader program used to create static models.
 * 
 * @author Aaron Frazer
 */
public class StaticShader extends ShaderProgram
{
	/**
	 * Filepath of vertex shader file.
	 */
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	
	/**
	 * Filepath of fragment shader file.
	 */
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

	/**
	 * Creates a static shader program.
	 */
	public StaticShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes()
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
}
