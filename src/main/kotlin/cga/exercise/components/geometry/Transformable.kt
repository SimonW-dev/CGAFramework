package cga.exercise.components.geometry

import org.joml.Matrix4f
import org.joml.Vector3f


open class Transformable(var modelMatrix: Matrix4f = Matrix4f(), var parent: Transformable? = null): ITransformable {

    /**
     * Rotates object around its own origin.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     */
    override fun rotateLocal(pitch: Float, yaw: Float, roll: Float) {
        val rotMat = Matrix4f().rotateXYZ(pitch, yaw, roll)

        //M = (M * R)
        modelMatrix.mul(rotMat)
    }

    /**
     * Rotates object around given rotation center.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */

    override fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f) {


        val transBackMat = Matrix4f().translate(altMidpoint)
        val transMat = Matrix4f(transBackMat).invert()
        val rotMat = Matrix4f().rotateXYZ(pitch, yaw, roll)

        //M = (M * TB * R * T)
        modelMatrix.mul(transBackMat).mul(rotMat).mul(transMat)
    }

    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    override fun translateLocal(deltaPos: Vector3f) {
        val transMat = Matrix4f().translate(deltaPos)
        // M = (M * T)
        modelMatrix.mul(transMat)
    }

    /**
     * Translates object based on its parent coordinate system.
     * Hint: global operations will be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    override fun translateGlobal(deltaPos: Vector3f) {
        val transMat = Matrix4f().translate(deltaPos)

        //  M = (T * M)
        transMat.mul(modelMatrix, modelMatrix)
    }

    /**
     * Scales object related to its own origin
     * @param scale scale factor (x, y, z)
     */
    override fun scaleLocal(scale: Vector3f) {
        val scaleMat = Matrix4f().scale(scale)

        //M = (M * S)
        modelMatrix.mul(scaleMat)
    }

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     * @return position
     */
    override fun getPosition(): Vector3f {

        return Vector3f(modelMatrix.m30(), modelMatrix.m31(), modelMatrix.m32())
    }

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    override fun getWorldPosition(): Vector3f {
        if (parent == null){
            return getPosition()
        }
        else{
            val m = getWorldModelMatrix()
            return Vector3f(m.m30(), m.m31(), m.m32())
        }
    }

    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     * @return x-axis
     */
    override fun getXAxis(): Vector3f {
        return Vector3f(modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02())
    }

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     * @return y-axis
     */
    override fun getYAxis(): Vector3f {
        return Vector3f(modelMatrix.m10(), modelMatrix.m11(), modelMatrix.m12())
    }

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     * @return z-axis
     */
    override fun getZAxis(): Vector3f {
        return Vector3f(modelMatrix.m20(), modelMatrix.m21(), modelMatrix.m22())
    }

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    override fun getWorldXAxis(): Vector3f {
        val m = parent?.modelMatrix ?: modelMatrix
        val v = Vector3f(m.m00(), m.m01(), m.m02())
        return v.normalize()
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    override fun getWorldYAxis(): Vector3f {
        val m = parent?.modelMatrix ?: modelMatrix
        val v = Vector3f(m.m10(), m.m11(), m.m12())
        return v.normalize()
    }

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    override fun getWorldZAxis(): Vector3f {
        val m =getWorldModelMatrix()
        val v = Vector3f(m.m20(), m.m21(), m.m22())
        return v.normalize()
    }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    override fun getWorldModelMatrix(): Matrix4f {
        if (parent == null) return getLocalModelMatrix()
        val parentMM =  parent!!.getWorldModelMatrix()
        return Matrix4f(parentMM.mul(modelMatrix))
    }

    /**
     * Returns object model matrix
     * @return modelMatrix
     */
    override fun getLocalModelMatrix(): Matrix4f {
        return Matrix4f(modelMatrix)
    }
}