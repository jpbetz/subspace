package com.github.jpbetz.subspace

import org.testng.annotations.Test

class Matrix4x4Test extends Asserts {
  private val point1 = Vector3(2, 5, -3)
  private val point1w = Vector4(point1, 1)

  private val point2 = Vector3(20, 50, -30)
  private val point2w = Vector4(point2, 1)

  private val matrix1 = Matrix4x4(
    1,2,3,4,
    5,6,2,3,
    4,5,6,7,
    1,5,3,2)

  private val roll90 = Quaternion.fromAxisAngle(Orientation.z, scala.math.Pi.toFloat/2)

  @Test def testRotation(): Unit = {
    val rotateMatrix = Matrix4x4.forRotation(roll90)
    val rotated = (rotateMatrix * point1w).xyz
    assertVectors(rotated, Vector3(-5, 2, -3))

    val rotatedWithQuat = point1.rotate(roll90)
    assertVectors(rotatedWithQuat, Vector3(-5, 2, -3))
  }

  @Test def testTranslation(): Unit = {
    val translateMatrix = Matrix4x4.forTranslation(Vector3(1, 2, 3))
    val translated = (translateMatrix * point1w).xyz
    assertVectors(translated, Vector3(3, 7, 0))
  }

  @Test def testScale(): Unit = {
    val scaleMatrix = Matrix4x4.forScale(Vector3(2, 3, 2))
    val scaled = (scaleMatrix * point1w).xyz
    assertVectors(scaled, Vector3(4, 15, -6))
  }

  @Test def testTransformMatrixComposition(): Unit = {
    val rotateMatrix = Matrix4x4.forRotation(roll90)
    val translateMatrix = Matrix4x4.forTranslation(Vector3(1, 2, 3))
    val scaleMatrix = Matrix4x4.forScale(Vector3(2, 3, 2))
    val trsMatrix = Matrix4x4.forTranslationRotationScale(Vector3(1, 2, 3), roll90, Vector3(2, 3, 2))

    val scaleAndRotate = scaleMatrix * rotateMatrix
    assertVectors((scaleAndRotate * point1w).xyz, Vector3(-10, 6, -6))

    val scaleAndTranslate = scaleMatrix * translateMatrix
    assertVectors((scaleAndTranslate * point1w).xyz, Vector3(6, 21, 0))

    val rotateAndTranslate = rotateMatrix * translateMatrix
    assertVectors((rotateAndTranslate * point1w).xyz, Vector3(-7, 3, 0))

    val rotateAndScale = rotateMatrix * scaleMatrix
    assertVectors((rotateAndScale * point1w).xyz, Vector3(-15, 4, -6))

    val translatedAndScaled = translateMatrix * scaleMatrix
    assertVectors((translatedAndScaled * point1w).xyz, Vector3(5, 17, -3))

    val translatedAndRotated = translateMatrix * rotateMatrix
    assertVectors((translatedAndRotated * point1w).xyz, Vector3(-4, 4, 0))

    assertVectors((trsMatrix * point1w).xyz, Vector3(-14, 6, -3))
  }

  @Test def testPerspective(): Unit = {
    val perspectiveMatrix = Matrix4x4.forPerspective(scala.math.Pi.toFloat/3f, 1f, 0.001f, 1000.0f)
    assertVectors(perspectiveMatrix * point1w, Vector4(3.5f, 8.7f, 3, 3))
    assertVectors(perspectiveMatrix * point2w, Vector4(34.6f, 86.6f, 30, 30))
  }

  @Test def testOrtho(): Unit = {
    val orthoMatrix = Matrix4x4.forOrtho(0f, 200f, 0f, 100f, 0.001f, 1000.0f)
    assertVectors(orthoMatrix * point1w, Vector4(-0.98f, -0.9f, -0.994f, 1))
    assertVectors(orthoMatrix * point2w, Vector4(-0.8f, 0, -0.940f, 1))
  }

  @Test def testTranspose(): Unit = {
    val transpose = Matrix4x4(
      1, 5, 4, 1,
      2, 6, 5, 5,
      3, 2, 6, 3,
      4, 3, 7, 2)

    assertMatrices(matrix1.transpose, transpose)
  }

  @Test def testInverse(): Unit = {
    val inverse = Matrix4x4(
      -0.653f, 0.04f, 0.413f, -0.2f,
      0.386f, 0.16f, -0.346f, 0.2f,
      -1.146f, -0.44f, 0.786f, 0.2f,
      1.08f, 0.24f, -0.52f, -0.2f)

    assertMatrices(matrix1.inverse, inverse)
  }
}
