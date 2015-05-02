module experiments::Compiler::muRascal::Parse

import experiments::Compiler::muRascal::Syntax;
import ParseTree;
//import Ambiguity;
import IO;

Tree parseMuRascal(loc s) {
  pt = parse( #start[Module], s);
  return pt;
  // Tmp, PK
  //dia = diagnose(pt);
  //if(dia != []){
  //   iprintln(dia);
  //   throw  "*** Ambiguities in muRascal code, see above report";
  //}
  //ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  //ast2 = preprocess(ast);
  //return ast2;						   
}

Tree parseMuRascal(str s) {
  pt = parse( #start[Module], s);
  return pt;
  //Tmp, PK
  //dia = diagnose(pt);
  //if(dia != []){
  //   iprintln(dia);
  //   throw  "*** Ambiguities in muRascal code, see above report";
  //}   
  //ast = implode(#experiments::Compiler::muRascal::AST::Module, pt);
  //ast2 = preprocess(ast);
  //return ast2;							   
}

Tree parseMuRascal(loc l){
  pt = parse( #start[Module], l);
  return pt;
}