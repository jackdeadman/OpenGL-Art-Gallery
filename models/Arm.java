package models;

import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;
import gmaths.*;
import meshes.*;
import com.jogamp.opengl.*;
import shaders.shaderconfigurators.OneTextureShader;

public class Arm extends Model {

    public final String MAIN_TEXTURE_PATH = "textures/arm_metal.jpg";

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
        Material material = new Material();
        material.setDiffuse(0.2f, 0.2f, 0.2f);
        material.setAmbient(0.2f, 0.2f, 0.2f);
        material.setSpecular(0.1f, 0.1f, 0.1f);
        material.setShininess(1f);
        ShaderConfigurator program = new OneTextureShader(gl, mainTexture, material);
        mainSegment = new Sphere(gl, program);
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
