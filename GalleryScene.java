import engine.*;
import meshes.*;
import models.hand.*;
import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;



public class GalleryScene extends Scene {

    private Mesh floor, back;
    private Light light;
    private Hand hand;

    public GalleryScene(Camera camera) {
        super(camera);
    }

    protected void initialise(GL3 gl) {
        int[] floorTexture = TextureLibrary.loadTexture(gl, "textures/chequerboard.jpg");
        int[] containerTexture = TextureLibrary.loadTexture(gl, "textures/container2.jpg");


        // make meshes
        floor = new TwoTriangles(gl, floorTexture);
        floor.setModelMatrix(Mat4Transform.scale(16,1,16));

        back = new TwoTriangles(gl, containerTexture);
        Mat4 model = Mat4.multiply(Mat4Transform.scale(16, 16, 1), Mat4Transform.rotateAroundX(90));
        model = Mat4.multiply(Mat4Transform.translate(0, 8, -8), model);
        back.setModelMatrix(model);

        light = new Light(gl);
        light.setCamera(camera);

        hand = new Hand(gl, light, camera, aspect);
        floor.setLight(light);
        floor.setCamera(camera);

        back.setLight(light);
        back.setCamera(camera);
    }

    protected void render(GL3 gl) {
        Mat4 perspective = Mat4Transform.perspective(45, aspect);
        light.setPerspective(perspective);
        floor.setPerspective(perspective);
        back.setPerspective(perspective);
        light.render(gl);
        floor.render(gl);
        back.render(gl);
        hand.render(perspective, gl);

    }

    protected void updatePerspectiveMatrices() {

    }

    protected void disposeMeshes(GL3 gl) {

    }
}
