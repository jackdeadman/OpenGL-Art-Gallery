public class ShaderHelpers {

    public static setupShaderForMultiLight(Shader shader, WorldConfiguration worldConfig) {

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

}
