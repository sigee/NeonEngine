#version 330

in vec3 texCoord0;

uniform samplerCube cubeMap;

const float PI = 3.14159265359f;

layout(location = 0) out vec4 outputFS;

void main(){
	vec3 N = normalize(texCoord0);
	
	vec3 irradiance = vec3(0.0);
	
	vec3 up = vec3(0.0, 1.0, 0.0);
	vec3 right = cross(up, N);
	up = cross(N, right);
	
	float sampleDelta = 0.025f;
	float nrSamples = 0.0f;
	for(float phi = 0.0; phi < 2.0 * PI; phi += sampleDelta){
		for(float theta = 0.0; theta < 0.5 * PI; theta += sampleDelta){
			vec3 tangentSample = vec3(sin(theta) * cos(phi),  sin(theta) * sin(phi), cos(theta));
			vec3 sampleVec = tangentSample.x * right + tangentSample.y * up + tangentSample.z * N;
			
			irradiance += texture(cubeMap, sampleVec).rgb * cos(theta) * sin(theta);
			nrSamples++;
		}
	}
	irradiance = PI * irradiance * (1.0 / float(nrSamples));
	
	outputFS = vec4(irradiance, 1.0);
}