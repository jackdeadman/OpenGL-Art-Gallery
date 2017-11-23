package models;

import engine.*;
import gmaths.*;
import meshes.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Lamp extends LightEmittingModel {

    // private Light light;
    private Light modelLight;
    private Mesh cube;

    public Lamp(GL3 gl, Light light, Camera camera) {
        super(camera, light);
        buildSceneGraph(gl);
    }

    private void buildSceneGraph(GL3 gl) {
        int[] rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        int[] rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        int[] metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");

        // Meshes
        cube = new Cube(gl, rustTexture, rustTextureSpecular);
        cube.setCamera(camera);
        cube.setLight(light);

        MeshNode cubeShape = new MeshNode("", cube);
        TransformNode transformCube = new TransformNode(
            "",
            Mat4.multiply(
                Mat4Transform.scale(1, 2.0f, 1),
                Mat4Transform.translate(0, 0.5f, 0)
            )
        );

        LightNode light = new LightNode("");

        root = new NameNode("Lamp");
            root.addChild(transformCube);
                transformCube.addChild(light);
                transformCube.addChild(cubeShape);

        root.update();
    }

    public Light getLight() {
        return modelLight;
    }

    public void render(GL3 gl, Mat4 perspective) {

    }

    public void setPerspective(Mat4 perspective) {
        cube.setPerspective(perspective);
    }
}
