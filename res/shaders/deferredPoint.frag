#version 330

in vec2 texCoord0;

#include "deferredlighting.glh"

uniform vec3 C0_eyePos;

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos, vec2 specular){
	return CalcPointLight(R_pointLight, normal, worldPos, specular, C0_eyePos);
}

#include "deferredLightingMain.fragh"