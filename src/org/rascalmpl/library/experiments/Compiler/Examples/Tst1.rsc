module experiments::Compiler::Examples::Tst1

import Type;
import IO;

bool strange(&L <: num arg1, &R <: &L arg2){
  println("typeOf(arg1) = <typeOf(arg1)>, typeOf(arg2) = <typeOf(arg2)>");
  return false;
}

value main() = strange(3, "abc");