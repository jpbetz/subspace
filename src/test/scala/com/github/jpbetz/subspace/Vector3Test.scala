package com.github.jpbetz.subspace

import org.testng.Assert._
import org.testng.annotations.Test

class Vector3Test {

  private val thousandths = 0.001f

  // TODO: How to handle divide by zero?  Scala x/0f results in Infinity.

  @Test def testMagnitude(): Unit = {
    assertEquals(Vector3(0, 0, 0).magnitude, 0f)
    assertEquals(Vector3(1, 0, 0).magnitude, 1f)
    assertEquals(Vector3(0, 1, 0).magnitude, 1f)
    assertEquals(Vector3(0, 0, 1).magnitude, 1f)
    assertEquals(Vector3(-1, 0, 0).magnitude, 1f)
    assertEquals(Vector3(1, 1, 0).magnitude, 1.414f, thousandths)
    assertEquals(Vector3(1, 0, 1).magnitude, 1.414f, thousandths)
    assertEquals(Vector3(0, 1, 1).magnitude, 1.414f, thousandths)
    assertEquals(Vector3(-1, -1, 0).magnitude, 1.414f, thousandths)
  }

  @Test def testNormalize(): Unit = {
    // TODO: ???
    //assertEquals(Vector3(0, 0).normalize, Vector3(Float.NaN, Float.NaN))

    assertEquals(Vector3(2, 0, 0).normalize, Vector3(1, 0, 0))
    assertEquals(Vector3(0, 2, 0).normalize, Vector3(0, 1, 0))
    assertEquals(Vector3(0, 0, 2).normalize, Vector3(0, 0, 1))

    Seq(Vector3(2, 2, 2), Vector3(-2, -2, -2)) foreach { vec =>
      val normal = vec.normalize
      assertEquals(normal.x, normal.y)
      assertEquals(normal.magnitude, 1f, thousandths)
    }
  }

  @Test def testDotProduct(): Unit = {
    assertEquals(Vector3(0, 0, 0).dotProduct(Vector3(0, 0, 0)), 0f, thousandths)
    assertEquals(Vector3(2, 4, 1).dotProduct(Vector3(3, 5, 6)), 32f, thousandths)
  }

  @Test def testArithmetic(): Unit = {
    assertEquals(-Vector3(0, 0, 0), Vector3(0, 0, 0))
    assertEquals(-Vector3(1, 2, -3), Vector3(-1, -2, 3))
    assertEquals(Vector3(2, 3, 4) + Vector3(5, 2, 1), Vector3(7, 5, 5))
    assertEquals(Vector3(2, 3, 4) - Vector3(5, 2, 1), Vector3(-3, 1, 3))

    assertEquals(Vector3(0, 0, 0) * 0, Vector3(0, 0, 0))
    assertEquals(Vector3(2, 3, 1) * 0, Vector3(0, 0, 0))
    assertEquals(Vector3(0, 0, 0) * 10, Vector3(0, 0, 0))
    assertEquals(Vector3(1, 2, 3) * 2, Vector3(2, 4, 6))

    // TODO: divide by zero should not be infinity.  Although it looks like that is the convention in scala.
    assertEquals(Vector3(1, 1, 1) / 0, Vector3(Float.PositiveInfinity, Float.PositiveInfinity, Float.PositiveInfinity))

    assertEquals(Vector3(0, 0, 0) / 5, Vector3(0, 0, 0))
  }

