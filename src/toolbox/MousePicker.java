package toolbox;

import entities.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.*;
import terrain.Terrain;

/**
 * A mouse picker class used to select objects with the mouse
 * @author Aaron Frazer
 */
public class MousePicker
{
    /**
     *
     */
    private static final int RECURSION_COUNT = 200;

    /**
     * Maximum distance of mouse ray?
     */
    private static final float RAY_RANGE = 600;

    /**
     * Current ray coming from the mouse
     */
    private Vector3f currentRay;

    /**
     * Projection matrix
     */
    private Matrix4f projectionMatrix;

    /**
     * View matrix
     */
    private Matrix4f viewMatrix;

    /**
     * Camera used to create view matrix
     */
    private Camera camera;

    /**
     * Terrain
     */
    private Terrain terrain;

    /**
     * Current position of the mouse where it is intersecting the terrain
     */
    private Vector3f currentTerrainPoint;

    /**
     * Constructs a MousePicker by setting the camera, projection matrix, view matrix, and terrain.
     * @param cam - camera
     * @param projection - projection matrix
     * @param terrain - terrain TODO: Make this method take in a grid (array) of terrains
     */
    public MousePicker(Camera cam, Matrix4f projection, Terrain terrain)
    {
        this.camera = cam;
        this.projectionMatrix = projection;
        this.viewMatrix = Maths.createViewMatrix(camera);
        this.terrain = terrain;
    }

    /**
     * Returns the current position the mouse is on the terrain.
     * @return currentTerrainPoint - mouse position on terrain
     */
    public Vector3f getCurrentTerrainPoint()
    {
        return currentTerrainPoint;
    }

    /**
     * Returns the current ray of the mouse.
     * @return currentRay - current mouse ray
     */
    public Vector3f getCurrentRay()
    {
        return currentRay;
    }

    /**
     * Updates this mouse picker.
     */
    public void update()
    {
        viewMatrix = Maths.createViewMatrix(camera); // update view matrix
        currentRay = calculateMouseRay(); // update mouse ray
        if (intersectionInRange(0, RAY_RANGE, currentRay))
        {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else
        {
            currentTerrainPoint = null;
        }
    }

    /**
     * Returns the mouse ray as a 3D vector.
     * @return 3d coordinates of mouse ray
     */
    private  Vector3f calculateMouseRay()
    {
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);

        return worldRay;
    }

    /**
     * Converts from eye coordinates to world coordinates.
     * @param eyeCoords - 4D coordinates in eye space
     * @return 4D coordinates in world space
     */
    private Vector3f toWorldCoords(Vector4f eyeCoords)
    {
        Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();

        return mouseRay;
    }

    /**
     * Converts from clip space to eye space.
     * @param clipCoords - 4D coordinates in clip space
     * @return 4D coordinates in eye space
     */
    private Vector4f toEyeCoords(Vector4f clipCoords)
    {
        Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);

        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    /**
     * Converts mouse coordinates into OpenGL coordinates based on the mouse cursor's position on the screen.
     * @param mouseX - x position of mouse
     * @param mouseY - y position of mouse
     * @return 2D vector of OpenGL coordinates
     */
    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY)
    {
        float x = (2f * mouseX) / Display.getWidth() - 1f;
        float y = (2f * mouseY) / Display.getHeight() - 1f;

        return new Vector2f(x, y);
    }

    //*******************************************************************

    /**
     * Returns the point on the ray for a given distance.
     * @param ray - 3D ray
     * @param distance - distance of ray
     * @return 3D point on the ray
     */
    private Vector3f getPointOnRay(Vector3f ray, float distance)
    {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);

        return Vector3f.add(start, scaledRay, null);
    }

    /**
     * Performs a binary search to find the point on the ray
     * that intersects with the terrain.
     * @param count - number of times to recursively execute this method
     * @param start - start point of ray
     * @param finish - end point of ray
     * @param ray - 3D ray
     * @return 3D point that intersects with terrain
     */
    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray)
    {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT)
        {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getTerrain(endPoint.getX(), endPoint.getZ());
            if (terrain != null)
                return endPoint;
            else
                return null;
        }

        if (intersectionInRange(start, half, ray))
            return binarySearch(count + 1, start, half, ray);
        else
            return binarySearch(count + 1, half, finish, ray);
    }

    /**
     * Checks if the intersection is inside of the range of the ray.
     * @param start - start point of range
     * @param finish - end point of range
     * @param ray - 3D ray
     * @return true if in range, false if out of range
     */
    private boolean intersectionInRange(float start, float finish, Vector3f ray)
    {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint))
            return true;
        else
            return false;
    }

    /**
     * Checks if a point is underneath the terrain.
     * @param testPoint - point to be checked
     * @return true if underground, false if not underground
     */
    private boolean isUnderGround(Vector3f testPoint)
    {
        Terrain terrain = getTerrain(testPoint.getX(), testPoint.getZ());
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
        }
        if (testPoint.y < height)
            return true;
        else
            return false;
    }

    /**
     * Returns the current terrain being used.
     * @param worldX - terrain X coordinate
     * @param worldZ - terrain Y coordinate
     * @return terrain
     */
    private Terrain getTerrain(float worldX, float worldZ)
    {
        // Use this for code for multiple terrains
//        int x = worldX / Terrain.SIZE;
//        int z = worldZ / Terrain.SIZE;
//        return terrians[x][z];

        return terrain;
    }
}