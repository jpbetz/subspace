package com.github.jpbetz.subspace

import org.testng.Assert._
import org.testng.annotations.Test

class Vector2Test extends Asserts {

  // TODO: How to handle divide by zero?  Scala x/0f results in Infinity.

  @Test def testMagnitude(): Unit = {
    assertEquals(Vector2(0, 0).magnitude, 0f)
    assertEquals(Vector2(0, 1).magnitude, 1f)
    assertEquals(Vector2(1, 0).magnitude, 1f)
    assertEquals(Vector2(0, -1).magnitude, 1f)
    assertEquals(Vector2(-1, 0).magnitude, 1f)
    assertFloat(Vector2(1, 1).magnitude, 1.414f)
    assertFloat(Vector2(-1, -1).magnitude, 1.414f)
  }

  @Test def testNormalize(): Unit = {
    assertEquals(Vector2(2, 0).normalize, Vector2(1, 0))
    assertEquals(Vector2(0, 2).normalize, Vector2(0, 1))

    Seq(Vector2(2, 2), Vector2(-2, -2)) foreach { vec =>
      val normal = vec.normalize
      assertEquals(normal.x, normal.y)
      assertFloat(normal.magnitude, 1f)
    }
  }

  @Test def testDotProduct(): Unit = {
    assertFloat(Vector2(0, 0).dotProduct(Vector2(0, 0)), 0f)
    assertFloat(Vector2(2, 4).dotProduct(Vector2(3, 5)), 26f)
  }

  @Test def testArithmetic(): Unit = {
    assertEquals(-Vector2(0, 0), Vector2(0, 0))
    assertEquals(-Vector2(1, 2), Vector2(-1, -2))
    assertEquals(Vector2(2, 3) + Vector2(5, 2), Vector2(7, 5))
    assertEquals(Vector2(2, 3) - Vector2(5, 2), Vector2(-3, 1))

    assertEquals(Vector2(0, 0) * 0, Vector2(0, 0))
    assertEquals(Vector2(2, 3) * 0, Vector2(0, 0))
    assertEquals(Vector2(0, 0) * 10, Vector2(0, 0))
    assertEquals(Vector2(1, 2) * 2, Vector2(2, 4))

    // TODO: divide by zero should not be infinity.  Although it looks like that is the convention in scala.
    assertEquals(Vector2(1, 1) / 0, Vector2(Float.PositiveInfinity, Float.PositiveInfinity))
    assertEquals(Vector2(0, 0) / 5, Vector2(0, 0))
  }

  @Test def testDistanceTo(): Unit = {
    assertEquals(Vector2(0, 0).distanceTo(Vector2(0, 0)), 0f)
    assertEquals(Vector2(0, 0).distanceTo(Vector2(1, 0)), 1f)
    assertFloat(Vector2(0, 0).distanceTo(Vector2(1, 1)), 1.414f)

    assertFloat(Vector2(0, 0).distanceTo(Vector2(1, 1)), 1.414f)
    assertFloat(Vector2(-0.5f, -0.5f).distanceTo(Vector2(0.5f, 0.5f)), 1.414f)
    assertFloat(Vector2(-0.5f, 0.5f).distanceTo(Vector2(0.5f, -0.5f)), 1.414f)
  }

  @Test def testClamp(): Unit = {
    assertEquals(Vector2(0, 0).clamp(Vector2(0, 0), Vector2(0, 0)), Vector2(0, 0))

    assertEquals(Vector2(4, 5).clamp(Vector2(-3, -2), Vector2(3, 2)), Vector2(3, 2))
    assertEquals(Vector2(-4, -5).clamp(Vector2(-3, -2), Vector2(3, 2)), Vector2(-3, -2))
  }

  @Test def testLerp(): Unit = {
    assertEquals(Vector2(0, 0).lerp(Vector2(0, 0), 0), Vector2(0, 0))
    assertEquals(Vector2(0, 0).lerp(Vector2(0, 0), 1), Vector2(0, 0))

    assertEquals(Vector2(-1, -3).lerp(Vector2(1, 3), 0f), Vector2(-1, -3))
    assertEquals(Vector2(-1, -3).lerp(Vector2(1, 3), 0.25f), Vector2(-0.5f, -1.5f))
    assertEquals(Vector2(-1, -3).lerp(Vector2(1, 3), 0.5f), Vector2(0, 0))
    assertEquals(Vector2(-1, -3).lerp(Vector2(1, 3), 0.75f), Vector2(0.5f, 1.5f))
    assertEquals(Vector2(-1, -3).lerp(Vector2(1, 3), 1f), Vector2(1, 3))
  }

  @Test def testCopy(): Unit = {
    assertEquals(Vector2(0, 0).copy(), Vector2(0, 0))
    assertEquals(Vector2(1, 2).copy(), Vector2(1, 2))
  }

  @Test def testBuffer(): Unit = {
    val v1 = Vector2(1, 10)

    val buffer = v1.allocateBuffer
    assertEquals(v1, Vector2.fromBuffer(buffer))

    val updateBuffer = Vector2.allocateEmptyBuffer
    v1.updateBuffer(updateBuffer)
    assertEquals(v1, Vector2.fromBuffer(updateBuffer))
  }
}
