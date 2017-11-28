package renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import textures.TextureData;
import toolbox.GameSettings;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Loads a 3D model into memory by storing positional data about the model in a VAO.
 */
public class Loader
{
    /**
     * Location of the textures directory
     */
    private static final String TEXTURES_LOC = "res/textures/";

    /**
     * Location of font textures directory
     */
    private static final String FONT_TEXTURES_LOC = "res/font/textures/";

    /**
     * Location of sky box directory
     */
    private static final String SKYBOX_LOC = "res/skybox/";

    /**
     * List of VAOs stored in memory
     */
    public List<Integer> vaos = new ArrayList<>();

    /**
     * Map of VAO -> VBO's
     */
    public static HashMap<Integer, List<Integer>> vaoCache = new HashMap();

    /**
     * List of VBOs stored in memory
     */
    private List<Integer> vbos = new ArrayList<>();

    /**
     * Array list of texture IDs
     */
    private List<Integer> textures = new ArrayList<>();

    /**
     * Loads positions into a VAO.
     * Used by ObjectFileLoader and Terrain.
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
        storeDataInAttributeList(0, 3, positions, vaoID);
        storeDataInAttributeList(1, 2, textureCoords, vaoID);
        storeDataInAttributeList(2, 3, normals, vaoID);
        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    /**
     * Loads positions into a VAO with normal mapping.
     * Used by NormalMappingObjectFileLoader.
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
        storeDataInAttributeList(0, 3, positions, vaoID);
        storeDataInAttributeList(1, 2, textureCoords, vaoID);
        storeDataInAttributeList(2, 3, normals, vaoID);
        storeDataInAttributeList(3, 3, tangents, vaoID);
        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    /**
     * Loads positions into a VAO and returns information about VAO as a RawModel object.
     * Used by GuiRenderer, SkyboxRenderer, and WaterRenderer.
     * @param positions array of vertex positions
     * @param dimensions number of dimensions (2D or 3D)
     * @return RawModel object of VAO
     */
    public RawModel loadToVAO(float[] positions, int dimensions)
    {
        int vaoID = createVAO();
        storeDataInAttributeList(0, dimensions, positions, vaoID);
        unbindVAO();

        return new RawModel(vaoID, positions.length / dimensions);
    }

    /**
     * Loads positions into a VAO.
     * Used by TextMaster for font rendering.
     * @param positions array of vertex positions
     * @param textureCoords array of texture coordinate vartices
     * @return VAO ID
     */
    public int loadToVAO(float[] positions, float[] textureCoords)
    {
        int vaoID = createVAO();
        storeDataInAttributeList(0, 2, positions, vaoID);
        storeDataInAttributeList(1, 2, textureCoords, vaoID);
        unbindVAO();

        return vaoID;
    }

    /**
     * Creates a new empty VBO.
     * The VBO will be filled every frame with new info for particles
     * @param floatCount maximum number of floats this VBO will hold
     * @return ID of VBO
     */
    public int createEmptyVbo(int floatCount)
    {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        return vboID;
    }

    /**
     * Updates a VBO with data.
     * @param vbo VBO to update
     * @param data array of data
     * @param buffer float buffer that can be reused
     */
    public void updateVbo(int vbo, float[] data, FloatBuffer buffer)
    {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Adds a per instance attribute to a VAO.
     * @param vao VAO attribute will be applied to
     * @param vbo VBO where attribute retrieves data from
     * @param attribute attribute number where data will be stored
     * @param dataSize size of data element
     * @param instanceDataLength stride
     * @param offset offset
     */
    public void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instanceDataLength, int offset)
    {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL30.glBindVertexArray(vao);
        GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instanceDataLength * 4, offset * 4);
        GL33.glVertexAttribDivisor(attribute, 1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    /**
     * Creates an empty VAO by adding it to the map with an empty list.
     * @return ID of created VAO
     */
    private int createVAO()
    {
        // There should be no VAO (good)
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        vaos.add(vaoID); // add it to list of VAOs (so that we can delete them later)
        List<Integer> associatedVbos = new ArrayList<>(); // List of VBOs associated to this VAO
        vaoCache.put(vaoID, associatedVbos); // add VBO to VAO

        return vaoID;

//        int vaoID = GL30.glGenVertexArrays();
//        vaos.add(vaoID); // add it to list of VAOs (so that we can delete them later)
//        GL30.glBindVertexArray(vaoID);
//        return vaoID;
    }

    /**
     * Stores data into an attribute list of a VAO.
     * @param attributeNumber number of attribute list
     * @param coordinateSize size of coordinate (2D or 3D)
     * @param data data to be stored
     */
    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data, int vaoID)
    {
        int vboID = GL15.glGenBuffers();
        vaoCache.get(vaoID).add(vboID);
        vbos.add(vboID); // add it to list of VBOs (so that we can delete them later)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
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
     * Deletes a VAO and its associated VBOs from memory if the text is never
     * going to be used again.
     * @param vaoID ID of VAO to be deleted
     */
    public void deleteVaoFromCache(int vaoID)
    {
        System.out.println("Deleting VAO: " + vaoID);
        List<Integer> associatedVbos = vaoCache.remove(vaoID);
        for (int vboID : associatedVbos)
        {
            GL15.glDeleteBuffers(vboID);
        }
        GL30.glDeleteVertexArrays(vaoID);
    }

    /**
     * Loads a texture into the game.
     * Implements mipmapping for textures that are further away from camera.
     * This makes normal maps sharper.
     * @param fileName filepath of texture
     * @return loaded texture's ID
     */
    public int loadGameTexture(String fileName)
    {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream(TEXTURES_LOC + fileName + ".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, GameSettings.MIPMAPPING);
            if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic)
            {
                float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, amount);
            } else
            {
                System.out.println("Anisotropic filtering not supported");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load texture: " + fileName + ".png");
            System.exit(-1);
        }
        textures.add(texture.getTextureID());

        return texture.getTextureID();
    }

    /**
     * Loads a font texture atlas.
     * Fully implements mipmapping effect.
     * @param fileName filepath of texture
     * @return loaded texture's ID
     */
    public int loadFontTextureAtlas(String fileName)
    {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream(FONT_TEXTURES_LOC + fileName + ".png"));
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
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

    /**
     * Deletes all VAOs, VBOs, and textures.
     */
    public void cleanUp()
    {
        for (int vaoID : vaos)
        {
            GL30.glDeleteVertexArrays(vaoID);
        }
        for (int vboID : vbos)
        {
            GL15.glDeleteBuffers(vboID);
        }
        for (int textureID : textures)
        {
            GL11.glDeleteTextures(textureID);
        }
    }

}