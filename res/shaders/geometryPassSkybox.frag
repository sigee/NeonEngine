#version 330

in vec3 texCoord0;

uniform samplerCube cubeMap;

layout(location = 0) out vec4 out0;
layout(location = 1) out vec4 out1;
layout(location = 2) out vec4 out2;
layout(location = 3) out vec4 out3;
layout(location = 4) out vec4 out4;

void main(){
	//outputFS = texture(cubeMap, texCoord0);
	//outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
	out0 = texture(cubeMap, texCoord0);
	out1 = vec4(0.0, 0.0, 0.0, 0.0);
	out2 = vec4(0.0, 0.0, 0.0, 0.0);
	out3 = vec4(0.0, 0.0, 0.0, 0.0);
	out4 = vec4(0.0, 0.0, 0.0, 0.0);
}