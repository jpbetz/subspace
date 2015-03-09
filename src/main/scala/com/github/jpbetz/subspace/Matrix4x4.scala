package com.github.jpbetz.subspace

import java.nio.FloatBuffer

object Matrix4x4 {

  lazy val identity = Matrix4x4(
    1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1)

  /**
   * Build a perspective transformation matrix.
   * @param fovRad Field of view, in radians.
   * @param aspect Width divided by height
   */
  def forPerspective(fovRad: Float, aspect: Float, near: Float, far: Float): Matrix4x4 = {
    val fov = 1 / scala.math.tan(fovRad / 2f).toFloat
    Matrix4x4(
      fov / aspect, 0, 0, 0,
      0, fov, 0, 0,
      0, 0, (far + near) / (near - far), -1,
      0, 0, (2 * far * near) / (near - far), 0)
  }

  /**
   * Build an orthographic transformation matrix
   */
  def forOrtho(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4x4 = {
    val w = right - left
    val h = top - bottom
    val p = far - near

    val x = (right + left) / w
    val y = (top + bottom) / h
    val z = (far + near) / p

    Matrix4x4(
      2/w, 0,   0,    0,
      0,   2/h, 0,    0,
      0,   0,   -2/p, 0,
      -x,  -y,  -z,   1)
  }

  def forRotation(q: Quaternion) = {
    val Quaternion(x, y, z, w) = q
    val x2 = x + x
    val y2 = y + y
    val z2 = z + z

    val xx = x * x2
    val xy = x * y2
    val xz = x * z2

    val yy = y * y2
    val yz = y * z2
    val zz = z * z2

    val wx = w * x2
    val wy = w * y2
    val wz = w * z2

    Matrix4x4(
      1 - ( yy + zz ), xy + wz, xz - wy, 0,
      xy - wz, 1 - ( xx + zz ), yz + wx, 0,
      xz + wy, yz - wx, 1 - ( xx + yy ), 0,
      0, 0, 0, 1)
  }

  def forTranslation(translation: Vector3): Matrix4x4 = {
    Matrix4x4(
      1, 0, 0, 0,
      0, 1, 0, 0,
      0, 0, 1, 0,
      translation.x, translation.y, translation.z, 1)
  }

  def forScale(scale: Vector3): Matrix4x4 = {
    Matrix4x4(
      scale.x, 0, 0, 0,
      0, scale.y, 0, 0,
      0, 0, scale.z, 0,
      0, 0, 0, 1)
  }

  /**
   * Translates, rotates and then scales.
   */
  def forTranslationRotationScale(translation: Vector3, rotation: Quaternion, scale: Vector3): Matrix4x4 = {
    forTranslation(translation) * forRotation(rotation) * forScale(scale)
  }
}

/**
 * Constructor takes values in column major order.  That is,  the first 4 values are the rows the first column.
 */
// Originally the matrix cells were put into fields in anticipation of making this all stack allocated (using AnyVal
// or similar).  That's not currently possible,  so this could potentially be transitioned to a simple Float array.
case class Matrix4x4(
    c0r0: Float, c0r1: Float, c0r2: Float, c0r3: Float,
    c1r0: Float, c1r1: Float, c1r2: Float, c1r3: Float,
    c2r0: Float, c2r1: Float, c2r2: Float, c2r3: Float,
    c3r0: Float, c3r1: Float, c3r2: Float, c3r3: Float)
  extends Bufferable{

  def unary_- : Matrix4x4 = negate
  def negate: Matrix4x4 = {
    Matrix4x4(
      -c0r0, -c0r1, -c0r2, -c0r3,
      -c1r0, -c1r1, -c1r2, -c1r3,
      -c2r0, -c2r1, -c2r2, -c2r3,
      -c3r0, -c3r1, -c3r2, -c3r3)
  }

  def *(f: Float): Matrix4x4 = multiply(f)
  def multiply(f: Float): Matrix4x4 = {
    Matrix4x4(
      c0r0*f, c0r1*f, c0r2*f, c0r3*f,
      c1r0*f, c1r1*f, c1r2*f, c1r3*f,
      c2r0*f, c2r1*f, c2r2*f, c2r3*f,
      c3r0*f, c3r1*f, c3r2*f, c3r3*f)
  }

  def *(m: Matrix4x4): Matrix4x4 = multiply(m)
  def multiply(m: Matrix4x4): Matrix4x4 = {
    Matrix4x4(
      c0r0 * m.c0r0 + c1r0 * m.c0r1 + c2r0 * m.c0r2 + c3r0 * m.c0r3,  c0r1 * m.c0r0 + c1r1 * m.c0r1 + c2r1 * m.c0r2 + c3r1 * m.c0r3,  c0r2 * m.c0r0 + c1r2 * m.c0r1 + c2r2 * m.c0r2 + c3r2 * m.c0r3,  c0r3 * m.c0r0 + c1r3 * m.c0r1 + c2r3 * m.c0r2 + c3r3 * m.c0r3,
      c0r0 * m.c1r0 + c1r0 * m.c1r1 + c2r0 * m.c1r2 + c3r0 * m.c1r3,  c0r1 * m.c1r0 + c1r1 * m.c1r1 + c2r1 * m.c1r2 + c3r1 * m.c1r3,  c0r2 * m.c1r0 + c1r2 * m.c1r1 + c2r2 * m.c1r2 + c3r2 * m.c1r3,  c0r3 * m.c1r0 + c1r3 * m.c1r1 + c2r3 * m.c1r2 + c3r3 * m.c1r3,
      c0r0 * m.c2r0 + c1r0 * m.c2r1 + c2r0 * m.c2r2 + c3r0 * m.c2r3,  c0r1 * m.c2r0 + c1r1 * m.c2r1 + c2r1 * m.c2r2 + c3r1 * m.c2r3,  c0r2 * m.c2r0 + c1r2 * m.c2r1 + c2r2 * m.c2r2 + c3r2 * m.c2r3,  c0r3 * m.c2r0 + c1r3 * m.c2r1 + c2r3 * m.c2r2 + c3r3 * m.c2r3,
      c0r0 * m.c3r0 + c1r0 * m.c3r1 + c2r0 * m.c3r2 + c3r0 * m.c3r3,  c0r1 * m.c3r0 + c1r1 * m.c3r1 + c2r1 * m.c3r2 + c3r1 * m.c3r3,  c0r2 * m.c3r0 + c1r2 * m.c3r1 + c2r2 * m.c3r2 + c3r2 * m.c3r3,  c0r3 * m.c3r0 + c1r3 * m.c3r1 + c2r3 * m.c3r2 + c3r3 * m.c3r3)
  }

  /**
   * Computes the matrix product of this matrix with a column vector.
   */
  def *(vec: Vector4): Vector4 = multiply(vec)

  /**
   * Computes the matrix product of this matrix with a column vector.
   */
  def multiply(vec: Vector4): Vector4 = {
    Vector4(
      c0r0 * vec.x + c1r0 * vec.y + c2r0 * vec.z + c3r0 * vec.w,
      c0r1 * vec.x + c1r1 * vec.y + c2r1 * vec.z + c3r1 * vec.w,
      c0r2 * vec.x + c1r2 * vec.y + c2r2 * vec.z + c3r2 * vec.w,
      c0r3 * vec.x + c1r3 * vec.y + c2r3 * vec.z + c3r3 * vec.w)
  }

  def transpose: Matrix4x4 = {
    Matrix4x4(
      c0r0, c1r0, c2r0, c3r0,
      c0r1, c1r1, c2r1, c3r1,
      c0r2, c1r2, c2r2, c3r2,
      c0r3, c1r3, c2r3, c3r3
    )
  }

  def determinant: Float = {
    val a = c1r1 * c2r2 * c3r3 + c2r1 * c3r2 * c1r3 + c3r1 * c1r2 * c2r3 - c1r3 * c2r2 * c3r1 - c2r3 * c3r2 * c1r1 - c3r3 * c1r2 * c2r1
    val b = c0r1 * c2r2 * c3r3 + c2r1 * c3r2 * c0r3 + c3r1 * c0r2 * c2r3 - c0r3 * c2r2 * c3r1 - c2r3 * c3r2 * c0r1 - c3r3 * c0r2 * c2r1
    val c = c0r1 * c1r2 * c3r3 + c1r1 * c3r2 * c0r3 + c3r1 * c0r2 * c1r3 - c0r3 * c1r2 * c3r1 - c1r3 * c3r2 * c0r1 - c3r3 * c0r2 * c1r1
    val d = c0r1 * c1r2 * c2r3 + c1r1 * c2r2 * c0r3 + c2r1 * c0r2 * c1r3 - c0r3 * c1r2 * c2r1 - c1r3 * c2r2 * c0r1 - c2r3 * c0r2 * c1r1

    c0r0 * a - c1r0 * b + c2r0 * c - c3r0 * d
  }

  def inverse: Matrix4x4 = {
    // Compute inverse of a Matrix using minors, cofactors and adjugate
    // TODO: fix this.  the below approach is crazy
    val matrixOfCofactors = Matrix4x4(
      +(c1r1 * c2r2 * c3r3 + c2r1 * c3r2 * c1r3 + c3r1 * c1r2 * c2r3 - c1r3 * c2r2 * c3r1 - c2r3 * c3r2 * c1r1 - c3r3 * c1r2 * c2r1),
      -(c1r0 * c2r2 * c3r3 + c2r0 * c3r2 * c1r3 + c3r0 * c1r2 * c2r3 - c1r3 * c2r2 * c3r0 - c2r3 * c3r2 * c1r0 - c3r3 * c1r2 * c2r0),
      +(c1r0 * c2r1 * c3r3 + c2r0 * c3r1 * c1r3 + c3r0 * c1r1 * c2r3 - c1r3 * c2r1 * c3r0 - c2r3 * c3r1 * c1r0 - c3r3 * c1r1 * c2r0),
      -(c1r0 * c2r1 * c3r2 + c2r0 * c3r1 * c1r2 + c3r0 * c1r1 * c2r2 - c1r2 * c2r1 * c3r0 - c2r2 * c3r1 * c1r0 - c3r2 * c1r1 * c2r0),

      -(c0r1 * c2r2 * c3r3 + c2r1 * c3r2 * c0r3 + c3r1 * c0r2 * c2r3 - c0r3 * c2r2 * c3r1 - c2r3 * c3r2 * c0r1 - c3r3 * c0r2 * c2r1),
      +(c0r0 * c2r2 * c3r3 + c2r0 * c3r2 * c0r3 + c3r0 * c0r2 * c2r3 - c0r3 * c2r2 * c3r0 - c2r3 * c3r2 * c0r0 - c3r3 * c0r2 * c2r0),
      -(c0r0 * c2r1 * c3r3 + c2r0 * c3r1 * c0r3 + c3r0 * c0r1 * c2r3 - c0r3 * c2r1 * c3r0 - c2r3 * c3r1 * c0r0 - c3r3 * c0r1 * c2r0),
      +(c0r0 * c2r1 * c3r2 + c2r0 * c3r1 * c0r2 + c3r0 * c0r1 * c2r2 - c0r2 * c2r1 * c3r0 - c2r2 * c3r1 * c0r0 - c3r2 * c0r1 * c2r0),

      +(c0r1 * c1r2 * c3r3 + c1r1 * c3r2 * c0r3 + c3r1 * c0r2 * c1r3 - c0r3 * c1r2 * c3r1 - c1r3 * c3r2 * c0r1 - c3r3 * c0r2 * c1r1),
      -(c0r0 * c1r2 * c3r3 + c1r0 * c3r2 * c0r3 + c3r0 * c0r2 * c1r3 - c0r3 * c1r2 * c3r0 - c1r3 * c3r2 * c0r0 - c3r3 * c0r2 * c1r0),
      +(c0r0 * c1r1 * c3r3 + c1r0 * c3r1 * c0r3 + c3r0 * c0r1 * c1r3 - c0r3 * c1r1 * c3r0 - c1r3 * c3r1 * c0r0 - c3r3 * c0r1 * c1r0),
      -(c0r0 * c1r1 * c3r2 + c1r0 * c3r1 * c0r2 + c3r0 * c0r1 * c1r2 - c0r2 * c1r1 * c3r0 - c1r2 * c3r1 * c0r0 - c3r2 * c0r1 * c1r0),

      -(c0r1 * c1r2 * c2r3 + c1r1 * c2r2 * c0r3 + c2r1 * c0r2 * c1r3 - c0r3 * c1r2 * c2r1 - c1r3 * c2r2 * c0r1 - c2r3 * c0r2 * c1r1),
      +(c0r0 * c1r2 * c2r3 + c1r0 * c2r2 * c0r3 + c2r0 * c0r2 * c1r3 - c0r3 * c1r2 * c2r0 - c1r3 * c2r2 * c0r0 - c2r3 * c0r2 * c1r0),
      -(c0r0 * c1r1 * c2r3 + c1r0 * c2r1 * c0r3 + c2r0 * c0r1 * c1r3 - c0r3 * c1r1 * c2r0 - c1r3 * c2r1 * c0r0 - c2r3 * c0r1 * c1r0),
      +(c0r0 * c1r1 * c2r2 + c1r0 * c2r1 * c0r2 + c2r0 * c0r1 * c1r2 - c0r2 * c1r1 * c2r0 - c1r2 * c2r1 * c0r0 - c2r2 * c0r1 * c1r0)
    )
    // TODO: if determinant is 0, throw a meaningful exception
    matrixOfCofactors.transpose.multiply(1 / determinant)
  }

  def normalMatrix: Matrix4x4 = inverse.transpose

  def column(index: Int): Vector4 = {
    index match {
      case 0 => Vector4(c0r0, c0r1, c0r2, c0r3)
      case 1 => Vector4(c1r0, c1r1, c1r2, c1r3)
      case 2 => Vector4(c2r0, c2r1, c2r2, c2r3)
      case 3 => Vector4(c3r0, c3r1, c3r2, c3r3)
      case _ => throw new IllegalArgumentException("index is out of range.  Must be between 0 and 4")
    }
  }

  def row(index: Int): Vector4 = {
    index match {
      case 0 => Vector4(c0r0, c1r0, c2r0, c3r0)
      case 1 => Vector4(c0r1, c1r1, c2r1, c3r1)
      case 2 => Vector4(c0r2, c1r2, c2r2, c3r2)
      case 3 => Vector4(c0r3, c1r3, c2r3, c3r3)
      case _ => throw new IllegalArgumentException("index is out of range.  Must be between 0 and 4")
    }
  }

  def allocateBuffer: FloatBuffer = {
    val direct = Buffers.createFloatBuffer(16)
    direct
      .put(c0r0).put(c0r1).put(c0r2).put(c0r3)
      .put(c1r0).put(c1r1).put(c1r2).put(c1r3)
      .put(c2r0).put(c2r1).put(c2r2).put(c2r3)
      .put(c3r0).put(c3r1).put(c3r2).put(c3r3)
    direct.flip()
    direct
  }

  def updateBuffer(buffer: FloatBuffer): Unit = {
    buffer.clear()
    buffer
      .put(c0r0).put(c0r1).put(c0r2).put(c0r3)
      .put(c1r0).put(c1r1).put(c1r2).put(c1r3)
      .put(c2r0).put(c2r1).put(c2r2).put(c2r3)
      .put(c3r0).put(c3r1).put(c3r2).put(c3r3)
    buffer.flip()
  }

  override def toString: String = {
    s"""[[$c0r0, $c0r1, $c0r2, $c0r3]
       | [$c1r0, $c1r1, $c1r2, $c1r3]
       | [$c2r0, $c2r1, $c2r2, $c2r3]
       | [$c3r0, $c3r1, $c3r2, $c3r3]]
     """.stripMargin
  }
}
