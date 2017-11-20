package models.hand;
import engine.*;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Finger extends Model {


    public Finger(GL3 gl, Light light, Camera camera) {
        super(camera, light);

        createSceneGraph(gl);
    }

    private Mesh segment, joint;
    private TransformNode lowerJointRotation, middleJointRotation, upperJointRotation;
    private final float MAX_BEND = 90;

    public void bend(float amount) {
        bend(amount, amount, amount);
    }


    public void bend(float joint1, float joint2, float joint3) {
        float degrees1 = joint1 * MAX_BEND;
        float degrees2 = joint2 * MAX_BEND;
        float degrees3 = joint3 * MAX_BEND;

        lowerJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees1));
        middleJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees2));
        upperJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees3));

        root.update();
    }

    private void createSceneGraph(GL3 gl) {

        root = new NameNode("Finger");

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
        MeshNode segmentShape2 = new MeshNode("Sphere (middleSegment)", segment);
        MeshNode segmentShape3 = new MeshNode("Sphere (upperSegment)", segment);
        MeshNode jointShape1 = new MeshNode("Sphere (lowerJoint)", joint);
        MeshNode jointShape2 = new MeshNode("Sphere (middleJoint)", joint);
        MeshNode jointShape3 = new MeshNode("Sphere (upperJoint)", joint);

        // Name nodes
        NameNode lowerJoint = new NameNode("lowerJoint");
        NameNode middleJoint = new NameNode("middleJoint");
        NameNode upperJoint = new NameNode("upperJoint");

        NameNode lowerFinger = new NameNode("lowerFinger");
        NameNode middleFinger = new NameNode("middleFinger");
        NameNode upperFinger = new NameNode("upperFinger");

        // Size the joint part will be scaled to
        float jointSize = 0.5f;
        float fingerRadius = 0.6f;


        // TransformNode lowerJointTranslation = new TransformNode("translate()", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode lowerJointTranslation = TransformNode.createTranslationNode(0.0f, jointSize, 0.0f);
        TransformNode lowerJointScale = TransformNode.createScaleNode(jointSize, jointSize, jointSize);
        // Default to 0, this will be later updated to bend finder
        lowerJointRotation = TransformNode.createRotateAroundXNode(0);



        TransformNode lowerSegmentTranslation = TransformNode.createTranslationNode(0f, jointSize, 0f);
        TransformNode lowerSegmentScale = TransformNode.createScaleNode(fingerRadius, 1.5f, fingerRadius);


        TransformNode middleJointTranslation = TransformNode.createTranslationNode(0.0f, jointSize, 0.0f);
        TransformNode middleJointScale = TransformNode.createScaleNode(jointSize, jointSize, jointSize);

        middleJointRotation = TransformNode.createRotateAroundXNode(0);

        TransformNode middleSegmentTranslation = TransformNode.createTranslationNode(0.0f, jointSize, 0.0f);
        TransformNode middleSegmentScale = TransformNode.createScaleNode(fingerRadius, 1.2f, fingerRadius);

        TransformNode upperJointTranslation = TransformNode.createTranslationNode(0.0f, jointSize, 0.0f);
        TransformNode upperJointScale = TransformNode.createScaleNode(jointSize, jointSize, jointSize);

        upperJointRotation = TransformNode.createRotateAroundXNode(0);

        TransformNode upperSegmentTranslation = TransformNode.createTranslationNode(0.0f, 0.3f, 0.0f);
        TransformNode upperSegmentScale = TransformNode.createScaleNode(0.5f, 0.8f, 0.5f);

        root.addChild(lowerJointTranslation);
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


        root.update();

    }



    public void setPerspective(Mat4 perspective) {
        segment.setPerspective(perspective);
        joint.setPerspective(perspective);
    }


    public void render(GL3 gl, Mat4 perspective) {
        segment.setPerspective(perspective);
        joint.setPerspective(perspective);

        root.draw(gl);
    }
}
