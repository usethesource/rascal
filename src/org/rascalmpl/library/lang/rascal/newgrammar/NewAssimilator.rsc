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
import lang::rascal::newsyntax::Rascal;
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Regular;
import lang::rascal::grammar::definition::Symbols;
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

public str createHole(ConcretePart hole, int idx) = createHole(hole.hole, idx);
public str createHole(ConcreteHole hole, int idx) = "\u0000<getTargetSymbol(sym2symbol(hole.symbol))>:<idx>\u0000";

public set[Production] holes(Grammar object) {
  // syntax N = [\a00] "N" ":" [0-9]+ [\a00];
  return  { regular(iter(\char-class([range(48,57)]))), 
            prod(label("$MetaHole",target),[\char-class([range(0,0)]),lit("<target>"),lit(":"),iter(\char-class([range(48,57)])),\char-class([range(0,0)])],{})  
          | Symbol nont <- object.rules, isNonterminal(nont), target := getTargetSymbol(nont)};
}

@doc{This is needed such that list variables can be repeatedly used as elements of the same list}
private Symbol getTargetSymbol(Symbol sym) {
  switch(sym) {
    case \iter(s) : return s;
    case \iter-star(s) : return s;  
    case \iter-seps(s, seps) : return s; 
    case \iter-star-seps(s, seps) : return s; 
    default: return sym;
  } 
}
