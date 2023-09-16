@bootstrapParser
module lang::rascalcore::compile::Examples::Tst4

import ParseTree;
//import Grammar;

extend lang::rascalcore::check::CheckerCommon;

import lang::rascal::\syntax::Rascal;
//import lang::rascalcore::compile::muRascal2Java::JGenie;
//import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;

value attr2IValue1(\tag(value v)) 
    = v;
   // = "tag(<value2IValue(v)>)";