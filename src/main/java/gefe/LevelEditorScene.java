package gefe;

import gefe.visual.Camera;
import gefe.visual.Scene;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {


    private float[] vertexArray = {
            //position             //color
            100.5f, -0.5f, 0.0f,     1.0f,0.0f,0.0f,1.0f, // Bottom right
            -0.5f, 100.5f, 0.0f,     0.0f,1.0f,0.0f,1.0f, // Top left
            100.5f, 100.5f, 0.0f,      0.0f,0.0f,1.0f,1.0f, // Top right
            -0.5f, -0.5f, 0.0f,    1.0f,1.0f,0.0f,1.0f, // Bottom left
    };

    // In counter-clockwise order
    private int[] elementArray = {
        2,1,0, // top right triangle
        0,1,3 // bottom left triangle
    };

    private Shader defaultShader;

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        defaultShader.link();

        // ==========================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ==========================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip(); // flip in order for OpenGL to work with the vertexArray

        // Create the VBo and upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0,positionSize,GL_FLOAT, false, vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT,false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);


    }

    @Override
    public void update(double deltaTime) {

        // Start using Shader
        defaultShader.use();
        defaultShader.uploadMatrix4f("uProjectionMatrix", camera.getProjectionMatrix());
        defaultShader.uploadMatrix4f("uViewMatrix", camera.getViewMatrix());

        // Bind the VAO
        glBindVertexArray(vaoID);

        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0); // 0 means none here

        defaultShader.detach(); // detaching the shader

    }


}
