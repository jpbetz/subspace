package com.github.jpbetz.subspace

import org.testng.Assert._
import org.testng.annotations.Test

class Vector3Test extends Asserts {

  // TODO: How to handle divide by zero?  Scala x/0f results in Infinity.

  @Test def testMagnitude(): Unit = {
    assertEquals(Vector3(0, 0, 0).magnitude, 0f)
    assertEquals(Vector3(1, 0, 0).magnitude, 1f)
    assertEquals(Vector3(0, 1, 0).magnitude, 1f)
    assertEquals(Vector3(0, 0, 1).magnitude, 1f)
    assertEquals(Vector3(-1, 0, 0).magnitude, 1f)
    assertFloat(Vector3(1, 1, 0).magnitude, 1.414f)
    assertFloat(Vector3(1, 0, 1).magnitude, 1.414f)
    assertFloat(Vector3(0, 1, 1).magnitude, 1.414f)
    assertFloat(Vector3(-1, -1, 0).magnitude, 1.414f)
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
      assertFloat(normal.magnitude, 1f)
    }
  }

  @Test def testDotProduct(): Unit = {
    assertFloat(Vector3(0, 0, 0).dotProduct(Vector3(0, 0, 0)), 0f)
    assertFloat(Vector3(2, 4, 1).dotProduct(Vector3(3, 5, 6)), 32f)
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
    assertFloat(Vector3(0, 0, 0).distanceTo(Vector3(1, 1, 0)), 1.414f)
    assertFloat(Vector3(0, 0, 0).distanceTo(Vector3(1, 0, 1)), 1.414f)
    assertFloat(Vector3(0, 0, 0).distanceTo(Vector3(0, 1, 1)), 1.414f)

    assertFloat(Vector3(0, 0, 0).distanceTo(Vector3(1, 1, 1)), 1.732f)

    assertFloat(Vector3(-0.5f, -0.5f, -0.5f).distanceTo(Vector3(0.5f, 0.5f, 0.5f)), 1.732f)
    assertFloat(Vector3(-0.5f, 0.5f, 0.5f).distanceTo(Vector3(0.5f, -0.5f, -0.5f)), 1.732f)
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

  @Test def testCopy(): Unit = {
    assertEquals(Vector3(0, 0, 0).copy(), Vector3(0, 0, 0))
    assertEquals(Vector3(1, 2, 3).copy(), Vector3(1, 2, 3))
  }

  @Test def testBuffer(): Unit = {
    val v1 = Vector3(1, 10, 5)

    val buffer = v1.allocateBuffer
    assertEquals(v1, Vector3.fromBuffer(buffer))

    val updateBuffer = Vector3.allocateEmptyBuffer
    v1.updateBuffer(updateBuffer)
    assertEquals(v1, Vector3.fromBuffer(updateBuffer))
  }
}
