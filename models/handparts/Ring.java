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

    public Ring(WorldConfiguration worldConfig) {
        super(worldConfig);
        light = new Spotlight(new Vec3(0f, 0f, -1f), new Vec3(0.1f, 0.18f, 0.0112f));
        light.setPosition(new Vec3(0f, 2f, 0f));
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
        SpotlightNode lightNode = new SpotlightNode("", light);
        MeshNode ringShape = new MeshNode("", ring);
        TransformNode ringTransform = new TransformNode("",
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
