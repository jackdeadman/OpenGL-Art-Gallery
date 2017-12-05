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
    private int[] mainTexture, mainTextureSpecular;

    public final float LAMP_HEIGHT = 4.0f;

    // Numbers chosen based on: https://learnopengl.com/#!Lighting/Light-casters
    public final Vec3 LIGHT_ATTENUATION = new Vec3(1f, 0.022f, 0.0019f);

    // File paths
    public final String MAIN_TEXTURE_PATH = "textures/steel3.jpg";
    public final String MAIN_TEXTURE_SPECULAR_PATH = "textures/steel3_spec.jpg";

    private Material createMaterialForLight() {
        // Setup the light properties
        Material material = new Material();
        material.setDiffuse(0.6f, 0.6f, 0.6f);
        material.setAmbient(0.8f, 0.8f, 0.8f);
        material.setSpecular(1.0f, 1.0f, 1.0f);
        return material;
    }

    public Lamp(WorldConfiguration worldConfig) {
        super(worldConfig);
        PointLight light = new PointLight(createMaterialForLight(), LIGHT_ATTENUATION);
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
        mainTexture = TextureLibrary.loadTexture(gl, MAIN_TEXTURE_PATH);
        mainTextureSpecular = TextureLibrary.loadTexture(gl, MAIN_TEXTURE_SPECULAR_PATH);
    }

    private void loadMeshes(GL3 gl) {
        // Create the Shader programs to be attached to the mesh
        ShaderConfigurator program = new SpecularShader(gl, mainTexture, mainTextureSpecular);

        // Load the Meshes
        pipe = new Cube(gl, program);
        bottomStand = new Sphere(gl, program);
        bulbHolder = new Sphere(gl, program);
        bulb = new Sphere(gl, new LightShader(gl, getContainedLight()));

        // Register the models so they setup for the scene
        registerMeshes(new Mesh[] { pipe, bottomStand, bulbHolder, bulb });
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
            String.format("Scale(0.1, %.2f, 0.1); Translate(0.0, 0.5, 0.0)", LAMP_HEIGHT),
            Mat4.multiply(
                Mat4Transform.scale(0.1f, LAMP_HEIGHT, 0.1f),
                Mat4Transform.translate(0.0f, 0.5f, 0.0f)
            )
        );

        TransformNode bulbHolderTransform = new TransformNode(
            String.format("Translate(0.0, %.2f, 0.0)", LAMP_HEIGHT),
            Mat4Transform.translate(0.0f, LAMP_HEIGHT, 0.0f)
        );

        TransformNode bulbTransform = new TransformNode(
            "Translate(0.0, 0.2, 0.0)",
            Mat4Transform.translate(0.0f, 0.2f, 0.0f)
        );

        // Combines nodes to create the tree
        SGNode root = new NameNode("Lamp");
            root.addChild(bottomStandShape);

            root.addChild(transformPipe);
                transformPipe.addChild(pipeShape);

            root.addChild(bulbHolderTransform);
                bulbHolderTransform.addChild(bulbHolderShape);

                bulbHolderTransform.addChild(bulbTransform);
                    bulbTransform.addChild(bulbShape);
                    bulbTransform.addChild(lightNode);

        root.update();
        setRoot(root);
    }
}
