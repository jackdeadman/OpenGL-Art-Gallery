import engine.*;
import meshes.*;
import models.*;
import models.hand.*;
import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import scene.*;


public class GalleryScene extends Scene {

    private HandConfiguration handConfiguration;

    // Models to be placed into the scene
    private Model hand, room;
    private LightEmittingModel lamp1, lamp2, ceilingLamp;

    public GalleryScene(Camera camera, HandConfiguration handConfiguration) {
        super(camera);
        this.handConfiguration = handConfiguration;
    }

    protected void initialise(GL3 gl) {
        lamp1 = new Lamp();
        lamp2 = new Lamp();
        ceilingLamp = new Lamp();
        room = new Room(10.0f, 10.0f, 12.0f);

        registerModels(new Model { lamp1, lamp2, room });
    }

    private void buildSceneGraph(GL3 gl) {
        SGNode scene = getSceneNode();

        room = new Room(gl, light, camera);
        hand = new Hand(gl, light, camera, handConfiguration);

        TransformNode handTransform = new TransformNode("",
                Mat4Transform.scale(1.0f, 1.0f, 1.0f));

        scene.addChild(room.getRoot());
            room.getAnchor().addChild(handTransform);
                handTransform.addChild(hand.getRoot());

        scene.update();
    }

}
