package models;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import engine.*;
import engine.WorldConfiguration;
import gmaths.*;
import meshes.*;

public class PictureFrame extends Model {
    public static class PictureDimension {
        private float width, height;
        public PictureDimension(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }

    public static PictureDimension HORIZONTAL_FRAME_LARGE = new PictureDimension(10, 6);
    public static PictureDimension VERTICAL_FRAME = new PictureDimension(6, 10);

    private String pathToPicture;
    private int[] frameTexture, pictureTexture;
    private Mesh frame;
    private PictureDimension dimensions;


    public PictureFrame(WorldConfiguration worldConfig, PictureDimension dimensions, String pathToPicture) {
        super(worldConfig);
        this.dimensions = dimensions;
        this.pathToPicture = pathToPicture;
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph();
    }

    private void loadTextures(GL3 gl) {
        frameTexture = TextureLibrary.loadTexture(gl, "textures/cloud.jpg");
        // pictureTexture = TextureLibrary.loadTexture(gl, pathToPicture);
    }

    private void loadMeshes(GL3 gl) {
        frame = new TwoTriangles(gl, frameTexture);
        registerMesh(frame);
    }

    private void buildSceneGraph() {
        MeshNode frameShape = new MeshNode("", frame);

        TransformNode transformFrame = new TransformNode("",
            Mat4Transform.scale(dimensions.width, 1.0f, dimensions.height)
        );

        SGNode root = new NameNode("PictureFrame");
        root.addChild(transformFrame);
            transformFrame.addChild(frameShape);
        root.update();
        setRoot(root);
    }
}
