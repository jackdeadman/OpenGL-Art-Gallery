package models;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import engine.*;
import engine.WorldConfiguration;
import gmaths.*;
import meshes.*;

public class PictureFrame extends Model {

    private String pathToPicture;
    private int[] frameTexture, pictureTexture;
    private Mesh frame;

    public PictureFrame(WorldConfiguration worldConfig, String pathToPicture) {
        super(worldConfig);
        this.pathToPicture = pathToPicture;
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph();
    }

    private void loadTextures(GL3 gl) {
        frameTexture = TextureLibrary.loadTexture(gl, "textures/floor_3.jpg");
        // pictureTexture = TextureLibrary.loadTexture(gl, pathToPicture);
    }

    private void loadMeshes(GL3 gl) {
        frame = new TwoTriangles(gl, frameTexture);
        registerMesh(frame);
    }

    private void buildSceneGraph() {
        MeshNode frameShape = new MeshNode("", frame);

        TransformNode transformFrame = new TransformNode("",
            Mat4Transform.scale(2.0f, 4.0f, 1.0f)
        );

        SGNode root = new NameNode("PictureFrame");
        root.addChild(transformFrame);
            transformFrame.addChild(frameShape);
        root.update();
        setRoot(root);
    }
}
