package engine.render;

import com.jogamp.opengl.*;

import gmaths.*;
import engine.*;
import engine.scenegraph.*;

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

    // Need to be able to register models here as well, as a model can be composed
    // of further models e.g finger of a hand
    protected void registerModel(Model model) {
        model.setWorldConfig(worldConfig);
        models.add(model);
    }

    // Helper to add multiple models
    protected void registerModels(Model[] models) {
        for (Model model : models) {
            registerModel(model);
        }
    }

    // Register a new mesh so configurations (e.g light, camera)
    // can be automatically set as well as automatically disposed.
    protected void registerMesh(Mesh mesh) {
        mesh.setWorldConfig(worldConfig);
        meshes.add(mesh);
    }

    // Helper to register multiple meshes.
    protected void registerMeshes(Mesh[] meshes) {
        for (Mesh mesh : meshes) {
            registerMesh(mesh);
        }
    }

    // Dispose all the registered meshes
    public void disposeMeshes(GL3 gl) {
        // Dispose all the meshes in the model
        for (Mesh mesh : meshes) {
            mesh.dispose(gl);
        }

        // Recursively dispose all meshes in the
        // containing models as well
        for (Model model: models) {
            model.disposeMeshes(gl);
        }
    }

    public void setPerspective(Mat4 perspective) {
        // Set the perspective matrix for all meshes
        for (Mesh mesh : meshes) {
            mesh.setPerspective(perspective);
        }

        // Set the perspective matrix for all the
        // containing models as well.
        for (Model model: models) {
            model.setPerspective(perspective);
        }
    }

    protected abstract void start(GL3 gl);

    protected void initialise(GL3 gl) {
        for (Model model: models) {
            System.out.println(model);
            model.initialise(gl);
        }
        start(gl);
    }

    // No ops for useful callbacks that can be
    // overridden.
    protected void onBeforeRender(GL3 gl) {}
    protected void onAfterRender(GL3 gl) {}

    public void render(GL3 gl) {
        // Useful callback hook for
        onBeforeRender(gl);
        root.update();
        root.draw(gl);
        onAfterRender(gl);
    }

}
