package models;

import engine.*;
import gmaths.*;
import meshes.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Lamp extends LightEmittingModel {

    private Mesh cube;

    // Textures
    int[] rustTexture, rustTextureSpecular, metalTexture;

    public Lamp(WorldConfiguration worldConfig) {
        super(worldConfig);

        Vec3 colour = new Vec3(1.0f, 1.0f, 1.0f);
        Vec3 attenuation = new Vec3(1f, 0.08f, 0.012f);
        setContainedLight(new PointLight(colour, attenuation));
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadModels(gl);
        buildSceneGraph(gl);
    }

    private void loadTextures(GL3 gl) {
        rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");
    }

    private void loadModels(GL3 gl) {
        // Meshes
        cube = new Cube(gl, rustTexture, rustTextureSpecular);
        registerMesh(cube);
    }

    public void buildSceneGraph(GL3 gl) {

        // Creates nodes
        MeshNode cubeShape = new MeshNode("", cube);
        TransformNode transformCube = new TransformNode(
            "",
            Mat4.multiply(
                Mat4Transform.scale(1.0f, 2.0f, 1.0f),
                Mat4Transform.translate(0.0f, 0.5f, 0.0f)
            )
        );

        LightNode lightNode = new LightNode("", containedLight);

        // Combines nodes
        SGNode root = new NameNode("Lamp");
            root.addChild(transformCube);
                transformCube.addChild(lightNode);
                transformCube.addChild(cubeShape);

        root.update();
        setRoot(root);
    }


}
