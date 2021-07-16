module lang::rascalcore::compile::Examples::Tst1

import Node;
import List;

test bool tstNode2(str name, list[value] children) = arity(makeNode(name, children)) == size(children) &&
                                                               getName(makeNode(name, children)) == name &&
                                                               getChildren(makeNode(name, children)) == children;
//import Type;
//
////value main() = \label("xxx", \int());
//test bool subtype_tuple1(Symbol s, Symbol t) = subtype(\tuple([s,t]), \void);

//test bool sub_type_label(Symbol s, str lbl) = subtype(s, \label(lbl, s));

//
//import ParseTree;
//
////data P = prop(str name) | and(P l, P r) | or(P l, P r) | not(P a) | t() | f() | axiom(P mine = t());
//data D[&T] = d1(&T fld);
//
//value main() //test bool reifyReified3() 
//    = #type[D[int]].symbol;// == \reified(\adt("D", [\int()]));


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