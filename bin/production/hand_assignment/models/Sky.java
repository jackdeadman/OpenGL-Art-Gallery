package models;

import com.jogamp.opengl.*;

import engine.*;
import engine.render.*;
import engine.utils.*;
import engine.scenegraph.*;
import gmaths.*;
import meshes.*;

public class Sky extends Model {
    private int[] skyTexture;
    private Mesh skyBox;

    public Sky(WorldConfiguration worldConfig) {
        super(worldConfig);
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph();
    }

    private void loadTextures(GL3 gl) {
        skyTexture = TextureLibrary.loadTexture(gl, "textures/just_sky.jpg");
    }

    private void loadMeshes(GL3 gl) {
        Material material = new Material();
        material.setAmbient(0.0f, 0.0f, 0.0f);
        material.setDiffuse(0.0f, 0.0f, 0.0f);
        material.setSpecular(0.0f, 0.0f, 0.0f);
        skyBox = new TwoTriangles4(gl, skyTexture);
        registerMesh(skyBox);
    }

    private void buildSceneGraph() {
        MeshNode skyBoxShape = new MeshNode("", skyBox);

        TransformNode transformFrame = new TransformNode("",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0f, 0f, -40f),
                Mat4Transform.rotateAroundX(90f),
                Mat4Transform.scale(30f, 30f, 30f)
            )
        );

        SGNode root = new NameNode("Sky");
        root.addChild(transformFrame);
            transformFrame.addChild(skyBoxShape);
        root.update();
        setRoot(root);
    }
}
