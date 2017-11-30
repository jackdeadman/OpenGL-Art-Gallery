package meshes;

import java.util.*;
import engine.*;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import meshes.shaderprograms.*;

/**
Adapted TwoTriangles to only be concerned with it's vertices.
*/
public class TwoTrianglesNew extends Mesh {

  private ShaderProgram program;

  public TwoTrianglesNew(GL3 gl, ShaderProgram program) {
    super(gl, program);
    super.vertices = vertices;
    super.indices = indices;
    fillBuffers(gl);
  }

  // ***************************************************
  /* THE DATA
   */
  // anticlockwise/counterclockwise ordering
  private float[] vertices = {      // position, colour, tex coords
    -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,  // top left
    -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // bottom left
     0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // bottom right
     0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f   // top right
  };

  private int[] indices = {
      0, 1, 2,
      0, 2, 3
  };

}
