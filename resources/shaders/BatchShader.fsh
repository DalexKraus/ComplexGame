#version 330

in vec2 pass_textureCoord;
out vec4 fragColor;

uniform sampler2D batchTexture;

void main()
{
    fragColor = texture(batchTexture, pass_textureCoord) + vec4(0.01f, 0.01f, 0.0f, 0.05f);
}