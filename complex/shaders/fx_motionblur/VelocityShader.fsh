
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
//    if (textureColor.a == 0)
//        discard;

    vec2 velocity = vec2(posA.x - posB.x, posA.y - posB.y);
    float velocity_length = length(velocity);


    //Normalize velocity
    velocity = normalize(velocity);
    velocity = clamp(velocity, 0, 1);

    velocity += 1.0;
    velocity /= 2.0;
    oVelocity = vec4(velocity, 1, 1);
}