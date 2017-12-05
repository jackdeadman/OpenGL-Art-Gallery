package shaders.shaderconfigurators;

import engine.render.*;

import com.jogamp.opengl.*;

import engine.*;
import gmaths.*;

public class SkyBoxShader extends ShaderConfigurator {

    protected Material material;
    protected int[] texture;

    public SkyBoxShader(GL3 gl, int[] texture) {
        super(gl, "shaders/glsl/default.vs.glsl", "shaders/glsl/skybox.fs.glsl");
        this.texture = texture;
    }


    public void sendSendDataToTheGPU(GL3 gl, Mat4 perspective, Mat4 model, WorldConfiguration worldConfig) {
        shader.use(gl);

        Camera camera = worldConfig.getCamera();
        Mat4 mvpMatrix = Mat4.multiply(perspective,
                Mat4.multiply(camera.getSkyboxViewMatrix(), model));


        shader.setFloatArray(gl, "model", model.toFloatArrayForGLSL());
        shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
        shader.setVec3(gl, "viewPos", camera.getPosition());


        shader.setInt(gl, "main_texture", 0);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0]);

    }

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, texture, 0);
    }
}
