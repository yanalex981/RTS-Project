varying vec3 varyingColour;


void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}