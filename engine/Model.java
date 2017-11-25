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
import java.util.*;

public abstract class Model {


    protected WorldConfiguration worldConfig;
    private ArrayList<Mesh> meshes = new ArrayList<Mesh>();
    private SGNode root;


    public Model(WorldConfiguration worldConfig) {
        this.worldConfig = worldConfig;
    }


    public void setWorldConfig(WorldConfiguration worldConfig) {
        this.worldConfig = worldConfig;
    }

    public SGNode getRoot() {
        return root;
    }

    protected void setRoot(SGNode root) {
        this.root = root;
    }

    protected void registerMesh(Mesh mesh) {
        mesh.setWorldConfig(worldConfig);
        meshes.add(mesh);
    }

    protected void registerMeshes(Mesh[] meshes) {
        for (Mesh mesh : meshes) {
            registerMesh(mesh);
        }
    }

    public void disposeMeshes(GL3 gl) {
        for (Mesh mesh : meshes) {
            mesh.dispose(gl);
        }
    }

    public void setPerspective(Mat4 perspective) {
        for (Mesh mesh : meshes) {
            mesh.setPerspective(perspective);
        }
    }

    protected abstract void start(GL3 gl);

    protected void initialise(GL3 gl) {
        start(gl);
    }

}
