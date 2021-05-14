package cga.exercise.game

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.OBJLoader
import org.lwjgl.opengl.GL11.*


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
        val houseMesh: Mesh
        val initialMesh: Mesh
        val sphereMesh: Mesh

    //scene setup
    init {
        staticShader = ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl")

        //initial opengl state
        glClearColor(0.4f, 0.4f, 0.4f, 1.0f); GLError.checkThrow()
        glEnable(GL_CULL_FACE); GLError.checkThrow()
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        val houseVertices = floatArrayOf(
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, //v0
            0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,  //v1
            0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f,  //v2
            0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f
        )

        val houseIndices = intArrayOf(
            0, 2, 4,
            0, 1, 2,
            4, 2, 3
        )

        val position = VertexAttribute(3, GL_FLOAT,false,24, 0)
        val colour= VertexAttribute(3, GL_FLOAT, false,24, 12)

        val vertexAtrributeArray = arrayOf (
            position,
            colour
        )

        houseMesh = Mesh(houseVertices, houseIndices, vertexAtrributeArray); GLError.checkThrow()

        val initialVertices = floatArrayOf(
            -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, // S1
            -0.8f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, // S2
            -0.8f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, // S3
            -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, // S4
            -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, // S5
            -0.8f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f // S6
        )
        val initialIndicies = intArrayOf(
            0, 1,2,
            2,4,1,
            4, 3,2,
            5, 4,3
        )

        initialMesh = Mesh(initialVertices, initialIndicies, vertexAtrributeArray); GLError.checkThrow()

        val sphere = OBJLoader.loadOBJ("assets/models/sphere.obj")
        val sphereOBJMesh = sphere.objects.first().meshes.first()

        val sphereAttrib = arrayOf(
            VertexAttribute(3, GL_FLOAT, false, 32, 0), //Position
            VertexAttribute(2, GL_FLOAT, false,32, 12),  //Tex
            VertexAttribute(3, GL_FLOAT, true, 32, 20)  //Normals
        )

        sphereMesh = Mesh(sphereOBJMesh.vertexData, sphereOBJMesh.indexData, sphereAttrib)
    }

    fun render(dt: Float, t: Float) {

        staticShader.use()
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        //houseMesh.render()
        //initialMesh.render()
        sphereMesh.render()
    }

    fun update(dt: Float, t: Float) {}

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}

    fun cleanup() {}
}
