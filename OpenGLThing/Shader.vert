#version 110

varying vec3 varyingColour;

void main()
{
	varyingColour = gl_Color.rgb;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}