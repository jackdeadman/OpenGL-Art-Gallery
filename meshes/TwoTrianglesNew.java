package meshes;

import java.util.*;
import engine.*;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import meshes.shaderprograms.*;

public class TwoTrianglesNew extends Mesh {

  private ShaderProgram program;

  public TwoTrianglesNew(GL3 gl, ShaderProgram program) {
    super(gl);
    super.vertices = this.vertices;
    super.indices = this.indices;
    this.program = program;

    fillBuffers(gl);
  }

  public void render(GL3 gl, Mat4 model) {
    program.sendSendDataToTheGPU(gl, perspective, model, worldConfig);

    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }

  public void dispose(GL3 gl) {
    super.dispose(gl);
    program.dispose(gl);
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

  private int[] indices = {         // Note that we start from 0!
      0, 1, 2,
      0, 2, 3
  };

}
