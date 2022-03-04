package renderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

/**
 * Handles loading the shader file within the Constructor method.
 * All other methods is separate, so we can catch errors more clearly if they happen
 */
public class Shader {

    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    // Identifiers so we know what we are working with when talking to the GPU
    private int vertexID,fragmentID,shaderProgramID;  // combination of vertex and fragment source

    /**
     * Handles loading the shader file
     * @param filePath string path to shader file
     */
    public Shader(String filePath) {
        this.filePath = filePath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] split = source.split("(#type)( )+([a-zA-Z]+)"); // where is the end of the line with #type in shader file

            // Find the first patter after #type 'pattern'
            int index = source.indexOf("#type") + 6; // find first index of #type and start at the beginning of the word
            int endOfLine = source.indexOf("\r\n",index);
            String firstPattern = source.substring(index, endOfLine).trim();

            // Find the second patter after #type 'pattern'
            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, endOfLine).trim();

            if(firstPattern.equals("vertex")){
                vertexSource = split[1];
            }else if (firstPattern.equals("fragment")){
                fragmentSource = split[1];
            }else{
                throw new IOException("Unexpected Token: " + firstPattern);
            }

            if(secondPattern.equals("vertex")){
                vertexSource = split[2];
            }else if (secondPattern.equals("fragment")){
                fragmentSource = split[2];
            }else{
                throw new IOException("Unexpected Token: " + firstPattern);
            }

        }catch (IOException e){
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: " + filePath;
        }
    }

    /**
     * Compile shaders
     */
    public void compile(){
        // Load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shaders source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation process
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: \" + filePath + \" \n\tVertex Shader compilation failed");
            System.out.println(glGetShaderInfoLog(vertexID, length));
            assert false : ""; // break out of the program
        }

        // Load and compile the fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shaders source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors in compilation process
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: \" + filePath + \" \n\tFragment Shader compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentID, length));
            assert false : ""; // break out of the program
        }


    }

    /**
     * Link shaders and check for errors
     */
    public void link(){
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: \" + filePath + \" \n\tLinking of Shaders failed!");
            System.out.println(glGetProgramInfoLog(shaderProgramID, length));
            assert false : ""; // break out of the program
        }
    }

    /**
     * Bind shader program
     */
    public void use(){
        glUseProgram(shaderProgramID);
    }

    /**
     * Detach shader program
     */
    public void detach(){
        glUseProgram(0); // 0 means no program
    }

    /**
     * This will upload the variable matrix4f to the shader, with the variable name
     * @param varName name of the shader
     * @param matrix4f Matrix4f to upload to the shader
     */
    public void uploadMatrix4f(String varName, Matrix4f matrix4f){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16); //4x4 matrix
        matrix4f.get(matBuffer);

        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
}
