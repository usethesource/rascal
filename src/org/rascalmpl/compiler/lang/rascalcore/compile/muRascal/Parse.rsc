module lang::rascalcore::compile::muRascal::Parse

import lang::rascalcore::compile::muRascal::Syntax;
import ParseTree;
//import Ambiguity;
import IO;

Tree parseMuRascal(loc s) {
  pt = parse( #start[MuPreModule], s);
  return pt;
  // Tmp, PK
  //dia = diagnose(pt);
  //if(dia != []){
  //   iprintln(dia);
  //   throw  "*** Ambiguities in muRascal code, see above report";
  //}
  //ast = implode(#lang::rascalcore::compile::muRascal::AST::Module, pt);
  //ast2 = preprocess(ast);
  //return ast2;						   
}

Tree parseMuRascal(str s) {
  pt = parse( #start[MuPreModule], s);
  return pt;
  //Tmp, PK
  //dia = diagnose(pt);
  //if(dia != []){
  //   iprintln(dia);
  //   throw  "*** Ambiguities in muRascal code, see above report";
  //}   
  //ast = implode(#lang::rascalcore::compile::muRascal::AST::Module, pt);
  //ast2 = preprocess(ast);
  //return ast2;							   
}

Tree parseMuRascal(loc l){
  pt = parse( #start[MuPreModule], l);
  return pt;
}