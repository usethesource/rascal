module experiments::Compiler::Tests::VIO
import Prelude;

void main(){
  l = |rascal:///experiments/Compiler/Tests/XXX|;
  v = #int;
  writeTextValueFile(l, v);
  w = readTextValueFile(l);
  println("<typeOf(v)> v = <v>, <typeOf(w)>  w = <w>");
}