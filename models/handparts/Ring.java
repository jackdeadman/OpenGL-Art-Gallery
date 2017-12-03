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

public class Ring extends Model {

    private Mesh ring;
    private Spotlight light;
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

        light = new Spotlight(material, Spotlight.MEDIUM_ATTENUATION);
        light.setPosition(new Vec3(0f, 2f, 0f));
        light.setDirection(new Vec3(0f, 0f, -1f));
        light.setCutOff((float) Math.cos(Math.toRadians(12.5)));
        light.setOuterCutOff((float) Math.cos(Math.toRadians(17.5)));
        worldConfig.setSpotlight(light);
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
        ShaderConfigurator shader = new OneTextureShader(gl, goldTexture, material);
        ring = new SphereNew(gl, shader);

        registerMeshes(new Mesh[] { ring });
    }

    private void buildSceneGraph(GL3 gl) {
        SpotlightNode lightNode = new SpotlightNode("Spotlight (Light)", light);
        MeshNode ringShape = new MeshNode("Sphere (Ring)", ring);
        TransformNode ringTransform = new TransformNode(
                "Scale(0.8f, 0.5f, 0.8f)",
                Mat4Transform.scale(
                    0.8f,0.5f, 0.8f
                )
        );

        SGNode root = new NameNode("Ring");
        root.addChild(ringTransform);
            ringTransform.addChild(lightNode);
            ringTransform.addChild(ringShape);
        root.update();
        setRoot(root);

    }

}
