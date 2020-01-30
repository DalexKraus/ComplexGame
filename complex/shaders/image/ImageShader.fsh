
in vec2 pass_textureCoordinates;
uniform sampler2D textureSampler;
out vec4 fragColor;

// Just some random shit to make funny waves lolxd
vec2 sineWave(vec2 pos) {
	float pi = 3.14159;
	float A = 0.15;
	float w = 10 * pi;
	float t = 30.0 * pi / 180;
	float y = sin(w*pos.x + t) * A;
	return vec2(pos.x, pos.y + y);
}

void main() {
	//vec2 pos = pass_textureCoordinates;
	//vec2 uv = sineWave(pos);
	//vec4 tColor = texture2D(textureSampler, uv);
	
	fragColor = texture(textureSampler, pass_textureCoordinates);
}
