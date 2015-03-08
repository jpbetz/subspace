package subspace.math

import org.testng.Assert._
import org.testng.annotations.Test

class Matrix4x4Test {
  private val thousandths = 0.001f

  @Test def testRotation(): Unit = {
    val rollLeft = Quaternion.fromAxisAngle(Orientation.z, scala.math.Pi.toFloat/2)
    val rollRight = Quaternion.fromAxisAngle(Orientation.z, -scala.math.Pi.toFloat/2)
    val vec = Vector3(1, 1, 1)

    val rotatedWithMatrix = Matrix4x4.forRotation(rollLeft) * Vector4(vec, 0)
    val rotatedWithQuat = vec.rotate(rollRight)

    assertVectors(rotatedWithQuat, Vector3(1, -1, 1))

    // TODO: is this right?  Should a matrix4x4 rotation be in the opposite direction as a vector.rotate(quaternion) ?
    assertVectors(rotatedWithMatrix.xyz, rotatedWithQuat)
  }

  def assertVectors(v1: Vector3, v2: Vector3) = {
    assertEquals(v1.x, v2.x, thousandths)
    assertEquals(v1.y, v2.y, thousandths)
    assertEquals(v1.z, v2.z, thousandths)
  }
}
