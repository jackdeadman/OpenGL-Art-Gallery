package meshes;

import java.util.*;
import engine.*;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class TwoTriangles extends Mesh {

  private int[] textureId;

  public TwoTriangles(GL3 gl, int[] textureId) {
    super(gl);
    super.vertices = this.vertices;
    super.indices = this.indices;
    this.textureId = textureId;
    material.setAmbient(0.0f, 0.0f, 0.0f);
    material.setDiffuse(0.2f, 0.2f, 0.2f);
    material.setSpecular(0.20f, 0.20f, 0.20f);
    material.setShininess(1.0f);
    shader = new Shader(gl, "shaders/vs_tt_05.txt", "shaders/fs_tt_05.txt");
    fillBuffers(gl);
  }

  public void render(GL3 gl, Mat4 model) {

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


    Spotlight spotlight = worldConfig.getSpotlight();
    shader.setVec3(gl, "spotlight.position", spotlight.getPosition());
    shader.setVec3(gl, "spotlight.direction", spotlight.getDirection());
    shader.setFloat(gl, "spotlight.cutOff", spotlight.getCutOff());
    shader.setFloat(gl, "spotlight.outerCutOff", spotlight.getOuterCutOff());

    shader.setFloat(gl, "spotlight.constant", spotlight.getConstant());
    shader.setFloat(gl, "spotlight.linear", spotlight.getLinear());
    shader.setFloat(gl, "spotlight.quadratic", spotlight.getQuadratic());

    shader.setVec3(gl, "spotlight.ambient", spotlight.getMaterial().getAmbient());
    shader.setVec3(gl, "spotlight.diffuse", spotlight.getMaterial().getDiffuse());
    shader.setVec3(gl, "spotlight.specular", spotlight.getMaterial().getSpecular());

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

    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());

    shader.setInt(gl, "first_texture", 0);

    gl.glActiveTexture(GL.GL_TEXTURE0);
    gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[0]);

    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }

  public void dispose(GL3 gl) {
    super.dispose(gl);
    gl.glDeleteBuffers(1, textureId, 0);
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
