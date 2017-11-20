package models.hand;
import engine.*;
import meshes.*;
import models.*;
import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Palm extends Model {

    private Mesh upperPalm, lowerPalm;
    private TransformNode topRotation;
    private NameNode upperPalmName;

    public Palm(GL3 gl, Light light, Camera camera) {
        super(camera, light);

        createSceneGraph(gl);
    }

    public void bendTop(float amount) {
        float degrees = amount * 90;
        topRotation.setTransform(Mat4Transform.rotateAroundX(degrees));
        root.update();
    }

    private void createSceneGraph(GL3 gl) {
        int[] rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        int[] rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        int[] metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");

        upperPalm = new Sphere(gl, metalTexture, rustTextureSpecular);
        lowerPalm = new Sphere(gl, rustTexture, rustTextureSpecular);

        upperPalmName = new NameNode("upperPalm");
        NameNode lowerPalmName = new NameNode("lowerPalm");
        MeshNode upperPalmShape = new MeshNode("dsnj", upperPalm);
        MeshNode lowerPalmShape = new MeshNode("dsnj", lowerPalm);

        TransformNode upperPalmScale = new TransformNode("sanj", Mat4Transform.scale(2.6f, 1.2f, 1f));
        topRotation = new TransformNode("ssnj", Mat4Transform.rotateAroundX(0f));

        TransformNode lowerPalmTranslate = new TransformNode("sanj", Mat4Transform.translate(0.0f, -0.8f, 0.0f));
        TransformNode lowerPalmScale = new TransformNode("sanj", Mat4Transform.scale(2.8f, 3f, 1f));
        TransformNode upperPalmTranslate = new TransformNode("snjn", Mat4Transform.translate(0.0f, 0.25f, 0.0f));

        upperPalm.setCamera(camera);
        upperPalm.setLight(light);

        lowerPalm.setCamera(camera);
        lowerPalm.setLight(light);

        root = new NameNode("Palm");

        root.addChild(lowerPalmName);
            lowerPalmName.addChild(lowerPalmTranslate);
            lowerPalmTranslate.addChild(lowerPalmScale);
            lowerPalmScale.addChild(lowerPalmShape);

            lowerPalmName.addChild(upperPalmName);
            upperPalmName.addChild(topRotation);
            upperPalmName.addChild(upperPalmTranslate);
                upperPalmTranslate.addChild(topRotation);
                topRotation.addChild(upperPalmScale);
            upperPalmScale.addChild(upperPalmShape);

        root.update();

    }

    public SGNode getAnchor() {
        return topRotation;
    }

    public void setPerspective(Mat4 perspective) {
        upperPalm.setPerspective(perspective);
        lowerPalm.setPerspective(perspective);
    }


    public void render(GL3 gl, Mat4 perspective) {
        upperPalm.setPerspective(perspective);
        lowerPalm.setPerspective(perspective);

        root.draw(gl);
    }

}
