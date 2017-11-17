// Main Assignemnt class
import engine.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Arty {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);

    private static Vec3 CAMERA_START_POSITION = new Vec3(4f, 12f, 18f);

    public static void main(String[] args) {

        Camera camera = new Camera(CAMERA_START_POSITION,
                                    Camera.DEFAULT_TARGET,
                                    Camera.DEFAULT_UP);

        Scene gallery = new GalleryScene(camera);
        GraphicsWindow window = new GraphicsWindow("Art Gallery", gallery);
        CameraController controller = new CameraController(camera);

        window.addMouseMotionListener(controller);
        window.addKeyListener(controller);

        window.start();

        window.setSize(dimension);
        window.setVisible(true);
    }

}
