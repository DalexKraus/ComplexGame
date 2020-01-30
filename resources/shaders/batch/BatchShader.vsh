
layout (location = 0) in vec4 vertex;                   //vec2 position, vec2 UV-Coordinate (interv. [0-1], normalized)
layout (location = 1) in mat4 projectionMatrix;         //Combined projection and (camera-!) translation matrix.
layout (location = 5) in mat4 transformationMatrix;     //Translation matrix for the vertex.

//We're not considering UV-Scaling here, since we won't need it anyway.
//(A simple translation of the UV-Coordinates will be enough)
layout (location = 9) in vec2 uvTranslation;
layout (location = 10) in vec2 uvScaleFactor;

//Recalculated UV-Coordinates (or texture coords)
out vec2 pass_textureCoord;

void main()
{
    gl_Position = projectionMatrix * transformationMatrix * vec4(vertex.x, vertex.y, 0.0, 1.0);

    //Pass texture coords
    //pass_textureCoord = vec2(0.5f, 0.0f) + vertex.zw * vec2(0.5f, 0.333333f);
    pass_textureCoord = uvTranslation + vertex.zw * uvScaleFactor;
}