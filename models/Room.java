package models;

import engine.*;
import meshes.*;
import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Room extends Model {

    private Mesh floor, back, left, right, roof, front;
    private final float FLOOR_WIDTH = 14;
    private final float FLOOR_LENGTH = 10;
    private final float CEILING_HEIGHT = 8;
    private NameNode floorName;

    public Room(GL3 gl, Light light, Camera camera) {
        super(camera, light);

        buildSceneGraph(gl);
    }

    private void buildSceneGraph(GL3 gl) {
        int[] floorTexture = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
        int[] containerTexture = TextureLibrary.loadTexture(gl, "textures/container2.jpg");

        // make meshes
        floor = new TwoTriangles(gl, floorTexture);
        back = new TwoTriangles(gl, containerTexture);
        left = new TwoTriangles(gl, containerTexture);
        right = new TwoTriangles(gl, containerTexture);
        roof = new TwoTriangles(gl, containerTexture);
        front = new TwoTriangles(gl, containerTexture);

        floor.setCamera(camera);
        floor.setLight(light);

        back.setCamera(camera);
        back.setLight(light);

        left.setCamera(camera);
        left.setLight(light);

        right.setCamera(camera);
        right.setLight(light);

        roof.setCamera(camera);
        roof.setLight(light);

        front.setCamera(camera);
        front.setLight(light);

        TransformNode floorTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4Transform.scale(FLOOR_WIDTH, 1, FLOOR_LENGTH)
        );

        TransformNode backTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0, CEILING_HEIGHT / 2.0f, -FLOOR_LENGTH / 2.0f),
                Mat4Transform.scale(FLOOR_WIDTH, CEILING_HEIGHT, 1),
                Mat4Transform.rotateAroundX(90.0f)
            )
        );

        TransformNode leftTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                // Mat4Transform.translate(0, CEILING_HEIGHT / 2.0f, -FLOOR_LENGTH / 2.0f),
                // Mat4Transform.scale(FLOOR_WIDTH, CEILING_HEIGHT, 1),
                Mat4Transform.scale(1, CEILING_HEIGHT, FLOOR_LENGTH),
                Mat4Transform.translate(-FLOOR_WIDTH/2.0f, 0.5f, 0),
                Mat4Transform.rotateAroundZ(-90.0f),
                Mat4Transform.rotateAroundY(90.0f)
                // Mat4Transform.rotateAroundZ(10.0f)
            )
        );

        TransformNode rightTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                // Mat4Transform.translate(0, CEILING_HEIGHT / 2.0f, -FLOOR_LENGTH / 2.0f),
                // Mat4Transform.scale(FLOOR_WIDTH, CEILING_HEIGHT, 1),
                Mat4Transform.scale(1, CEILING_HEIGHT, FLOOR_LENGTH),
                Mat4Transform.translate(FLOOR_WIDTH/2.0f, 0.5f, 0),
                Mat4Transform.rotateAroundZ(90.0f),
                Mat4Transform.rotateAroundY(-90.0f)
                // Mat4Transform.rotateAroundZ(10.0f)
            )
        );

        TransformNode roofTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                // Mat4Transform.translate(0, CEILING_HEIGHT / 2.0f, -FLOOR_LENGTH / 2.0f),
                // Mat4Transform.scale(FLOOR_WIDTH, CEILING_HEIGHT, 1),
                Mat4Transform.scale(FLOOR_WIDTH, 1, FLOOR_LENGTH),
                Mat4Transform.translate(0.0f, CEILING_HEIGHT, 0),
                Mat4Transform.rotateAroundX(180.0f)
            )
        );

        TransformNode frontTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0, CEILING_HEIGHT / 2.0f, FLOOR_LENGTH / 2.0f),
                // Mat4Transform.translate(0, 1.0f, 0),
                Mat4Transform.scale(FLOOR_WIDTH, CEILING_HEIGHT, 1),
                Mat4Transform.rotateAroundX(-90.0f)
            )
        );

        MeshNode floorShape = new MeshNode("TwoTriangles", floor);
        MeshNode backShape = new MeshNode("TwoTriangles", back);
        MeshNode leftShape = new MeshNode("TwoTriangles", left);
        MeshNode rightShape = new MeshNode("TwoTriangles", right);
        MeshNode roofShape = new MeshNode("TwoTriangles", roof);
        MeshNode frontShape = new MeshNode("TwoTriangles", front);

        floorName = new NameNode("Floor");

        root = new NameNode("Room");
        root.addChild(floorName);
            floorName.addChild(floorTransform);
                floorTransform.addChild(floorShape);
            floorName.addChild(backTransform);
                backTransform.addChild(backShape);
            floorName.addChild(leftTransform);
                leftTransform.addChild(leftShape);
            floorName.addChild(rightTransform);
                rightTransform.addChild(rightShape);
            floorName.addChild(roofTransform);
                roofTransform.addChild(roofShape);
            floorName.addChild(frontTransform);
                frontTransform.addChild(frontShape);

        root.update();

        // Mat4 model = Mat4.multiply(Mat4Transform.scale(16, 16, 1), Mat4Transform.rotateAroundX(90));
        // model = Mat4.multiply(Mat4Transform.translate(0, 8, -8), model);
        // back.setModelMatrix(model);

    }

    public void setPerspective(Mat4 perspective) {
        floor.setPerspective(perspective);
        back.setPerspective(perspective);
        left.setPerspective(perspective);
        right.setPerspective(perspective);
        roof.setPerspective(perspective);
        front.setPerspective(perspective);
    }

    public SGNode getAnchor() {
        return floorName;
    }

    public void render(GL3 gl, Mat4 perspective) {
        root.draw(gl);
    }
}
