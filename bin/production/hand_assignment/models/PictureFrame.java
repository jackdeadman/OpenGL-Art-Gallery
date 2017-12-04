package models;

import com.jogamp.opengl.*;

import com.jogamp.opengl.util.glsl.ShaderProgram;
import engine.WorldConfiguration;
import gmaths.*;
import meshes.*;
import galleryscene.shaderprograms.*;

import engine.render.*;
import engine.utils.*;
import engine.scenegraph.*;

import javax.xml.soap.Text;

public class PictureFrame extends Model {

    // Very specific class for PictureFrames
    public static class PictureDimension {
        private float width, height;
        public PictureDimension(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }

    // Some useful dimensions to use.
    public static PictureDimension HORIZONTAL_FRAME = new PictureDimension(6, 4);
    public static PictureDimension VERTICAL_FRAME = new PictureDimension(4, 6);
    public static PictureDimension HORIZONTAL_FRAME_SMALL = new PictureDimension(3, 2);
    public static PictureDimension VERTICAL_FRAME_SMALL = new PictureDimension(2, 3);

    private String pathToPicture;
    private int[] frameTexture, pictureTexture, blendTexture;
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
        System.out.println("Loading textures");
        frameTexture = TextureLibrary.loadTexture(gl, "textures/hands/frame2.jpg");
        pictureTexture = TextureLibrary.loadTexture(gl, pathToPicture);
        // Using nearest as linear will have unwanted results when choosing which texture
        blendTexture = TextureLibrary.loadTexture(gl, "textures/hands/frame_blend.jpg",
                GL.GL_REPEAT, GL.GL_REPEAT, GL.GL_NEAREST, GL.GL_NEAREST);
    }

    private void loadMeshes(GL3 gl) {
//        frame = new TwoTriangles2(gl, frameTexture, pictureTexture, "shaders/picture.fs.glsl");
        MultiLightShader program = new MultiLightShader(gl, "shaders/correct/picture_frame.fs.glsl");
        program.addTexture("frame_texture", frameTexture);
        program.addTexture("picture_texture", pictureTexture);
        program.addTexture("blend_texture", blendTexture);

        frame = new TwoTrianglesNew(gl, program);
//        frame = new TwoTrianglesNew(gl, new OneTextureShader(gl, frameTexture));
//        frame = new TwoTrianglesNew(gl, new BlendShader(frameTexture, pictureTexture, blendTexture));
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
