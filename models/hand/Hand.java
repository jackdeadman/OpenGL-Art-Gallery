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

public class Hand extends Model {

    private Finger finger1;
    private Finger finger2;
    private Finger finger3;
    private Finger finger4;
    private Mesh upperPalm;

    public Hand(GL3 gl, Light light, Camera camera) {
        super(camera, light);
        createSceneGraph(gl);
    }

    private void createSceneGraph(GL3 gl) {
        finger1 = new Finger(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(0f, 0.0f, 0.0f),
            Mat4Transform.scale(1f, 0.6f, 1f)
        ));

        finger2 = new Finger(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(0.0f, 0.0f, 0.0f),
            Mat4Transform.scale(1f, 0.9f, 1f)
        ));

        finger3 = new Finger(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(0, 0, 0),
            Mat4Transform.scale(1f, 1f, 1f)
        ));

        finger4 = new Finger(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(0, 0, 0),
            Mat4Transform.scale(1f, 0.85f, 1f)
        ));

        int[] rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        int[] rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");

        upperPalm = new Sphere(gl, rustTexture, rustTextureSpecular);
        NameNode upperPalmName = new NameNode("upperPalm");
        MeshNode upperPalmShape = new MeshNode("dsnj", upperPalm);

        TransformNode finger1Transform = new TransformNode("snajn", Mat4Transform.translate(0f, 0.0f, 0.0f));
        TransformNode finger2Transform = new TransformNode("snajn", Mat4Transform.translate(0.7f, 0.0f, 0.0f));
        TransformNode finger3Transform = new TransformNode("snajn", Mat4Transform.translate(0.7f, 0, 0));
        TransformNode finger4Transform = new TransformNode("snajn", Mat4Transform.translate(0.7f, 0, 0));

        TransformNode fingerOffset = new TransformNode("", Mat4Transform.translate(-1.1f, 0.0f, 0.0f));


        TransformNode upperPalmScale = new TransformNode("sanj", Mat4Transform.scale(3.5f, 1f, 1f));

        upperPalm.setCamera(camera);
        upperPalm.setLight(light);


        // finger1.bend(0.2f);

        TransformNode handTranslate = new TransformNode("", Mat4Transform.translate(0, 2, 0));

        root = new NameNode("Hand");
        root.addChild(handTranslate);
        handTranslate.addChild(upperPalmName);
            upperPalmName.addChild(fingerOffset);

            fingerOffset.addChild(finger1.getRoot());
            fingerOffset.addChild(finger2Transform);
                finger2Transform.addChild(finger2.getRoot());
            finger2Transform.addChild(finger3Transform);
                finger3Transform.addChild(finger3.getRoot());
            finger3Transform.addChild(finger4Transform);
                finger4Transform.addChild(finger4.getRoot());

            upperPalmName.addChild(upperPalmScale);
                upperPalmScale.addChild(upperPalmShape);

        root.update();
    }

    protected double getSeconds() {
      return System.currentTimeMillis()/1000.0;
    }

    public void render(GL3 gl, Mat4 perspective) {
        finger1.setPerspective(perspective);
        finger2.setPerspective(perspective);
        finger3.setPerspective(perspective);
        finger4.setPerspective(perspective);
        upperPalm.setPerspective(perspective);
        finger1.bend(0.5f*((float)Math.sin(getSeconds()*3)+1));
        finger2.bend(0.5f*((float)Math.sin(getSeconds()*3)+1));
        finger3.bend(0.5f*((float)Math.sin(getSeconds()*3)+1));
        finger4.bend(0.5f*((float)Math.sin(getSeconds()*3)+1));
        root.draw(gl);
        // finger2.bend(1f);
        // finger3.bend(1f);


        //
        // finger1.render(gl, perspective);
        // finger2.render(gl, perspective);
        // finger3.render(gl, perspective);
        // finger4.render(gl, perspective);
    }

/*
    SGNode hand = new NameNode("Hand");
    Mesh palm, bottom, middle, top;

    GL3 gl;
    Light light;
    Camera camera;
    float aspect;



    public Hand(GL3 gl, Light light, Camera camera, float aspect) {
        this.light = light;
        this.camera = camera;
        this.aspect = aspect;

        createSceneGraph(gl);
    }

    private SGNode createFinger(GL3 gl, int[] tex1, int[] tex2) {
        bottom = new Cube(gl, tex1, tex2);
        middle = new Cube(gl, tex1, tex2);
        top = new Cube(gl, tex1, tex2);

        bottom.setLight(light);
        bottom.setCamera(camera);

        middle.setLight(light);
        middle.setCamera(camera);

        top.setLight(light);
        top.setCamera(camera);

        NameNode name = new NameNode("Finger");

        Mat4 bottomTransform = Mat4.multiply(
            Mat4Transform.scale(0.25f, 1f, 1f),
            Mat4Transform.translate(0f, 1f, 0f)
        );

        MeshNode bottomShape = new MeshNode("baz", bottom);
        TransformNode bottomTransformNode = new TransformNode("bar", bottomTransform);

        name.addChild(bottomTransformNode);
            bottomTransformNode.addChild(bottomShape);

        return name;

    }

    private void createSceneGraph(GL3 gl) {
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
        int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");

        palm = new Cube(gl, textureId3, textureId4);

        palm.setLight(light);
        palm.setCamera(camera);

        Mat4 palmTransform = Mat4.multiply(
            Mat4Transform.scale(3, 3, 1),
            Mat4Transform.translate(0, 0.5f, 0)
        );

        MeshNode palmShape = new MeshNode("Cube(body)", palm);
        TransformNode palmTransformNode = new TransformNode("foo", palmTransform);
        SGNode finger1 = createFinger(gl, textureId3, textureId4);

        hand.addChild(palmTransformNode);
            palmTransformNode.addChild(palmShape);
            palmTransformNode.addChild(finger1);


        hand.update();
    }

    /*

    // make scene graph
        robot.addChild(robotMoveTranslate);
          robotMoveTranslate.addChild(robotTranslate);
            robotTranslate.addChild(body);
              body.addChild(bodyTransform);
                bodyTransform.addChild(bodyShape);
              body.addChild(head);
                head.addChild(headTransform);
                headTransform.addChild(headShape);
              body.addChild(leftarm);
                leftarm.addChild(leftArmTranslate);
                leftArmTranslate.addChild(leftArmRotate);
                leftArmRotate.addChild(leftArmScale);
                leftArmScale.addChild(leftArmShape);
              body.addChild(rightarm);
                rightarm.addChild(rightArmTranslate);
                rightArmTranslate.addChild(rightArmRotate);
                rightArmRotate.addChild(rightArmScale);
                rightArmScale.addChild(rightArmShape);
              body.addChild(leftleg);
                leftleg.addChild(leftlegTransform);
                leftlegTransform.addChild(leftLegShape);
              body.addChild(rightleg);
                rightleg.addChild(rightlegTransform);
                rightlegTransform.addChild(rightLegShape);




    public void render(Mat4 perspective, GL3 gl) {
        palm.setPerspective(perspective);
        bottom.setPerspective(perspective);
        middle.setPerspective(perspective);
        top.setPerspective(perspective);
        hand.draw(gl);
    }
*/
}
