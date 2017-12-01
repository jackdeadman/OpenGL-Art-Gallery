package galleryscene.shaderprograms;

import engine.*;
import engine.render.*;
import engine.lighting.*;
import gmaths.*;
import com.jogamp.opengl.*;

public class LightShader extends ShaderConfigurator {

    protected PointLight light;

    public LightShader(GL3 gl, PointLight light) {
        super(gl, "shaders/correct/default.vs.glsl", "shaders/correct/light.fs.glsl");
        this.light = light;
    }

    public void sendSendDataToTheGPU(GL3 gl, Mat4 perspective, Mat4 model, WorldConfiguration worldConfig) {
        shader.use(gl);

        Camera camera = worldConfig.getCamera();
        Mat4 mvpMatrix = Mat4.multiply(perspective,
            Mat4.multiply(camera.getViewMatrix(), model));

        shader.setFloatArray(gl, "model", model.toFloatArrayForGLSL());
        shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
        shader.setVec3(gl, "viewPos", camera.getPosition());

        shader.setVec3(gl, "light.position", light.getPosition());
        shader.setVec3(gl, "light.ambient", light.getMaterial().getAmbient());
        shader.setVec3(gl, "light.specular", light.getMaterial().getDiffuse());
        shader.setVec3(gl, "light.shininess", light.getMaterial().getSpecular());

        Vec3 attenuation = light.getAttenuation();
        shader.setFloat(gl, "light.constant", attenuation.x);
        shader.setFloat(gl, "light.linear", attenuation.y);
        shader.setFloat(gl, "light.quadratic", attenuation.z);

    }

    public void dispose(GL3 gl) { }
}
