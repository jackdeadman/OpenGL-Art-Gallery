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

        Vec3 colour = new Vec3(1.0f, 1.0f, 1.0f);
        Vec3 attenuation = new Vec3(1.0f, 0.09f, 0.032f);
        PointLight light = new PointLight(colour, attenuation);

        LightNode lightNode = new LightNode("", light);

        root = new NameNode("Lamp");
            root.addChild(transformCube);
                transformCube.addChild(lightNode);
                transformCube.addChild(cubeShape);

        root.update();
        System.out.println(light.getPosition());
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
