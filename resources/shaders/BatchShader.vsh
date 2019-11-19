#version 330

//uniform mat4 projectionMatrix;

layout (location = 0) in vec2 vertex; //vec2 position
layout (location = 1) in mat4 projectionMatrix;
layout (location = 5) in mat4 transformationMatrix;

//Works with multiple arrays
layout (location = 9)  in float uvCoordinates1[4];
layout (location = 10) in float uvCoordinates2[4];
layout (location = 11) in float uvCoordinates3[4];

out vec2 pass_textureCoord;

void main()
{
    gl_Position = projectionMatrix * transformationMatrix * vec4(vertex.x, vertex.y, 0.0f, 1.0f);

    //Pass uv coordinates (texture coordinates)
    float uvCoords[12] = float[12] (uvCoordinates1[0], uvCoordinates1[1], uvCoordinates1[2], uvCoordinates1[3],
                                    uvCoordinates2[0], uvCoordinates2[1], uvCoordinates2[2], uvCoordinates2[3],
                                    uvCoordinates3[0], uvCoordinates3[1], uvCoordinates3[2], uvCoordinates3[3]);

    pass_textureCoord = vec2(uvCoords[gl_VertexID * 2], uvCoords[gl_VertexID * 2 + 1]);
}