#version 330

layout (location = 0) in vec4 vertex;                   //vec2 position, vec2 UV-Coordinate (interv. [0-1], normalized)
layout (location = 1) in mat4 projectionMatrix;         //Combined projection and (camera-!) translation matrix.
layout (location = 5) in mat4 transformationMatrix;     //Translation matrix for the vertex.

//We're not consider UV-Scaling here, since we won't need it anyway.
//(A simple translation of the UV-Coordinates will be enough)
layout (location = 9) in vec2 uvTranslation;

//Recalculated UV-Coordinates (or texture coords)
out vec2 pass_textureCoord;

void main()
{
    gl_Position = projectionMatrix * transformationMatrix * vec4(vertex.x, vertex.y, 0.0f, 1.0f);

    //TODO: uv translation SHOULD WORK IN THEORY, but the uv square must be resized as well,
    //TODO: the problem here is, that the quad should only be scaled, not translated while scaling.
    //TODO: Solution: matrix? (???Won't work as UV coords have a different origin???)

    //Pass texture coords
    pass_textureCoord = vertex.zw + uvTranslation;
}