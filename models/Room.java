package models;

import com.jogamp.opengl.*;

import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;

import gmaths.*;
import meshes.*;
import shaders.shaderconfigurators.MultiLightShader;
import shaders.shaderconfigurators.OneTextureShader;

public class Room extends Model {

    private Mesh floor, back, left, right, roof, front, rug;
    // defaults
    private float floorWidth = 16, floorLength = 12, ceilingHeight = 10;

    private NameNode floorName;

    private int[] floorTexture, backWallTexture, backWallNormal, backWallBlend,
            windowTexture , beamsTexture, wallpaperTexture , ceilingTexture, rugTexture;

    public enum WallPosition { LEFT_CLOSE, LEFT_MIDDLE, LEFT_FAR, RIGHT_CLOSE, RIGHT_MIDDLE, RIGHT_FAR };

    private TransformNode moveLeftWall;

    public Room(WorldConfiguration worldConfig, int floorWidth, int floorLength, int ceilingHeight) {
        super(worldConfig);
        this.floorWidth = floorWidth;
        this.floorLength = floorLength;
        this.ceilingHeight = ceilingHeight;
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph();
    }

    public void addPictureToWall(WallPosition pos, PictureFrame picture) {
        SGNode wallPosition = pictureFrameTransformations[0];
        switch (pos) {
            case LEFT_CLOSE:
                wallPosition = pictureFrameTransformations[0];
                break;
            case LEFT_MIDDLE:
                wallPosition = pictureFrameTransformations[1];
                break;
            case LEFT_FAR:
                wallPosition = pictureFrameTransformations[2];
                break;
            case RIGHT_CLOSE:
                wallPosition = pictureFrameTransformations[3];
                break;
            case RIGHT_MIDDLE:
                wallPosition = pictureFrameTransformations[4];
                break;
            case RIGHT_FAR:
                wallPosition = pictureFrameTransformations[5];
                break;
        }

        // Stops the fighting with frame
        TransformNode nudge = new TransformNode("Translate(0f, 0.1f, 0f);",
                Mat4Transform.translate(0.0f, 0.1f, 0.0f));

        wallPosition.addChild(nudge);
            nudge.addChild(picture.getRoot());
        getRoot().update();
    }

    public final String FLOOR_TEXTURE_PATH = "textures/wood_floor.jpg";

    // backWallNormal, backWallTexture, backWallBlend
    public final String BACK_WALL_TEXTURE_PATH = "textures/wall_wood.jpg";
    public final String BACK_WALL_NORMAL_PATH = "textures/wall_wood_normal.jpg";
    public final String BACK_WALL_BLEND_PATH = "textures/window_mask.jpg";
    public final String WINDOW_TEXTURE_PATH = "textures/window.jpg";

    public final String BEAMS_PATH = "textures/window_black.jpg";
    public final String WALLPAPER_PATH = "textures/wallpaper.jpg";
    public final String CEILING_PATH = "textures/ceiling.jpg";
    public final String RUG_PATH = "textures/rug.jpg";

    public final String BACK_WALL_SHADER_PATH = "shaders/glsl/backwall.fs.glsl";
    public final String BLEND_WALL_SHADER_PATH = "shaders/glsl/blend_wall.fs.glsl";


    private void loadTextures(GL3 gl) {
        floorTexture = TextureLibrary.loadTexture(gl, FLOOR_TEXTURE_PATH);

        backWallTexture = TextureLibrary.loadTexture(gl, BACK_WALL_TEXTURE_PATH);
        backWallNormal = TextureLibrary.loadTexture(gl, BACK_WALL_NORMAL_PATH);
        backWallBlend = TextureLibrary.loadTexture(gl, BACK_WALL_BLEND_PATH);
        windowTexture = TextureLibrary.loadTexture(gl, WINDOW_TEXTURE_PATH);

        beamsTexture = TextureLibrary.loadTexture(gl, BEAMS_PATH);
        wallpaperTexture = TextureLibrary.loadTexture(gl, WALLPAPER_PATH);

        ceilingTexture = TextureLibrary.loadTexture(gl, CEILING_PATH);
        rugTexture = TextureLibrary.loadTexture(gl, RUG_PATH);
    }

    private SGNode[] pictureFrameTransformations = new SGNode[6];

    private void createPictureFrames() {
        // Could probably do this better, but it's easier to read when together
        pictureFrameTransformations[0] = new TransformNode("",Mat4Transform.translate(-6.5f,0f, 0f));
        pictureFrameTransformations[1] = new NameNode("Left Picture Frames");
        pictureFrameTransformations[2] = new TransformNode("",Mat4Transform.translate(6.5f,0f, 0f));

        pictureFrameTransformations[1].addChild(pictureFrameTransformations[0]);
        pictureFrameTransformations[1].addChild(pictureFrameTransformations[2]);

        pictureFrameTransformations[3] = new TransformNode("",Mat4Transform.translate(-6.5f,0f, 0f));
        pictureFrameTransformations[4] = new NameNode("Right Picture Frames");
        pictureFrameTransformations[5] = new TransformNode("",Mat4Transform.translate(6.5f,0f, 0f));

        pictureFrameTransformations[4].addChild(pictureFrameTransformations[3]);
        pictureFrameTransformations[4].addChild(pictureFrameTransformations[5]);
    }

