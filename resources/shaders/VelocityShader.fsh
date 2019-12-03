#version 330

in vec4 vPosition;
in vec4 vPrevPosition;
in vec2 pass_textureCoordinates;
uniform sampler2D textureSampler;

out vec4 oVelocity;

void main()
{
    vec2 posA = vPosition.xy * 0.5 + 0.5;
    vec2 posB = vPrevPosition.xy * 0.5 + 0.5;

    //Sample the acutal image of the original model and discard any
    //pixels who are transparent, to exactly represent object's outline
    //in the velocity buffer.
    vec4 textureColor = texture(textureSampler, pass_textureCoordinates);
    if (textureColor.a == 0)
        discard;

    vec2 velocity = vec2(posA.x - posB.x, posA.y - posB.y);
    //Normalize velocity (-1|1)to fit inside the color range (0|1)
    velocity += 1f;
    velocity /= 2f;

    oVelocity = vec4(velocity, 0f, 1f);
}