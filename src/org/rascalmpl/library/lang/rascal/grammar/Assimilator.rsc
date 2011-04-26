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

public data Symbol = meta(Symbol wrapped);

bool isNonterminal(Symbol x) { 
    return \lit(_) !:= x 
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
  return {prod(str2syms(q),lit(q),\attrs([\literal()])) | q <- ["`","(",")","\<","\>"] };
}

public set[Production] layoutProductions(Grammar object) {
  return {prod([\iter-star(\char-class([range(9,10),range(13,13),range(32,32)]))],layouts("$QUOTES"),attrs([term("lex"())]))};
}

private Symbol rl = layouts("$QUOTES");

// TODO: this does not generate productions for bound parameterized symbols
public set[Production] fromRascal(Grammar object) {
  rejects = rejectedSymbols(object);
  
  return  { prod([lit("`"),rl,nont,rl,lit("`")],meta(sort("Expression")),attrs([term("cons"("ConcreteQuoted"))])),
        prod([lit("("),rl,symLits,rl,lit(")"),rl,lit("`"),rl,nont,rl,lit("`")],meta(sort("Expression")),attrs([term("cons"("ConcreteTypedQuoted"))])),
        prod([lit("`"),rl,nont,rl,lit("`")],meta(sort("Pattern")),attrs([term("cons"("ConcreteQuoted"))])),
        prod([lit("("),rl,symLits,rl,lit(")"),rl,lit("`"),rl,nont,rl,lit("`")],meta(sort("Pattern")),attrs([term("cons"("ConcreteTypedQuoted"))])),
        { prod(str2syms(L),l,attrs([\literal()])) | l:lit(L) <- symLits } // to define the literals (TODO factor this out, we implemented this to many times)
      | Symbol nont <- object.rules, isNonterminal(nont), nont notin rejects, symLits := symbolLiterals(nont) };
}

// TODO: this does not generate productions for bound parameterized symbols
public set[Production] toRascal(Grammar object) {
  return  { prod([lit("\<"),rl,meta(sort("Pattern")),rl,lit("\>")],nont,attrs([term("cons"("MetaVariable"))])) 
          | Symbol nont <- object.rules, isNonterminal(nont)};
}

private list[Symbol] symbolLiterals(Symbol sym) {
  switch (sym) {
    case \sort(n) : return [lit(n)];
    case \opt(s) : return [symbolLiterals,rl,lit("?")];
    case \START() : return [lit("START")];
    case \start(s) : return [lit("start"),rl,lit("["),rl,symbolLiterals(s),rl,lit("]")];
    case \label(n,s) : return symbolLiterals(s); 
    case \lit(n) : return [lit(quote(n))];
    case \cilit(n) : return [lit(ciquote(n))]; 
    case \cf(s) : return symbolLiterals(s);
    case \lex(s) : return symbolLiterals(s);
    case \empty() : return "";
    case \layouts(n) : return [];
    case \iter(s) : return [symbolLiterals(s),rl,lit("+")];
    case \iter-star(s) : return [symbolLiterals(s),rl,lit("*")];
    case \iter-seps(s, seps) : return [lit("{"),rl,symbolLiterals(s),rl,tail([rl,symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("+")];
    case \iter-seps(s, seps) : return [lit("{"),rl,symbolLiterals(s),rl,tail([rl,symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("*")];
    case \empty() : return [lit("("), rl, lit(")")];
    case \alt(alts) : return [lit("("),rl,tail(tail(tail([rl,lit("|"),rl,symbolLiterals(t) | t <- seps ]))),rl,lit(")")];
    case \seq(elems) : return [lit("("),rl,tail([rl,symbolLiterals(t) | t <- elems]),rl,lit(")")];  
    case \parameterized-sort(n, params) : return [lit(n),rl,lit("["),rl,tail(tail(tail([rl,lit(","),rl,symbolLiterals(p) | p <- params]))),rl,lit("]")]; 
    case \parameter(n) : return [lit("&"),rl,lit(n)];
    case \char-class(list[CharRange] ranges) : return [lit("["),rl,tail([rl,rangeLiterals(r) | r <- ranges]),rl,lit("]")];
    case \at-column(c) : return [lit("@"),rl,lit("<c>")];
    case \start-of-line() : return [lit("^")];
    case \end-of-line() : return [lit("$")];
    default: throw "unsupported symbol <sym>";
  }
}

private list[Symbol] rangeLiterals(CharRange r) {
  switch (r) {
    case range(c,c) : return [lit(makeCharClassChar(c))];
    case range(c,d) : return [lit(makeCharClassChar(c)),lit("-"),lit(makeCharClassChar(d))];
  }
}

