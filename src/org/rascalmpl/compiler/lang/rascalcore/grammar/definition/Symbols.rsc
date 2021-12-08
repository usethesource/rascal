@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascalcore::grammar::definition::Symbols

import lang::rascalcore::grammar::definition::Literals;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascal::\syntax::Rascal;
//import ParseTree;
import lang::rascalcore::check::AType;
import String;
//import IO;
import Node;


default AType striprec(AType s_ori) = visit(s_ori) { 
	case AType s => strip(s) when s.label?
	case conditional(AType s, set[ACondition] _) => strip(s)
};
////default AType striprec(AType s) = visit(s) { case AType t => strip(t) };

AType strip(conditional(AType s, set[ACondition] _)) = strip(s);
default AType strip(AType s) = s.label? ? unset(s, "label") : s;

//public bool match(AType checked, AType referenced) {
//  while (checked is conditional || checked is label)
//    checked = checked.symbol;
//  while (referenced is conditional || referenced is label)
//    referenced = referenced.symbol;
//    
//  return referenced == checked;
//} 

public AType delabel(AType s) = visit(s) { case AType t => unset(t, "label") when t.label? };

public AType sym2AType(Sym sym) {
  switch (sym) {
    case lang::rascal::\syntax::Rascal::nonterminal(Nonterminal n) : 
      return AType::aadt("<n>", [], dataSyntax());
    case \start(Nonterminal n) : 
        return \start(AType::aadt("<n>", [], dataSyntax));
    case literal(StringConstant l): 
      return AType::lit(unescapeLiteral(l));
    case caseInsensitiveLiteral(CaseInsensitiveStringConstant l): 
      return AType::cilit(unescapeLiteral(l));
    case \parametrized(Nonterminal n, {Sym ","}+ syms) : 
        return AType::aadt("<n>",separgs2ATypes(syms), dataSyntax()); 
    case labeled(Sym s, NonterminalLabel n) : 
      return sym2AType(s)[label="<n>"];
    case optional(Sym s)  : 
      return AType::opt(sym2AType(s));
    case characterClass(Class cc): 
      return cc2ranges(cc);
    case parameter(Nonterminal n) : 
      return AType::\aparameter("<n>", aadt("Tree", [], contextFreeSyntax()));
    case empty() : 
      return AType::\empty();
    case alternative(Sym first, {Sym "|"}+ alts) : 
      return alt({sym2AType(first)} + {sym2AType(elem) | elem <- alts});
    case iterStar(Sym s)  : 
      return AType::\iter-star(sym2AType(s));
    case iter(Sym s)  : 
      return AType::\iter(sym2AType(s));
    case iterStarSep(Sym s, Sym sep)  : 
      return AType::\iter-star-seps(sym2AType(s), [sym2AType(sep)]);
    case iterSep(Sym s, Sym sep)  : 
      return AType::\iter-seps(sym2AType(s), [sym2AType(sep)]);
    case sequence(Sym first, Sym+ sequence) : 
      return seq([sym2AType(first)] + [sym2AType(elem) | elem <- sequence]);
    case startOfLine(Sym s) : 
      return conditional(sym2AType(s), {\begin-of-line()});
    case endOfLine(Sym s) : 
      return conditional(sym2AType(s), {\end-of-line()});
    case column(Sym s, IntegerLiteral i) : 
      return conditional(sym2AType(s), {\at-column(toInt("<i>"))}); 
    case follow(Sym s, Sym r) : 
      return conditional(sym2AType(s), {\follow(sym2AType(r))});
    case notFollow(Sym s, Sym r) : 
      return conditional(sym2AType(s), {\not-follow(sym2AType(r))});
    case precede(Sym s, Sym r) : 
      return conditional(sym2AType(r), {\precede(sym2AType(s))});
    case notPrecede(Sym s, Sym r) : 
      return conditional(sym2AType(r), {\not-precede(sym2AType(s))});
    case unequal(Sym s, Sym r) : 
      return conditional(sym2AType(s), {\delete(sym2AType(r))});
    case except(Sym s, NonterminalLabel n):
      return conditional(sym2AType(s), {\except("<n>")});
    default: 
      throw "sym2AType, missed a case <sym>";
  }
}

public AType defsym2AType(Sym sym, SyntaxRole sr) {
    switch(sym){
    case lang::rascal::\syntax::Rascal::nonterminal(Nonterminal n) : 
      return AType::aadt("<n>", [], sr);
   
    case \parametrized(Nonterminal n, {Sym ","}+ syms) : 
        return AType::aadt("<n>",separgs2ATypes(syms), sr); 
    
    default: 
      throw "defsym2AType, missed a case <sym>";
  }
}

public list[AType] args2ATypes(Sym* args) {
  return [sym2AType(s) | Sym s <- args];
}

public list[AType] separgs2ATypes({Sym ","}+ args) {
  return [sym2AType(s) | Sym s <- args];
}

// flattening rules for regular expressions
public AType \seq([*AType a, \seq(list[AType] b), *AType c]) = \seq(a + b + c);

public AType \alt({*AType a, \alt(set[AType] b)}) = \alt(a + b);

// flattening for conditionals

public AType \conditional(\conditional(AType s, set[ACondition] cs1), set[ACondition] cs2) 
  = \conditional(s, cs1 + cs2);

public AType \conditional(AType s, set[ACondition] cs) {
  // if there is a nested conditional, lift the nested conditions toplevel and make the nested AType unconditional.
  if (c <- cs, c has atype, c.atype is conditional) {
     return \conditional(s, {c[atype=c.atype.atype], *c.atype.conditions, *(cs - {c})}); //SPLICING
  }
  else fail;
}             
