module experiments::Compiler::Examples::Extension2

extend experiments::Compiler::Examples::Base2;
import ParseTree;
import IO; 
syntax A = "a";    
//syntax B = "b";

//default void EXTENDED_FUNCTION(Tree t) { println("default use: <t> :-("); }

void EXTENDED_FUNCTION(a: (A) `a`) { println(":-) ^^^NON-default use ***"); }
//void EXTENDED_FUNCTION(b: (B) `b`) { println(":-) ***NON-default use ***"); }

value main() { bug2(); return -1; } 

value dobug2() { bug2(); return -1; }

//void bug2() =  
//EXTENDED_FUNCTION(
//appl(
//  prod(
//    sort("A"),
//    [lit("a")],
//    {}),
//  [appl(
//      prod(
//        lit("a"),
//        [\char-class([range(97,97)])],
//        {}),
//      [char(97)])])[
//  @\loc=|test-modules:///ConsoleInput.rsc|(0,1,<1,0>,<1,1>)
//]);


//int f(1) = 0; 
////default int f(value x) = 2;
//
//int testF() {
//  value x = 1;
//  return f(x);
//} 
//
//value main() = testF();