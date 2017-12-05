#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;

struct PointLight {
    vec3 position;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct Material {
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;
  float shininess;
};

uniform Material material;
uniform PointLight light;
uniform vec3 viewPos;

vec3 calcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir) {
    // ambient
    vec3 ambient = light.ambient;

    // diffuse
    vec3 norm = normalize(ourNormal);
    vec3 lightDir = normalize(light.position - fragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff;


    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = max(dot(viewDir, reflectDir), 0.0);
    vec3 specular = light.specular * spec;
    return ambient + diffuse + specular;
}

void main() {
  // specular
  vec3 viewDir = normalize(viewPos - fragPos);

  // Flip the normal so it looks like the light is inside the object
  vec3 result = calcPointLight(light, ourNormal, fragPos, viewDir);
  fragColor = vec4(result, 1.0);
  // fragColor = vec4(light.ambient, 1.0);
}
