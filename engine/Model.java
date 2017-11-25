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
    private ArrayList<Model> models = new ArrayList<>();
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

    // Need to be able to register models has well has a model can be composed
    // of further models e.g finger of a hand
    protected void registerModel(Model model) {
        model.setWorldConfig(worldConfig);
        models.add(model);
    }

    protected void registerModels(Model[] models) {
        for (Model model : models) {
            registerModel(model);
        }
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

        for (Model model: models) {
            model.disposeMeshes(gl);
        }
    }

    public void setPerspective(Mat4 perspective) {
        for (Mesh mesh : meshes) {
            mesh.setPerspective(perspective);
        }

        for (Model model: models) {
            model.setPerspective(perspective);
        }
    }

    protected abstract void start(GL3 gl);

    protected void initialise(GL3 gl) {
        for (Model model: models) {
            model.start(gl);
        }
        start(gl);
    }

}
