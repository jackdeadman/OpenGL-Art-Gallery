package meshes;

import engine.render.*;
import com.jogamp.opengl.*;

/**
Adapted TwoTriangles to only be concerned with it's vertices.
*/
public class TwoTriangles extends Mesh {

  private ShaderConfigurator program;

  public TwoTriangles(GL3 gl, ShaderConfigurator program) {
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
