#version 330
const int MAX_SAMPLES = 16;

uniform sampler2D uTexInput;
uniform sampler2D uTexVelocity;
uniform float velocityScale;
out vec4 resultColor;

float cone(vec2 X, vec2 Y, vec2 V)
{
    return clamp(1.0 - length(X - Y) / length(V), 0.0, 1.0);
}

float cylinder(vec2 X, vec2 Y, vec2 V)
{
    return 1.0 - smoothstep(0.95 * length(V), 1.05 * length(V), length(X - Y));
}

vec3 motion_blur()
{
    //Calculate texture coordinate using pixel's position on screen
    vec2 texelSize = 1.0 / vec2(textureSize(uTexInput, 0));
    vec2 screenTexCoords = gl_FragCoord.xy * texelSize;

    //Sample scene and velocity image
    vec3 final = texture(uTexInput, screenTexCoords).rgb;
    vec2 velocity = texture(uTexVelocity, screenTexCoords).rg;
    if (length(velocity) != 0.0) {
        velocity *= 2.0;
        velocity -= 1.0;
    }

    float speed = length(velocity / texelSize);
    int nSamples = clamp(int(speed), 1, MAX_SAMPLES);
    nSamples = 8;

    float velocity_length = length(velocity);
    if (velocity_length < 0.0001)
        return final;

    float weight = 1.0 / velocity_length;
    for (int i = 1; i < nSamples; i++)
    {
        vec2 offset = velocity * (float(i) / float(nSamples - 1.0) - 0.5) * 0.01;
        vec2 sampleTexCoord = screenTexCoords + offset;
        vec2 sampleVelocity = texture(uTexVelocity, sampleTexCoord).rg * velocityScale;

        float temp = cone(sampleTexCoord, screenTexCoords, sampleVelocity) +
                    cone(screenTexCoords, sampleTexCoord, velocity) +
                    cylinder(sampleTexCoord, screenTexCoords, sampleVelocity) * cylinder(screenTexCoords, sampleTexCoord, velocity) * 2.0;

        vec3 currentSample = texture(uTexInput, sampleTexCoord).rgb;
        final += currentSample;
    }

    final /= nSamples;
    return final;
}

void main()
{
    resultColor = vec4(motion_blur(), 1);
}
