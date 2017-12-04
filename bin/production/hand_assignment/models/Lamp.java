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

    // Meshes that make up a Lamp
    private Mesh pipe, bottomStand, bulbHolder, bulb;

    // Textures
    private int[] steelTexture, steelTextureSpecular;
    private PointLight light;

    public Lamp(WorldConfiguration worldConfig) {
        super(worldConfig);

        // Setup the light properties
        Material material = new Material();
        material.setDiffuse(0.6f, 0.6f, 0.6f);
        material.setAmbient(0.8f, 0.8f, 0.8f);
        material.setSpecular(1.0f, 1.0f, 1.0f);

        // Setup the point light specific properties
        // Numbers chosen based on: https://learnopengl.com/#!Lighting/Light-casters
        Vec3 attenuation = new Vec3(1f, 0.022f, 0.0019f);
        light = new PointLight(material, attenuation);

        setContainedLight(light);
    }

    // OpenGL has been loaded
    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph(gl);
    }

    // Load in the textures to be used later
    private void loadTextures(GL3 gl) {
        steelTexture = TextureLibrary.loadTexture(gl, "textures/steel3.jpg");
        steelTextureSpecular = TextureLibrary.loadTexture(gl, "textures/steel3_spec.jpg");
    }

    private void loadMeshes(GL3 gl) {
        // Create the Shader programs to be attached to the mesh
        ShaderConfigurator program = new SpecularShader(gl, steelTexture, steelTextureSpecular);

        // Load the Meshes
        pipe = new CubeNew(gl, program);
        bottomStand = new SphereNew(gl, program);
        bulbHolder = new SphereNew(gl, program);
        bulb = new SphereNew(gl, new LightShader(gl, getContainedLight()));

        // Register the models so they setup for the scene
        registerMeshes(new Mesh[] {pipe, bottomStand, bulbHolder, bulb});
    }


    public void buildSceneGraph(GL3 gl) {

        // Creates nodes
        MeshNode pipeShape = new MeshNode("Cube (Pipe)", pipe);
        MeshNode bottomStandShape = new MeshNode("Sphere (Bottom Stand)", bottomStand);
        MeshNode bulbHolderShape = new MeshNode("Sphere (Bulb holder)", bulbHolder);
        MeshNode bulbShape = new MeshNode("Sphere (Bulb)", bulb);

        PointLightNode lightNode = new PointLightNode("PointLight (Light)", containedLight);

        // Transforms
        TransformNode transformPipe = new TransformNode(
            "Scale(0.1f, 4.0f, 0.1f); Translate(0.0f, 0.5f, 0.0f)",
            Mat4.multiply(
                Mat4Transform.scale(0.1f, 4.0f, 0.1f),
                Mat4Transform.translate(0.0f, 0.5f, 0.0f)
            )
        );

        TransformNode bottomStandTransform = new TransformNode(
                "Scale(1.0f, 1.0f, 1.0f)",
                Mat4Transform.scale(1.0f, 1.0f, 1.0f)
        );

        TransformNode bulbHolderTransform = new TransformNode(
            "Translate(0.0f, 4.0f, 0.0f)",
            Mat4Transform.translate(0.0f, 4.0f, 0.0f)
        );

        TransformNode bulbTransform = new TransformNode(
            "Translate(0.0f, 0.2f, 0.0f)",
            Mat4Transform.translate(0.0f, 0.2f, 0.0f)
        );

        // Combines nodes to create the tree
        SGNode root = new NameNode("Lamp");
            root.addChild(transformPipe);
                transformPipe.addChild(pipeShape);

            root.addChild(bottomStandTransform);
                bottomStandTransform.addChild(bottomStandShape);

            root.addChild(bulbHolderTransform);
                bulbHolderTransform.addChild(bulbHolderShape);

                bulbHolderTransform.addChild(bulbTransform);
                    bulbTransform.addChild(bulbShape);
                    bulbTransform.addChild(lightNode);

        root.update();
        setRoot(root);
    }
}
