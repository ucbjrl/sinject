package sinject.failure.usingdynamic

object Prog extends sinject.Module[Prog]

class Prog(x: Int, s: String) {
  def value = s
  def apply() = new Class(s).run
}
