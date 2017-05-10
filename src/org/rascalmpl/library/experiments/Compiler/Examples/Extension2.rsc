module experiments::Compiler::Examples::Extension2

extend experiments::Compiler::Examples::Base2;
import ParseTree;
import IO; 
syntax A = "a";    

void EXTENDED_FUNCTION(int n, Tree a: (A) `a`) { println(":-) NON-default use ***"); }

value main() { bug2(); return -1; } 

value dobug2() { bug2(); return -1; }
//int f(1) = 0; 
////default int f(value x) = 2;
//
//int testF() {
//  value x = 1;
//  return f(x);
//} 
//
//value main() = testF();