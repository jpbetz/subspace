package com.github.jpbetz.subspace

import org.testng.Assert._
import org.testng.annotations.Test

class Vector4Test extends Asserts {

  // TODO: How to handle divide by zero?  Scala x/0f results in Infinity.

  @Test def testMagnitude(): Unit = {
    assertEquals(Vector4(0, 0, 0, 0).magnitude, 0f)
    assertEquals(Vector4(1, 0, 0, 0).magnitude, 1f)
    assertEquals(Vector4(0, 1, 0, 0).magnitude, 1f)
    assertEquals(Vector4(0, 0, 1, 0).magnitude, 1f)
    assertEquals(Vector4(0, 0, 0, 1).magnitude, 1f)
    assertEquals(Vector4(-1, 0, 0, 0).magnitude, 1f)
    assertFloat(Vector4(1, 1, 0, 0).magnitude, 1.414f)
    assertFloat(Vector4(1, 0, 1, 0).magnitude, 1.414f)
    assertFloat(Vector4(0, 1, 1, 0).magnitude, 1.414f)
    assertFloat(Vector4(-1, -1, 0, 0).magnitude, 1.414f)
  }

  @Test def testNormalize(): Unit = {
    // TODO: ???
    //assertEquals(Vector4(0, 0).normalize, Vector4(Float.NaN, Float.NaN))

    assertEquals(Vector4(2, 0, 0, 0).normalize, Vector4(1, 0, 0, 0))
    assertEquals(Vector4(0, 2, 0, 0).normalize, Vector4(0, 1, 0, 0))
    assertEquals(Vector4(0, 0, 2, 0).normalize, Vector4(0, 0, 1, 0))
    assertEquals(Vector4(0, 0, 0, 2).normalize, Vector4(0, 0, 0, 1))

    Seq(Vector4(2, 2, 2, 2), Vector4(-2, -2, -2, -2)) foreach { vec =>
      val normal = vec.normalize
      assertEquals(normal.x, normal.y)
      assertFloat(normal.magnitude, 1f)
    }
  }

  @Test def testDotProduct(): Unit = {
    assertFloat(Vector4(0, 0, 0, 0).dotProduct(Vector4(0, 0, 0, 0)), 0f)
    assertFloat(Vector4(2, 4, 1, 1).dotProduct(Vector4(3, 5, 6, 3)), 35f)
  }

  @Test def testArithmetic(): Unit = {
    assertEquals(-Vector4(0, 0, 0, 0), Vector4(0, 0, 0, 0))
    assertEquals(-Vector4(1, 2, -3, 1), Vector4(-1, -2, 3, 1))
    assertEquals(Vector4(2, 3, 4, 1) + Vector4(5, 2, 1, 1), Vector4(7, 5, 5, 1))
    assertEquals(Vector4(2, 3, 4, 1) - Vector4(5, 2, 1, 1), Vector4(-3, 1, 3, 1))

    assertEquals(Vector4(0, 0, 0, 1) * 0, Vector4(0, 0, 0, 1))
    assertEquals(Vector4(2, 3, 1, 1) * 0, Vector4(0, 0, 0, 1))
    assertEquals(Vector4(0, 0, 0, 1) * 10, Vector4(0, 0, 0, 1))
    assertEquals(Vector4(1, 2, 3, 1) * 2, Vector4(2, 4, 6, 1))

    // TODO: divide by zero should not be infinity.  Although it looks like that is the convention in scala.
    assertEquals(Vector4(1, 1, 1, 1) / 0, Vector4(Float.PositiveInfinity, Float.PositiveInfinity, Float.PositiveInfinity, Float.PositiveInfinity))

    assertEquals(Vector4(0, 0, 0, 0) / 5, Vector4(0, 0, 0, 0))
  }

