package sinject.failure.constructor



object Prog extends sinject.Module[Prog]

class Prog(x: Int, s: String) {
  def value = s
  def apply() = Other.get
}

object Other{
  def get = new Class("cow").run
}
