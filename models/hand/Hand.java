package models.hand;
import engine.*;
import meshes.*;
import models.*;
import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import scene.*;

public class Hand extends Model {

    private Finger finger1;
    private Finger finger2;
    private Finger finger3;
    private Finger finger4;
    private Palm palm;
    private Thumb thumb;
    private HandConfiguration handConfiguration;

    public Hand(GL3 gl, Light light, Camera camera, HandConfiguration handConfiguration) {
        super(camera, light);
        this.handConfiguration = handConfiguration;
        createSceneGraph(gl);
    }

    public void applyFingerBend() {
        float[][] fingerValues = handConfiguration.getFingerValues();
        finger1.bend(fingerValues[0][0], fingerValues[0][1], fingerValues[0][2]);
        finger1.turn(fingerValues[0][3], fingerValues[0][4], fingerValues[0][5]);

        finger2.bend(fingerValues[1][0], fingerValues[1][1], fingerValues[1][2]);
        finger2.turn(fingerValues[1][3], fingerValues[1][4], fingerValues[1][5]);

        finger3.bend(fingerValues[2][0], fingerValues[2][1], fingerValues[2][2]);
        finger3.turn(fingerValues[2][3], fingerValues[2][4], fingerValues[2][5]);

        finger4.bend(fingerValues[3][0], fingerValues[3][1], fingerValues[3][2]);
        finger4.turn(fingerValues[3][3], fingerValues[3][4], fingerValues[3][5]);
    }


    final float FINGER1_SIZE = 0.7f;
    final float FINGER2_SIZE = 0.95f;
    final float FINGER3_SIZE = 1.0f;
    final float FINGER4_SIZE = 0.95f;
    final float FINGER_OFFSET = 0.6f;

    private TransformNode createFingerTransform(float offsetX, float offsetY, float fingerSize) {
        return new TransformNode(
            "Translate(" + offsetX + ", " + offsetY + ", 0.0); Scale(1.0, " + fingerSize + ", 1.0)",
            Mat4.multiply(
                Mat4Transform.translate(offsetX, offsetY, 0.0f),
                Mat4Transform.scale(1.0f, fingerSize, 1.0f)
            )
        );
    }

    private void createSceneGraph(GL3 gl) {
        // Load textures
        int[] rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        int[] rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");

        // Create parts
        finger1 = new Finger(gl, light, camera);
        finger2 = new Finger(gl, light, camera);
        finger3 = new Finger(gl, light, camera);
        finger4 = new Finger(gl, light, camera);

        palm = new Palm(gl, light, camera);
        thumb = new Thumb(gl, light, camera);

        // Transform for each finger
        // (offsetX, offsetY, fingerSize)
        TransformNode transformFinger1 = createFingerTransform(0, -0.1f, FINGER1_SIZE);
        TransformNode transformFinger2 = createFingerTransform(FINGER_OFFSET, 0.1f, FINGER2_SIZE);
        TransformNode transformFinger3 = createFingerTransform(FINGER_OFFSET * 2.0f, 0.1f, FINGER3_SIZE);
        TransformNode transformFinger4 = createFingerTransform(FINGER_OFFSET * 3.0f, -0.1f, FINGER4_SIZE);

        // Place the thumb
        TransformNode transformThumb = new TransformNode(
            "Translate(1.0, -0.7, 0.0); rotateAroundZ(-45 degrees); Scale(1.2, 0.8, 1.0)",
            Mat4.multiplyVariable(
                Mat4Transform.translate(1.0f, -0.7f, 0.0f),
                Mat4Transform.rotateAroundZ(-45f),
                Mat4Transform.scale(1.2f, 0.8f, 1.0f)
            )
        );

        // Move fingers together


        // Create the scene graph
        root = new NameNode("Hand");
        NameNode palmName= new NameNode("Palm");
        NameNode fingersName= new NameNode("Fingers");

        TransformNode moveFingers = new TransformNode("Translate()", Mat4Transform.translate(-0.9f, 0.5f, 0.0f));

        // Temp
        TransformNode moveHand = new TransformNode("", Mat4Transform.translate(0.0f, 2.0f, 0.0f));

        root.addChild(moveHand);
            moveHand.addChild(palm.getRoot());

            palm.getRoot().addChild(transformThumb);
                transformThumb.addChild(thumb.getRoot());

            palm.getAnchor().addChild(fingersName);
                fingersName.addChild(moveFingers);
                    moveFingers.addChild(transformFinger1);
                        transformFinger1.addChild(finger1.getRoot());
                    moveFingers.addChild(transformFinger2);
                        transformFinger2.addChild(finger2.getRoot());
                    moveFingers.addChild(transformFinger3);
                        transformFinger3.addChild(finger3.getRoot());
                    moveFingers.addChild(transformFinger4);
                        transformFinger4.addChild(finger4.getRoot());
        root.update();
    }

    public void setPerspective(Mat4 perspective) {
        finger1.setPerspective(perspective);
        finger2.setPerspective(perspective);
        finger3.setPerspective(perspective);
        finger4.setPerspective(perspective);
        thumb.setPerspective(perspective);
        palm.setPerspective(perspective);
    }

    public void render(GL3 gl, Mat4 perspective) {
        finger1.setPerspective(perspective);
        finger2.setPerspective(perspective);
        finger3.setPerspective(perspective);
        finger4.setPerspective(perspective);
        thumb.setPerspective(perspective);
        palm.setPerspective(perspective);

        root.draw(gl);
    }

}
