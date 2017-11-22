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


    final float FINGER1_SIZE = 0.8f;
    final float FINGER2_SIZE = 0.95f;
    final float FINGER3_SIZE = 1.0f;
    final float FINGER4_SIZE = 0.95f;
    final float FINGER_OFFSET = 0.7f;

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
        TransformNode transformFinger1 = createFingerTransform(0, -0.0f, FINGER1_SIZE);
        TransformNode transformFinger2 = createFingerTransform(FINGER_OFFSET, 0.28f, FINGER1_SIZE);
        TransformNode transformFinger3 = createFingerTransform(FINGER_OFFSET * 2.0f, 0.28f, FINGER1_SIZE);
        TransformNode transformFinger4 = createFingerTransform(FINGER_OFFSET * 3.0f, -0.1f, FINGER1_SIZE);

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

        TransformNode moveFingers = new TransformNode("Translate()", Mat4Transform.translate(-1f, 0.0f, 0.0f));

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
            // transformFinger1.addChild(finger1.getRoot());

        //
        // palm.getAnchor().addChild(fingerOffset);
        //
        //         fingerOffset.addChild(finger1.getRoot());
        //         fingerOffset.addChild(finger2Transform);
        //             finger2Transform.addChild(finger2.getRoot());
        //         finger2Transform.addChild(finger3Transform);
        //             finger3Transform.addChild(finger3.getRoot());
        //         finger3Transform.addChild(finger4Transform);
        //             finger4Transform.addChild(finger4.getRoot());
        //
        //
        //     palm.getRoot().addChild(placeThumb);
        //         placeThumb.addChild(thumb.getRoot());
    }
/*
    private void createSceneGraph2(GL3 gl) {

        float finger1Size = 0.8f;
        float finger2Size = 0.95f;
        float finger3Size = 1.0f;
        float finger4Size = 0.95f;

        finger1 = new Finger(gl, light, camera);
        finger2 = new Finger(gl, light, camera);
        finger3 = new Finger(gl, light, camera);
        finger4 = new Finger(gl, light, camera);

        palm = new Palm(gl, light, camera);
        thumb = new Thumb(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(0, 0, 0),
            Mat4Transform.scale(1f, 1f, 1f)
        ));

        int[] rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        int[] rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");

        // upperPalm = new Sphere(gl, rustTexture, rustTextureSpecular);
        NameNode upperPalmName = new NameNode("UpperPalm");
        // MeshNode upperPalmShape = new MeshNode("dsnj", upperPalm);
        String fingerSpacingString = "Translate(0.7, 0.0, 0.0)";
        Mat4 fingerSpacing = Mat4Transform.translate(0.7f, 0.0f, 0.0f);


        TransformNode finger1Transform = new TransformNode(
                        "Scale(1.0f, " + finger1Size + ", 1.0)",
                        Mat4Transform.scale(1.0f, finger1Size, 1.0f));

        TransformNode finger2Transform = new TransformNode(
            "Translate(0.0, -0.2, 0.0);" + fingerSpacingString + ";Scale(1.0, " + fingerSpacing + ", 1.0)",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0.0f, -0.2f, 0.0f),
                fingerSpacing,
                Mat4Transform.scale(1.0f, finger1Size, 1.0f)
            )
        );


        TransformNode finger3Transform = new TransformNode("snajn",
                Mat4Transform.translate(0.7f, 0, 0));
        TransformNode finger4Transform = new TransformNode("snajn", Mat4Transform.translate(0.7f, 0, 0));

        TransformNode fingerOffset = new TransformNode("", Mat4Transform.translate(-1.1f, 0.0f, 0.0f));

        TransformNode upperPalmScale = new TransformNode("sanj", Mat4Transform.scale(3.5f, 1f, 1f));


        TransformNode handTranslate = new TransformNode("", Mat4Transform.translate(0f, 2.5f, 0f));
        TransformNode placeThumb = new TransformNode("", Mat4.multiplyVariable(
            Mat4Transform.translate(1.0f, -0.7f, 0.0f),
            Mat4Transform.rotateAroundZ(-45f),
            Mat4Transform.scale(1.2f, 0.8f, 1.0f)
        ));


        root = new NameNode("Hand");
        root.addChild(handTranslate);
        handTranslate.addChild(palm.getAnchor());
        handTranslate.addChild(palm.getRoot());
            palm.getAnchor().addChild(fingerOffset);

                fingerOffset.addChild(finger1.getRoot());
                fingerOffset.addChild(finger2Transform);
                    finger2Transform.addChild(finger2.getRoot());
                finger2Transform.addChild(finger3Transform);
                    finger3Transform.addChild(finger3.getRoot());
                finger3Transform.addChild(finger4Transform);
                    finger4Transform.addChild(finger4.getRoot());


            palm.getRoot().addChild(placeThumb);
                placeThumb.addChild(thumb.getRoot());

        root.update();
    }

    protected double getSeconds() {
      return System.currentTimeMillis()/1000.0;
    }
*/

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

        finger1.bend(handConfiguration.finger1Bend);
        finger2.bend(handConfiguration.finger2Bend);
        finger3.bend(handConfiguration.finger3Bend);
        finger4.bend(handConfiguration.finger4Bend);
        thumb.bend(handConfiguration.thumbBend);

        root.draw(gl);
    }

}
