package models;

import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;
import gmaths.*;
import meshes.*;
import com.jogamp.opengl.*;
import galleryscene.shaderprograms.*;

public class Arm extends Model {

    private Mesh mainSegment;
    private int[] mainTexture, mainTextureSpec;
    private TransformNode anchor;

    public Arm(WorldConfiguration worldConfig) {
        super(worldConfig);
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph();
    }

    private void loadTextures(GL3 gl) {
        mainTexture = TextureLibrary.loadTexture(gl, "textures/main_metal.jpg");
        mainTextureSpec = TextureLibrary.loadTexture(gl, "textures/used/arm_main_spec.jpg");
    }

    private void loadMeshes(GL3 gl) {
        // Meshes
        mainSegment = new SphereNew(gl, new OneTextureShader(gl, mainTexture));
        registerMeshes(new Mesh[] { mainSegment });
    }

    public void buildSceneGraph() {
        MeshNode mainSegmentShape = new MeshNode("", mainSegment);

        TransformNode scaleArm = new TransformNode(
            "",
            Mat4.multiply(
                Mat4Transform.scale(1.5f, 2.5f, 1.5f),
                Mat4Transform.translate(0.0f, 0.5f, 0.0f)
            )
        );

        anchor = new TransformNode("",
                Mat4Transform.translate(0.0f, 2.5f, 0.0f));

        SGNode root = new NameNode("Arm");
        root.addChild(scaleArm);
            scaleArm.addChild(mainSegmentShape);
        root.addChild(anchor);

        root.update();
        setRoot(root);
    }

    public SGNode getAnchor() {
        return anchor;
    }

}
