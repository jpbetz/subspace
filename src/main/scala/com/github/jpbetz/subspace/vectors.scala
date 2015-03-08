package com.github.jpbetz.subspace

trait Vector {
  def size: Int
  def apply(index: Int): Float
  def magnitude: Float
}
