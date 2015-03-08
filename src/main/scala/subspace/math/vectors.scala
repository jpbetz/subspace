package subspace.math

trait Vector {
  def size: Int
  def apply(index: Int): Float
  def magnitude: Float
}
