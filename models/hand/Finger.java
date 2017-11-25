package models.hand;
import engine.*;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Finger extends Model {


    // Textures
    int[] rustTexture, rustTextureSpecular, metalTexture;

    public Finger(WorldConfiguration worldConfig) {
        super(worldConfig);
    }

    // OpenGL loaded
    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph(gl);
    }

    private void loadTextures(GL3 gl) {
        // Textures
        rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");
    }

    private void loadMeshes(GL3 gl) {
        // Meshes
        segment = new Sphere(gl, rustTexture, rustTextureSpecular);
        joint = new Sphere(gl, metalTexture, metalTexture);

        registerMeshes(new Mesh[] { segment, joint });
    }

    private Mesh segment, joint;
    private TransformNode lowerJointRotation, middleJointRotation, upperJointRotation, fingerTransform;
    private final float MAX_BEND = 120;

    public void bend(float amount) {
        bend(amount, amount, amount);
    }

    public void turn(float x, float y, float z) {
        fingerTransform.setTransform(
            Mat4.multiplyVariable(
                Mat4Transform.rotateAroundZ(z * 90),
                Mat4Transform.rotateAroundY(y * 90),
                Mat4Transform.rotateAroundX(x * 90)
            )
        );

        getRoot().update();
    }


    public void bend(float joint1, float joint2, float joint3) {
        float degrees1 = joint1 * MAX_BEND;
        float degrees2 = joint2 * MAX_BEND;
        float degrees3 = joint3 * MAX_BEND;

        lowerJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees1));
        middleJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees2));
        upperJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees3));

        getRoot().update();
    }

    private void buildSceneGraph(GL3 gl) {
        SGNode root = new NameNode("Finger");

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
        TransformNode lowerJointTranslation = TransformNode.createTranslationNode(0.0f, 0.0f, 0.0f);
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

        fingerTransform = TransformNode.createRotateAroundXNode(0);

        root.addChild(fingerTransform);
            fingerTransform.addChild(lowerJointTranslation);
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
        setRoot(root);

    }

}
