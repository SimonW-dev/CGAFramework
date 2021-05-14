package cga.exercise.components.geometry

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30.*

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>, private val drawMode: Int = GL_TRIANGLES) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    init {

        indexcount = indexdata.size

        //generating ID's
        vao = glGenVertexArrays()
        vbo = glGenBuffers()
        ibo = glGenBuffers()

        //binding & uploading objects
        glBindVertexArray(vao)


        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW)

        for ((i, a) in attributes.withIndex()) {
            glVertexAttribPointer(i, a.n, a.type, a.normalized, a.stride, a.offset)
            glEnableVertexAttribArray(i)
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW)

    }

    /**
     * renders the mesh
     */
    fun render() {
        glBindVertexArray(vao)
        glDrawElements(drawMode, indexcount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) glDeleteBuffers(ibo)
        if (vbo != 0) glDeleteBuffers(vbo)
        if (vao != 0) glDeleteVertexArrays(vao)
    }
}