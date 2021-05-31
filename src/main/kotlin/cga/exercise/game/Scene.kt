package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.OBJLoader
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11.*
import kotlin.math.PI


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram
    val groundMesh: Mesh
    val sphereMesh: Mesh
    //val groundMat = Matrix4f().rotateX(0.5f * PI.toFloat()).scale(0.03f)
    val ground: Renderable
    //val sphereMat = Matrix4f().scale(0.5f)
    val sphere: Renderable
    val camera: TronCamera

    private val movementSpeed: Float = 1f
    private val rotationSpeed: Float =1.5f


    //scene setup
    init {
        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")

        //initial opengl state
        glClearColor(0.4f, 0.4f, 0.4f, 1.0f); GLError.checkThrow()
        glEnable(GL_CULL_FACE); GLError.checkThrow()
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()


        //ground object
        val groundObj = OBJLoader.loadOBJ("assets/models/ground.obj")
        val groundOBJMesh = groundObj.objects.first().meshes.first()

        val groundAttrib = arrayOf(
            VertexAttribute(3, GL_FLOAT, false, 32, 0),
            VertexAttribute(3, GL_FLOAT, false, 32, 12),
            VertexAttribute(3, GL_FLOAT, false, 32, 20)
        )

        groundMesh = Mesh(groundOBJMesh.vertexData, groundOBJMesh.indexData, groundAttrib)
        ground = Renderable(mutableListOf(groundMesh))
        ground.rotateLocal(0.5f * PI.toFloat(), 0f, 0f)
        ground.scaleLocal(Vector3f(0.03f))
                //rotateX(0.5f * PI.toFloat()).scale(0.03f)

        //sphere object
        val sphereObj = OBJLoader.loadOBJ("assets/models/sphere.obj")
        val sphereOBJMesh = sphereObj.objects.first().meshes.first()

        val sphereAttrib = arrayOf(
            VertexAttribute(3, GL_FLOAT, false, 32, 0), //Position
            VertexAttribute(2, GL_FLOAT, false,32, 12),  //Tex
            VertexAttribute(3, GL_FLOAT, true, 32, 20)  //Normals
        )
        sphereMesh = Mesh(sphereOBJMesh.vertexData, sphereOBJMesh.indexData, sphereAttrib)
        sphere = Renderable(mutableListOf(sphereMesh))

        camera = TronCamera(cameraTarget = Vector3f(0f),parent = sphere)
        camera.rotateLocal(-0.35f, 0f, 0f)
        camera.translateLocal(Vector3f(0f, 0f, 4.0f))
    }

    fun render(dt: Float, t: Float) {

        staticShader.use()
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        camera.bind(staticShader)
        ground.render(staticShader)
        sphere.render(staticShader)


        /*
        sphere.rotateLocal(0.02f * PI.toFloat(),0f ,0f)
        sphere.scaleLocal(Vector3f(0.9999f))

    }

    fun update(dt: Float, t: Float) {}

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}

    fun cleanup() {}
}
