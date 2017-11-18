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

    public Hand(GL3 gl, Light light, Camera camera) {
        super(camera, light);
        finger1 = new Finger(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(0f, 2.0f, 0.0f),
            Mat4Transform.scale(1f, 0.6f, 1f)
        ));

        finger2 = new Finger(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(0.7f, 2.0f, 0.0f),
            Mat4Transform.scale(1f, 0.9f, 1f)
        ));

        finger3 = new Finger(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(1.4f, 2.0f, 0.0f),
            Mat4Transform.scale(1f, 1f, 1f)
        ));

        finger4 = new Finger(gl, light, camera, Mat4.multiply(
            Mat4Transform.translate(2.1f, 2.0f, 0.0f),
            Mat4Transform.scale(1f, 0.85f, 1f)
        ));


        // finger2 = new Finger(gl, light, camera, 3, 2, 1);
        // finger3 = new Finger(gl, light, camera, 3, 2, 1);
        // finger4 = new Finger(gl, light, camera, 3, 2, 1);
    }

    public void render(GL3 gl, Mat4 perspective) {

        finger1.bend(0.4f);


        finger1.render(gl, perspective);
        finger2.render(gl, perspective);
        finger3.render(gl, perspective);
        finger4.render(gl, perspective);
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
