package engine;

import engine.*;
import meshes.*;
import gmaths.*;

import java.nio.*;
import java.util.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public abstract class Scene implements GLEventListener {

  protected float aspect;

  protected Camera camera;
  private HashMap<String, int[]> textureMap = new HashMap<>();
  // private HashMap<String, Model> modelMap = new HashMap<>();

  public Scene(Camera camera) {
    this.camera = camera;
  }

  protected void loadTexture(GL3 gl, String name, String path) {
      int[] texture = TextureLibrary.loadTexture(gl, path);
      textureMap.put(name, texture);
  }

  protected int[] getTexture(String name) {
      return textureMap.get(name);
  }

  protected void addModel(String label) {
    //   label.set(label, model);
  }

  // ***************************************************
  /* TIME
   */

  private double startTime;

  protected double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    startTime = getSeconds();

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

  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    aspect = (float)width/(float)height;
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

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   */

  protected abstract void initialise(GL3 gl);
  protected abstract void render(GL3 gl);
  protected abstract void updatePerspectiveMatrices();
  protected abstract void disposeMeshes(GL3 gl);
}
