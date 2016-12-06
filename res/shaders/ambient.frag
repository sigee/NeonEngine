#version 330

in vec2 texCoord0;

uniform vec3 R_ambient;
uniform sampler2D R0_filterTexture;
uniform sampler2D R4_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	vec4 emissive = texture(R4_filterTexture, texCoord0);
	
	outputFS = texture(R0_filterTexture, texCoord0) * (vec4(R_ambient, 1.0) + vec4(emissive.b, emissive.b, emissive.b, 1.0));
}