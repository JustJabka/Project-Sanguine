#version 330

uniform sampler2D Sampler0;

layout(std140) uniform Insanity {
    float progress;
    float time;
};

in vec2 texCoord;

out vec4 fragColor;

float rand(vec2 uv){
    return fract(sin(dot(uv.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main(){
    vec2 uv = texCoord;
    uv.x += rand(uv + vec2(time)) * 0.005 * progress;

    vec4 color = texture(Sampler0, uv);

    float grayscale = 0.299 * color.r + 0.587 * color.g + 0.114 * color.b;
    vec4 grayscaleColor = vec4(grayscale, grayscale, grayscale, 1.0);

    color = mix(color, grayscaleColor, clamp(progress, 0.0, 0.75));

    fragColor = color;
}