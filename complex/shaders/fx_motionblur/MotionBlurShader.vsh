
layout (location = 0) in vec3 position;
uniform mat4 projectionAndViewMatrix;

void main()
{
    gl_Position = projectionAndViewMatrix * vec4(position, 1f);
}