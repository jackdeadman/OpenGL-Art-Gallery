package galleryscene;

import com.jogamp.opengl.*;

import engine.*;
import engine.animation.*;
import engine.lighting.*;
import engine.render.*;
import engine.scenegraph.*;

import gmaths.*;
import models.*;

public class GalleryScene extends Scene {

    // Models in the scene
    private Hand hand;
    private Room room;
    private Lamp lamp1, lamp2;
    private Arm arm;
    private PictureFrame pictureFrame;
    private Sky skyBox;

    private HandConfiguration handConfiguration;
    private final Vec3 DIRECTIONAL_LIGHT_DIR = new Vec3(0.2f, -0.2f, 0.3f);
    private AnimationEngine<HandConfiguration> animator;


    public GalleryScene(Camera camera, HandConfiguration handConfiguration, AnimationEngine<HandConfiguration> animator) {
        super(camera);

        this.handConfiguration = handConfiguration;
        this.animator = animator;

        // tmp
//        Spotlight light1 = new Spotlight(new Vec3(0f, 1f, 0f), new Vec3(0.1f, 0.18f, 0.0112f));
//        worldConfig.setSpotlight(light1);

        Vec3 colour = new Vec3(0.1f, 0.1f, 0.1f);
        DirectionalLight light = new DirectionalLight(DIRECTIONAL_LIGHT_DIR, colour);
        setDirectionalLight(light);

        setupModels();
    }

    public Lamp[] getLampModels() {
        return new Lamp[] { lamp1, lamp2 };
    }

    public DirectionalLight getWorldLight() {
        return getDirectionalLight();
    }


    private void setupModels() {
        // worldConfig.setSpotlight(new Spotlight(new Vec3(1f, 1f, 0f), new Vec3(0.1f, 0.18f, 0.0112f)));
        lamp1 = new Lamp(worldConfig);
        lamp2 = new Lamp(worldConfig);
        room = new Room(worldConfig, 16, 28, 10);
        hand = new Hand(worldConfig, handConfiguration);
        arm = new Arm(worldConfig);
        skyBox = new Sky(worldConfig);
        pictureFrame = new PictureFrame(worldConfig, PictureFrame.HORIZONTAL_FRAME_LARGE, "");

        registerModels(new Model[] { arm, lamp1, lamp2, room, hand, pictureFrame, skyBox });
    }


    protected void buildSceneGraph(GL3 gl) {
        room.addPictureToLeftWall((PictureFrame) pictureFrame);
        SGNode scene = new NameNode("Gallery Scene");

        TransformNode moveLight1 = new TransformNode("",
                Mat4Transform.translate(4.0f, 0.0f, -6.0f));

        TransformNode moveLight2 = new TransformNode("",
                Mat4Transform.translate(-4.0f, 0.0f, 6.0f));

        scene.addChild(room.getRoot());
        room.getAnchor().addChild(moveLight1);
                moveLight1.addChild(lamp1.getRoot());
        room.getAnchor().addChild(moveLight2);
                moveLight2.addChild(lamp2.getRoot());
        room.getAnchor().addChild(arm.getRoot());
            arm.getAnchor().addChild(hand.getRoot());

        scene.update();
        setSceneNode(scene);
    }

    protected void beforeSceneDraw(GL3 gl) {
        // Sky is not part of the scene graph.
        // Also needs to be render first so alpha blending works
        // correctly.
         skyBox.render(gl);
    }

    protected void update(GL3 gl) {
        // Update the hand based on the animation
        handConfiguration = animator.getAnimationState();
        hand.setConfiguration(handConfiguration);
        hand.applyFingerBend();
    }

}
