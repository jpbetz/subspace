package com.github.jpbetz.subspace

import java.nio.FloatBuffer

object Quaternion {
  def fromAxisAngle(axis: Vector3, angle: Float): Quaternion = {
    val halfAngle = angle / 2
    val s = Math.sin(halfAngle).toFloat
    Quaternion(axis.x * s, axis.y * s, axis.z * s, scala.math.cos(halfAngle).toFloat)
  }
}

case class Quaternion(x: Float, y: Float, z: Float, w: Float) extends Bufferable {
  def vector: Vector4 = Vector4(x, y, z, w)
  def allocateBuffer: FloatBuffer = vector.allocateBuffer
  def updateBuffer(buffer: FloatBuffer) = vector.updateBuffer(buffer)
}

object Orientation {
  val x = Vector3(1, 0, 0)
  val y = Vector3(0, 1, 0)
  val z = Vector3(0, 0, 1)
}
