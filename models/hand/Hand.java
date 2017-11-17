package models.hand;
import engine.*;
import meshes.*;
import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Hand {

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

    /*

    Palm
        finger
            bottom
            middle
            up

    */

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


    */

    public void render(Mat4 perspective, GL3 gl) {
        palm.setPerspective(perspective);
        bottom.setPerspective(perspective);
        middle.setPerspective(perspective);
        top.setPerspective(perspective);
        hand.draw(gl);
    }

}
