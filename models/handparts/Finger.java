package models.handparts;
import engine.*;
import engine.render.*;
import engine.utils.*;
import engine.scenegraph.*;
import galleryscene.shaderprograms.OneTextureShader;
import galleryscene.shaderprograms.SpecularShader;
import meshes.*;
import gmaths.*;

import com.jogamp.opengl.*;

import javax.xml.soap.Text;

public class Finger extends Model {
    private Model ring;
    private boolean hasRing;

    // Textures
    private int[] rustTexture, rustTextureSpecular, metalTexture, mainMetal, mainMetalSpecular;

    public Finger(WorldConfiguration worldConfig, boolean hasRing) {
        super(worldConfig);
        this.hasRing = hasRing;
        loadModels();
    }

    private void loadModels() {
        if (hasRing) {
            ring = new Ring(worldConfig);
            registerModel(ring);
        }
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
        rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/steel3_spec.jpg");
        metalTexture = TextureLibrary.loadTexture(gl, "textures/steel3.jpg");
        mainMetal = TextureLibrary.loadTexture(gl, "textures/green.jpg");
        mainMetalSpecular = TextureLibrary.loadTexture(gl, "textures/green_spec.jpg");
    }

    private void loadMeshes(GL3 gl) {
        // Meshes
        segment = new SphereNew(gl, new OneTextureShader(gl, mainMetal));
        joint = new Sphere(gl, metalTexture, rustTextureSpecular);

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

        if (hasRing) {
            middleJointRotation.addChild(ring.getRoot());
        }

        root.update();
        setRoot(root);

    }

}
