module experiments::Compiler::Examples::Tst1

import ParseTree;

syntax A = "a";

value main() {
  try {
    parse(#A,"b");
    return false;
  }
  catch value x: {
    return true;
  }
}