#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 catgirl;
layout(location = 2) in vec3 normal;

// uniforms
// translation object to world
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

out struct VertexData
{
    vec3 position;
    vec3 normal;
} vertexData;

//
void main(){
    vec4 pos = projection_matrix * view_matrix * model_matrix * vec4(position, 1.0f);
    gl_Position = pos;

    //normals are weird, that's why we need to transform them using the inverse transposed model matrix
    //also, we don't want to translate them, so the homogeneous coordinate has to be 0
    vec4 norm = transpose(inverse(view_matrix * model_matrix)) * vec4(normal, 0.0f);

    vertexData.position = pos.xyz;
    vertexData.normal = norm.xyz;
}
