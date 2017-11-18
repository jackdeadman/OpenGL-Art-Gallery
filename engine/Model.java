package engine;
import meshes.*;
import models.*;
import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public abstract class Model {

    protected Camera camera;
    protected Light light;
    protected SGNode root;

    public Model(Camera camera, Light light) {
        this.camera = camera;
        this.light = light;
    }

    public SGNode getRoot() {
        return root;
    }


    public void reshape(GL3 gl, Mat4 perspective) {
        // Loop and set
    }

    public abstract void render(GL3 gl, Mat4 perspective);

}
