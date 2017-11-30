package engine;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import java.util.*;

public abstract class MeshImproved extends Mesh{

  protected float[] vertices;
  protected int[] indices;
  private int vertexStride = 8;
  private int vertexXYZFloats = 3;
  private int vertexNormalFloats = 3;
  private int vertexTexFloats = 2;
  private int[] vertexBufferId = new int[1];
  protected int[] vertexArrayId = new int[1];
  private int[] elementBufferId = new int[1];

  protected Material material;
  protected Shader shader;
  protected Mat4 model;

  protected Mat4 perspective;
  protected WorldConfiguration worldConfig;

  protected boolean useMultiLighting = false;

  protected int[][] textures;
  protected int[][] specularMaps;
  protected int[][] diffuseMaps;
  protected int[][] normalMaps;

  public MeshImproved(GL3 gl, String vs, String fs, boolean useMultiLighting,
              Material material, int[][] textures, int[][] specularMaps,
              int[][] diffuseMaps, int[][] normalMaps) {

    super(gl);
    model = new Mat4(1);

    this.useMultiLighting = useMultiLighting;
    this.material = material;
    this.textures = textures;
    this.specularMaps = specularMaps;
    this.diffuseMaps = diffuseMaps;
    this.normalMaps = normalMaps;

    shader = new Shader(gl, vs, fs);
  }

  public void setWorldConfig(WorldConfiguration worldConfig) {
      this.worldConfig = worldConfig;
  }

  public void setModelMatrix(Mat4 m) {
    model = m;
  }

  public void setPerspective(Mat4 perspective) {
    this.perspective = perspective;
  }

  private void deleteTextureBuffers(int[][] textures, GL3 gl) {
      for (int i=0; i<textures.length; ++i) {
          gl.glDeleteBuffers(1, textures[i], 0);
      }
  }

  public void dispose(GL3 gl) {
    gl.glDeleteBuffers(1, vertexBufferId, 0);
    gl.glDeleteVertexArrays(1, vertexArrayId, 0);
    gl.glDeleteBuffers(1, elementBufferId, 0);

    deleteTextureBuffers(textures, gl);
    deleteTextureBuffers(specularMaps, gl);
    deleteTextureBuffers(diffuseMaps, gl);
    deleteTextureBuffers(normalMaps, gl);
  }

  protected void fillBuffers(GL3 gl) {
    gl.glGenVertexArrays(1, vertexArrayId, 0);
    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glGenBuffers(1, vertexBufferId, 0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);
    FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);

    gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);

    int stride = vertexStride;
    int numXYZFloats = vertexXYZFloats;
    int offset = 0;
    gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
    gl.glEnableVertexAttribArray(0);

    int numNormalFloats = vertexNormalFloats; // x,y,z for each vertex
    offset = numXYZFloats*Float.BYTES;  // the normal values are three floats after the three x,y,z values
                                    // so change the offset value
    gl.glVertexAttribPointer(1, numNormalFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
                                    // the vertex shader uses location 1 (sometimes called index 1)
                                    // for the normal information
                                    // location, size, type, normalize, stride, offset
                                    // offset is relative to the start of the array of data
    gl.glEnableVertexAttribArray(1);// Enable the vertex attribute array at location 1

    // now do the texture coordinates  in vertex attribute 2
    int numTexFloats = vertexTexFloats;
    offset = (numXYZFloats+numNormalFloats)*Float.BYTES;
    gl.glVertexAttribPointer(2, numTexFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
    gl.glEnableVertexAttribArray(2);

    gl.glGenBuffers(1, elementBufferId, 0);
    IntBuffer ib = Buffers.newDirectIntBuffer(indices);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * indices.length, ib, GL.GL_STATIC_DRAW);
    gl.glBindVertexArray(0);
  }

  private void loadTextures(GL3 gl, String varName, int[][] texturesToLoad, int offset) {
      for (int i=0; i<specularMaps.length; ++i) {
          shader.setInt(gl, varName + "[" + i + "]", 0);
          // Can just do i+ as they are numerated internally
          gl.glActiveTexture(GL.GL_TEXTURE0+i);
          gl.glBindTexture(GL.GL_TEXTURE_2D, texturesToLoad[i][0]);
      }
  }

  public void setupShaderForMultiLight(GL3 gl) {

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
  }

  public void render(GL3 gl, Mat4 model) {
      Camera camera = worldConfig.getCamera();
      Mat4 mvpMatrix = Mat4.multiply(perspective, Mat4.multiply(camera.getViewMatrix(), model));

      if (useMultiLighting) {
          setupShaderForMultiLight(gl);
      }

      // Load textures onto the GPU
      int offset = 0;
      loadTextures(gl, "textures", textures, offset);

      offset += textures.length;
      loadTextures(gl, "normalMaps", normalMaps, offset);

      offset += normalMaps.length;
      loadTextures(gl, "specularMaps", specularMaps, offset);

      offset += specularMaps.length;
      loadTextures(gl, "diffuseMaps", diffuseMaps, offset);

      offset += diffuseMaps.length;
      loadTextures(gl, "normalMaps", normalMaps, offset);

      gl.glBindVertexArray(vertexArrayId[0]);
      gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
      gl.glBindVertexArray(0);

  };



  public void render(GL3 gl) {
    render(gl, model);
  }

}
