// Main Assignemnt class
import engine.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import gmaths.*;
import models.*;
import engine.animation.*;
import engine.lighting.*;
import engine.utils.*;
import galleryscene.*;

public class Arty {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);

    private static HandConfiguration handConfiguration;


    public static void main(String[] args) {
        // Initial hand configuration
        handConfiguration = new HandConfiguration();

        // Setup the camera into the default position
        Camera camera = new Camera(Camera.DEFAULT_POSITION,
                                    Camera.DEFAULT_TARGET,
                                    Camera.DEFAULT_UP);

        // Create the animator
        AnimationEngine<HandConfiguration> animator = new AnimationEngine<>();

        // The one and only scene to be rendered
        GalleryScene gallery = new GalleryScene(camera, handConfiguration, animator);

        // Create the window
        GraphicsWindow window = new GraphicsWindow("Art Gallery", gallery);
        CameraController controller = new CameraController(camera);

        window.addMouseMotionListener(controller);
        window.addKeyListener(controller);

        window.start();

        // Get the lighting configuration
        WorldConfiguration worldConfig = gallery.getWorldConfig();

        SceneControls controls = new SceneControls(handConfiguration, worldConfig);
        controls.setAnimationEngine(animator);
        controls.setCamera(camera);

        JPanel scrollFrame = new JPanel();
        controls.setAutoscrolls(true);
        scrollFrame.setPreferredSize(new Dimension( 250, 300));
        controls.setPreferredSize(new Dimension( 250, 300));

        scrollFrame.add(controls, BorderLayout.PAGE_START);

        window.getContentPane().add(scrollFrame, BorderLayout.EAST);
        window.setSize(dimension);
        window.setVisible(true);
    }

}
