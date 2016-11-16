module experiments::Compiler::Examples::Tst1

data B = and(B lhs, B rhs) | t();

test bool visitTest() {
  visit(and(t(),t())) { 
    case t(): return true; 
  };
  
  return false;
}

test bool matchTest() = /t() := and(t(),t());

value main() { return matchTest(); }