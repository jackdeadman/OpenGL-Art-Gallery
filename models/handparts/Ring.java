package models.handparts;

import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.lighting.*;

import engine.utils.TextureLibrary;
import galleryscene.shaderprograms.*;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;

public class Ring extends SpotLightEmittingModel {

    private Mesh ring, bulb;
    private int[] goldTexture;

    private Material createLightMaterial() {
        Material material = new Material();
        material.setDiffuse(0.4f, 0.56f, 0.86f);
        material.setSpecular(0.4f, 0.56f, 0.86f);
        material.setAmbient(0.4f, 0.56f, 0.86f);
        material.setShininess(10f);

        return material;
    }

    public Ring(WorldConfiguration worldConfig) {
        super(worldConfig);
        Material material = createLightMaterial();

        Spotlight light = new Spotlight(material, Spotlight.MEDIUM_ATTENUATION);
        light.setPosition(new Vec3(0f, 2f, 0f));
        light.setDirection(new Vec3(0f, 0f, -1f));
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
        goldTexture = TextureLibrary.loadTexture(gl, "textures/gold.jpg");
    }

    private void loadMeshes(GL3 gl) {
        Material material = new Material();
        material.setDiffuse(0.5f, 0.5f, 0.5f);
        material.setAmbient(0.6f, 0.6f, 0.6f);
        material.setSpecular(0.8f, 0.8f, 0.8f);
        material.setShininess(15f);

        ShaderConfigurator ringShader = new OneTextureShader(gl, goldTexture, material);
        ShaderConfigurator bulbShader = new LightShader(gl, getContainedLight());

        ring = new SphereNew(gl, ringShader);
        bulb = new SphereNew(gl, bulbShader);

        registerMeshes(new Mesh[] { ring, bulb });
    }

    private void buildSceneGraph(GL3 gl) {
        System.out.println(getContainedLight());
        SpotlightNode lightNode = new SpotlightNode("Spotlight (Light)", getContainedLight());

        MeshNode bulbShape = new MeshNode("Sphere (Bulb)", bulb);
        TransformNode bulbTransform = new TransformNode(
                "Sc",
                Mat4.multiply(
                    Mat4Transform.translate(0f, 0f, -0.5f),
                    Mat4Transform.scale(0.3f, 0.3f, 0.3f)
                )
        );

        MeshNode ringShape = new MeshNode("Sphere (Ring)", ring);
        TransformNode ringTransform = new TransformNode(
                "Scale(0.8f, 0.5f, 0.8f)",
                Mat4Transform.scale(
                    0.8f, 0.4f, 0.8f
                )
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
