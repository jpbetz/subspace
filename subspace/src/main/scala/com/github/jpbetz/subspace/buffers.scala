package com.github.jpbetz.subspace

import java.nio.{ByteBuffer, ByteOrder, FloatBuffer}

trait Bufferable {
  def allocateBuffer: FloatBuffer
  def updateBuffer(buffer: FloatBuffer): Unit
}

private[subspace] object Buffers {
  def createFloatBuffer(size: Int): FloatBuffer = {
    val sizeInBytes = size << 2
    ByteBuffer.allocateDirect(sizeInBytes).order(ByteOrder.nativeOrder).asFloatBuffer()
  }
}
