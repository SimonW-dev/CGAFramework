package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f
import kotlin.math.PI

class TronCamera(val fieldOfView: Float = 0.5f * PI.toFloat(),
                 val aspectRatio: Float = 16f / 9f,
                 val nearPlane: Float = 0.1f,
                 val farPlane: Float = 100f,
                 val cameraTarget: Vector3f,
                 parent: Transformable
): ICamera, Transformable(parent = parent) {

    //View Transformation
    override fun getCalculateViewMatrix(): Matrix4f {
        return Matrix4f().lookAt(getWorldPosition(), cameraTarget, getWorldYAxis())
    }

    //Projection
    override fun getCalculateProjectionMatrix(): Matrix4f {
        return Matrix4f().perspective(fieldOfView, aspectRatio, nearPlane, farPlane)
    }

    override fun bind(shader: ShaderProgram) {
        shader.setUniform("view_matrix", getCalculateViewMatrix(), false)
        shader.setUniform("projection_matrix", getCalculateProjectionMatrix(), false)
    }
}