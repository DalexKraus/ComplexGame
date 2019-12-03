#version 330
const int MAX_SAMPLES = 16;

uniform sampler2D uTexInput;
uniform sampler2D uTexVelocity;
uniform float velocityScale;
out vec4 oResult;

void main()
{
    //Calculate texture coordinate using pixel's position on screen
    vec2 texelSize = 1.0f / vec2(textureSize(uTexInput, 0));
    vec2 screenTexCoords = gl_FragCoord.xy * texelSize;

    vec2 velocity = texture(uTexVelocity, screenTexCoords).rg;
    //Denormalized velocity from (range 0 to 1) to (range -1 to 1)
    velocity *= 2f;
    velocity -= 1f;
    velocity *= velocityScale;

    float speed = length(velocity / texelSize);
//    int nSamples = clamp(int(speed), 1, MAX_SAMPLES);
    int nSamples = 5;

    //Sample scene image
    oResult = texture(uTexInput, screenTexCoords);
    for (int i = 0; i < nSamples; i++)
    {
        vec2 offset = velocity * (i / float(nSamples));
        vec2 offsetVelocity = texture(uTexVelocity, screenTexCoords + offset).rg;
        if (length(offsetVelocity) == 0) continue;

        oResult += texture(uTexVelocity, screenTexCoords + offset);
        oResult /= 2;
    }
    oResult.a = 1;
}