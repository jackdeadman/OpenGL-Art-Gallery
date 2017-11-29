#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 texCoord;

out vec3 fragPos;
out vec3 ourNormal;
out vec2 ourTexCoord;

uniform mat4 model;
uniform mat4 mvpMatrix;


vec4 metaballs(vec2 uv, float tick)
{
    //Shifting uv coordinates so they're between -1 and 1 rather than 0 and 1
    uv = uv * 2.0 - 1.0;

    //Aspect ratio correction (if texture is square, you don't need this)
    //Comment the line out and you'll immediately see what it does though
    // uv *= vec2(1.0, resolution.y / resolution.x);

    //Defining coordinates of metaballs in 2D space w/ sine animation
    vec2 ball1 = 0.3 * sin(tick * 1.0 + vec2(4.0, 0.5) + 1.0);
    vec2 ball2 = 0.3 * sin(tick * 1.3 + vec2(1.0, 2.0) + 2.0);
    vec2 ball3 = 0.3 * sin(tick * 1.5 + vec2(0.0, 2.0) + 4.0);

    //Potential calculations
    //I modified this to align with what Steve Maddock would have taught you
    //Equation for metaball potential is usually 1/(distance^2) so i changed it to that
    //Multiplied by 0.025 to reduce the influence of the field, so bigger value = bigger metaball
    float potential = 0.0;
    potential += 0.025 * 1.0 / (length(uv - ball1) * length(uv - ball1));
    potential += 0.025 * 1.0 / (length(uv - ball2) * length(uv - ball2));
    potential += 0.025 * 1.0 / (length(uv - ball3) * length(uv - ball3));

    vec4 metaballColor = vec4(1.0, 0.6, 0.0, 1.0);
    vec4 backgroundColor = potential * metaballColor; //Gives a nice glow (note that alpha is also multiplied by potential)

    //Index for lerp between 0 and 1
    //Anything below 0.9 will be backgroundColor, above 0.91 metaballColor, and anything between
    //will be smoothly interpolated based on the smoothstep curve
    float a = smoothstep(0.9, 0.91, potential);

    //Mix is just a linear interpolation
    return mix(backgroundColor, metaballColor, a);
}

void main() {
  gl_Position = mvpMatrix * vec4(position, 1.0);
  fragPos = vec3(model*vec4(position, 1.0f));
  mat4 normalMatrix = transpose(inverse(model));
  vec3 norm = normalize(normal);
  ourNormal = mat3(normalMatrix) * norm;

  //ourNormal = vec3((normalMatrix) * vec4(normal,1.0));
  ourTexCoord = texCoord;
}
