#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;

uniform sampler2D frameTexture;
uniform sampler2D pictureTexture;
uniform sampler2D blendTexture;

uniform vec3 viewPos;

struct PointLight {
    vec3 position;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct DirectionalLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct SpotLight {
    vec3 position;
    vec3 direction;
    float cutOff;
    float outerCutOff;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform PointLight pointLights[2];
uniform DirectionalLight dirLight;
uniform SpotLight spotlight;

struct Material {
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;
  float shininess;
};

uniform Material material;

vec3 calcDirLight(DirectionalLight light, vec3 normal, vec3 viewDir, vec3 fragColor)
{
    // Flip the vector
    vec3 lightDir = normalize(-light.direction);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    // combine results
    vec3 ambient  = light.ambient  * vec3(fragColor);
    vec3 diffuse  = light.diffuse  * diff * vec3(fragColor);
    vec3 specular = light.specular * spec * vec3(fragColor);
    return (ambient + diffuse + specular);
}

vec3 calcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir, vec3 fragColor)
{
    vec3 lightDir = normalize(light.position - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    // attenuation
    float distance = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // spotlight intensity
    float theta = dot(lightDir, normalize(-light.direction));
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);
    // combine results
    vec3 ambient = light.ambient * vec3(fragColor);
    vec3 diffuse = light.diffuse * diff * vec3(fragColor);
    vec3 specular = light.specular * spec * vec3(fragColor);
    ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;
    return (ambient + diffuse + specular);
}


void main() {

  // specular
  vec3 viewDir = normalize(viewPos - fragPos);
  // sampler2D textureMix = mix(texture(first_texture, ourTexCoord), texture(second_texture, ourTexCoord));
  vec4 tex1 = texture(frameTexture, ourTexCoord);
  vec4 tex2 = texture(pictureTexture, ourTexCoord);
  vec4 tex3 = texture(blendTexture, ourTexCoord);

  vec4 textureMix = tex3;

 if (tex3.r < 0.1 && tex3.g > 0.6 && tex3.b < 0.1) {
     textureMix = tex1;
 } else {
     textureMix = tex2;
 }

  vec3 result = calcDirLight(dirLight, ourNormal, viewDir, textureMix.rgb);

  for (int i=0; i < 2; i++) {
      PointLight light = pointLights[i];
      // ambient
      vec3 ambient = light.ambient * material.ambient * textureMix.rgb;

      // diffuse
      vec3 norm = normalize(ourNormal);
      vec3 lightDir = normalize(light.position - fragPos);
      float diff = max(dot(norm, lightDir), 0.0);
      vec3 diffuse = light.diffuse * (diff * material.diffuse) * textureMix.rgb;


      vec3 reflectDir = reflect(-lightDir, norm);
      float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
      vec3 specular = light.specular * (spec * material.specular);
      result += ambient + diffuse + specular;
  }

  result += calcSpotLight(spotlight, ourNormal, fragPos, viewDir, textureMix.rgb);

  fragColor = vec4(result, 1);
}
