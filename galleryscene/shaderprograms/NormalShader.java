package galleryscene.shaderprograms;

import engine.*;
import engine.render.*;
import gmaths.*;
import com.jogamp.opengl.*;

public class NormalShader extends ShaderConfigurator {

    protected int[] diffuseTexture, normalTexture;
    protected Material material;

    public NormalShader(GL3 gl, int[] diffuseTexture, int[] normalTexture, Material material) {
        super(gl, "shaders/correct/default.vs.glsl", "shaders/correct/normal.fs.glsl");
        this.material = material;
        this.diffuseTexture = diffuseTexture;
        this.normalTexture = normalTexture;
    }

    public NormalShader(GL3 gl, int[] diffuseTexture, int[] normalTexture) {
        this(gl, diffuseTexture, normalTexture, null);
        this.material = createDefaultMaterial();
    }

    public void sendSendDataToTheGPU(GL3 gl, Mat4 perspective, Mat4 model, WorldConfiguration worldConfig) {
        shader.use(gl);

        Camera camera = worldConfig.getCamera();
        Mat4 mvpMatrix = Mat4.multiply(perspective,
            Mat4.multiply(camera.getViewMatrix(), model));


        shader.setFloatArray(gl, "model", model.toFloatArrayForGLSL());
        shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
        shader.setVec3(gl, "viewPos", camera.getPosition());

        setupShaderForMultipleLighting(gl, worldConfig);

        shader.setVec3(gl, "material.ambient", material.getAmbient());
        shader.setVec3(gl, "material.diffuse", material.getDiffuse());
        shader.setVec3(gl, "material.specular", material.getSpecular());
        shader.setFloat(gl, "material.shininess", material.getShininess());

        shader.setInt(gl, "main_texture", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, diffuseTexture[0]);

        shader.setInt(gl, "normal_texture", 1);
        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, normalTexture[0]);

    }

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, diffuseTexture, 0);
        gl.glDeleteBuffers(1, normalTexture, 0);
    }
}
