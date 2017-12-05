package shaders.shaderconfigurators;

import engine.*;
import engine.render.ShaderConfigurator;
import gmaths.*;
import com.jogamp.opengl.*;

public class SolidColorShader extends ShaderConfigurator {

    protected Vec3 color;

    public SolidColorShader(GL3 gl, Vec3 color) {
        super(gl, "shaders/glsl/solid_color.vs.glsl", "shaders/glsl/solid_color.fs.glsl");
        this.color = color;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }


    public void sendSendDataToTheGPU(GL3 gl, Mat4 perspective, Mat4 model, WorldConfiguration worldConfig) {
        shader.use(gl);

        Camera camera = worldConfig.getCamera();
        Mat4 mvpMatrix = Mat4.multiply(perspective,
            Mat4.multiply(camera.getViewMatrix(), model));

        shader.setFloatArray(gl, "model", model.toFloatArrayForGLSL());
        shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
        shader.setVec3(gl, "viewPos", camera.getPosition());
        shader.setVec3(gl, "color", color);

    }

    public void dispose(GL3 gl) {}
}
