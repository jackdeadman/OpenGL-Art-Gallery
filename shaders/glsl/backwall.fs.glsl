#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;

uniform sampler2D mainTexture;
uniform sampler2D normalTexture;
uniform sampler2D windowTexture;
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
     vec3 ambient  = light.ambient  * fragColor;
     vec3 diffuse  = light.diffuse  * diff * fragColor;
     vec3 specular = light.specular * spec * material.specular;
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
     vec3 ambient = light.ambient * fragColor;
     vec3 diffuse = light.diffuse * diff * fragColor;
     vec3 specular = light.specular * spec * material.specular;

     ambient *= attenuation * intensity;
     diffuse *= attenuation * intensity;
     specular *= attenuation * intensity;
     return (ambient + diffuse + specular);
 }

void main() {

  vec3 viewDir = normalize(viewPos - fragPos);

  vec3 wall = texture(mainTexture, ourTexCoord).rgb;
  vec3 window = texture(windowTexture, ourTexCoord).rgb;
  vec3 blend = texture(blendTexture, ourTexCoord).rgb;

  vec3 textureMix;
  vec3 norm;

  if (blend.g > 0.6 && blend.r < 0.2 && blend.b < 0.2) {
    textureMix = wall;
      // Adjust normal based on the normal map
      vec3 normalMapColor = ((2*texture(normalTexture, ourTexCoord))-1).rgb;
      norm = vec3(normalMapColor.r, normalMapColor.g*ourNormal.g, normalMapColor.b*ourNormal.b);
      norm = normalize(vec3(normalMapColor.x, -normalMapColor.y, normalMapColor.z)/2);
  } else if (blend.r > 0.6 && blend.g < 0.2 && blend.b < 0.2) {
    fragColor = vec4(0);
    return; //Dont need to calculate lighting here so end now.
  } else {
    textureMix = window;
    norm = normalize(ourNormal);
  }


  vec3 result = calcDirLight(dirLight, ourNormal, viewDir, textureMix);

  for (int i=0; i < 2; i++) {
      PointLight light = pointLights[i];
      // ambient
      vec3 ambient = light.ambient * material.ambient * textureMix.rgb;

      // diffuse
      norm = normalize(norm);
      vec3 lightDir = normalize(light.position - fragPos);
      float diff = max(dot(norm, lightDir), 0.0);
      vec3 diffuse = light.diffuse * (diff * material.diffuse) * textureMix.rgb;


      vec3 reflectDir = reflect(-lightDir, norm);
      float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
      vec3 specular = light.specular * (spec * material.specular);
      result += ambient + diffuse + specular;
  }

  result += calcSpotLight(spotlight, ourNormal, fragPos, viewDir, textureMix);

  fragColor = vec4(result, 1);
}
