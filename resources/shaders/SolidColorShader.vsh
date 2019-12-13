
uniform mat4 projectionMatrix;
layout (location = 0) in vec3 vertices;

void main()
{
    gl_Position = projectionMatrix * vec4(vertices, 1);
}