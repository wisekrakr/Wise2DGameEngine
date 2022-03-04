package gefe.input;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class GamepadListener {
    private static GamepadListener instance; // GamepadListener Singleton
    private static boolean buttonPressed[];

    private GamepadListener() {
    }

    public static GamepadListener get() {
        if (GamepadListener.instance == null) {
            GamepadListener.instance = new GamepadListener();
        }

        return GamepadListener.instance;
    }

    /**
     * The callback function receives the ID of the joystick that has been connected and disconnected and the event that occurred.
     *
     * @param jid Gamepad id
     * @param event Gamepad event
     */
    public static void gamePadCallback(int jid, int event) {
        ByteBuffer byteBuffer = glfwGetJoystickButtons(jid);
        if(glfwJoystickIsGamepad(jid)){
            if (event == GLFW_CONNECTED) {
                // The joystick was connected
                System.out.println("Gamepad: " + jid + " connected");



            } else if (event == GLFW_DISCONNECTED) {
                // The joystick was disconnected
                System.out.println("Gamepad: " + jid + " disconnected");
            }
        }
    }


}
