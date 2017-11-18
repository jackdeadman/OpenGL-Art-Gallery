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

    public Palm(GL3 gl, Light light, Camera camera) {
        super(camera, light);

        createSceneGraph(gl);
    }

    private void createSceneGraph(GL3 gl) {
        int[] rustTexture = TextureLibrary.loadTexture(gl, "textures/metal_rust.jpg");
        int[] rustTextureSpecular = TextureLibrary.loadTexture(gl, "textures/metal_rust_specular.jpg");
        int[] metalTexture = TextureLibrary.loadTexture(gl, "textures/metal_texture.jpg");

        upperPalm = new Sphere(gl, metalTexture, rustTextureSpecular);
        lowerPalm = new Sphere(gl, rustTexture, rustTextureSpecular);

        NameNode upperPalmName = new NameNode("upperPalm");
        MeshNode upperPalmShape = new MeshNode("dsnj", upperPalm);
        MeshNode lowerPalmShape = new MeshNode("dsnj", lowerPalm);

        TransformNode upperPalmScale = new TransformNode("sanj", Mat4Transform.scale(2.6f, 0.8f, 1f));

        TransformNode lowerPalmTranslate = new TransformNode("sanj", Mat4Transform.translate(0.0f, -0.6f, 0.0f));
        TransformNode lowerPalmScale = new TransformNode("sanj", Mat4Transform.scale(2.8f, 2f, 1f));

        upperPalm.setCamera(camera);
        upperPalm.setLight(light);

        lowerPalm.setCamera(camera);
        lowerPalm.setLight(light);

        root = new NameNode("Palm");
        root.addChild(upperPalmScale);
            upperPalmScale.addChild(upperPalmShape);

        root.addChild(lowerPalmTranslate);
            lowerPalmTranslate.addChild(lowerPalmScale);
            lowerPalmScale.addChild(lowerPalmShape);
        root.update();

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
