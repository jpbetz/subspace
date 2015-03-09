package com.github.jpbetz.subspace

import org.testng.Assert._

trait Asserts {
  private val thousandths = 0.001f

  def assertFloat(f1: Float, f2: Float) = {
    assertEquals(f1, f2, thousandths)
  }

  def assertVectors(v1: Vector2, v2: Vector2) = {
    0 until 2 foreach { i =>
      assertEquals(v1(i), v2(i), thousandths, s"$v1 != $v2")
    }
  }

  def assertVectors(v1: Vector3, v2: Vector3) = {
    0 until 3 foreach { i =>
      assertEquals(v1(i), v2(i), thousandths, s"$v1 != $v2")
    }
  }

  def assertVectors(v1: Vector4, v2: Vector4) = {
    0 until 4 foreach { i =>
      assertEquals(v1(i), v2(i), thousandths, s"$v1 != $v2")
    }
  }

  def assertMatrices(m1: Matrix4x4, m2: Matrix4x4) = {
    0 until 4 foreach { c =>
      0 until 4 foreach { r =>
        assertEquals(m1.column(c)(r), m2.column(c)(r), thousandths, s"$m1 != $m2")
      }
    }
  }
}
