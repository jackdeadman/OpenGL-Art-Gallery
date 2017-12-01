package models.handparts;
import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;

public class Thumb extends Model {

    int[] rustTexture, rustTextureSpecular, metalTexture;
    private TransformNode thumbTransform;

    public Thumb(WorldConfiguration worldConfig) {
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
        rustTexture = TextureLibrary.loadTexture(gl, "textures/floor_2.jpg");
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
    private TransformNode lowerJointRotation, middleJointRotation, upperJointRotation;

    public void bend(float amount) {
        bend(amount, amount);
    }

    public void bend(float amount1, float amount2) {
        float degrees1 = amount1 * 90;
        float degrees2 = amount2 * 90;
        lowerJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees1));
        middleJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees2));
        getRoot().update();
    }

    public void turn(float x, float y, float z) {
        thumbTransform.setTransform(
            Mat4.multiplyVariable(
                Mat4Transform.rotateAroundZ(z * 90),
                Mat4Transform.rotateAroundY(y * 90),
                Mat4Transform.rotateAroundX(x * 90)
            )
        );
        getRoot().update();
    }

    private void buildSceneGraph(GL3 gl) {

        SGNode root = new NameNode("finger");

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


        TransformNode lowerJointTranslation = new TransformNode("2", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode lowerJointScale = new TransformNode("3", Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        lowerJointRotation = new TransformNode("3", Mat4Transform.rotateAroundX(0));

        TransformNode lowerSegmentTranslation = new TransformNode("4", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode lowerSegmentScale = new TransformNode("5", Mat4Transform.scale(0.6f, 1.5f, 0.6f));

        TransformNode middleJointTranslation = new TransformNode("6", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode middleJointScale = new TransformNode("7", Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        middleJointRotation = new TransformNode("3", Mat4Transform.rotateAroundX(0));

        TransformNode middleSegmentTranslation = new TransformNode("8", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode middleSegmentScale = new TransformNode("9", Mat4Transform.scale(0.5f, 1.0f, 0.5f));

        TransformNode upperJointTranslation = new TransformNode("6", Mat4Transform.translate(0f, 0.5f, 0f));
        TransformNode upperJointScale = new TransformNode("7", Mat4Transform.scale(0.5f, 0.5f, 0.5f));
        upperJointRotation = new TransformNode("3", Mat4Transform.rotateAroundX(0));

        TransformNode upperSegmentTranslation = new TransformNode("8", Mat4Transform.translate(0f, 0.3f, 0f));
        TransformNode upperSegmentScale = new TransformNode("9", Mat4Transform.scale(0.5f, 0.8f, 0.5f));

        thumbTransform = new TransformNode("", Mat4Transform.scale(1.0f, 1.0f, 1.0f));

        root.addChild(thumbTransform);
        thumbTransform.addChild(lowerJointTranslation);
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
        root.update();
        setRoot(root);

    }

}
