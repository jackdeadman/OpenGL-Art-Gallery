package models;

import com.jogamp.opengl.*;

import engine.WorldConfiguration;
import gmaths.*;
import meshes.*;
import galleryscene.shaderprograms.*;

import engine.render.*;
import engine.utils.*;
import engine.scenegraph.*;

public class PictureFrame extends Model {

    // Very specific class for PictureFrames
    public static class PictureDimension {
        private float width, height;
        public PictureDimension(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }

    // Some useful dimensions to use for a picture frame
    public static PictureDimension HORIZONTAL_FRAME = new PictureDimension(6, 4);
    public static PictureDimension VERTICAL_FRAME = new PictureDimension(4, 6);
    public static PictureDimension HORIZONTAL_FRAME_SMALL = new PictureDimension(3, 2);
    public static PictureDimension VERTICAL_FRAME_SMALL = new PictureDimension(2, 3);

    private PictureDimension dimensions;
    private Mesh frame;
    private int[] frameTexture, pictureTexture, blendTexture;
    private String pathToPicture;

    // Texture Paths
    public final String FRAME_PATH = "textures/hands/frame2.jpg";
    // Kinda hacky texture for determining when to use picture
    // and when to use frame.
    public final String BLEND_PATH = "textures/hands/frame_blend.jpg";

    // Custom Shader
    public final String SHADER_PATH = "shaders/correct/picture_frame.fs.glsl";

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
        frameTexture = TextureLibrary.loadTexture(gl, FRAME_PATH);
        pictureTexture = TextureLibrary.loadTexture(gl, pathToPicture);

        // Using nearest as linear will have unwanted results when choosing which texture
        blendTexture = TextureLibrary.loadTexture(gl, BLEND_PATH,
                GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_NEAREST, GL.GL_NEAREST);
    }

    private void loadMeshes(GL3 gl) {
        MultiLightShader program = new MultiLightShader(gl, SHADER_PATH);

        // Setup the data for the shader
        program.addTexture("frameTexture", frameTexture);
        program.addTexture("pictureTexture", pictureTexture);
        program.addTexture("blendTexture", blendTexture);

        frame = new TwoTriangles(gl, program);
        registerMesh(frame);
    }

    private void buildSceneGraph() {
        MeshNode frameShape = new MeshNode("TwoTriangles (Frame)", frame);

        // Scale frame to be the specified framesize
        TransformNode transformFrame = new TransformNode(
            String.format("Scale(%.1f, 1.0, %.1f)", dimensions.width, dimensions.height),
            Mat4Transform.scale(dimensions.width, 1.0f, dimensions.height)
        );

        SGNode root = new NameNode("PictureFrame");
        root.addChild(transformFrame);
            transformFrame.addChild(frameShape);
        root.update();
        setRoot(root);
    }
}
