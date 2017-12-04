package engine.render;

import engine.*;
import gmaths.*;
import com.jogamp.opengl.*;
import engine.lighting.*;
import java.util.*;

public abstract class ShaderConfigurator {

    protected Shader shader;

    public ShaderConfigurator(GL3 gl, String vs, String fs) {
        // GLSL files need to be provided
        shader = new Shader(gl, vs, fs);
    }

    public Shader getShader() {
        return shader;
    }

    protected Material createDefaultMaterial() {
        // Helper function to create a quite neutral material.
        Material material = new Material();
        material.setAmbient(0.2f, 0.2f, 0.2f);
        material.setDiffuse(0.2f, 0.2f, 0.2f);
        material.setSpecular(0.2f, 0.2f, 0.2f);
        material.setShininess(1.0f);

        return material;
    }

    protected void setupShaderForMultipleLighting(GL3 gl, WorldConfiguration worldConfig) {
        // Helper function for sending data to the gpu for shaders compatible with multiple lighting

        // Send directional light data to the shader program
        DirectionalLight dirLight = worldConfig.getDirectionalLight();
        shader.setVec3(gl, "dirLight.direction", dirLight.getDirection());
        shader.setVec3(gl, "dirLight.ambient", dirLight.getMaterial().getAmbient());
        shader.setVec3(gl, "dirLight.diffuse", dirLight.getMaterial().getDiffuse());
        shader.setVec3(gl, "dirLight.specular", dirLight.getMaterial().getSpecular());

        // Send Spotlight data the the shader program
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

        // Send an array of pointLights to the shader.
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
    }

    // Classes extending this class must provide a way to send data to the GPU
    public abstract void sendSendDataToTheGPU(GL3 gl, Mat4 perspective, Mat4 model, WorldConfiguration worldConfig);
    // Need to a way to dispose the shader program because they may load textures
    public abstract void dispose(GL3 gl);
}
