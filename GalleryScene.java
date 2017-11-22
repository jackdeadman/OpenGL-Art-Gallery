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

    private Mesh floor, back;
    private Light light;
    private Hand hand;
    private Room room;
    private HandConfiguration handConfiguration;
    private SGNode scene = new NameNode("Scene");

    public GalleryScene(Camera camera, HandConfiguration handConfiguration) {
        super(camera);
        this.handConfiguration = handConfiguration;
    }

    protected void initialise(GL3 gl) {

        light = new Light(gl);
        light.setCamera(camera);

        buildSceneGraph(gl);

    }

    private void buildSceneGraph(GL3 gl) {
        room = new Room(gl, light, camera);
        hand = new Hand(gl, light, camera, handConfiguration);

        TransformNode handTransform = new TransformNode("",
                Mat4Transform.scale(1.0f, 1.0f, 1.0f));

        scene.addChild(room.getRoot());
            room.getAnchor().addChild(handTransform);
                handTransform.addChild(hand.getRoot());

        scene.update();
    }

    protected void render(GL3 gl) {
        Mat4 perspective = Mat4Transform.perspective(45, aspect);
        hand.setPerspective(perspective);
        room.setPerspective(perspective);
        hand.applyFingerBend();
        scene.draw(gl);
    }

    protected void updatePerspectiveMatrices() {

    }

    protected void disposeMeshes(GL3 gl) {

    }
}
