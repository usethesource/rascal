@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@bootstrapParser
module lang::rascal::grammar::definition::Symbols

import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Characters;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import String;


default Symbol striprec(Symbol s_ori) = visit(s_ori) { 
	case label(str _, Symbol s) => strip(s)
	case conditional(Symbol s, set[Condition] _) => strip(s)
};
//default Symbol striprec(Symbol s) = visit(s) { case Symbol t => strip(t) };
Symbol strip(label(str _, Symbol s)) = strip(s);
Symbol strip(conditional(Symbol s, set[Condition] _)) = strip(s);
default Symbol strip(Symbol s) = s;

public bool match(Symbol checked, Symbol referenced) {
  while (checked is conditional || checked is label)
    checked = checked.symbol;
  while (referenced is conditional || referenced is label)
    referenced = referenced.symbol;
    
  return referenced == checked;
} 

public Symbol delabel(Symbol s) = visit(s) { case label(_,t) => t };

public Symbol sym2symbol(Sym sym) {
  switch (sym) {
    case nonterminal(Nonterminal n) : 
      return Symbol::sort("<n>");
    case \start(Nonterminal n) : 
      return Symbol::\start(sort("<n>"));
    case literal(StringConstant l): 
      return Symbol::lit(unescapeLiteral(l));
    case caseInsensitiveLiteral(CaseInsensitiveStringConstant l): 
      return Symbol::cilit(unescapeLiteral(l));
    case \parametrized(Nonterminal n, {Sym ","}+ syms) : 
      return Symbol::\parameterized-sort("<n>",separgs2symbols(syms)); 
    case labeled(Sym s, NonterminalLabel n) : 
      return Symbol::label("<n>", sym2symbol(s));
    case optional(Sym s)  : 
      return Symbol::opt(sym2symbol(s));
    case characterClass(Class cc): 
      return cc2ranges(cc);
    case parameter(Nonterminal n) : 
      return Symbol::\parameter("<n>", adt("Tree", []));
    case empty() : 
      return Symbol::\empty();
    case alternative(Sym first, {Sym "|"}+ alts) : 
      return alt({sym2symbol(first)} + {sym2symbol(elem) | elem <- alts});
    case iterStar(Sym s)  : 
      return Symbol::\iter-star(sym2symbol(s));
    case iter(Sym s)  : 
      return Symbol::\iter(sym2symbol(s));
    case iterStarSep(Sym s, Sym sep)  : 
      return Symbol::\iter-star-seps(sym2symbol(s), [sym2symbol(sep)]);
    case iterSep(Sym s, Sym sep)  : 
      return Symbol::\iter-seps(sym2symbol(s), [sym2symbol(sep)]);
    case sequence(Sym first, Sym+ sequence) : 
      return seq([sym2symbol(first)] + [sym2symbol(elem) | elem <- sequence]);
    case startOfLine(Sym s) : 
      return conditional(sym2symbol(s), {\begin-of-line()});
    case endOfLine(Sym s) : 
      return conditional(sym2symbol(s), {\end-of-line()});
    case column(Sym s, IntegerLiteral i) : 
      return conditional(sym2symbol(s), {\at-column(toInt("<i>"))}); 
    case follow(Sym s, Sym r) : 
      return conditional(sym2symbol(s), {\follow(sym2symbol(r))});
    case notFollow(Sym s, Sym r) : 
      return conditional(sym2symbol(s), {\not-follow(sym2symbol(r))});
    case precede(Sym s, Sym r) : 
      return conditional(sym2symbol(r), {\precede(sym2symbol(s))});
    case notPrecede(Sym s, Sym r) : 
      return conditional(sym2symbol(r), {\not-precede(sym2symbol(s))});
    case unequal(Sym s, Sym r) : 
      return conditional(sym2symbol(s), {\delete(sym2symbol(r))});
    case except(Sym s, NonterminalLabel n):
      return conditional(sym2symbol(s), {\except("<n>")});
    default: 
      throw "sym2symbol, missed a case <sym>";
  }
}

public list[Symbol] args2symbols(Sym* args) {
  return [sym2symbol(s) | Sym s <- args];
}

public list[Symbol] separgs2symbols({Sym ","}+ args) {
  return [sym2symbol(s) | Sym s <- args];
}

// flattening rules for regular expressions
public Symbol \seq([*Symbol a, \seq(list[Symbol] b), *Symbol c]) = \seq(a + b + c);

public Symbol \alt({*Symbol a, \alt(set[Symbol] b)}) = \alt(a + b);

// flattening for conditionals

public Symbol \conditional(\conditional(Symbol s, set[Condition] cs1), set[Condition] cs2) 
  = \conditional(s, cs1 + cs2);

public Symbol \conditional(Symbol s, set[Condition] cs) {
  // if there is a nested conditional, lift the nested conditions toplevel and make the nested symbol unconditional.
  if (c <- cs, c has symbol, c.symbol is conditional) {
     return \conditional(s, {c[symbol=c.symbol.symbol], *c.symbol.conditions, *(cs - {c})}); //SPLICING
  }
  else fail;
}             
