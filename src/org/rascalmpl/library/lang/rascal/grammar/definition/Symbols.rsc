@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::grammar::definition::Symbols

import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Characters;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import String;
import IO;

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

@deprecated{use striprec}
public Symbol delabel(Symbol s) = visit(s) { case label(_,t) => t };

public Symbol sym2symbol(Sym sym) {
  switch (sym) {
    case \token(Nonterminal n) :
      return \token("<n>");
    case lang::rascal::\syntax::Rascal::nonterminal(Nonterminal n) : 
      return sort("<n>");
    case \bracket(Sym s) :
      return sym2symbol(s);
    case dependAlign(Sym s):
      return \align(sym2symbol(s));
    case dependOffside(Sym s):
      return \offside(sym2symbol(s));
    case dependVoidFormals(Sym s, Parameters f) :
      return addParameters(sym2symbol(s), f);
    case dependFormals(Sym s, Type t, Parameters f) :
      return addParameters(sym2symbol(s), type2symbol(t), f);
    case dependNonterminal(_, Nonterminal n, {Expression ","}* a, kwArgs) :
      return addActuals(sort("<n>"), a); // TODO don't forget about the kwArgs
    case dependParametrized(_, Nonterminal n, {Sym ","}+ syms, {Expression ","}* a, kwArgs) :
      return addActuals(\parameterized-sort("<n>",separgs2symbols(syms)), a);  // TODO don't forget about the kwArgs
    case dependScope(Sym+ syms) :
      return \scope(args2symbols(syms));
    case dependCode(Sym s, Statement block) :
      return \do(sym2symbol(s), block);
    case dependConditionAfter(Sym s, Expression condition) :
      return \when(sym2symbol(s), condition);
    case dependConditionBefore(Expression condition, Sym s) :
      return \if(condition, sym2symbol(s));
    case dependAlternative(Expression condition, Sym i, Sym e) :
      return \ifElse(condition, sym2symbol(i), sym2symbol(e));
    case dependLoop(Expression condition, Sym s) :
      return \while(condition, sym2symbol(s));
    case \start(Nonterminal n) : 
      return \start(sort("<n>"));
    case literal(StringConstant l): 
      return lit(unescape(l));
    case caseInsensitiveLiteral(CaseInsensitiveStringConstant l): 
      return cilit(unescape(l));
    case \parametrized(Nonterminal n, {Sym ","}+ syms) : 
      return \parameterized-sort("<n>",separgs2symbols(syms)); 
    case labeled(Sym s, NonterminalLabel n) : 
      return label("<n>", sym2symbol(s));
    case optional(Sym s)  : 
      return opt(sym2symbol(s));
    case characterClass(Class cc): 
      return cc2ranges(cc);
    case parameter(Nonterminal n) : 
      return \parameter("<n>", adt("Tree", []));
    case empty() : 
      return \empty();
    case alternative(Sym first, {Sym "|"}+ alts) : 
      return alt({sym2symbol(first)} + {sym2symbol(elem) | elem <- alts});
    case iterStar(Sym s)  : 
      return \iter-star(sym2symbol(s));
    case iter(Sym s)  : 
      return \iter(sym2symbol(s));
    case iterStarSep(Sym s, Sym sep)  : 
      return \iter-star-seps(sym2symbol(s), [sym2symbol(sep)]);
    case iterSep(Sym s, Sym sep)  : 
      return \iter-seps(sym2symbol(s), [sym2symbol(sep)]);
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
    case farFollow(Sym s, Sym r) : 
      return conditional(sym2symbol(s), {\far-follow(sym2symbol(r))});
    case farNotFollow(Sym s, Sym r) : 
      return conditional(sym2symbol(s), {\far-not-follow(sym2symbol(r))});
    case farPrecede(Sym s, Sym r) : 
      return conditional(sym2symbol(r), {\far-precede(sym2symbol(s))});
    case farNotPrecede(Sym s, Sym r) : 
      return conditional(sym2symbol(r), {\far-not-precede(sym2symbol(s))});  
    case unequal(Sym s, Sym r) : 
      return conditional(sym2symbol(s), {\delete(sym2symbol(r))});
    case except(Sym s, NonterminalLabel n):
      return conditional(sym2symbol(s), {\except("<n>")});
    default: 
      throw "missed a case <sym>";
  }
}

// TODO: support all types by reusing ConvertTypes
Symbol type2symbol(Type t) {
  switch(t) {
    case basic(BasicType::\int()) : return Symbol::\int();
    case basic(BasicType::\bool()) : return Symbol::\bool();
    case basic(BasicType::\real()) : return Symbol::\real();
    case symbol(Sym s) : return sym2symbol(s);
    default: throw "TODO unsupportedType <t>";
  }
}

Symbol addParameters(Symbol s, Symbol t, Parameters f) = addParameters(s, f)[returnType = s];

Symbol addParameters(Symbol s, Parameters p)  
  = s[formals=convertFormals(formals)][keywordTypes=convertKeywordFormals(kwFormals)][keywordDefaults=getKeywordDefaults(kwFormals)]
    when \default(Formals formals, KeywordFormals kwFormals) := p;

list[Symbol] convertFormals(Formals f) 
  = [ label("<name>", type2symbol(\type)) | typedVariable(Type \type, Name name) <- formals] 
  when \default({Pattern ","}* formals) := f;
  
map[str,Symbol] convertKeywordFormals(Formals f) // TODO
  = () when \none() := f; 
    
map[str,Symbol] convertKeywordFormals(KeywordFormals kwFormals) // TODO
  = (); 
  
map[str, Tree] getKeywordDefaults(KeywordFormals kwFormals) // TODO
  = ();
  
default Symbol addParameters(Symbol s, Parameters p) {
  throw "TODO varargs not yet implemented";
}

Symbol addActuals(Symbol s,  {Expression ","}* args, KeywordFormals kwf) 
  = addActuals(s, args) when \none() := kwf;

default Symbol addActuals(Symbol s,  {Expression ","}* args, KeywordFormals _) {
  throw "keyword arguments not yet default";
}
  
Symbol addActuals(Symbol s,  {Expression ","}* args)
  = s[actuals=[ arg | Expression arg <- args]];

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