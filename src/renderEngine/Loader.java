package renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import textures.TextureData;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads a 3D model into memory by storing positional data about the model in a VAO.
 */
public class Loader
{
    /**
     * Location of the textures file directory
     */
    private static final String TEXTURES_LOC = "res/textures/";

    /**
     * Location of sky box file directory
     */
    private static final String SKYBOX_LOC = "res/skybox/";


    /**
     * Array list of VAOs
     */
    private List<Integer> vaos = new ArrayList<>();

    /**
     * Array list of VBOs
     */
    private List<Integer> vbos = new ArrayList<>();

    /**
     * Array list of texture IDs
     */
    private List<Integer> textures = new ArrayList<>();

    /**
     * Loads positions into a VAO.
     * @param positions array of vertex positions
     * @param textureCoords array of texture coordinate vertices
     * @param normals array of normals
     * @param indices array of indices that correlates to which vertices are connected
     * @return raw model of VAO
     */
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices)
    {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    /**
     * Loads positions into a VAO with normal mapping.
     * @param positions array of vertex positions
     * @param textureCoords array of texture coordinate vertices
     * @param normals array of normals
     * @param tangents array of tangents
     * @param indices array of indices that correlates to which vertices are connected
     * @return raw model of VAO
     */
    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, float[] tangents, int[] indices)
    {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        storeDataInAttributeList(3, 3, tangents);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    /**
     * Loads positions into a VAO and returns information about VAO as a RawModel object.
     * @param positions array of vertex positions
     * @param dimensions number of dimensions (2D or 3D)
     * @return RawModel object of VAO
     */
    public RawModel loadToVAO(float[] positions, int dimensions)
    {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();

        return new RawModel(vaoID, positions.length / dimensions);
    }

    /**
     * Loads a texture in OpenGL.
     * Implements mipmapping for textures that are further away from camera.
     * @param fileName filepath of texture
     * @return loaded texture's ID
     */
    public int loadTexture(String fileName)
    {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream(TEXTURES_LOC + fileName + ".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load texture: " + fileName + ".png");
            System.exit(-1);
        }
        textures.add(texture.getTextureID());

        return texture.getTextureID();
    }

    /**
     * Loads a cube map to OpenGL.
     * @param textureFiles 6 textures used to make up cube map
     * @return cubemap texture's ID
     */
    public int loadCubeMap(String[] textureFiles)
    {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i < textureFiles.length; i++)
        {
            TextureData data = decodeTextureFile(SKYBOX_LOC + textureFiles[i] + ".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        // Remove visible seams from skybox image
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        textures.add(texID);

        return texID;
    }

    /**
     * Deletes all VAOs, VBOs, and textures.
     */
    public void cleanUp()
    {
        for (int vao : vaos)
        {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos)
        {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture : textures)
        {
            GL11.glDeleteTextures(texture);
        }
    }

    /**
     * Creates an empty VAO.
     * @return ID of created VAO
     */
    private int createVAO()
    {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    /**
     * Unbinds a VAO.
     * This method is called when VAO is no longer in use.
     */
    private void unbindVAO()
    {
        GL30.glBindVertexArray(0);
    }

    /**
     * Stores data into an attribute list of a VAO.
     * @param attributeNumber number of attribute list
     * @param coordinateSize size of coordinate (2D or 3D)
     * @param data data to be stored
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
     * Loads indices buffer and binds it to a VAO.
     * @param indices array of indices
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
     * Uses PNG Decoder to load an image into a byte buffer and returns
     * it as a TextureData object.
     * @param fileName image filename
     * @return TextureData object
     */
    private TextureData decodeTextureFile(String fileName)
    {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load texture " + fileName + ".png");
            System.exit(-1);
        }

        return new TextureData(buffer, width, height);
    }

    /**
     * Converts an array of floats into a FloatBuffer object.
     * @param data array of floats
     * @return FloatBuffer object
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data)
    {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    /**
     * Converts an array of ints into a FloatBuffer object.
     * @param data array of ints
     * @return FloatBuffer object
     */
    private IntBuffer storeDataInIntBuffer(int[] data)
    {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

}