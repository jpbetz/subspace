package com.github.jpbetz.subspace

import java.nio.FloatBuffer

object Vector4 {
  def fill(value: Float): Vector4 = Vector4(value, value, value, value)

  // convenience constructors
  def apply(xyz: Vector3, w: Float): Vector4 = Vector4(xyz(0), xyz(1), xyz(2), w)
  def apply(x: Float, yzw: Vector3): Vector4 = Vector4(x, yzw(0), yzw(1), yzw(2))
  def apply(xy: Vector2, z: Float, w: Float): Vector4 = Vector4(xy(0), xy(1), z, w)
  def apply(x: Float, yz: Vector2, w: Float): Vector4 = Vector4(x, yz(0), yz(1), w)
  def apply(x: Float, y: Float, zw: Vector2): Vector4 = Vector4(x, y, zw(0), zw(1))
  def apply(xy: Vector2, zw: Vector2): Vector4 = Vector4(xy(0), xy(1), zw(0), zw(1))
}

case class Vector4(
    x: Float,
    y: Float,
    z: Float,
    w: Float)
  extends Bufferable {

  def apply(index: Int): Float = {
    index match {
      case 0 => x
      case 1 => y
      case 2 => z
      case 3 => w
      case _ => throw new ArrayIndexOutOfBoundsException(index)
    }
  }

  def magnitude: Float = {
    Math.sqrt(x * x + y * y + z * z + w * w).toFloat
  }

  def normalize: Vector4 = {
    val l = 1f / magnitude
    Vector4(x * l, y * l, z * l, w * l)
  }

  def unary_- : Vector4 = negate
  def negate: Vector4 = Vector4(-x, -y, -z, -w)

  def +(vec: Vector4): Vector4 = add(vec)
  def add(vec: Vector4): Vector4 = add(vec.x, vec.y, vec.z)
  def add(x: Float, y: Float, z: Float): Vector4 = {
    Vector4(this.x + x, this.y + y, this.z + z, this.w + w)
  }

  def -(vec: Vector4): Vector4 = subtract(vec)
  def subtract(vec: Vector4): Vector4 = subtract(vec.x, vec.y, vec.z, vec.w)
  def subtract(x: Float, y: Float, z: Float, w: Float): Vector4 = {
    Vector4(this.x - x, this.y - y, this.z - z, this.w - w)
  }

  def scale(f: Float): Vector4 = scale(f, f, f, f)
  def scale(vec: Vector4): Vector4 = scale(vec.x, vec.y, vec.z, vec.w)
  def scale(x: Float, y: Float, z: Float, w: Float): Vector4 = {
    Vector4(this.x * x, this.y * y, this.z * z, this.w * w)
  }

  def dotProduct(vec: Vector4): Float = {
    x * vec.x + y * vec.y + z * vec.z + w * vec.w
  }

  def lerp(vec: Vector4, t: Float): Vector4 = {
    Vector4(
      Floats.lerp(x, vec.x, t),
      Floats.lerp(y, vec.y, t),
      Floats.lerp(z, vec.z, t),
      Floats.lerp(w, vec.w, t)
    )
  }

  def distanceTo(vec: Vector4): Float = {
    (this - vec).magnitude
  }

  def clamp(min: Float, max: Float): Vector4 = {
    if (min > max) throw new IllegalArgumentException("min must not be greater than max")
    Vector4(
      Floats.clamp(x, min, max),
      Floats.clamp(y, min, max),
      Floats.clamp(z, min, max),
      Floats.clamp(w, min, max)
    )
  }

  def clamp(min: Vector4, max: Vector4): Vector4 = {
    Vector4(
      Floats.clamp(x, min.x, max.x),
      Floats.clamp(y, min.y, max.y),
      Floats.clamp(z, min.z, max.z),
      Floats.clamp(w, min.w, max.w)
    )
  }

  def copy(): Vector4 = {
    Vector4(this.x, this.y, this.z, this.w)
  }

  override def toString: String = {
    s"($x, $y, $z, $w)"
  }

  // fizzle my swizzle
  def xx = Vector2(x, x)
  def xy = Vector2(x, y)
  def xz = Vector2(x, z)
  def xw = Vector2(x, w)

  def yx = Vector2(y, x)
  def yy = Vector2(y, y)
  def yz = Vector2(y, z)
  def yw = Vector2(y, w)

  def zx = Vector2(z, x)
  def zy = Vector2(z, y)
  def zz = Vector2(z, z)
  def zw = Vector2(z, w)

  def wx = Vector2(w, x)
  def wy = Vector2(w, y)
  def wz = Vector2(w, z)
  def ww = Vector2(w, w)

  def xxx = Vector3(x, x, x)
  def xxy = Vector3(x, x, y)
  def xxz = Vector3(x, x, z)
  def xxw = Vector3(x, x, w)

  def xyx = Vector3(x, y, x)
  def xyy = Vector3(x, y, y)
  def xyz = Vector3(x, y, z)
  def xyw = Vector3(x, y, w)

  def xzx = Vector3(x, z, x)
  def xzy = Vector3(x, z, y)
  def xzz = Vector3(x, z, z)
  def xzw = Vector3(x, z, w)

  def yxx = Vector3(y, x, x)
  def yxy = Vector3(y, x, y)
  def yxz = Vector3(y, x, z)
  def yxw = Vector3(y, x, w)

  def yyx = Vector3(y, y, x)
  def yyy = Vector3(y, y, y)
  def yyz = Vector3(y, y, z)
  def yyw = Vector3(y, y, w)

  def yzx = Vector3(y, z, x)
  def yzy = Vector3(y, z, y)
  def yzz = Vector3(y, z, z)
  def yzw = Vector3(y, z, w)

  def zxx = Vector3(z, x, x)
  def zxy = Vector3(z, x, y)
  def zxz = Vector3(z, x, z)
  def zxw = Vector3(z, x, w)

  def zyx = Vector3(z, y, x)
  def zyy = Vector3(z, y, y)
  def zyz = Vector3(z, y, z)
  def zyw = Vector3(z, y, w)

  def zzx = Vector3(z, z, x)
  def zzy = Vector3(z, z, y)
  def zzz = Vector3(z, z, z)
  def zzw = Vector3(z, z, w)

  def wxx = Vector3(w, x, x)
  def wxy = Vector3(w, x, y)
  def wxz = Vector3(w, x, z)
  def wxw = Vector3(w, x, w)

  def wyx = Vector3(w, y, x)
  def wyy = Vector3(w, y, y)
  def wyz = Vector3(w, y, z)
  def wyw = Vector3(w, y, w)

  def wzx = Vector3(w, z, x)
  def wzy = Vector3(w, z, y)
  def wzz = Vector3(w, z, z)
  def wzw = Vector3(w, z, w)

  // TODO: swizzle operators for vec4?  So many..  Maybe there is a better way to implement swizzle operators in scala?

  def allocateBuffer: FloatBuffer = {
    val direct = Buffers.createFloatBuffer(4)
    direct.put(x).put(y).put(z).put(w)
    direct.flip()
    direct
  }

  def updateBuffer(buffer: FloatBuffer): Unit = {
    buffer.clear()
    buffer.put(x).put(y).put(z).put(w)
    buffer.flip()
  }
}
