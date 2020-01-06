
uniform mat4 projectionMatrix;

layout (location = 0) in vec3 position;

in vec2 textureCoordinates;
out vec2 pass_textureCoordinates;

void main()
{
    pass_textureCoordinates = textureCoordinates;
    gl_Position = projectionMatrix * vec4(position, 1.0f);
}