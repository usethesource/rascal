module lang::rascalcore::compile::Examples::Tst1

import lang::rascalcore::compile::Examples::Tst2;


value main() = x().right;
//
//data D = d(tuple[int i, str s] t);
//
//value main()
//{   x = d(<3, "a">);
//    x.t.s = "b";
//    return x.t.s;
//   // return x;
//}

//value main() //test bool testLocationFieldUpdate4() 
//{ loc l = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
//  l.end.line = 14; 
//  return l;//.end.line;
//  //return l.end.line == 14;
//}

//import ValueIO;
//import IO;
//import util::UUID;
//
//loc value_io_test = |test-temp:///value-io-<"<uuidi()>">.test|;
//
//private bool  binaryWriteRead(type[&T] _, value exp) {
//   writeBinaryValueFile(value_io_test,exp);
//   rb = readBinaryValueFile(value_io_test);
//   println(rb);
//   if (&T N := rb && N == exp) return true;
//   return false;
//   }
//   
//private bool  binaryWriteRead(type[&T] typ) {
//   writeBinaryValueFile(value_io_test,typ);
//   rtyp = readBinaryValueFile(value_io_test);
//   println(typ);
//   println(rtyp);
//   if (type[&T] N := rtyp && N == typ) return true;
//   return false;
//}
//   
//value main() {//test bool reifyRel2()   
//    return binaryWriteRead(#rel[int i, str s]);
//}



//import Node;
//value main() {node n = makeNode("f"); return getName(n) == "f" && arity(n) == 0 && getChildren(n) == []; }

//import Type;
//
//data D = a(bool b) | a(str s1, str s2) | a(int n, str color = "blue") |  a(str s, int sz = 10);
//
//value main() //test bool tstMake1() 
//    = make(#D, "a", [true]);// ==  a(true);


//import ParseTree;
//
//syntax A = "a";
//
////test bool concreteExpressionsHaveSourceLocations1() 
////  = (A) `a`.src?;
//  
//value main() //est bool concreteExpressionsHaveSourceLocations2() 
//  = (A) `a`.\loc;//.length == 1;


//import ParseTree;
//import lang::rascal::\syntax::Rascal;
//
////test bool isThisATuple() = (Expression)`\< <{Expression ","}+ _> \>` := parse(#Expression, "\<1\>");
//
//value main() = [Expression] "\<1\>";