    private void loadMeshes(GL3 gl) {
        // make meshes
        floor = new TwoTriangles(gl, new OneTextureShader(gl, floorTexture));

        // Unique one off shader so making it here instead of making a new ShaderConfigurator
        MultiLightShader backShaderProgram = new MultiLightShader(gl, BACK_WALL_SHADER_PATH);
        backShaderProgram.addTexture("normalTexture", backWallNormal);
        backShaderProgram.addTexture("mainTexture", backWallTexture);
        backShaderProgram.addTexture("blendTexture", backWallBlend);
        backShaderProgram.addTexture("windowTexture", windowTexture);
        back = new TwoTriangles(gl, backShaderProgram);

        // Same again
        MultiLightShader blendShader = new MultiLightShader(gl, BLEND_WALL_SHADER_PATH);
        blendShader.addTexture("first_texture", beamsTexture);
        blendShader.addTexture("second_texture", wallpaperTexture);

        // All these sides have the same texturing
        left = new TwoTriangles(gl, blendShader);
        right = new TwoTriangles(gl, blendShader);

        // Being lazy here, could add a door and stuff, but mostly likely this
        // wall won't even be seen most of the time.
        front = new TwoTriangles(gl, blendShader);

        roof = new TwoTriangles(gl, new OneTextureShader(gl, ceilingTexture));
        rug = new TwoTriangles(gl, new OneTextureShader(gl, rugTexture));

        registerMeshes(new Mesh[] { floor, back, left, right, roof, front, rug });
    }

    private void buildSceneGraph() {

        TransformNode floorTransform = new TransformNode(
            "Transform floor",
            Mat4Transform.scale(floorWidth, 1, floorLength)
        );

        TransformNode backTransform = new TransformNode(
            "Transform back wall",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0, ceilingHeight / 2.0f, -floorLength / 2.0f),
                Mat4Transform.scale(floorWidth, ceilingHeight, 1),
                Mat4Transform.rotateAroundX(90.0f)
            )
        );

        TransformNode scaleLeftWall = new TransformNode(
            "Scale left wall", Mat4Transform.scale(floorLength, 1, ceilingHeight));
        moveLeftWall = new TransformNode(
            "Move left wall",
            Mat4.multiplyVariable(
                Mat4Transform.translate(-floorWidth/2.0f, ceilingHeight / 2.0f, 0),
                Mat4Transform.rotateAroundZ(-90.0f),
                Mat4Transform.rotateAroundY(90.0f)
                // Mat4Transform.rotateAroundZ(10.0f)
            )
        );


        TransformNode scaleRightWall = new TransformNode(
                "Scale right wall",
                Mat4Transform.scale(floorLength, 1, ceilingHeight));
        TransformNode moveRightWall = new TransformNode(
                "Move right wall",
                Mat4.multiplyVariable(
                        Mat4Transform.translate(floorWidth/2.0f, ceilingHeight / 2.0f, 0),
                        Mat4Transform.rotateAroundZ(90.0f),
                        Mat4Transform.rotateAroundY(-90.0f)
                )
        );


        TransformNode roofTransform = new TransformNode(
            "Transform roof",
            Mat4.multiplyVariable(
                Mat4Transform.scale(floorWidth, 1, floorLength),
                Mat4Transform.translate(0.0f, ceilingHeight, 0),
                Mat4Transform.rotateAroundX(180.0f)
            )
        );

        TransformNode frontTransform = new TransformNode(
                "Transform front wall",
                Mat4.multiplyVariable(
                    Mat4Transform.translate(0, ceilingHeight / 2.0f, floorLength / 2.0f),
                    Mat4Transform.scale(floorWidth, ceilingHeight, 1),
                    Mat4Transform.rotateAroundX(-90.0f)
                )
        );

        MeshNode floorShape = new MeshNode("TwoTriangles (floor)", floor);
        MeshNode backShape = new MeshNode("TwoTriangles (back)", back);
        MeshNode leftShape = new MeshNode("TwoTriangles (left)", left);
        MeshNode rightShape = new MeshNode("TwoTriangles (right)", right);
        MeshNode roofShape = new MeshNode("TwoTriangles (roof)", roof);
        MeshNode frontShape = new MeshNode("TwoTriangles (front)", front);
        MeshNode rugShape = new MeshNode("TwoTriangles (Rug)", rug);

        TransformNode rugTransform = new TransformNode("Transform rug",
                Mat4.multiply(
                        Mat4Transform.translate(0f, 0.01f, 0f),
                        Mat4Transform.scale(5f, 1f, 7f)
                )
        );

        createPictureFrames();
        SGNode leftPictures = pictureFrameTransformations[1];
        SGNode rightPictures = pictureFrameTransformations[4];

        // This will be the anchor, things inside the room will be a child
        // of the floor.
        floorName = new NameNode("Floor");

        // Create the graph
        SGNode root = new NameNode("Room");
        root.addChild(floorName);
            floorName.addChild(floorTransform);
                floorTransform.addChild(floorShape);
            floorName.addChild(backTransform);
                backTransform.addChild(backShape);
            floorName.addChild(moveLeftWall);
                moveLeftWall.addChild(leftPictures);
                moveLeftWall.addChild(scaleLeftWall);
                scaleLeftWall.addChild(leftShape);
            floorName.addChild(moveRightWall);
                moveRightWall.addChild(rightPictures);
                moveRightWall.addChild(scaleRightWall);
                        scaleRightWall.addChild(rightShape);
            floorName.addChild(roofTransform);
                roofTransform.addChild(roofShape);
            floorName.addChild(frontTransform);
                frontTransform.addChild(frontShape);
            floorName.addChild(rugTransform);
                rugTransform.addChild(rugShape);

        root.update();
        setRoot(root);
    }

    public SGNode getAnchor() {
        return floorName;
    }
}
