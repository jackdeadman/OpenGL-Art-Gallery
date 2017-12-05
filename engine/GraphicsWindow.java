package engine;

import engine.render.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

// An abstraction to create a OpenGL enable JFrame
public class GraphicsWindow extends JFrame {

  private GLCanvas canvas;
  private FPSAnimator animator;


  public void setSize(Dimension dimension) {
      getContentPane().setPreferredSize(dimension);
      pack();
  }


  public void addKeyListener(KeyListener listener) {
      canvas.addKeyListener(listener);
  }

  public void addMouseMotionListener(MouseMotionListener listener) {
      canvas.addMouseMotionListener(listener);
  }

  public GraphicsWindow(String textForTitleBar, GLEventListener scene) {
    super(textForTitleBar);

    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));

    // Anti-aliasing
    glcapabilities.setSampleBuffers(true);
    glcapabilities.setNumSamples(4);

    canvas = new GLCanvas(glcapabilities);

    // Attach our scene to the canvas
    canvas.addGLEventListener(scene);
    getContentPane().add(canvas, BorderLayout.CENTER);

  }

  public void start() {

      addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          animator.stop();
          remove(canvas);
          dispose();
          System.exit(0);
        }
      });

      animator = new FPSAnimator(canvas, 60);
      animator.start();
  }

}
