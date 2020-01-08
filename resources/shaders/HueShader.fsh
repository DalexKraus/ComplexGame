
in vec2 pass_textureCoordinates;
out vec4 fragColor;

uniform sampler2D textureSampler;
uniform float hueValue;
uniform float saturationValue;

vec4 average(vec4 base)
{
    vec3 temp = base.rgb / 3.0;
    float average = temp.r + temp.g + temp.b;
    return vec4(average, average, average, 1.0);
}

vec4 hueShift(vec3 color, float hueAdjust)
{
    const vec3 kRGBToYPrime = vec3(0.299,  0.587,  0.114);
    const vec3 kRGBToI      = vec3(0.596, -0.275, -0.321);
    const vec3 kRGBToQ      = vec3(0.212, -0.523,  0.311);

    const vec3 kYIQToR     = vec3(1.0,  0.956,  0.621);
    const vec3 kYIQToG     = vec3(1.0, -0.272, -0.647);
    const vec3 kYIQToB     = vec3(1.0, -1.107,  1.704);

    float YPrime = dot(color, kRGBToYPrime);
    float I      = dot(color, kRGBToI);
    float Q      = dot(color, kRGBToQ);
    float hue    = atan(Q, I);
    float chroma = sqrt(I * I + Q * Q);

    hue += hueAdjust;

    Q = chroma * sin(hue);
    I = chroma * cos(hue);

    vec3 yIQ = vec3(YPrime, I, Q);
    return vec4(dot(yIQ, kYIQToR), dot(yIQ, kYIQToG), dot(yIQ, kYIQToB), 1.0);
}

vec4 saturate(vec4 color, float saturation)
{
    vec4 avg = average(color);
    return mix(color, avg, saturation);
}

void main() {
    vec4 srcColor = texture(textureSampler, pass_textureCoordinates);
    fragColor = saturate(srcColor, hueValue);
}