  @Test def testDistanceTo(): Unit = {
    assertEquals(Vector4(0, 0, 0, 0).distanceTo(Vector4(0, 0, 0, 0)), 0f)
    assertEquals(Vector4(0, 0, 0, 1).distanceTo(Vector4(1, 0, 0, 1)), 1f)
    assertFloat(Vector4(0, 0, 0, 1).distanceTo(Vector4(1, 1, 0, 1)), 1.414f)
    assertFloat(Vector4(0, 0, 0, 1).distanceTo(Vector4(1, 0, 1, 1)), 1.414f)
    assertFloat(Vector4(0, 0, 0, 1).distanceTo(Vector4(0, 1, 1, 1)), 1.414f)

    assertFloat(Vector4(0, 0, 0, 1).distanceTo(Vector4(1, 1, 1, 1)), 1.732f)

    assertFloat(Vector4(-0.5f, -0.5f, -0.5f, 1).distanceTo(Vector4(0.5f, 0.5f, 0.5f, 1)), 1.732f)
    assertFloat(Vector4(-0.5f, 0.5f, 0.5f, 1).distanceTo(Vector4(0.5f, -0.5f, -0.5f, 1)), 1.732f)
  }

  @Test def testClamp(): Unit = {
    assertEquals(Vector4(0, 0, 0, 1).clamp(Vector4(0, 0, 0, 1), Vector4(0, 0, 0, 1)), Vector4(0, 0, 0, 1))

    assertEquals(Vector4(4, 5, 6, 1).clamp(Vector4(-3, -2, -1, 1), Vector4(3, 2, 1, 1)), Vector4(3, 2, 1, 1))
    assertEquals(Vector4(-4, -5, -6, 1).clamp(Vector4(-3, -2, -1, 1), Vector4(3, 2, 1, 1)), Vector4(-3, -2, -1, 1))
  }

  @Test def testLerp(): Unit = {
    assertEquals(Vector4(0, 0, 0, 1).lerp(Vector4(0, 0, 0, 1), 0), Vector4(0, 0, 0, 1))
    assertEquals(Vector4(0, 0, 0, 1).lerp(Vector4(0, 0, 0, 1), 1), Vector4(0, 0, 0, 1))

    assertEquals(Vector4(-1, -3, -5, 1).lerp(Vector4(1, 3, 5, 1), 0f), Vector4(-1, -3, -5, 1))
    assertEquals(Vector4(-1, -3, -5, 1).lerp(Vector4(1, 3, 5, 1), 0.25f), Vector4(-0.5f, -1.5f, -2.5f, 1))
    assertEquals(Vector4(-1, -3, -5, 1).lerp(Vector4(1, 3, 5, 1), 0.5f), Vector4(0, 0, 0, 1))
    assertEquals(Vector4(-1, -3, -5, 1).lerp(Vector4(1, 3, 5, 1), 0.75f), Vector4(0.5f, 1.5f, 2.5f, 1))
    assertEquals(Vector4(-1, -3, -5, 1).lerp(Vector4(1, 3, 5, 1), 1f), Vector4(1, 3, 5, 1))
  }

  @Test def testCopy(): Unit = {
    assertEquals(Vector4(0, 0, 0, 1).copy(), Vector4(0, 0, 0, 1))
    assertEquals(Vector4(1, 2, 3, 1).copy(), Vector4(1, 2, 3, 1))
  }

  @Test def testBuffer(): Unit = {
    val buffer = Vector4(1, 10, 5, 1).allocateBuffer
    assertEquals(buffer.get(0), 1f)
    assertEquals(buffer.get(1), 10f)
    assertEquals(buffer.get(2), 5f)
    assertEquals(buffer.get(3), 1f)

    Vector4(-2, -20, -5, -1).updateBuffer(buffer)
    assertEquals(buffer.get(0), -2f)
    assertEquals(buffer.get(1), -20f)
    assertEquals(buffer.get(2), -5f)
    assertEquals(buffer.get(3), -1f)
  }
}
