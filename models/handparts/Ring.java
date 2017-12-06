package models.handparts;

import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.lighting.*;

import engine.utils.TextureLibrary;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;
import shaders.shaderconfigurators.LightShader;
import shaders.shaderconfigurators.OneTextureShader;

public class Ring extends SpotLightEmittingModel {
    /**
    * @author Jack Deadman
    */

    public final String MAIN_TEXT_PATH = "textures/gold.jpg";

    private Mesh ring, bulb;
    private int[] mainTexture;

    private Material createLightMaterial() {
        Material material = new Material();
        material.setDiffuse(0.4f, 0.56f, 0.86f);
        material.setSpecular(0.4f, 0.56f, 0.86f);
        material.setAmbient(0.4f, 0.56f, 0.86f);
        material.setShininess(10f);

        return material;
    }

    private Material createBulbMaterial() {
        Material material = new Material();
        material.setDiffuse(0.5f, 0.5f, 0.5f);
        material.setAmbient(0.6f, 0.6f, 0.6f);
        material.setSpecular(0.8f, 0.8f, 0.8f);
        material.setShininess(15f);

        return material;
    }

    public Ring(WorldConfiguration worldConfig) {
        super(worldConfig);
        Material material = createLightMaterial();

        Spotlight light = new Spotlight(material, Spotlight.LONG_ATTENUATION);

        // Position and direction chosen based on where the light should be
        // placed after local transformations. The global pos and direction
        // will be automatically adjusted.
        light.setPosition(new Vec3(0f, 2f, 0f));
        light.setDirection(new Vec3(0f, 0f, -1f));

        // Based on learn openGL values.
        light.setCutOff((float) Math.cos(Math.toRadians(12.5)));
        light.setOuterCutOff((float) Math.cos(Math.toRadians(17.5)));

        setContainedLight(light);
    }

    // OpenGL loaded
    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph(gl);
    }

    private void loadTextures(GL3 gl) {
        mainTexture = TextureLibrary.loadTexture(gl, MAIN_TEXT_PATH);
    }

    private void loadMeshes(GL3 gl) {
        Material material = createBulbMaterial();

        ShaderConfigurator ringShader = new OneTextureShader(gl, mainTexture, material);
        ShaderConfigurator bulbShader = new LightShader(gl, getContainedLight());

        ring = new Sphere(gl, ringShader);
        bulb = new Sphere(gl, bulbShader);

        registerMeshes(new Mesh[] { ring, bulb });
    }

    private void buildSceneGraph(GL3 gl) {
        SpotlightNode lightNode = new SpotlightNode("Spotlight (Light)", getContainedLight());

        MeshNode bulbShape = new MeshNode("Sphere (Bulb)", bulb);
        TransformNode bulbTransform = new TransformNode(
                "Scale(0.3, 0.3, 0.3); Translate(0, 0, -0.5)",
                Mat4.multiply(
                    Mat4Transform.translate(0f, 0f, -0.5f),
                    Mat4Transform.scale(0.3f, 0.3f, 0.3f)
                )
        );

        MeshNode ringShape = new MeshNode("Sphere (Ring)", ring);
        TransformNode ringTransform = new TransformNode(
                "Scale(0.8f, 0.5f, 0.8f)",
                Mat4Transform.scale(0.8f, 0.4f, 0.8f)
        );

        SGNode root = new NameNode("Ring");
        root.addChild(ringTransform);
            ringTransform.addChild(ringShape);
            ringTransform.addChild(bulbTransform);
                bulbTransform.addChild(lightNode);
                lightNode.addChild(bulbShape);
        root.update();
        setRoot(root);

    }

}
