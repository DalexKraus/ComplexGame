#version 330

//uniform mat4 projectionMatrix;

layout (location = 0) in vec4 vertex; //vec2 position, vec2 texCoord
layout (location = 1) in mat4 projectionMatrix;
layout (location = 5) in mat4 transformationMatrix;

layout (location = 9) in float uvCoordinates[12];

out vec2 pass_textureCoord;

void main()
{
    gl_Position = projectionMatrix * transformationMatrix * vec4(vertex.x, vertex.y, 0.0f, 1.0f);

    //Pass uv coordinates (texture coordinates)
    int vertexIndex = gl_VertexID;
    pass_textureCoord = vec2(uvCoordinates[vertexIndex * 2], uvCoordinates[vertexIndex * 2 + 1]);

    //Last thing done yesterday:
    // Tried loading up uv coordinates for each vertex (vec2) into one big array (6*vec2 = 12 floats)
    // Still no visible result?

    //TODO: remove uvs from vertex data, as they're redundant!
}