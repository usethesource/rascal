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
module lang::rascal::newgrammar::NewAssimilator

import ValueIO;
import List;
import IO;
import ParseTree;
import Grammar;
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Regular;
import lang::rascal::format::Escape;

bool isNonterminal(Symbol x) { 
    return \lit(_) !:= x 
       && \empty() !:= x
       && \cilit(_) !:= x 
       && \char-class(_) !:= x 
       && \layouts(_) !:= x
       && \keywords(_) !:= x
       && \start(_) !:= x
       && \parameterized-sort(_,[\parameter(_),_*]) !:= x
       && \parameterized-lex(_,[\parameter(_),_*]) !:= x;
}
  
public Grammar addHoles(Grammar object) = compose(object, grammar({}, holes(object)));

public set[Production] holes(Grammar object) {
  // syntax N = [\a00] "N" ":" [0-9]+ [\a00];
  return  { prod(label("MetaHole",nont),[\char-class([range(0,0)]),lit("<nont>"),lit(":"),iter(char-class([range(48,57)])),\char-class([range(0,0)])],{})  
          | Symbol nont <- object.rules, isNonterminal(nont)};
}

private list[Symbol] symbolLiterals(Symbol sym) {
  switch (sym) {
    case \sort(n) : return [lit(n)];
    case \lex(n) : return [lit(n)];
    case \conditional(s,_) : return symbolLiterals(s);
    case \keywords(n): return [lit(n)];
    case \empty() : return [lit("("), rl, lit(")")];
    case \opt(s) : return [*symbolLiterals(s),rl,lit("?")]; 
    case \start(s) : return [lit("start"),rl,lit("["),rl,*symbolLiterals(s),rl,lit("]")]; 
    case \label(n,s) : return symbolLiterals(s); 
    case \lit(n) : return [lit(quote(n))];
    case \cilit(n) : return [lit(ciquote(n))]; 
    case \layouts(n) : return [];
    case \iter(s) : return [*symbolLiterals(s),rl,lit("+")];
    case \iter-star(s) : return [*symbolLiterals(s),rl,lit("*")];  
    case \iter-seps(s, [layouts(_)]) : return [*symbolLiterals(s),rl,lit("+")]; 
    case \iter-seps(s, [layouts(_),sep,layouts(_)]) : return [lit("{"),rl,*symbolLiterals(s),rl,*symbolLiterals(sep),rl,lit("}"),rl,lit("+")];  
    case \iter-seps(s, seps) : return [lit("{"),rl,*symbolLiterals(s),rl,*tail([rl,*symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("+")]; 
    case \iter-star-seps(s, [layouts(_)]) : return [*symbolLiterals(s),rl,lit("*")]; 
    case \iter-star-seps(s, [layouts(_),sep,layouts(_)]) : return [lit("{"),rl,*symbolLiterals(s),rl,*symbolLiterals(sep),rl,lit("}"),rl,lit("*")]; 
    case \iter-star-seps(s, seps) : return [lit("{"),rl,*symbolLiterals(s),rl,*tail([rl,*symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("*")]; 
    case \alt(alts) : return [lit("("),rl,*tail(tail(tail([rl,lit("|"),rl,*symbolLiterals(t) | t <- alts ]))),rl,lit(")")]; 
    case \seq(elems) : return [lit("("),rl,*tail([rl,*symbolLiterals(t) | t <- elems]),rl,lit(")")];  
    case \parameterized-sort(n, params) : return [lit(n),rl,lit("["),rl,*tail(tail(tail([rl,lit(","),rl,*symbolLiterals(p) | p <- params]))),rl,lit("]")]; //SPLICE
    case \parameterized-lex(n, params) : return [lit(n),rl,lit("["),rl,*tail(tail(tail([rl,lit(","),rl,*symbolLiterals(p) | p <- params]))),rl,lit("]")]; //SPLICE
    case \parameter(n) : return [lit("&"),rl,lit(n)];
    case \char-class(list[CharRange] ranges) : return [lit("["),rl,*tail([rl,*rangeLiterals(r) | r <- ranges]),rl,lit("]")];    
    default: throw "unsupported symbol <sym>";
  }
}

private list[Symbol] rangeLiterals(CharRange r) {
  switch (r) {
    case range(c,c) : return [lit(makeCharClassChar(c))];
    case range(c,d) : return [lit(makeCharClassChar(c)),lit("-"),lit(makeCharClassChar(d))];
  }
}

