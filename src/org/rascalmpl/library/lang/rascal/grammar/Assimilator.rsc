@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc{
  This module provides functionality for merging the Rascal grammar and arbitrary user-defined grammars
}
@bootstrapParser
module lang::rascal::grammar::Assimilator

import ValueIO;
import List;
import IO;
import ParseTree;
import Grammar;
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Literals;

public data Symbol = meta(Symbol wrapped);

bool isNonterminal(Symbol x) { 
    return \lit(_) !:= x 
       && \empty() !:= x
       && \cilit(_) !:= x 
       && \char-class(_) !:= x 
       && \layouts(_) !:= x
       && \keywords(_) !:= x
       && \start(_) !:= x
       && \parameterized-sort(_,[\parameter(_),_*]) !:= x;
}
  
@doc{
  It is assumed both the Rascal grammar and the object grammar have been normalized
}

public set[Production] quotes() {
  return {prod(lit(q),str2syms(q),{}) | q <- ["`","(",")","\<","\>"] };
}

public set[Production] layoutProductions(Grammar object) {
  return {prod(layouts("$QUOTES"),[\iter-star(\char-class([range(9,10),range(13,13),range(32,32)]))],{})};
}

private Symbol rl = layouts("$QUOTES");

// TODO: this does not generate productions for bound parameterized symbols
public set[Production] fromRascal(Grammar object) {
  return  {prod(meta(label("ConcreteQuoted",sort("Expression"))),[lit("`"),rl,nont,rl,lit("`")],{}),
        prod(meta(label("ConcreteTypedQuoted",sort("Expression"))),[lit("("),rl,symLits,rl,lit(")"),rl,lit("`"),rl,nont,rl,lit("`")],{}),
        prod(meta(label("ConcreteQuoted",sort("Pattern"))),[lit("`"),rl,nont,rl,lit("`")],{}),
        prod(meta(label("ConcreteTypedQuoted",sort("Pattern"))),[lit("("),rl,symLits,rl,lit(")"),rl,lit("`"),rl,nont,rl,lit("`")],{}),
        { prod(l,str2syms(L),{}) | l:lit(L) <- symLits } // to define the literals (TODO factor this out, we implemented this to many times)
      | Symbol nont <- object.rules, isNonterminal(nont), symLits := symbolLiterals(nont) };
}

// TODO: this does not generate productions for bound parameterized symbols
public set[Production] toRascal(Grammar object) {
  return  { prod(label("MetaVariable",nont),[lit("\<"),rl,meta(sort("Pattern")),rl,lit("\>")],{}) 
          | Symbol nont <- object.rules, isNonterminal(nont)};
}

private list[Symbol] symbolLiterals(Symbol sym) {
  switch (sym) {
    case \sort(n) : return [lit(n)];
    case \lex(n) : return [lit(n)];
    case \keywords(n): return [lit(n)];
    case \empty() : return [lit("("), rl, lit(")")];
    case \opt(s) : return [symbolLiterals,rl,lit("?")];
    case \START() : return [lit("START")];
    case \start(s) : return [lit("start"),rl,lit("["),rl,symbolLiterals(s),rl,lit("]")];
    case \label(n,s) : return symbolLiterals(s); 
    case \lit(n) : return [lit(quote(n))];
    case \cilit(n) : return [lit(ciquote(n))]; 
    case \layouts(n) : return [];
    case \iter(s) : return [symbolLiterals(s),rl,lit("+")];
    case \iter-star(s) : return [symbolLiterals(s),rl,lit("*")];
    case \iter-seps(s, seps) : return [lit("{"),rl,symbolLiterals(s),rl,tail([rl,symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("+")];
    case \iter-seps(s, seps) : return [lit("{"),rl,symbolLiterals(s),rl,tail([rl,symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("*")];
    case \alt(alts) : return [lit("("),rl,tail(tail(tail([rl,lit("|"),rl,symbolLiterals(t) | t <- seps ]))),rl,lit(")")];
    case \seq(elems) : return [lit("("),rl,tail([rl,symbolLiterals(t) | t <- elems]),rl,lit(")")];  
    case \parameterized-sort(n, params) : return [lit(n),rl,lit("["),rl,tail(tail(tail([rl,lit(","),rl,symbolLiterals(p) | p <- params]))),rl,lit("]")]; 
    case \parameter(n) : return [lit("&"),rl,lit(n)];
    case \char-class(list[CharRange] ranges) : return [lit("["),rl,tail([rl,rangeLiterals(r) | r <- ranges]),rl,lit("]")];
    default: throw "unsupported symbol <sym>";
  }
}

private list[Symbol] rangeLiterals(CharRange r) {
  switch (r) {
    case range(c,c) : return [lit(makeCharClassChar(c))];
    case range(c,d) : return [lit(makeCharClassChar(c)),lit("-"),lit(makeCharClassChar(d))];
  }
}

