import gefe.visual.Window;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.run();

        System.out.println(window.getFps());
    }
}
