package models;
import engine.*;
import gmaths.*;
import com.jogamp.opengl.*;

import galleryscene.*;
import models.handparts.*;

import engine.scenegraph.*;
import engine.render.*;

public class Hand extends Model {

    private Finger finger1, finger2, finger3, finger4;
    private Palm palm;
    private Thumb thumb;
    private Ring ring;
    private HandConfiguration handConfiguration;

    private TransformNode rotateHand;

    // Finger details
    public final float FINGER1_SIZE = 0.7f;
    public final float FINGER2_SIZE = 0.95f;
    public final float FINGER3_SIZE = 1.0f;
    public final float FINGER4_SIZE = 0.95f;

    // Gap between each of the fingers
    final float FINGER_OFFSET = 0.6f;

    private int[] rustTexture, rustTextureSpecular;

    public Hand(WorldConfiguration worldConfig, HandConfiguration handConfiguration) {
        super(worldConfig);
        this.handConfiguration = handConfiguration;
        loadModels();
    }

    // OpenGL loaded
    protected void start(GL3 gl) {
        buildSceneGraph(gl);
    }

    public void loadModels() {
        // Create parts of the hand
        finger1 = new Finger(worldConfig);
        finger2 = new Finger(worldConfig);
        finger3 = new Finger(worldConfig);
        finger4 = new Finger(worldConfig);
        ring = new Ring(worldConfig);

        palm = new Palm(worldConfig);
        thumb = new Thumb(worldConfig);

        registerModels(new Model[] {
            finger1, finger2, finger3, finger4, palm, thumb, ring
        });
    }

    public void setConfiguration(HandConfiguration config) {
        handConfiguration = config;
    }

    // Rotate the whole hand
    public void rotate(float x, float y) {
        rotateHand.setTransform(
            Mat4.multiplyVariable(
                Mat4Transform.rotateAroundY(y * 90),
                Mat4Transform.rotateAroundX(x * 90)
            )
        );
        getRoot().update();
    }

    // Updates the joints in the fingers based on the handConfiguration state
    public void applyFingerBend() {
        float[][] fingerValues = handConfiguration.getFingerValues();
        float[] thumbValues = handConfiguration.getThumbValues();
        float[] wristValues = handConfiguration.getWristValues();

        finger1.bend(fingerValues[0][0], fingerValues[0][1], fingerValues[0][2]);
        finger1.turn(fingerValues[0][3], fingerValues[0][4], fingerValues[0][5]);

        finger2.bend(fingerValues[1][0], fingerValues[1][1], fingerValues[1][2]);
        finger2.turn(fingerValues[1][3], fingerValues[1][4], fingerValues[1][5]);

        finger3.bend(fingerValues[2][0], fingerValues[2][1], fingerValues[2][2]);
        finger3.turn(fingerValues[2][3], fingerValues[2][4], fingerValues[2][5]);

        finger4.bend(fingerValues[3][0], fingerValues[3][1], fingerValues[3][2]);
        finger4.turn(fingerValues[3][3], fingerValues[3][4], fingerValues[3][5]);

        thumb.turn(thumbValues[0], thumbValues[1], thumbValues[2]);
        thumb.bend(thumbValues[3], thumbValues[4]);

        rotate(wristValues[0], wristValues[1]);
    }

    // Helper to clean up building the scene graph
    private TransformNode createFingerTransform(float offsetX, float offsetY, float fingerSize) {
        return new TransformNode(
            "Translate(" + offsetX + ", " + offsetY + ", 0.0); Scale(1.0, " + fingerSize + ", 1.0)",
            Mat4.multiply(
                Mat4Transform.translate(offsetX, offsetY, 0.0f),
                Mat4Transform.scale(1.0f, fingerSize, 1.0f)
            )
        );
    }

    protected void buildSceneGraph(GL3 gl) {

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

        // Create the scene graph
        SGNode root = new NameNode("Hand");
        NameNode palmName= new NameNode("Palm");
        NameNode fingersName= new NameNode("Fingers");

        // Move all the fingers together
        TransformNode moveFingers = new TransformNode(
                "Translate(-0.9f, 0.5f, 0.0f)",
                Mat4Transform.translate(-0.9f, 0.5f, 0.0f));

        rotateHand = new TransformNode("Rotate hand", new Mat4(1));

        // Make 0,0 be the bottom of the hand
        TransformNode moveHand = TransformNode.createTranslationNode(0.0f, 2.0f, 0.0f);

        root.addChild(rotateHand);
            rotateHand.addChild(moveHand);
                moveHand.addChild(palm.getRoot());

                palm.getRoot().addChild(transformThumb);
                    transformThumb.addChild(thumb.getRoot());

                // Add fingers to the top of the palm and not the root
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

        // Add the ring to just the 4th finger
        finger4.addRing(ring);

        root.update();
        setRoot(root);
    }

}
