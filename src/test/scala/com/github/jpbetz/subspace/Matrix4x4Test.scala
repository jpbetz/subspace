package com.github.jpbetz.subspace

import org.testng.Assert._
import org.testng.annotations.Test

class Matrix4x4Test {
  private val thousandths = 0.001f

  private val v1 = Vector3(2, 5, -3)
  private val roll90 = Quaternion.fromAxisAngle(Orientation.z, scala.math.Pi.toFloat/2)

  @Test def testRotation(): Unit = {
    val rotateMatrix = Matrix4x4.forRotation(roll90)
    val rotated = (rotateMatrix * Vector4(v1, 1)).xyz
    assertVectors(rotated, Vector3(-5, 2, -3))

    val rotatedWithQuat = v1.rotate(roll90)
    assertVectors(rotatedWithQuat, Vector3(-5, 2, -3))
  }

  @Test def testTranslation(): Unit = {
    val translateMatrix = Matrix4x4.forTranslation(Vector3(1, 2, 3))
    val translated = (translateMatrix * Vector4(v1, 1)).xyz
    assertVectors(translated, Vector3(3, 7, 0))
  }

  @Test def testScale(): Unit = {
    val scaleMatrix = Matrix4x4.forScale(Vector3(2, 3, 2))
    val translated = (scaleMatrix * Vector4(v1, 1)).xyz
    assertVectors(translated, Vector3(4, 15, -6))
  }

  @Test def testMatrixMultiplication(): Unit = {

    val rotateMatrix = Matrix4x4.forRotation(roll90)
    val translateMatrix = Matrix4x4.forTranslation(Vector3(1, 2, 3))
    val scaleMatrix = Matrix4x4.forScale(Vector3(2, 3, 2))

    val scaleAndRotate = scaleMatrix * rotateMatrix
    assertVectors((scaleAndRotate * Vector4(v1, 1)).xyz, Vector3(-10, 6, -6))

    val rotateAndTranslate = rotateMatrix * translateMatrix
    assertVectors((rotateAndTranslate * Vector4(v1, 1)).xyz, Vector3(-7, 3, 0))
  }

  def assertVectors(v1: Vector3, v2: Vector3) = {
    assertEquals(v1.x, v2.x, thousandths, s"$v1 != $v2")
    assertEquals(v1.y, v2.y, thousandths, s"$v1 != $v2")
    assertEquals(v1.z, v2.z, thousandths, s"$v1 != $v2")
  }
}
