package meshes;

import engine.render.*;
import com.jogamp.opengl.*;

public class Sphere extends Mesh {

  private int[] textureId1;
  private int[] textureId2;

  public Sphere(GL3 gl, ShaderConfigurator program) {
    super(gl, program);

    createVertices();
    super.vertices = this.vertices;
    super.indices = this.indices;

    fillBuffers(gl);
  }

  // ***************************************************
  /* THE DATA: Created by Steve Maddock
   */
  // anticlockwise/counterclockwise ordering


  private float[] vertices;
  private int[] indices;

  private void createVertices() {
    int XLONG = 30;
    int YLAT = 30;
    double r = 0.5;
    int step = 8;
    //float[]
    vertices = new float[XLONG*YLAT*step];
    for (int j = 0; j<YLAT; ++j) {
      double b = Math.toRadians(-90+180*(double)(j)/(YLAT-1));
      for (int i = 0; i<XLONG; ++i) {
        double a = Math.toRadians(360*(double)(i)/(XLONG-1));
        double z = Math.cos(b) * Math.cos(a);
        double x = Math.cos(b) * Math.sin(a);
        double y = Math.sin(b);
        vertices[j*XLONG*step+i*step+0] = (float)(r*x);
        vertices[j*XLONG*step+i*step+1] = (float)(r*y);
        vertices[j*XLONG*step+i*step+2] = (float)(r*z);
        vertices[j*XLONG*step+i*step+3] = (float)x;
        vertices[j*XLONG*step+i*step+4] = (float)y;
        vertices[j*XLONG*step+i*step+5] = (float)z;
        vertices[j*XLONG*step+i*step+6] = (float)(i)/(float)(XLONG-1);
        vertices[j*XLONG*step+i*step+7] = (float)(j)/(float)(YLAT-1);
      }
    }

    indices = new int[(XLONG-1)*(YLAT-1)*6];
    for (int j = 0; j<YLAT-1; ++j) {
      for (int i = 0; i<XLONG-1; ++i) {
        indices[j*(XLONG-1)*6+i*6+0] = j*XLONG+i;
        indices[j*(XLONG-1)*6+i*6+1] = j*XLONG+i+1;
        indices[j*(XLONG-1)*6+i*6+2] = (j+1)*XLONG+i+1;
        indices[j*(XLONG-1)*6+i*6+3] = j*XLONG+i;
        indices[j*(XLONG-1)*6+i*6+4] = (j+1)*XLONG+i+1;
        indices[j*(XLONG-1)*6+i*6+5] = (j+1)*XLONG+i;
      }
    }

  }

}
