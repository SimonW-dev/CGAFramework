#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec3 normal;
} vertexData;

//fragment shader output
out vec4 color;


void main(){

    vec3 n = abs(normalize(vertexData.normal));
    color = vec4(n.xyz, 1.0f);

}