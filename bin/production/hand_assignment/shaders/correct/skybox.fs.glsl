#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;

uniform sampler2D main_texture;
uniform vec3 viewPos;
uniform float offset;


void main() {
    fragColor = texture(main_texture, ourTexCoord + vec2(offset, 0));
}
