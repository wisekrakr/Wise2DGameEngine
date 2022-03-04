package gefe.visual;

import gefe.LevelEditorScene;
import gefe.LevelScene;
import gefe.input.GamepadListener;
import gefe.input.KeyListener;
import gefe.input.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Object to visualize the game in.
 * This can only be created once and will function as the main Window for all gaming states.
 */
public class Window {

    private int width, height;
    private String title;
    private static float r, g, b, a;
    private float fps; // frames per second

    /**
     * This is a Number address where this window is in the memory space.
     */
    private long glfwWindow;

    /**
     * This Window object will be our Singleton object
     */
    private static Window window = null;

    /**
     * Current Scene in the Window
     */
    private static Scene currentScene = null;

    /**
     * This constructor is private because we only want one window built
     * and only this class can create it.
     */
    private Window() {
        this.width = 800;
        this.height = 800;
        this.title = "Super Perico";

        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("LWJGL Activated! - version: " + Version.getVersion());

        init();
        render();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Initializes the window and GLFW functionality
     * GLFW stands for Graphics Library Framework. It provides programmers with the ability
     * to create and manage windows and OpenGL contexts, as well as handle joystick, keyboard and mouse input.
     */
    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        // Will give hints to do the basic window operations: resize, close, etc.
        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        // Not visible until the window is done creating itself
        glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        // Window is resizable
        glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        // Window is maximized when opened
        glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

        // Create the window on the primary monitor and do not worry about sharing
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW Window");
        }

        // Set up mouse callbacks and forward mouse position to MouseListener methods
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::scrollCallback);

        // Set up key callbacks and forward keystroke to KeyListener methods
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Set up Joystick/Gamepad callbacks and forward keystroke to KeyListener methods
        glfwSetJoystickCallback(GamepadListener::gamePadCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync (buffer swapping) there is no wait time between frames swapping
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        changeScene(0);
    }

    /**
     * Called when the window should render itself.
     */
    public void render() {
        float startTime = (float) glfwGetTime();
        float endTime;
        float deltaTime = -1.0f;

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            glClearColor(r, g, b, a); // gives the window its color
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if (deltaTime >= 0){
                currentScene.update(deltaTime);
            }

            glfwSwapBuffers(glfwWindow); // swap the color buffers

            // Calculate fps
            fps = (float) Math.abs(1.0 / deltaTime);

            endTime = (float) glfwGetTime();
            deltaTime = endTime - startTime;
            startTime = endTime; // interruptions can be recorded by adding startTime in the end instead of beginning of loop

        }
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown Scene: " + newScene;
                break;
        }
    }

    /**
     * Fades the screen to black
     */
    public static void fadeToBlack() {
        if (r > 0.01f && g > 0.01f && b > 0.01f) {
            r = Math.max(r - 0.1f, 0);
            g = Math.max(g - 0.1f, 0);
            b = Math.max(r - 0.1f, 0);
        }
    }

    public double getFps() {
        return fps;
    }
}
