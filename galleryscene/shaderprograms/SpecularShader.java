package galleryscene.shaderprograms;

import engine.*;
import engine.render.*;
import gmaths.*;
import com.jogamp.opengl.*;

public class SpecularShader extends ShaderConfigurator {

    protected int[] diffuseTexture, specularTexture;
    protected Material material;

    public SpecularShader(GL3 gl, int[] diffuseTexture, int[] specularTexture, Material material) {
        super(gl, "shaders/correct/default.vs.glsl", "shaders/correct/specular.fs.glsl");
        this.material = material;
        this.diffuseTexture = diffuseTexture;
        this.specularTexture = specularTexture;
    }

    public SpecularShader(GL3 gl, int[] diffuseTexture, int[] specularTexture) {
        this(gl, diffuseTexture, specularTexture, null);
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

        shader.setInt(gl, "material.diffuse", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, diffuseTexture[0]);

        shader.setInt(gl, "material.specular", 1);
        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glBindTexture(GL.GL_TEXTURE_2D, specularTexture[0]);

    }

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, diffuseTexture, 0);
        gl.glDeleteBuffers(1, specularTexture, 0);
    }
}
