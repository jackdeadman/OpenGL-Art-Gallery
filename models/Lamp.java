package models;

import com.jogamp.opengl.*;
import engine.*;
import engine.lighting.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;
import galleryscene.shaderprograms.*;
import gmaths.*;
import meshes.*;


public class Lamp extends PointLightEmittingModel {

    private Mesh cube, bottom, topPart1, topPart2;

    // Textures
    int[] rustTexture, rustTextureSpecular, metalTexture;
    private PointLight onLight;

    public Lamp(WorldConfiguration worldConfig) {
        super(worldConfig);

        Material material = new Material();
        material.setDiffuse(0.6f, 0.6f, 0.6f);
        material.setAmbient(0.8f, 0.8f, 0.8f);
        material.setSpecular(1.0f, 1.0f, 1.0f);

        Vec3 attenuation = new Vec3(1f, 0.022f, 0.0019f);
        onLight = new PointLight(material, attenuation);

        setContainedLight(onLight);
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph(gl);
    }

    private void loadTextures(GL3 gl) {
        rustTexture = TextureLibrary.loadTexture(gl, "textures/steel3.jpg");
        rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/steel3_spec.jpg");
        metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");
    }

    private void loadMeshes(GL3 gl) {
        ShaderConfigurator program = new SpecularShader(gl, rustTexture, rustTextureSpecular);
        // Meshes
        cube = new CubeNew(gl, program);
        bottom = new SphereNew(gl, program);
        topPart1 = new SphereNew(gl, program);
        topPart2 = new SphereNew(gl, new LightShader(gl, getContainedLight()));
        registerMeshes(new Mesh[] { cube, bottom, topPart1, topPart2 });
    }

    public void set(boolean isOn) {
        getContainedLight().set(isOn);
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
//                    lightTransform.addChild(lightNode);

                transformCube.addChild(cubeShape);

            root.addChild(bottomTransform);
                bottomTransform.addChild(bottomShape);

            root.addChild(topTransform);
                topTransform.addChild(topPart1Shape);

                topTransform.addChild(part2Transform);
                    part2Transform.addChild(topPart2Shape);
                    part2Transform.addChild(lightNode);

        root.update();
        setRoot(root);
    }


}
