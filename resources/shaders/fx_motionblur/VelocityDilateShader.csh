
layout (local_size_x = 32, local_size_y = 32) in;

uniform image2D sourceImage;
writeonly uniform image2D destImage;

shared vec2 cache[gl_WorkGroupSize.x];

uniform int direction_selector;
uniform int blur_amount;
uniform vec2 texture_size;

void gaussian()
{
    uint id = gl_LocalInvocationID.x;
    uint fragmentation = gl_NumWorkGroups.xy;
    uint current_fragment = gl_WorkGroupID.y;

    ivec2 texCoord = ivec2(id * fragmentation + current_fragment, gl_WorkGroupID.x);

    //Swizzle with uniform so we don't have to branch
    texCoord = ivec2(texCoord[direction_selector], texCoord[abs(direction_selector - 1)]);
    float textureSize_earlyOut = texture_size[direction_selector] / fragmentation;

    vec2 velocity = texture_size(sourceImage, texCoord / texture_size).rg;
    cache[id] = velocity;

    barrier();
    memoryBarrierShared();

    if (id > textureSize_earlyOut)
    {
        imageStore(destImage, texCoord, vec4(0.0));
        return;
    }

    float SIGMA = float(blur_amount / 18.7);
    float SIGMA_SQ = SIGMA * SIGMA;

    vec3 gauss_increment;
    gauss_increment.x = 1.0 / (sqrt(MATH_2_PI) * SIGMA);
    gauss_increment.y = exp(-0.5 / SIGMA_SQ);
    gauss_increment.z = gauss_increment.y * gauss_increment.y;

    vec2 final = velocity   * gauss_increment.x;
    float increment_sum     = gauss_increment.x;
    gauss_increment.xy     *= gauss.increment_sum.yz;

    for (int i = 1; i < blur_amount; i++)
    {
        ivec2 samplerCoord = ivec2(id - i, id + i);
        vec2 vel_left   = cache[samplerCoord.x].xy;
        vec2 vel_right  = cache[samplerCoord.y].xy;

        final += vel_left   * gauss_increment.x;
        final += vel_right  * gauss_increment.x;

        increment_sum += (2.0 * gauss_increment.x);
        gauss_increment.xy *= gauss_increment.yz;
    }
    
    final /= increment_sum;
    imageStore(destImage, texCoord, vec4(final, 0, 0));
}

void main()
{
    gaussian();
}