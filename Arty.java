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

    private static Vec3 CAMERA_START_POSITION = new Vec3(0f, 6f, 14.0f);
    private static HandConfiguration handConfiguration;


    public static void main(String[] args) {

        handConfiguration = new HandConfiguration();

        Camera camera = new Camera(CAMERA_START_POSITION,
                                    Camera.DEFAULT_TARGET,
                                    Camera.DEFAULT_UP);

        AnimationEngine<HandConfiguration> animator = new AnimationEngine<>();
        GalleryScene gallery = new GalleryScene(camera, handConfiguration, animator);

        Lamp[] lamps = gallery.getLampModels();
        DirectionalLight worldLight = gallery.getWorldLight();

        GraphicsWindow window = new GraphicsWindow("Art Gallery", gallery);
        CameraController controller = new CameraController(camera);

        window.addMouseMotionListener(controller);
        window.addKeyListener(controller);

        window.start();

        WorldConfiguration worldConfig = gallery.getWorldConfig();

        SceneControls controls = new SceneControls(handConfiguration, worldConfig);
        controls.setAnimationEngine(animator);
        controls.setCamera(camera);

        JScrollPane scrollFrame = new JScrollPane(controls);
        controls.setAutoscrolls(true);
        scrollFrame.setPreferredSize(new Dimension( 330, window.getHeight()));

        window.getContentPane().add(scrollFrame, BorderLayout.EAST);
        window.setSize(dimension);
        window.setVisible(true);
    }

}
