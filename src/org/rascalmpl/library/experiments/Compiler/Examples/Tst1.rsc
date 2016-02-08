module experiments::Compiler::Examples::Tst1


//import lang::rascal::grammar::ParserGenerator;
import Grammar;
//import lang::rascal::grammar::definition::Parameters;
//import lang::rascal::grammar::definition::Literals;
//import IO;
//import String;
import ParseTree;

//private Production pr(Symbol rhs, list[Symbol] lhs) {
//  return prod(rhs,lhs,{});
//}


//public map[int,Production] MP = (123: choice(sort("S"), { }) );

//public Grammar G0 = grammar({sort("S")}, (
//    sort("S"): choice(sort("S"), { P() })
//   // lit("0"): choice(lit("0"), { pr(lit("0"),[\char-class([range(48,48)])]) })
//));
value main() =  (123: choice(sort("S"), { }) );