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

    public final String MAIN_TEXTURE_PATH = "textures/main_metal.jpg";
    public final String MAIN_TEXTURE_SPECULAR_PATH = "textures/used/arm_main_spec.jpg";

    private Mesh mainSegment;
    private int[] mainTexture;
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
        mainTexture = TextureLibrary.loadTexture(gl, MAIN_TEXTURE_PATH);
    }

    private void loadMeshes(GL3 gl) {
        ShaderConfigurator program = new OneTextureShader(gl, mainTexture);
        mainSegment = new SphereNew(gl, program);
        registerMesh(mainSegment);
    }

    public void buildSceneGraph() {
        MeshNode mainSegmentShape = new MeshNode("Sphere (Main Segment)", mainSegment);

        TransformNode scaleArm = new TransformNode(
            "Translate(0, 0.5, 0); Scale(1.5, 2.5, 1.5)",
            Mat4.multiply(
                Mat4Transform.scale(1.5f, 2.5f, 1.5f),
                Mat4Transform.translate(0.0f, 0.5f, 0.0f)
            )
        );

        // The point where further scene graphs should extend from
        anchor = new TransformNode(
                "Translate(0, 2.5, 0)",
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
