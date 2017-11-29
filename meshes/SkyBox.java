package meshes;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import engine.*;
import engine.PointLight;
import gmaths.*;
import java.nio.*;
import java.util.ArrayList;

/**
    Edited version of Steve Maddock's Cube Mesh
    @author Jack Deadman
*/

public class SkyBox extends Mesh {

  private int[] textureId1;
  private int[] textureId2;

  public SkyBox(GL3 gl, int[] textureId1, int[] textureId2) {
    super(gl);
    super.vertices = this.vertices;
    super.indices = this.indices;
    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
    material.setAmbient(1.0f, 0.5f, 0.31f);
    material.setDiffuse(1.0f, 0.5f, 0.31f);
    material.setSpecular(0.5f, 0.5f, 0.5f);
    material.setShininess(32.0f);
    shader = new Shader(gl, "shaders/vs_cube_04.txt", "shaders/fs_cube_04.txt");
    fillBuffers(gl);
  }

  public void render(GL3 gl, Mat4 model) {
    //Mat4 model = getObjectModelMatrix();
    Camera camera = worldConfig.getCamera();

    Mat4 mvpMatrix = Mat4.multiply(perspective, Mat4.multiply(camera.getViewMatrix(), model));

    shader.use(gl);
    shader.setFloatArray(gl, "model", model.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());

    shader.setVec3(gl, "viewPos", camera.getPosition());


    DirectionalLight dirLight = worldConfig.getDirectionalLight();
    shader.setVec3(gl, "dirLight.direction", dirLight.getDirection());
    shader.setVec3(gl, "dirLight.ambient", dirLight.getMaterial().getAmbient());
    shader.setVec3(gl, "dirLight.diffuse", dirLight.getMaterial().getDiffuse());
    shader.setVec3(gl, "dirLight.specular", dirLight.getMaterial().getSpecular());

    ArrayList<PointLight> pointLights = worldConfig.getPointLights();

    for (int i=0; i<pointLights.size(); ++i) {

        PointLight light = pointLights.get(i);
        shader.setVec3(gl, "pointLights[" + i + "].position", light.getPosition());
        shader.setVec3(gl, "pointLights[" + i + "].ambient", light.getMaterial().getAmbient());
        shader.setVec3(gl, "pointLights[" + i + "].diffuse", light.getMaterial().getDiffuse());
        shader.setVec3(gl, "pointLights[" + i + "].specular", light.getMaterial().getSpecular());

        Vec3 attenuation = light.getAttenuation();
        shader.setFloat(gl, "pointLights[" + i + "].constant", attenuation.x);
        shader.setFloat(gl, "pointLights[" + i + "].linear", attenuation.y);
        shader.setFloat(gl, "pointLights[" + i + "].quadratic", attenuation.z);
    }


    //shader.setVec3(gl, "material.ambient", material.getAmbient());
    //shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    //shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());

    shader.setInt(gl, "material.diffuse", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
    shader.setInt(gl, "material.specular", 1);

    gl.glActiveTexture(GL.GL_TEXTURE0);
    gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
    gl.glActiveTexture(GL.GL_TEXTURE1);
    gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);

    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }

  public void dispose(GL3 gl) {
    super.dispose(gl);
    gl.glDeleteBuffers(1, textureId1, 0);
    gl.glDeleteBuffers(1, textureId2, 0);
  }

  // ***************************************************
  /* THE DATA
   */
  // anticlockwise/counterclockwise ordering

   private float[] vertices = new float[] {  // x,y,z, nx,ny,nz, s,t
       -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0.0f,  0.25f, 0.33f,  // 0 // done
       -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f,  0.0f, 0.33f,  // 1 // done
       -0.5f,  0.5f, -0.5f,  1.0f, 0.0f, 0.0f,  0.25f, 0.66f,  // 2 // done
       -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f,  0.0f, 0.66f,  // 3 // done

        0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0.0f,  0.5f, 0.66f,  // 4 =
        0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f,  0.4f, 0.33f,  // 5
        0.5f,  0.5f, -0.5f,  1.0f, 0.0f, 0.0f,  0.75f, 0.66f,  // 6 =
        0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f,  0.75f, 0.33f,  // 7 =

       -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  0.67f, 0.33f,  // 8+
       -0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f,  0.33f, 0.67f,  // 9-
       -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  0.67f, 0.67f,  // 10+
       -0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 0.0f,  0.33f, 1.0f,  // 11-
        0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  0.33f, 0.33f,  // 12+
        0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f,  0.67f, 0.67f,  // 13-
        0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  0.33f, 0.67f,  // 14+
        0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 0.0f,  0.67f, 1.0f,  // 15-

       -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 1.0f,  0.67f, 0.67f,  // 16-
       -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f,  0.67f, 1.0f,  // 17-
       -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f,  0.0f, 0.67f,  // 18+
       -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f,  0.0f, 0.33f,  // 19+
        0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 1.0f,  1.0f, 0.67f,  // 20-
        0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f,  1.0f, 1.0f,  // 21-
        0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f,  0.33f, 0.67f,  // 22+
        0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f,  0.33f, 0.33f   // 23+
   };

   private int[] indices =  new int[] {
      3,1,0, // x -ve // done
      0,2,3, // x -ve // done
      7,6,4, // x +ve
      4,5,7, // x +ve

      15,13,9, // z +ve
      9,11,15, // z +ve
      14,10,8, // z -ve
      8,12,14, // z -ve
      21,20,16, // y -ve
      16,17,21, // y -ve
      18,22,23, // y +ve
      23,19,18  // y +ve
  };

}
