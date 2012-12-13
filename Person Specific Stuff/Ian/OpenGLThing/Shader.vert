#version 110

vec3 reflect3f(vec3 incident, vec3 normal);

varying vec3 varyingColour;

void main()
{
	vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;

	vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);

	vec3 surfaceNormal  = (gl_NormalMatrix * gl_Normal).xyz;

	float diffuseLightIntensity = dot(surfaceNormal, lightDirection);

	varyingColour.rgb = diffuseLightIntensity * gl_Color.rgb;

	varyingColour += gl_LightModel.ambient.rgb;

	vec3 reflectionDirection = normalize(reflect3f(-lightDirection, surfaceNormal));
	
	float specular = dot(surfaceNormal, reflectionDirection);

	float fspecular = pow(specular, gl_FrontMaterial.shininess);
	varyingColour.rgb += vec3(fspecular, fspecular, fspecular);
	
	
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}

vec3 reflect3f(vec3 incident, vec3 normal)
{
	return (incident - 2.0 * dot(incident, normal) * normal);
}