package com.github.jpbetz.subspace

import java.nio.FloatBuffer

object Quaternion {
  val identity = Quaternion(0, 0, 0, 1)

  /**
   * Build a rotation that rotates in radians around the.
   */
  def forAxisAngle(axis: Vector3, angle: Float): Quaternion = {
    val halfAngle = angle / 2
    val s = Math.sin(halfAngle).toFloat
    Quaternion(axis.x * s, axis.y * s, axis.z * s, scala.math.cos(halfAngle).toFloat)
  }

  /**
   * Build a rotation that rotates in radians around the three axis in the order: z, x, y.
   */
  def forEuler(eulerAngles: Vector3): Quaternion = {
    // TODO: optimize?
    Quaternion.forAxisAngle(Orientation.z, eulerAngles.z) *
      Quaternion.forAxisAngle(Orientation.x, eulerAngles.x) *
      Quaternion.forAxisAngle(Orientation.y, eulerAngles.y)
  }
}

case class Quaternion(x: Float, y: Float, z: Float, w: Float) extends Bufferable {
  def vector: Vector4 = Vector4(x, y, z, w)
  def allocateBuffer: FloatBuffer = vector.allocateBuffer
  def updateBuffer(buffer: FloatBuffer) = vector.updateBuffer(buffer)

  def *(rhs: Quaternion): Quaternion = multiply(rhs)
  def multiply(rhs: Quaternion): Quaternion = {
    Quaternion(
      this.w * rhs.x + this.x * rhs.w + this.y * rhs.z - this.z * rhs.y,
      this.w * rhs.y + this.y * rhs.w + this.z * rhs.x - this.x * rhs.z,
      this.w * rhs.z + this.z * rhs.w + this.x * rhs.y - this.y * rhs.x,
      this.w * rhs.w - this.x * rhs.x - this.y * rhs.y - this.z * rhs.z)
  }

  def *(rhs: Vector3): Vector3 = multiply(rhs)
  def multiply(rhs: Vector3): Vector3 = {
    val x2 = this.x * 2f
    val y2 = this.y * 2f
    val z2 = this.z * 2f
    val xx2 = this.x * x2
    val yy2 = this.y * y2
    val zz2 = this.z * z2
    val xy2 = this.x * y2
    val xz2 = this.x * z2
    val yz2 = this.y * z2
    val wx2 = this.w * x2
    val wy2 = this.w * y2
    val wz2 = this.w * z2

    Vector3(
      (1f - (yy2 + zz2)) * rhs.x + (xy2 - wz2) * rhs.y + (xz2 + wy2) * rhs.z,
      (xy2 + wz2) * rhs.x + (1f - (xx2 + zz2)) * rhs.y + (yz2 - wx2) * rhs.z,
      (xz2 - wy2) * rhs.x + (yz2 + wx2) * rhs.y + (1f - (xx2 + yy2)) * rhs.z)
  }
}

object Orientation {
  val x = Vector3(1, 0, 0)
  val y = Vector3(0, 1, 0)
  val z = Vector3(0, 0, 1)
}
