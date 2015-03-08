package subspace.math

import java.nio.FloatBuffer

object Matrix3x3 {
  // convenience constructors
  def apply(column1: Vector3, column2: Vector3, column3: Vector3): Matrix3x3 = Matrix3x3(
    column1.x, column1.y, column1.z,
    column2.x, column2.y, column2.z,
    column3.x, column3.y, column3.z
  )

  lazy val identity = Matrix3x3(
    1, 0, 0,
    0, 1, 0,
    0, 0, 1)
}

// Use fields so we can stack allocate the matrix
case class Matrix3x3(
    m00: Float, m01: Float, m02: Float,
    m10: Float, m11: Float, m12: Float,
    m20: Float, m21: Float, m22: Float)
  extends Matrix
  with Bufferable {

  def rowCount: Int = 4
  def columnCount: Int = 4

  def apply(columnIndex: Int, rowIndex: Int): Float = {
    // ugh,  Should I switch to an array for internal representation?
    (columnIndex, rowIndex) match {
      case (0, 0) => m00
      case (0, 1) => m01
      case (0, 2) => m02

      case (1, 0) => m10
      case (1, 1) => m11
      case (1, 2) => m12

      case (2, 0) => m20
      case (2, 1) => m21
      case (2, 2) => m22

      case _ => throw new ArrayIndexOutOfBoundsException(s"column: $columnIndex, row: $rowIndex is out of range.")
    }
  }

  def unary_- : Matrix3x3 = negate
  def negate: Matrix3x3 = {
    Matrix3x3(
      -m00, -m01, -m02,
      -m10, -m11, -m12,
      -m20, -m21, -m22)
  }

  def *(f: Float): Matrix3x3 = multiply(f)
  def multiply(f: Float): Matrix3x3 = {
    Matrix3x3(
      m00*f, m01*f, m02*f,
      m10*f, m11*f, m12*f,
      m20*f, m21*f, m22*f)
  }

  def *(m: Matrix3x3): Matrix3x3 = multiply(m)
  def multiply(m: Matrix3x3): Matrix3x3 = {
    Matrix3x3(
      m00 * m.m00 + m01 * m.m10 + m02 * m.m20, m00 * m.m01 + m01 * m.m11 + m02 * m.m21, m00 * m.m02 + m01 * m.m12 + m02 * m.m22,
      m10 * m.m00 + m11 * m.m10 + m12 * m.m20, m10 * m.m01 + m11 * m.m11 + m12 * m.m21, m10 * m.m02 + m11 * m.m12 + m12 * m.m22,
      m20 * m.m00 + m21 * m.m10 + m22 * m.m20, m20 * m.m01 + m21 * m.m11 + m22 * m.m21, m20 * m.m02 + m21 * m.m12 + m22 * m.m22
    )
  }

  /**
   * Computes the matrix product of this matrix with a column vector.
   */
  def *(vec: Vector3): Vector3 = multiply(vec)

  /**
   * Computes the matrix product of this matrix with a column vector.
   */
  def multiply(vec: Vector3): Vector3 = {
    Vector3(
      m00 * vec.x + m01 * vec.y + m02 * vec.z,
      m10 * vec.x + m11 * vec.y + m12 * vec.z,
      m20 * vec.x + m21 * vec.y + m22 * vec.z
    )
  }

  // vec3 matrix product?
  // cross product?

  def transpose: Matrix3x3 = {
    Matrix3x3(
      m00, m10, m20,
      m01, m11, m21,
      m02, m12, m22
    )
  }

  def determinant: Float = {
    m00 * m11 * m22 +
    m10 * m21 * m02 +
    m20 * m01 * m12 -
    m02 * m11 * m20 -
    m12 * m21 * m00 -
    m22 * m01 * m10
  }

  def inverse: Matrix3x3 = {
    // Inverse of a Matrix using minors, cofactors and adjugate
    val matrixOfCofactors = Matrix3x3(
      +(m11 * m22 - m12 * m21), -(m10 * m22 - m12 * m20), +(m10 * m21 - m11 * m20),
      -(m01 * m22 - m02 * m21), +(m00 * m22 - m02 * m20), -(m00 * m21 - m01 * m20),
      +(m01 * m12 - m02 * m11), -(m00 * m12 - m02 * m10), +(m00 * m11 - m01 * m10)
    )
    // TODO: if determinant is 0, throw a meaningful exception
    matrixOfCofactors.transpose.multiply(1 / determinant)
  }

  def normalMatrix: Matrix3x3 = inverse.transpose

  def allocateBuffer: FloatBuffer = {
    val direct = Buffers.createFloatBuffer(9)
    direct
      .put(m00).put(m01).put(m02)
      .put(m10).put(m11).put(m12)
      .put(m20).put(m21).put(m22)
    direct.flip()
    direct
  }

  def updateBuffer(buffer: FloatBuffer): Unit = {
    buffer.clear()
    buffer
      .put(m00).put(m01).put(m02)
      .put(m10).put(m11).put(m12)
      .put(m20).put(m21).put(m22)
    buffer.flip()
  }
}
