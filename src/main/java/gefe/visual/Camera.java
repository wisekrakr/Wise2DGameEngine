package gefe.visual;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * The way to let the 'player' view the scene. The user can choose between a Perspective or Orthographic camera.
 * <p>
 * A view attributes that will be calculated within this class: <br>
 * <ul>
 *     <li>
 *         <b>Projection Matrix-</b>  mathematical function that will make your objects either have depth, or not have depth <br>
 *     </li>
 *     <li>
 *         <b>View Matrix-</b> the position of the camera in world space, and the direction it is pointing.
 *     </li>
 * </ul>
 */
public class Camera {
    // 4x4 matrices
    private Matrix4f projectionMatrix; // how big the screen space is
    private Matrix4f viewMatrix; // where the camera is in relation to the world space

    public Vector2f position; // camera position in the world

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    /**
     * Defines how many units we want our world space to be
     */
    public void adjustProjection() {
        // Dimensions of the camera
        projectionMatrix.setOrtho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    /**
     * Calculates the view matrix (where the camera is in relation to the world space) and returns it.
     *
     * To calculate the view matrix we set it to look at:
     * <ul>
     *     <li>
     *         where the camera is in our world space
     *     </li>
     *     <li>
     *         where the center of the camera is pointing at
     *     </li>
     *     <li>
     *         where the up direction is
     *     </li>
     * </ul>
     * @return Matrix4f viewMatrix
     */
    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        // Creates a view matrix
        viewMatrix.setLookAt(
                new Vector3f(
                        position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 20.0f),
                cameraUp
        );

        return viewMatrix;
    }

    /**
     * Returns the projection matrix:
     * Mathematical function that will make your objects either have depth, or not have depth
     * @return Matrix4f
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
