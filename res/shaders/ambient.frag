#version 330

in vec2 texCoord0;

uniform vec3 R_ambient;
uniform sampler2D R0_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	vec4 texture = texture(R0_filterTexture, texCoord0);
	
	outputFS = vec4(texture.rgb, 1.0) * (vec4(R_ambient, 1.0) + vec4(texture.a, texture.a, texture.a, 1.0));
}