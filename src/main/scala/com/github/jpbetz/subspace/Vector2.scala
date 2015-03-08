package com.github.jpbetz.subspace

import java.nio.FloatBuffer

object Vector2 {
  lazy val origin = Vector2(0f, 0f)
}

case class Vector2(x: Float, y: Float) extends Vector with Bufferable {

  def size: Int = 2

  def apply(index: Int): Float = {
    index match {
      case 0 => x
      case 1 => y
      case _ => throw new ArrayIndexOutOfBoundsException(index)
    }
  }

  def magnitude: Float = {
    Math.sqrt(x * x + y * y).toFloat
  }

  def normalize: Vector2 = {
    val l = 1f / magnitude
    Vector2(x * l, y * l)
  }

  def dotProduct(vec: Vector2): Float = {
    x * vec.x + y * vec.y
  }

  def unary_- : Vector2 = negate
  def negate: Vector2 = Vector2(-x, -y)

  def +(vec: Vector2): Vector2 = add(vec)
  def add(vec: Vector2): Vector2 = add(vec.x, vec.y)
  def add(x: Float, y: Float): Vector2 = {
    Vector2(this.x + x, this.y + y)
  }

  def -(vec: Vector2): Vector2 = subtract(vec.x, vec.y)
  def subtract(vec: Vector2): Vector2 = subtract(vec.x, vec.y)
  def subtract(x: Float, y: Float): Vector2 = {
    Vector2(this.x - x, this.y - y)
  }

  def *(f: Float): Vector2 = scale(f)
  def /(f: Float): Vector2 = scale(1/f)

  def scale(f: Float): Vector2 = scale(f, f)
  def scale(vec: Vector2): Vector2 = scale(vec.x, vec.y)
  def scale(x: Float, y: Float): Vector2 = {
    Vector2(this.x * x, this.y * y)
  }

  def clamp(min: Float, max: Float): Vector2 = {
    if (min > max) throw new IllegalArgumentException("min must not be greater than max")
    Vector2(
      Floats.clamp(x, min, max),
      Floats.clamp(y, min, max)
    )
  }

  def clamp(min: Vector2, max: Vector2): Vector2 = {
    if (min.x > max.x) throw new IllegalArgumentException("min.x must not be greater than max.x")
    if (min.y > max.y) throw new IllegalArgumentException("min.y must not be greater than max.y")
    Vector2(
      Floats.clamp(x, min.x, max.x),
      Floats.clamp(y, min.y, max.y)
    )
  }

  def distanceTo(vec: Vector2): Float = {
    (this - vec).magnitude
  }

  def lerp(vec: Vector2, t: Float): Vector2 = {
    if (t < 0f || t > 1f) throw new IllegalArgumentException("t must be between 0 and 1, inclusively.")
    Vector2(
      Floats.lerp(x, vec.x, t),
      Floats.lerp(y, vec.y, t)
    )
  }

  def round(): Vector2 = {
    Vector2(
      scala.math.round(x),
      scala.math.round(y)
    )
  }

  def rotate(): Vector2 = ???

  def copy(): Vector2 = {
    Vector2(this.x, this.y)
  }

  override def toString: String = {
    s"($x, $y)"
  }

  def allocateBuffer: FloatBuffer = {
    val direct = Buffers.createFloatBuffer(2)
    direct.put(x).put(y)
    direct.flip()
    direct
  }

  def updateBuffer(buffer: FloatBuffer): Unit = {
    buffer.clear()
    buffer.put(x).put(y)
    buffer.flip()
  }
}
