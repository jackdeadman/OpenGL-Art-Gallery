package engine.render;

import com.jogamp.opengl.*;
import engine.*;
import engine.scenegraph.*;
import engine.lighting.*;
import gmaths.*;

import java.util.ArrayList;


public abstract class Scene implements GLEventListener {
  /**
    * @author Jack Deadman
  */

  protected WorldConfiguration worldConfig;
  private float aspect;
  private ArrayList<Model> models = new ArrayList<>();
  private SGNode scene;

  public Scene(Camera camera) {
    worldConfig = new WorldConfiguration(camera);
  }

  public WorldConfiguration getWorldConfig() {
      return worldConfig;
  }

  protected void setDirectionalLight(DirectionalLight light) {
      worldConfig.setDirectionalLight(light);
  }

  protected DirectionalLight getDirectionalLight() {
      return worldConfig.getDirectionalLight();
  }

  protected void setSceneNode(SGNode scene) {
      this.scene = scene;
  }

  protected void registerModel(Model model) {
      // Set the config so the models know about the camera and lighting.
      model.setWorldConfig(worldConfig);
      models.add(model);
  }

  protected void registerModels(Model[] models) {
      // Helper for multiple models.
      for (Model model : models) {
          registerModel(model);
      }
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
    gl.glEnable(GL.GL_BLEND);
    gl.glEnable(GL.GL_DEPTH_TEST);

    // Allow transparency
    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);
    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(GL.GL_BACK);


    initialise(gl);
  }

  private Mat4 calcPerspectiveMatrix() {
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


  private void initialise(GL3 gl) {
      for (Model model : models) {
          model.initialise(gl);
      }

      buildSceneGraph(gl);
  }

  protected void render(GL3 gl) {
      update(gl);
      beforeSceneDraw(gl);
      scene.draw(gl);
      afterSceneDraw(gl);
  };

  protected abstract void buildSceneGraph(GL3 gl);
  protected abstract void update(GL3 gl);

  // Optional callbacks to override. Default to no ops
  protected void beforeSceneDraw(GL3 gl) {};
  protected void afterSceneDraw(GL3 gl) {};

  private void disposeMeshes(GL3 gl) {
      for (Model model : models) {
          model.disposeMeshes(gl);
      }
  };
}
