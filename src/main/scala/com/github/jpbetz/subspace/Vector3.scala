package com.github.jpbetz.subspace

import java.nio.FloatBuffer

/**
 * A 3 dimensional vector.
 *
 * Uses right hand rule orientation (note that OpenGL uses right handed, but DirectX use left handed).
 */
object Vector3 {
  def fill(value: Float): Vector3 = Vector3(value, value, value)

  // convenience constructors
  def apply(xy: Vector2, z: Float): Vector3 = Vector3(xy(0), xy(1), z)
  def apply(x: Float, yz: Vector2): Vector3 = Vector3(x, yz(0), yz(1))
}

case class Vector3(x: Float, y: Float, z: Float) extends Bufferable {

  def apply(index: Int): Float = {
    index match {
      case 0 => x
      case 1 => y
      case 2 => z
      case _ => throw new ArrayIndexOutOfBoundsException(index)
    }
  }

  def magnitude: Float = {
    Math.sqrt(x * x + y * y + z * z).toFloat
  }

  def normalize: Vector3 = {
    val l = 1f / magnitude
    Vector3(x * l, y * l, z * l)
  }

  def unary_- : Vector3 = negate
  def negate: Vector3 = Vector3(-x, -y, -z)

  def +(vec: Vector3): Vector3 = add(vec)
  def add(vec: Vector3): Vector3 = add(vec.x, vec.y, vec.z)
  def add(x: Float, y: Float, z: Float): Vector3 = {
    Vector3(this.x + x, this.y + y, this.z + z)
  }

  def -(vec: Vector3): Vector3 = subtract(vec)
  def subtract(vec: Vector3): Vector3 = subtract(vec.x, vec.y, vec.z)
  def subtract(x: Float, y: Float, z: Float): Vector3 = {
    Vector3(this.x - x, this.y - y, this.z - z)
  }

  def *(f: Float): Vector3 = scale(f)
  def /(f: Float): Vector3 = scale(1/f)

  def scale(f: Float): Vector3 = scale(f, f, f)
  def scale(vec: Vector3): Vector3 = scale(vec.x, vec.y, vec.z)
  def scale(x: Float, y: Float, z: Float): Vector3 = {
    Vector3(this.x * x, this.y * y, this.z * z)
  }

  def crossProduct(vec: Vector3): Vector3 = {
    Vector3(
      y * vec.z - z * vec.y,
      z * vec.x - x * vec.z,
      x * vec.y - y * vec.x
    )
  }

  def dotProduct(vec: Vector3): Float = {
    x * vec.x + y * vec.y + z * vec.z
  }

  def clamp(min: Float, max: Float): Vector3 = {
    if (min > max) throw new IllegalArgumentException("min must not be greater than max")
    Vector3(
      Floats.clamp(x, min, max),
      Floats.clamp(y, min, max),
      Floats.clamp(z, min, max)
    )
  }

  def clamp(min: Vector3, max: Vector3): Vector3 = {
    Vector3(
      Floats.clamp(x, min.x, max.x),
      Floats.clamp(y, min.y, max.y),
      Floats.clamp(z, min.z, max.z)
    )
  }

  def rotate(axis: Vector3, angle: Float): Vector3 = {
    rotate(Quaternion.fromAxisAngle(axis, angle))
  }

  def rotate(q: Quaternion): Vector3 = {
    val ix =  q.w * x + q.y * z - q.z * y
    val iy =  q.w * y + q.z * x - q.x * z
    val iz =  q.w * z + q.x * y - q.y * x
    val iw = - q.x * x - q.y * y - q.z * z

    Vector3(
      ix * q.w + iw * - q.x + iy * - q.z - iz * - q.y,
      iy * q.w + iw * - q.y + iz * - q.x - ix * - q.z,
      iz * q.w + iw * - q.z + ix * - q.y - iy * - q.x
    )
  }

  def distanceTo(vec: Vector3): Float = {
    (this - vec).magnitude
  }

  def lerp(vec: Vector3, t: Float): Vector3 = {
    Vector3(
      Floats.lerp(x, vec.x, t),
      Floats.lerp(y, vec.y, t),
      Floats.lerp(z, vec.z, t)
    )
  }

  def round(): Vector3 = {
    Vector3(
      scala.math.round(x),
      scala.math.round(y),
      scala.math.round(z)
    )
  }

  // TODO: Projects a vector onto another vector.
  //def project(onNormal: Vector3): Vector3 = ???

  // TODO: Projects a vector onto a plane defined by a normal orthogonal to the plane.
  //def projectOntoPlane(planeNormal: Vector3): Vector3 = ???

  // TODO: Reflects a vector off the plane defined by a normal.
  //def reflect(inNormal: Vector3): Vector3 = ???

  // TODO: Rotates a vector current towards target.
  //This function is similar to MoveTowards except that the vector is treated as a direction rather than a position. The current vector will be rotated round toward the target direction by an angle of maxRadiansDelta, although it will land exactly on the target rather than overshoot. If the magnitudes of current and target are different then the magnitude of the result will be linearly interpolated during the rotation. If a negative value is used for maxRadiansDelta, the vector will rotate away from target/ until it is pointing in exactly the opposite direction, then stop.
  def rotateTowards(target: Vector3, maxRadiansDelta: Float, maxMagnitudeDelta: Float): Vector3 = ???

  def copy(): Vector3 = {
    Vector3(this.x, this.y, this.z)
  }

  override def toString: String = {
    s"($x, $y, $z)"
  }

  // swizzle operators
  def xx = Vector2(x, x)
  def xy = Vector2(x, y)
  def xz = Vector2(x, z)

  def yx = Vector2(y, x)
  def yy = Vector2(y, y)
  def yz = Vector2(y, z)

  def zx = Vector2(z, x)
  def zy = Vector2(z, y)
  def zz = Vector2(z, z)

  def xxx = Vector3(x, x, x)
  def xxy = Vector3(x, x, y)
  def xxz = Vector3(x, x, z)

  def xyx = Vector3(x, y, x)
  def xyy = Vector3(x, y, y)
  def xyz = Vector3(x, y, z)

  def xzx = Vector3(x, z, x)
  def xzy = Vector3(x, z, y)
  def xzz = Vector3(x, z, z)

  def yxx = Vector3(y, x, x)
  def yxy = Vector3(y, x, y)
  def yxz = Vector3(y, x, z)

  def yyx = Vector3(y, y, x)
  def yyy = Vector3(y, y, y)
  def yyz = Vector3(y, y, z)

  def yzx = Vector3(y, z, x)
  def yzy = Vector3(y, z, y)
  def yzz = Vector3(y, z, z)

  def zxx = Vector3(z, x, x)
  def zxy = Vector3(z, x, y)
  def zxz = Vector3(z, x, z)

  def zyx = Vector3(z, y, x)
  def zyy = Vector3(z, y, y)
  def zyz = Vector3(z, y, z)

  def zzx = Vector3(z, z, x)
  def zzy = Vector3(z, z, y)
  def zzz = Vector3(z, z, z)

  def allocateBuffer: FloatBuffer = {
    val direct = Buffers.createFloatBuffer(3)
    direct.put(x).put(y).put(z)
    direct.flip()
    direct
  }

  def updateBuffer(buffer: FloatBuffer): Unit = {
    buffer.clear()
    buffer.put(x).put(y).put(z)
    buffer.flip()
  }
}
