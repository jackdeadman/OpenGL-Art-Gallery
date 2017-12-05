package models.handparts;
import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;
import meshes.*;
import gmaths.*;

import com.jogamp.opengl.*;
import shaders.shaderconfigurators.OneTextureShader;
import shaders.shaderconfigurators.SpecularShader;


public class Palm extends Model {

    private Mesh upperPalm, lowerPalm;

    private TransformNode topRotation;

    public final String UPPER_TEXTURE_PATH = "textures/scratches.jpg";
    public final String UPPER_TEXTURE_SPEC_PATH = "textures/scratches_spec.jpg";
    public final String LOWER_TEXTURE_PATH = "textures/green_metal.jpg";

    private int[] upperTexture, upperTextureSpec, lowerTexture;

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
        upperTexture = TextureLibrary.loadTexture(gl, UPPER_TEXTURE_PATH);
        upperTextureSpec = TextureLibrary.loadTexture(gl, UPPER_TEXTURE_SPEC_PATH);
        lowerTexture = TextureLibrary.loadTexture(gl, LOWER_TEXTURE_PATH);
    }

    private void loadMeshes(GL3 gl) {

        Material lowerMaterial = new Material();
        lowerMaterial.setAmbient(0.2f, 0.2f, 0.2f);
        lowerMaterial.setDiffuse(0.2f, 0.2f, 0.2f);
        lowerMaterial.setSpecular(0.1f, 0.1f, 0.1f);
        lowerMaterial.setShininess(5f);
        upperPalm = new Sphere(gl, new SpecularShader(gl, upperTexture, upperTextureSpec));
        lowerPalm = new Sphere(gl, new OneTextureShader(gl, lowerTexture, lowerMaterial));

        registerMeshes(new Mesh[] { upperPalm, lowerPalm });
    }

    // Bend from the top part of the palm
    public void bendTop(float amount) {
        float degrees = amount * 90;
        topRotation.setTransform(Mat4Transform.rotateAroundX(degrees));
        getRoot().update();
    }

    private void buildSceneGraph(GL3 gl) {

        NameNode upperPalmName = new NameNode("Upper Palm");
        NameNode lowerPalmName = new NameNode("Lower Palm");
        MeshNode upperPalmShape = new MeshNode("Sphere (Upper Palm)", upperPalm);
        MeshNode lowerPalmShape = new MeshNode("Sphere (Lower Palm)", lowerPalm);

        TransformNode upperPalmScale = TransformNode.createScaleNode(2.6f, 1.2f, 1f);

        // Possibly will be updated when bending palm
        topRotation = TransformNode.createRotateAroundXNode(0f);

        TransformNode lowerPalmTranslate = TransformNode.createTranslationNode(0.0f, -0.8f, 0.0f);
        TransformNode lowerPalmScale = TransformNode.createScaleNode(2.8f, 3f, 1f);
        TransformNode upperPalmTranslate = TransformNode.createTranslationNode(0.0f, 0.25f, 0.0f);

        SGNode root = new NameNode("Palm");

        root.addChild(lowerPalmName);
            lowerPalmName.addChild(lowerPalmTranslate);
                lowerPalmTranslate.addChild(lowerPalmScale);
                    lowerPalmScale.addChild(lowerPalmShape);

            lowerPalmName.addChild(upperPalmName);
                upperPalmName.addChild(topRotation);
                    topRotation.addChild(upperPalmScale);
                        upperPalmScale.addChild(upperPalmShape);
                upperPalmName.addChild(upperPalmTranslate);
                    upperPalmTranslate.addChild(topRotation);
                        // Stuff will be extended from here (i.e the anchor)

        root.update();
        setRoot(root);

    }

    public SGNode getAnchor() {
        return topRotation;
    }

}
