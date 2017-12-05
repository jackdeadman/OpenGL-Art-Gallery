package models.handparts;
import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;

public class Thumb extends Finger {

    private TransformNode thumbTransform;

    public Thumb(WorldConfiguration worldConfig) {
        super(worldConfig);
    }

    public void bend(float amount) {
        bend(amount, amount);
    }

    // Bend the thumb at two joints with 1 degree of freedom for each joint
    // Where amount should be given between -1 and 1
    public void bend(float amount1, float amount2) {
        float degrees1 = amount1 * 90;
        float degrees2 = amount2 * 90;
        lowerJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees1));
        middleJointRotation.setTransform(Mat4Transform.rotateAroundX(degrees2));
        getRoot().update();
    }

    // Turn the thumb with 3 degrees of freedom from the bottom
    // of the thumb
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

    protected void buildSceneGraph(GL3 gl) {

        SGNode root = new NameNode("finger");

        // MeshNodes
        MeshNode segmentShape1 = new MeshNode("Sphere (lowerSegment)", segment);
        MeshNode segmentShape2 = new MeshNode("Sphere (lowerSegment)", segment);
        MeshNode segmentShape3 = new MeshNode("Sphere (lowerSegment)", segment);
        MeshNode jointShape1 = new MeshNode("Sphere (lowerJoint)", joint);
        MeshNode jointShape2 = new MeshNode("Sphere (lowerJoint)", joint);
        MeshNode jointShape3 = new MeshNode("Sphere (lowerJoint)", joint);

        // These are all done seperately because of needing to update the rotation to bend

        TransformNode lowerJointTranslation = TransformNode.createTranslationNode(0f, 0.5f, 0f);
        TransformNode lowerJointScale = TransformNode.createScaleNode(0.5f, 0.5f, 0.5f);
        lowerJointRotation = TransformNode.createRotateAroundXNode(0);

        TransformNode lowerSegmentTranslation = TransformNode.createTranslationNode(0f, 0.5f, 0f);
        TransformNode lowerSegmentScale = TransformNode.createScaleNode(0.6f, 1.5f, 0.6f);

        TransformNode middleJointTranslation = TransformNode.createTranslationNode(0f, 0.5f, 0f);
        TransformNode middleJointScale = TransformNode.createScaleNode(0.5f, 0.5f, 0.5f);
        middleJointRotation = TransformNode.createRotateAroundXNode(0);

        TransformNode middleSegmentTranslation = TransformNode.createTranslationNode(0f, 0.5f, 0f);
        TransformNode middleSegmentScale = TransformNode.createScaleNode(0.5f, 1.0f, 0.5f);

        thumbTransform = TransformNode.createScaleNode(1.0f, 1.0f, 1.0f);

        // Build the graph
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
