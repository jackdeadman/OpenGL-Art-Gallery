package meshes;

import java.util.*;
import engine.*;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class TwoTrianglesImproved extends MeshImproved {

  public TwoTrianglesImproved(
            GL3 gl, String vs, String fs, boolean useMultiLighting,
            Material material, int[][] textures, int[][] specularMaps,
            int[][] diffuseMaps, int[][] normalMaps) {

    super(gl, vs, fs, useMultiLighting, material, textures, specularMaps,
            diffuseMaps, normalMaps);

    super.vertices = this.vertices;
    super.indices = this.indices;

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

  private int[] indices = {         // Note that we start from 0!
      0, 1, 2,
      0, 2, 3
  };

}
