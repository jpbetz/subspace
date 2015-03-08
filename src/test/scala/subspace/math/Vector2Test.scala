package subspace.math

import org.testng.Assert._
import org.testng.annotations.Test

class Vector2Test {

  private val thousandths = 0.001f

  // TODO: How to handle divide by zero?  Scala x/0f results in Infinity.

  @Test def testMagnitude(): Unit = {
    assertEquals(Vector2(0, 0).magnitude, 0f)
    assertEquals(Vector2(0, 1).magnitude, 1f)
    assertEquals(Vector2(1, 0).magnitude, 1f)
    assertEquals(Vector2(0, -1).magnitude, 1f)
    assertEquals(Vector2(-1, 0).magnitude, 1f)
    assertEquals(Vector2(1, 1).magnitude, 1.414f, thousandths)
    assertEquals(Vector2(-1, -1).magnitude, 1.414f, thousandths)
  }

  @Test def testNormalize(): Unit = {
    // TODO: ???
    //assertEquals(Vector2(0, 0).normalize, Vector2(Float.NaN, Float.NaN))

    assertEquals(Vector2(2, 0).normalize, Vector2(1, 0))
    assertEquals(Vector2(0, 2).normalize, Vector2(0, 1))

    Seq(Vector2(2, 2), Vector2(-2, -2)) foreach { vec =>
      val normal = vec.normalize
      assertEquals(normal.x, normal.y)
      assertEquals(normal.magnitude, 1f, thousandths)
    }
  }

  @Test def testDotProduct(): Unit = {
    assertEquals(Vector2(0, 0).dotProduct(Vector2(0, 0)), 0f, thousandths)
    assertEquals(Vector2(2, 4).dotProduct(Vector2(3, 5)), 26f, thousandths)
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
    assertEquals(Vector2(0, 0).distanceTo(Vector2(1, 1)), 1.414f, thousandths)

    assertEquals(Vector2(0, 0).distanceTo(Vector2(1, 1)), 1.414f, thousandths)
    assertEquals(Vector2(-0.5f, -0.5f).distanceTo(Vector2(0.5f, 0.5f)), 1.414f, thousandths)
    assertEquals(Vector2(-0.5f, 0.5f).distanceTo(Vector2(0.5f, -0.5f)), 1.414f, thousandths)
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
    val buffer = Vector2(1, 10).allocateBuffer
    assertEquals(buffer.get(0), 1f)
    assertEquals(buffer.get(1), 10f)

    Vector2(-2, -20).updateBuffer(buffer)
    assertEquals(buffer.get(0), -2f)
    assertEquals(buffer.get(1), -20f)
  }
}
