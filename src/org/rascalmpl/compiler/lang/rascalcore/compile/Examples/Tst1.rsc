module lang::rascalcore::compile::Examples::Tst1
      
import Exception;
import IO;

@expected{ArithmeticException}
test bool divByZero(int x){ if(x / 0 == 0) {println("<x>: no exception"); return false; } else return false; }