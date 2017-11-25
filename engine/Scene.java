package engine;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import engine.*;
import gmaths.*;
import java.nio.*;
import java.util.*;
import java.util.ArrayList;
import meshes.*;

public abstract class Scene implements GLEventListener {

  private float aspect;
  protected WorldConfiguration worldConfig;
  private ArrayList<Model> models = new ArrayList<>();
  private SGNode scene;

  public Scene(Camera camera) {
    worldConfig = new WorldConfiguration(camera);
  }

  protected SGNode getSceneNode() {
      return scene;
  }

  protected void setSceneNode(SGNode scene) {
      System.out.println("Set scene node");
      System.out.println(scene);
      this.scene = scene;
  }

  protected void registerModel(Model model) {
      model.setWorldConfig(worldConfig);
      models.add(model);
  }

  protected void registerModels(Model[] models) {
      for (Model model : models) {
          registerModel(model);
      }
  }

  // protected void loadTexture(GL3 gl, String name, String path) {
  //     int[] texture = TextureLibrary.loadTexture(gl, path);
  //     textureMap.put(name, texture);
  // }
  //
  // protected int[] getTexture(String name) {
  //     return textureMap.get(name);
  // }

  protected void addModel(String label) {
    //   label.set(label, model);
  }

  // ***************************************************
  /* TIME
   */

  private double startTime;
  private double startTimeMil;

  protected double getElapsedTime() {
      return System.currentTimeMillis() - startTimeMil;
  }

  protected double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    startTime = getSeconds();
    startTimeMil = System.currentTimeMillis();

    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());

    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);
    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(GL.GL_BACK);
    initialise(gl);
  }

  protected Mat4 calcPerspectiveMatrix() {
      return Mat4Transform.perspective(45, aspect);
  }

  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    aspect = (float)width/(float)height;
    Mat4 perspective = calcPerspectiveMatrix();

    for (Model model : models) {
        model.setPerspective(perspective);
    }
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    disposeMeshes(gl);
  }


  protected void initialise(GL3 gl) {
      for (Model model : models) {
          model.initialise(gl);
      }

      buildSceneGraph(gl);
  }

  protected void render(GL3 gl) {
      update(gl);
      scene.draw(gl);
  };

  protected abstract void buildSceneGraph(GL3 gl);
  protected abstract void update(GL3 gl);

  protected void disposeMeshes(GL3 gl) {
      for (Model model : models) {
          model.disposeMeshes(gl);
      }
  };
}
