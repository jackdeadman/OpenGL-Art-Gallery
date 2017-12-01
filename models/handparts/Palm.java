package models.handparts;
import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;
import meshes.*;
import gmaths.*;
import galleryscene.shaderprograms.*;

import com.jogamp.opengl.*;


public class Palm extends Model {

    private Mesh upperPalm, lowerPalm;
    private TransformNode topRotation;
    private NameNode upperPalmName;
    private int[] rustTexture, rustTextureSpecular, metalTexture;

    public Palm(WorldConfiguration worldConfig) {
        super(worldConfig);
    }

    // OpenGL loaded
    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph(gl);
    }

    private void loadTextures(GL3 gl) {
        rustTexture = TextureLibrary.loadTexture(gl, "textures/green.jpg");
        rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");
    }

    private void loadMeshes(GL3 gl) {
        upperPalm = new SphereNew(gl, new OneTextureShader(gl, rustTexture));
        lowerPalm = new SphereNew(gl, new OneTextureShader(gl, rustTexture));

        registerMeshes(new Mesh[] { upperPalm, lowerPalm });
    }

    public void bendTop(float amount) {
        float degrees = amount * 90;
        topRotation.setTransform(Mat4Transform.rotateAroundX(degrees));
        getRoot().update();
    }

    private void buildSceneGraph(GL3 gl) {
        System.out.println("Building scene graph");

        upperPalmName = new NameNode("upperPalm");
        NameNode lowerPalmName = new NameNode("lowerPalm");
        MeshNode upperPalmShape = new MeshNode("dsnj", upperPalm);
        MeshNode lowerPalmShape = new MeshNode("dsnj", lowerPalm);

        TransformNode upperPalmScale = new TransformNode("sanj", Mat4Transform.scale(2.6f, 1.2f, 1f));
        topRotation = new TransformNode("ssnj", Mat4Transform.rotateAroundX(0f));

        TransformNode lowerPalmTranslate = new TransformNode("sanj", Mat4Transform.translate(0.0f, -0.8f, 0.0f));
        TransformNode lowerPalmScale = new TransformNode("sanj", Mat4Transform.scale(2.8f, 3f, 1f));
        TransformNode upperPalmTranslate = new TransformNode("snjn", Mat4Transform.translate(0.0f, 0.25f, 0.0f));

        SGNode root = new NameNode("Palm");

        root.addChild(lowerPalmName);
            lowerPalmName.addChild(lowerPalmTranslate);
            lowerPalmTranslate.addChild(lowerPalmScale);
            lowerPalmScale.addChild(lowerPalmShape);

            lowerPalmName.addChild(upperPalmName);
            upperPalmName.addChild(topRotation);
            upperPalmName.addChild(upperPalmTranslate);
                upperPalmTranslate.addChild(topRotation);
                topRotation.addChild(upperPalmScale);
            upperPalmScale.addChild(upperPalmShape);

        root.update();
        setRoot(root);

    }

    public SGNode getAnchor() {
        return topRotation;
    }

}
