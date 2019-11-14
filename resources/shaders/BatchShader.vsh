#version 330

uniform mat4 projectionMatrix;

layout (location = 0) in vec4 vertex; //vec2 position, vec2 texCoord
out vec2 pass_textureCoord;

void main()
{
    gl_Position = projectionMatrix * vec4(vertex.x / 2, vertex.y / 2, 0.0f, 1.0f);
    pass_textureCoord = vertex.zw;
}