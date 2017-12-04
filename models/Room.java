package models;

import com.jogamp.opengl.*;

import engine.*;
import engine.render.*;
import engine.scenegraph.*;
import engine.utils.*;

import galleryscene.shaderprograms.*;

import gmaths.*;
import meshes.*;

public class Room extends Model {

    private Mesh floor, back, left, right, roof, front;
    private float floorWidth = 16, floorLength = 12, ceilingHeight = 10;
    private NameNode floorName;

    private int[] floorTexture, containerTexture, windowTexture, window2, backWallTexture, backWallNormal, ceilingTexture, windowNormal, backWallBlend;

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

    private void loadTextures(GL3 gl) {
        floorTexture = TextureLibrary.loadTexture(gl, "textures/wood_floor.jpg");
        containerTexture = TextureLibrary.loadTexture(gl, "textures/wallpaper.jpg");
        windowTexture = TextureLibrary.loadTexture(gl, "textures/window_black.jpg");
        ceilingTexture = TextureLibrary.loadTexture(gl, "textures/back_wall_tex.jpg");
        window2 = TextureLibrary.loadTexture(gl, "textures/window2.jpg");
        backWallTexture = TextureLibrary.loadTexture(gl, "textures/wall_wood.jpg");
        backWallNormal = TextureLibrary.loadTexture(gl, "textures/wall_wood_normal.jpg");
        windowNormal = TextureLibrary.loadTexture(gl, "textures/window_black_normal.jpg");
        backWallBlend = TextureLibrary.loadTexture(gl, "textures/window2_mask.jpg");
    }

    private SGNode[] pictureFrameTransformations = new SGNode[6];

    private void createPictureFrames() {
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
        // floor = new TwoTriangles(gl, floorTexture);
        floor = new TwoTrianglesNew(gl, new OneTextureShader(gl, floorTexture));

        MultiLightShader backShaderProgram = new MultiLightShader(gl, "shaders/correct/backwall.fs.glsl");
        backShaderProgram.addTexture("normal_texture", backWallNormal);
        backShaderProgram.addTexture("main_texture", backWallTexture);
        backShaderProgram.addTexture("blend_texture", backWallBlend);
        backShaderProgram.addTexture("window_texture", window2);
        back = new TwoTrianglesNew(gl, backShaderProgram);

//        back = new TwoTriangles3(gl, backWallTexture, backWallTexture, window2, backWallNormal);
        left = new TwoTriangles2(gl, windowTexture, containerTexture);
        right = new TwoTriangles2(gl, windowTexture, containerTexture);
        // roof = new TwoTriangles(gl, containerTexture);
        roof = new TwoTrianglesNew(gl, new OneTextureShader(gl, ceilingTexture));
        front = new TwoTriangles2(gl, windowTexture, containerTexture);

        registerMeshes(new Mesh[] { floor, back, left, right, roof, front });
    }

    private void buildSceneGraph() {

        TransformNode floorTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4Transform.scale(floorWidth, 1, floorLength)
        );

        TransformNode backTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0, ceilingHeight / 2.0f, -floorLength / 2.0f),
                Mat4Transform.scale(floorWidth, ceilingHeight, 1),
                Mat4Transform.rotateAroundX(90.0f)
            )
        );

        TransformNode scaleLeftWall = new TransformNode("", Mat4Transform.scale(floorLength, 1, ceilingHeight));

        moveLeftWall = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                Mat4Transform.translate(-floorWidth/2.0f, ceilingHeight / 2.0f, 0),
                Mat4Transform.rotateAroundZ(-90.0f),
                Mat4Transform.rotateAroundY(90.0f)
                // Mat4Transform.rotateAroundZ(10.0f)
            )
        );


        TransformNode scaleRightWall = new TransformNode(String.format("Scale(1f, %f, %f)", ceilingHeight, floorLength),
                Mat4Transform.scale(floorLength, 1, ceilingHeight));
        TransformNode moveRightWall = new TransformNode(
                "Scale(16, 1, 16)",
                Mat4.multiplyVariable(
                        Mat4Transform.translate(floorWidth/2.0f, ceilingHeight / 2.0f, 0),
                        Mat4Transform.rotateAroundZ(90.0f),
                        Mat4Transform.rotateAroundY(-90.0f)
                )
        );


        TransformNode roofTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                // Mat4Transform.translate(0, ceilingHeight / 2.0f, -floorLength / 2.0f),
                // Mat4Transform.scale(floorWidth, ceilingHeight, 1),
                Mat4Transform.scale(floorWidth, 1, floorLength),
                Mat4Transform.translate(0.0f, ceilingHeight, 0),
                Mat4Transform.rotateAroundX(180.0f)
            )
        );

        TransformNode frontTransform = new TransformNode(
                String.format("RotateAroundX(-90 Deg); Scale(%f, %f, 1f); Translate(0f, %f, %f)",
                        floorWidth, ceilingHeight, ceilingHeight/2f, floorLength/2f),
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

        createPictureFrames();
        SGNode leftPictures = pictureFrameTransformations[1];
        SGNode rightPictures = pictureFrameTransformations[4];

        floorName = new NameNode("Floor");

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

        root.update();
        setRoot(root);
    }

    public SGNode getAnchor() {
        return floorName;
    }
}
