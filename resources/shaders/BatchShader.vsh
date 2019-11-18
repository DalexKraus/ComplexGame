#version 330

//uniform mat4 projectionMatrix;

layout (location = 0) in vec2 vertex; //vec2 position
layout (location = 1) in mat4 projectionMatrix;
layout (location = 5) in mat4 transformationMatrix;

layout (location = 9) in float uvCoordinates[3];
layout (location = 10) in float uvCoordinates2[3];
layout (location = 11) in float uvCoordinates3[3];

out vec2 pass_textureCoord;

void main()
{
    gl_Position = projectionMatrix * transformationMatrix * vec4(vertex.x, vertex.y, 0.0f, 1.0f);

    //Pass uv coordinates (texture coordinates)
    pass_textureCoord = vec2(uvCoordinates[0], uvCoordinates3[2]);
//    pass_textureCoord = vec2(0.0f, 0.5f);

}