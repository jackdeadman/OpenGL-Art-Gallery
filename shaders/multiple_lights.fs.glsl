#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;

uniform sampler2D first_texture;
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

vec3 calcDirLight(DirectionalLight light, vec3 normal, vec3 viewDir)
{
    // Flip the vector
    vec3 lightDir = normalize(-light.direction);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    // combine results
    vec3 ambient  = light.ambient  * vec3(texture(first_texture, ourTexCoord));
    vec3 diffuse  = light.diffuse  * diff * vec3(texture(first_texture, ourTexCoord));
    vec3 specular = light.specular * spec * vec3(texture(first_texture, ourTexCoord));
    return (ambient + diffuse + specular);
}

vec3 calcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
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
    vec3 ambient = light.ambient * vec3(texture(first_texture, ourTexCoord));
    vec3 diffuse = light.diffuse * diff * vec3(texture(first_texture, ourTexCoord));
    vec3 specular = light.specular * spec * vec3(texture(first_texture, ourTexCoord));
    ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;
    return (ambient + diffuse + specular);
}

void main() {
  // specular
  vec3 viewDir = normalize(viewPos - fragPos);

  vec3 result = calcDirLight(dirLight, ourNormal, viewDir);

  for (int i=0; i < 2; i++) {
      PointLight light = pointLights[i];
      // ambient
      vec3 ambient = light.ambient * material.ambient * texture(first_texture, ourTexCoord).rgb;

      // diffuse
      vec3 norm = normalize(ourNormal);
      vec3 lightDir = normalize(light.position - fragPos);
      float diff = max(dot(norm, lightDir), 0.0);
      vec3 diffuse = light.diffuse * (diff * material.diffuse) * texture(first_texture, ourTexCoord).rgb;


      vec3 reflectDir = reflect(-lightDir, norm);
      float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
      vec3 specular = light.specular * (spec * material.specular);
      result += ambient + diffuse + specular;
  }

  result += calcSpotLight(spotlight, ourNormal, fragPos, viewDir);

  fragColor = vec4(result, 1.0);
}
