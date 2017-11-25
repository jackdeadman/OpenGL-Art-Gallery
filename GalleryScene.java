import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import engine.*;
import gmaths.*;
import gmaths.Mat4;
import meshes.*;
import models.*;
import models.hand.*;
import scene.*;
import scene.handpositions.*;

public class GalleryScene extends Scene {

    private Light light;
    // private Hand hand;
    private Room room;
    private Model lamp1;
    private Model lamp2;

    private HandConfiguration handConfiguration;
    private SGNode scene = new NameNode("Scene");
    private HandPosition currentPosition = new LetterD();

    public GalleryScene(Camera camera, HandConfiguration handConfiguration) {
        super(camera);
        this.handConfiguration = handConfiguration;

        setupModels();
    }

    private void setupModels() {
        lamp1 = new Lamp(worldConfig);
        lamp2 = new Lamp(worldConfig);
        room = new Room(worldConfig, 16, 24, 10);
        // hand = new Hand(gl, light, camera, handConfiguration);

        registerModels(new Model[] { lamp1, lamp2, room });
    }


    protected void buildSceneGraph(GL3 gl) {
        System.out.println("Building scene graph");
        SGNode scene = new NameNode("Gallery Scene");
        TransformNode handTransform = new TransformNode("",
                Mat4Transform.scale(1.0f, 1.0f, 1.0f));

        TransformNode moveLight1 = new TransformNode("",
                Mat4Transform.translate(0.0f, 1.0f, 0.0f));

        TransformNode moveLight2 = new TransformNode("",
                Mat4Transform.translate(-6.0f, 4.0f, -6.0f));


        scene.addChild(room.getRoot());
        room.getAnchor().addChild(moveLight1);
                moveLight1.addChild(lamp1.getRoot());
        room.getAnchor().addChild(moveLight2);
                // moveLight2.addChild(lamp2.getRoot());
            // room.getAnchor().addChild(handTransform);
        //         // handTransform.addChild(hand.getRoot());

        scene.update();

        setSceneNode(scene);
    }

    protected void update(GL3 gl) {
        // Update the hand based on the animation
        // handConfiguration.setFingerValues(
        //     currentPosition.getAnimationState((float)(getElapsedTime())));
        // hand.applyFingerBend();
    }

}
