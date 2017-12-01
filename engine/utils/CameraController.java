package engine.utils;

import gmaths.*;
import engine.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class CameraController extends KeyAdapter implements MouseMotionListener {

    Camera camera;
    Point lastpoint;

    public CameraController(Camera camera) {
        this.camera = camera;
    }

    public void keyPressed(KeyEvent e) {
        Camera.Movement m = Camera.Movement.NO_MOVEMENT;
        switch (e.getKeyCode()) {
          case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
          case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
          case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
          case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
          case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
          case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
        }
        camera.keyboardInput(m);
      }


    public void mouseDragged(MouseEvent e) {
        Point ms = e.getPoint();
        float sensitivity = 0.001f;
        float dx=(float) (ms.x-lastpoint.x)*sensitivity;
        float dy=(float) (ms.y-lastpoint.y)*sensitivity;
        //System.out.println("dy,dy: "+dx+","+dy);
        if (e.getModifiers()==MouseEvent.BUTTON1_MASK)
          camera.updateYawPitch(dx, -dy);
        lastpoint = ms;
    }

    /**
    * mouse is used to control camera position
    *
    * @param e  instance of MouseEvent
    */
    public void mouseMoved(MouseEvent e) {
        lastpoint = e.getPoint();
    }
}