  @Test def testDistanceTo(): Unit = {
    assertEquals(Vector3(0, 0, 0).distanceTo(Vector3(0, 0, 0)), 0f)
    assertEquals(Vector3(0, 0, 0).distanceTo(Vector3(1, 0, 0)), 1f)
    assertEquals(Vector3(0, 0, 0).distanceTo(Vector3(1, 1, 0)), 1.414f, thousandths)
    assertEquals(Vector3(0, 0, 0).distanceTo(Vector3(1, 0, 1)), 1.414f, thousandths)
    assertEquals(Vector3(0, 0, 0).distanceTo(Vector3(0, 1, 1)), 1.414f, thousandths)

    assertEquals(Vector3(0, 0, 0).distanceTo(Vector3(1, 1, 1)), 1.732f, thousandths)

    assertEquals(Vector3(-0.5f, -0.5f, -0.5f).distanceTo(Vector3(0.5f, 0.5f, 0.5f)), 1.732f, thousandths)
    assertEquals(Vector3(-0.5f, 0.5f, 0.5f).distanceTo(Vector3(0.5f, -0.5f, -0.5f)), 1.732f, thousandths)
  }

  @Test def testClamp(): Unit = {
    assertEquals(Vector3(0, 0, 0).clamp(Vector3(0, 0, 0), Vector3(0, 0, 0)), Vector3(0, 0, 0))

    assertEquals(Vector3(4, 5, 6).clamp(Vector3(-3, -2, -1), Vector3(3, 2, 1)), Vector3(3, 2, 1))
    assertEquals(Vector3(-4, -5, -6).clamp(Vector3(-3, -2, -1), Vector3(3, 2, 1)), Vector3(-3, -2, -1))
  }

  @Test def testLerp(): Unit = {
    assertEquals(Vector3(0, 0, 0).lerp(Vector3(0, 0, 0), 0), Vector3(0, 0, 0))
    assertEquals(Vector3(0, 0, 0).lerp(Vector3(0, 0, 0), 1), Vector3(0, 0, 0))

    assertEquals(Vector3(-1, -3, -5).lerp(Vector3(1, 3, 5), 0f), Vector3(-1, -3, -5))
    assertEquals(Vector3(-1, -3, -5).lerp(Vector3(1, 3, 5), 0.25f), Vector3(-0.5f, -1.5f, -2.5f))
    assertEquals(Vector3(-1, -3, -5).lerp(Vector3(1, 3, 5), 0.5f), Vector3(0, 0, 0))
    assertEquals(Vector3(-1, -3, -5).lerp(Vector3(1, 3, 5), 0.75f), Vector3(0.5f, 1.5f, 2.5f))
    assertEquals(Vector3(-1, -3, -5).lerp(Vector3(1, 3, 5), 1f), Vector3(1, 3, 5))
  }

  @Test def testRotate(): Unit = {
    assertVectors(Vector3(0, 0, 0).rotate(Vector3(1, 0, 0), scala.math.Pi.toFloat/2), Vector3(0, 0, 0))

    assertVectors(Vector3(1, 0, 0).rotate(Vector3(1, 0, 0), scala.math.Pi.toFloat/2), Vector3(1, 0, 0))
    assertVectors(Vector3(1, 0, 0).rotate(Vector3(0, 1, 0), scala.math.Pi.toFloat/2), Vector3(0, 0, -1))
    assertVectors(Vector3(1, 0, 0).rotate(Vector3(0, 0, 1), scala.math.Pi.toFloat/2), Vector3(0, 1, 0))
  }

  @Test def testCopy(): Unit = {
    assertEquals(Vector3(0, 0, 0).copy(), Vector3(0, 0, 0))
    assertEquals(Vector3(1, 2, 3).copy(), Vector3(1, 2, 3))
  }

  @Test def testBuffer(): Unit = {
    val buffer = Vector3(1, 10, 5).allocateBuffer
    assertEquals(buffer.get(0), 1f)
    assertEquals(buffer.get(1), 10f)
    assertEquals(buffer.get(2), 5f)

    Vector3(-2, -20, -5).updateBuffer(buffer)
    assertEquals(buffer.get(0), -2f)
    assertEquals(buffer.get(1), -20f)
    assertEquals(buffer.get(2), -5f)
  }

  def assertVectors(v1: Vector3, v2: Vector3) = {
    assertEquals(v1.x, v2.x, thousandths)
    assertEquals(v1.y, v2.y, thousandths)
    assertEquals(v1.z, v2.z, thousandths)
  }
}
