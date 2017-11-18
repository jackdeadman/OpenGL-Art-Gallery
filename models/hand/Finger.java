package models.hand;
import engine.*;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Finger extends Model {


    public Finger(GL3 gl, Light light, Camera camera, Mat4 baseTransformation) {
        super(camera, light);

        createSceneGraph(gl, baseTransformation);
    }

    private Mesh segment, joint;
    private TransformNode lowerJointRotation, middleJointRotation, upperJointRotation;

    public void bend(float amount) {
        float degrees = amount * 90;
        lowerJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees));
        middleJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees));
        upperJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees));
        root.update();
    }

    private void createSceneGraph(GL3 gl, Mat4 baseTransformation) {

        root = new NameNode("finger");

        // Textures
        int[] rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        int[] rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        int[] metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");

        // Meshes
        segment = new Sphere(gl, rustTexture, rustTextureSpecular);
        joint = new Sphere(gl, metalTexture, metalTexture);

        segment.setLight(light);
        segment.setCamera(camera);
        joint.setLight(light);
        joint.setCamera(camera);

        // MeshNodes
        MeshNode segmentShape1 = new MeshNode("Sphere (lowerSegment)", segment);
        MeshNode segmentShape2 = new MeshNode("Sphere (lowerSegment)", segment);
        MeshNode segmentShape3 = new MeshNode("Sphere (lowerSegment)", segment);
        MeshNode jointShape1 = new MeshNode("Sphere (lowerJoint)", joint);
        MeshNode jointShape2 = new MeshNode("Sphere (lowerJoint)", joint);
        MeshNode jointShape3 = new MeshNode("Sphere (lowerJoint)", joint);

        // Name nodes
        NameNode lowerJoint = new NameNode("lowerJoint");
        NameNode middleJoint = new NameNode("middleJoint");
        NameNode upperJoint = new NameNode("upperJoint");

        NameNode lowerFinger = new NameNode("lowerFinger");
        NameNode middleFinger = new NameNode("middleFinger");
        NameNode upperFinger = new NameNode("upperFinger");

        // Transformations
        TransformNode fingerTranformation = new TransformNode("1", baseTransformation);

        TransformNode lowerJointTranslation = new TransformNode("2", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode lowerJointScale = new TransformNode("3", Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        lowerJointRotation = new TransformNode("3", Mat4Transform.rotateAroundX(0));

        TransformNode lowerSegmentTranslation = new TransformNode("4", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode lowerSegmentScale = new TransformNode("5", Mat4Transform.scale(0.6f, 1.5f, 0.6f));

        TransformNode middleJointTranslation = new TransformNode("6", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode middleJointScale = new TransformNode("7", Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        middleJointRotation = new TransformNode("3", Mat4Transform.rotateAroundX(0));

        TransformNode middleSegmentTranslation = new TransformNode("8", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode middleSegmentScale = new TransformNode("9", Mat4Transform.scale(0.5f, 1.2f, 0.5f));

        TransformNode upperJointTranslation = new TransformNode("6", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode upperJointScale = new TransformNode("7", Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        upperJointRotation = new TransformNode("3", Mat4Transform.rotateAroundX(0));

        TransformNode upperSegmentTranslation = new TransformNode("8", Mat4Transform.translate(0f, 0.3f, 0f));
        TransformNode upperSegmentScale = new TransformNode("9", Mat4Transform.scale(0.5f, 0.8f, 0.5f));

        /*
        Finger
            Move joint
                Scale Joint
                    Joint
                Move segment
                    Scale segment
                        segment
                    Move joint
                        Scale joint
                            joint

        */

        root.addChild(fingerTranformation);
            fingerTranformation.addChild(lowerJointTranslation);
                lowerJointTranslation.addChild(lowerJointRotation);
                    lowerJointRotation.addChild(lowerJointScale);
                    lowerJointScale.addChild(jointShape1);

                lowerJointRotation.addChild(lowerSegmentTranslation);
                    lowerSegmentTranslation.addChild(lowerSegmentScale);
                    lowerSegmentScale.addChild(segmentShape1);

                    lowerSegmentTranslation.addChild(middleJointTranslation);
                        middleJointTranslation.addChild(middleJointRotation);
                        middleJointRotation.addChild(middleJointScale);
                        middleJointScale.addChild(jointShape2);

                        middleJointRotation.addChild(middleSegmentTranslation);
                            middleSegmentTranslation.addChild(middleSegmentScale);
                            middleSegmentScale.addChild(segmentShape2);

                            middleSegmentTranslation.addChild(upperJointTranslation);
                                upperJointTranslation.addChild(upperJointRotation);
                                upperJointRotation.addChild(upperJointScale);
                                upperJointScale.addChild(jointShape3);

                                upperJointRotation.addChild(upperSegmentTranslation);
                                    upperSegmentTranslation.addChild(upperSegmentScale);
                                    upperSegmentScale.addChild(segmentShape3);



        // Build scene graph
        // root.addChild(fingerTranformation);
        //   fingerTranformation.addChild(lowerJointTransformation);
        //     lowerJointTransformation.addChild(lowerJointShape);
        //       lowerJointTransformation.addChild(lowerSegmentTransformation);
        //         lowerSegmentTransformation.addChild(lowerSegmentShape);

        root.update();

    }

    /*
    private void createSceneGraph2(GL3 gl, Mat4 baseTransformation) {
        root = new NameNode("finger");
        NameNode lowerJoint = new NameNode("lowerJoint");
        NameNode middleJoint = new NameNode("middleJoint");
        NameNode upperJoint = new NameNode("upperJoint");

        NameNode lowerFinger = new NameNode("lowerFinger");
        NameNode middleFinger = new NameNode("middleFinger");
        NameNode upperFinger = new NameNode("upperFinger");

        TransformNode lowerJointTransformation = new TransformNode("foo", Mat4Transform.scale(1, 1, 1));

        Mat4 trans = Mat4.multiplyVariable(
            Mat4Transform.translate(0f, 0.5f, 0f),
            Mat4Transform.scale(0.5f, 2f, 0.5f)
        );
        TransformNode lowerFingerTransformation = new TransformNode("foo", trans);

        TransformNode baseT = new TransformNode("foo", baseTransformation);

        int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");

        lowerFingerShape = new Sphere(gl, textureId1, textureId2);
        lowerJointShape = new Sphere(gl, textureId3, textureId2);

        lowerFingerShape.setLight(light);
        lowerFingerShape.setCamera(camera);

        lowerJointShape.setLight(light);
        lowerJointShape.setCamera(camera);

        TransformNode robotTranslate = new TransformNode("foo", Mat4Transform.scale(1, 1, 1));

        // Make scene graph
        root.addChild(baseT);
          baseT.addChild(robotTranslate);
            robotTranslate.addChild(lowerJoint);
              lowerJoint.addChild(lowerJointTransformation);
                lowerJointTransformation.addChild(new MeshNode("sxnj", lowerJointShape));
              lowerJoint.addChild(lowerFinger);
                lowerFinger.addChild(lowerFingerTransformation);
                    lowerFingerTransformation.addChild(new MeshNode("sxnj", lowerFingerShape));
                lowerFingerTransformation.addChild(new MeshNode("sxnj", lowerJointShape));

        root.update();
    }
*/

    public void setPerspective(Mat4 perspective) {
        segment.setPerspective(perspective);
        joint.setPerspective(perspective);
    }


    public void render(GL3 gl, Mat4 perspective) {
        segment.setPerspective(perspective);
        joint.setPerspective(perspective);
        // lowerFingerShape.setPerspective(perspective);

        root.draw(gl);
        // lower.setModelMatrix(
        //     Mat4.multiply(
        //         base,
        //         Mat4Transform.scale(1, lowerSize, 1))
        // );
        //
        // lower.setModelMatrix(
        //     Mat4.multiply(
        //         base,
        //         Mat4Transform.translate(1, lowerSize, 1))
        // );
        //
        // lower.setPerspective(perspective);
        // lower.render(gl);
        //
        // knuckle.setPerspective(perspective);
        // knuckle.render(gl);
    }
}
