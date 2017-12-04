package models;

import com.jogamp.opengl.*;

import engine.*;
import engine.render.*;
import engine.utils.*;
import engine.scenegraph.*;
import gmaths.*;
import meshes.*;

public class Sky extends Model {

    private final String IMAGE_PATH = "textures/just_sky.jpg";

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
        skyTexture = TextureLibrary.loadTexture(gl, IMAGE_PATH);
    }

    private void loadMeshes(GL3 gl) {
        // Change to a skybox shader
        skyBox = new TwoTriangles(gl, skyTexture);
        registerMesh(skyBox);
    }

    private void buildSceneGraph() {
        MeshNode skyBoxShape = new MeshNode("TwoTriangles (SkyBox)", skyBox);

        TransformNode transformFrame = new TransformNode("",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0f, 0f, -40f),
                Mat4Transform.rotateAroundX(90f),
                // Move it far away
                Mat4Transform.scale(30f, 30f, 30f)
            )
        );

        // Build graph
        SGNode root = new NameNode("Sky");
        root.addChild(transformFrame);
            transformFrame.addChild(skyBoxShape);
        root.update();
        setRoot(root);
    }
}
