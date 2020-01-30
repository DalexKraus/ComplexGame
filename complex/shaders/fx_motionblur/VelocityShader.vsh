
uniform mat4 currentModelViewProjectionMat;
uniform mat4 previousModelViewProjectionMat;

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoordinates;

out vec4 vPosition;
out vec4 vPrevPosition;
out vec2 pass_textureCoordinates;

void main()
{
    vec4 vertexPos = vec4(position, 1.0f);
    vPosition       = currentModelViewProjectionMat  * vertexPos;
    vPrevPosition   = previousModelViewProjectionMat * vertexPos;

    gl_Position     = vPosition;
    pass_textureCoordinates = textureCoordinates;
}