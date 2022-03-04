package gefe.input;

import static org.lwjgl.glfw.GLFW.*;

/**
 * https://www.glfw.org/docs/latest/input_guide.html#input_mouse
 * <p>
 * Mouse input comes in many forms, including mouse motion, button presses and scrolling offsets.
 */
public class MouseListener {
    private static MouseListener instance; // MouseListener Singleton
    private double xPos, yPos, lastX, lastY, scrollX, scrollY; // Mouse positioning and control
    private boolean mouseButtonPressed[] = new boolean[3]; // Mouse buttons to be able to press
    private boolean isDragging;

    /**
     * This constructor is private because it should not be able to other classes
     * The only class to create a new MouseListener is the MouseListener Class
     */
    private MouseListener() {
        // Initialize mouse positions so that the mouse does not bug out on start.
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    /**
     * The callback functions receives the cursor position, measured in screen coordinates
     * but relative to the top-left corner of the window content area. On platforms that provide it,
     * the full sub-pixel cursor position is passed on.
     *
     * @param window Window memory location
     * @param xPos   X position for mouse
     * @param yPos   Y position for mouse
     */
    public static void mousePosCallback(long window, double xPos, double yPos) {
        // Set the last positions, resetting according what it needs to be, before we change it to new position
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;

        // If the mouse button is pressed and just moved, then the user is dragging
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    /**
     * The callback function receives the mouse button, button action and modifier bits.
     *
     * @param window Window memory location
     * @param button Button number
     * @param action Button action
     * @param mods   Modifier bits (for instance: pressing ctrl while pressing a button)
     */
    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            // What if mouse has more buttons than previously expected
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            // What if mouse has more buttons than previously expected
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    /**
     * The callback function receives two-dimensional scroll offsets.
     *
     * @param window  Window memory location
     * @param xOffset x-axis scroll offset
     * @param yOffset y-axis scroll offset
     */
    public static void scrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX(){
        return (float) get().xPos;
    }

    public static float getY(){
        return (float) get().yPos;
    }

    /**
     * @return the amount of elapsed x position in the current frame
     */
    public static float getDx(){
        return (float) (get().lastX - get().xPos);
    }

    /**
     * @return the amount of elapsed y position in the current frame
     */
    public static float getDy(){
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX(){
        return (float) get().scrollX;
    }

    public static float getScrollY(){
        return (float) get().scrollY;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }else{
            return false;
        }
    }
}
