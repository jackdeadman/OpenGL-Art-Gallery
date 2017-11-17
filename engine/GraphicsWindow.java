package engine;

import gmaths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class GraphicsWindow extends JFrame implements ActionListener {

  private GLCanvas canvas;
  private FPSAnimator animator;
  private Camera camera;
  private GLEventListener scene;


  public void setSize(Dimension dimension) {
      getContentPane().setPreferredSize(dimension);
      pack();
  }

  public void setCamera(Camera camera) {
      this.camera = camera;
  }

  public void setScene(Scene scene) {
      this.scene = scene;
  }

  public void addKeyListener(KeyListener listener) {
      canvas.addKeyListener(listener);
  }

  public void addMouseMotionListener(MouseMotionListener listener) {
      canvas.addMouseMotionListener(listener);
  }

  public GraphicsWindow(String textForTitleBar, GLEventListener scene) {
    super(textForTitleBar);
    this.scene = scene;

    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);

    canvas.addGLEventListener(scene);
    getContentPane().add(canvas, BorderLayout.CENTER);



    // JMenuBar menuBar=new JMenuBar();
    // this.setJMenuBar(menuBar);
    //   JMenu fileMenu = new JMenu("File");
    //     JMenuItem quitItem = new JMenuItem("Quit");
    //     quitItem.addActionListener(this);
    //     fileMenu.add(quitItem);
    // menuBar.add(fileMenu);

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

  public void actionPerformed(ActionEvent e) {
  }

}
