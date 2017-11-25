package models;

import engine.*;
import gmaths.*;
import meshes.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Lamp extends LightEmittingModel {

    private Mesh cube, bottom, topPart1, topPart2;

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
        loadMeshes(gl);
        buildSceneGraph(gl);
    }

    private void loadTextures(GL3 gl) {
        rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");
    }

    private void loadMeshes(GL3 gl) {
        // Meshes
        cube = new Cube(gl, rustTexture, rustTextureSpecular);
        bottom = new Sphere(gl, rustTexture, rustTextureSpecular);
        topPart1 = new Sphere(gl, metalTexture, metalTexture);
        topPart2 = new Sphere(gl, rustTexture, metalTexture);
        registerMeshes(new Mesh[] { cube, bottom, topPart1, topPart2 });
    }

    public void buildSceneGraph(GL3 gl) {

        // Creates nodes
        MeshNode cubeShape = new MeshNode("", cube);
        MeshNode bottomShape = new MeshNode("", bottom);
        MeshNode topPart1Shape = new MeshNode("", topPart1);
        MeshNode topPart2Shape = new MeshNode("", topPart2);

        LightNode lightNode = new LightNode("", containedLight);

        TransformNode transformCube = new TransformNode(
            "",
            Mat4.multiply(
                Mat4Transform.scale(0.1f, 4.0f, 0.1f),
                Mat4Transform.translate(0.0f, 0.5f, 0.0f)
            )
        );

        TransformNode bottomTransform = new TransformNode(
            "",
            // Mat4.multiply(
                Mat4Transform.scale(1.0f, 1.0f, 1.0f)
                // Mat4Transform.translate(0.0f, 0.5f, 0.0f)
            // )
        );

        TransformNode topTransform = new TransformNode(
            "",
            Mat4Transform.translate(0.0f, 4.0f, 0.0f)
        );

        TransformNode lightTransform = new TransformNode(
            "",
            Mat4Transform.translate(0.0f, 2.0f, 0.0f));

        TransformNode part2Transform = new TransformNode(
            "",
            Mat4Transform.translate(0.0f, 0.2f, 0.0f)
        );

        // Combines nodes
        SGNode root = new NameNode("Lamp");
            root.addChild(transformCube);

                transformCube.addChild(lightTransform);
                    lightTransform.addChild(lightNode);

                transformCube.addChild(cubeShape);

            root.addChild(bottomTransform);
                bottomTransform.addChild(bottomShape);

            root.addChild(topTransform);
                topTransform.addChild(topPart1Shape);

                topTransform.addChild(part2Transform);
                    part2Transform.addChild(topPart2Shape);

        root.update();
        setRoot(root);
    }


}
