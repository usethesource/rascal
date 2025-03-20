@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::agrammar::definition::Symbols

import lang::rascalcore::agrammar::definition::Literals;
import lang::rascalcore::agrammar::definition::Characters;
import lang::rascal::\syntax::Rascal;
//import ParseTree;
import lang::rascalcore::check::ATypeBase;
import String;
import IO;
import Node;


default AType striprec(AType s_ori) = visit(s_ori) { 
	case AType s => strip(s) when s.alabel?
	case conditional(AType s, set[ACondition] _) => strip(s)
};
////default AType striprec(AType s) = visit(s) { case AType t => strip(t) };

AType strip(conditional(AType s, set[ACondition] _)) = strip(s);
default AType strip(AType s) = s.alabel? ? unset(s, "alabel") : s;

//public bool match(AType checked, AType referenced) {
//  while (checked is conditional || checked is label)
//    checked = checked.symbol;
//  while (referenced is conditional || referenced is label)
//    referenced = referenced.symbol;
//    
//  return referenced == checked;
//} 

public AType delabel(AType s) = visit(s) { case AType t => unset(t, "alabel") when t.alabel? };

public AType sym2AType(Sym sym) {
  switch (sym) {
    case lang::rascal::\syntax::Rascal::nonterminal(Nonterminal n) : 
      return AType::aadt("<n>", [], dataSyntax());
    case \start(Nonterminal n) : 
        return \start(AType::aadt("<n>", [], dataSyntax()));
    case literal(StringConstant l): 
      return AType::alit(unescapeLiteral(l));
    case caseInsensitiveLiteral(CaseInsensitiveStringConstant l): 
      return AType::acilit(unescapeLiteral(l));
    case \parametrized(Nonterminal n, {Sym ","}+ syms) : 
        return AType::aadt("<n>",separgs2ATypes(syms), dataSyntax()); 
    case labeled(Sym s, NonterminalLabel n) : 
      return sym2AType(s)[alabel="<n>"];
    case optional(Sym s)  : 
      return AType::opt(sym2AType(s));
    case characterClass(Class cc): 
      return cc2ranges(cc);
    case parameter(Nonterminal n) : 
      return AType::\aparameter("<n>", aadt("Tree", [], contextFreeSyntax()));
    case empty() : 
      return AType::\aempty();
    case alternative(Sym first, {Sym "|"}+ alts) : 
      return AType::alt({sym2AType(first)} + {sym2AType(elem) | elem <- alts});
    case iterStar(Sym s)  : 
      return AType::\iter-star(sym2AType(s));
    case iter(Sym s)  : 
      return AType::\iter(sym2AType(s));
    case iterStarSep(Sym s, Sym sep)  : 
      return AType::\iter-star-seps(sym2AType(s), [sym2AType(sep)]);
    case iterSep(Sym s, Sym sep)  : 
      return AType::\iter-seps(sym2AType(s), [sym2AType(sep)]);
    case sequence(Sym first, Sym+ sequence) : 
      return AType::seq([sym2AType(first)] + [sym2AType(elem) | elem <- sequence]);
    case startOfLine(Sym s) : 
      return conditional(sym2AType(s), {ACondition::\a-begin-of-line()});
    case endOfLine(Sym s) : 
      return conditional(sym2AType(s), {ACondition::\a-end-of-line()});
    case column(Sym s, IntegerLiteral i) : 
      return conditional(sym2AType(s), {ACondition::\a-at-column(toInt("<i>"))}); 
    case follow(Sym s, Sym r) : 
      return conditional(sym2AType(s), {ACondition::\follow(sym2AType(r))});
    case notFollow(Sym s, Sym r) : 
      return conditional(sym2AType(s), {ACondition::\not-follow(sym2AType(r))});
    case precede(Sym s, Sym r) : 
      return conditional(sym2AType(r), {ACondition::\precede(sym2AType(s))});
    case notPrecede(Sym s, Sym r) : 
      return conditional(sym2AType(r), {ACondition::\not-precede(sym2AType(s))});
    case unequal(Sym s, Sym r) : 
      return conditional(sym2AType(s), {ACondition::\delete(sym2AType(r))});
    case except(Sym s, NonterminalLabel n):
      return conditional(sym2AType(s), {ACondition::\a-except("<n>")});
    default: {
      iprintln(sym);
      throw "sym2AType, missed a case <sym>";
    }
  }
}

public AType defsym2AType(Sym sym, SyntaxRole sr) {
    switch(sym){
    case lang::rascal::\syntax::Rascal::nonterminal(Nonterminal n) : 
      return AType::aadt("<n>", [], sr);
   
    case \parametrized(Nonterminal n, {Sym ","}+ syms) : 
        return AType::aadt("<n>",separgs2ATypes(syms), sr); 
    
    case \start(Nonterminal n) : 
        return \start(AType::aadt("<n>", [], sr));
        
    default: {
      iprintln(sym);
      throw "defsym2AType, missed a case <sym>";
    }
  }
}

public list[AType] args2ATypes(Sym* args) {
  return [sym2AType(s) | Sym s <- args];
}

public list[AType] separgs2ATypes({Sym ","}+ args) {
  return [sym2AType(s) | Sym s <- args];
}

// flattening rules for regular expressions
//public AType seq([*AType a, AType::seq(list[AType] b), *AType c]) = AType::seq(a + b + c);

//public AType alt({*AType a, AType::alt(set[AType] b)}) = AType::alt(a + b);

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
