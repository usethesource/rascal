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
import lang::rascal::grammar::definition::Regular;
import lang::rascal::format::Escape;

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
  return {prod(layouts("$QUOTES"),[conditional(\iter-star(\char-class([range(9,10),range(13,13),range(32,32)])),{\not-follow(\char-class([range(9,10),range(13,13),range(32,32)]))})],{}),
          prod(layouts("$BACKTICKS"),[],{})};
}

private Symbol rl = layouts("$QUOTES");
private Symbol bt = layouts("$BACKTICKS");

public set[Production] fromRascal(Grammar object) {
  return  { *untypedQuotes(nont), *typedQuotes(nont) | Symbol nont <- object.rules, isNonterminal(nont), !isRegular(nont)} 
        + { *typedQuotes(nont) | Symbol nont <- object.rules, isRegular(nont) } 
        + { *typedQuotes(\iter-star(s)) | \iter(s) <- object.rules } 
        + { *typedQuotes(\iter(s)) | \iter-star(s) <- object.rules } 
        + { *typedQuotes(\iter-star-seps(s,l)) | \iter-seps(s, l) <- object.rules }
        + { *typedQuotes(\iter-seps(s,l)) | \iter-star-seps(s, l) <- object.rules } 
        + { literal(L) | Symbol nont <- object.rules, lit(L) <- symbolLiterals(nont) }
        ; 
}

public set[Production] untypedQuotes(Symbol nont) =
  {prod(label("ConcreteQuoted",meta(sort("Expression"))),[lit("`"),bt,nont,bt,lit("`")],{}),
   prod(label("ConcreteQuoted",meta(sort("Pattern"))),[lit("`"),bt,nont,bt,lit("`")],{})
  };
  
public set[Production] typedQuotes(Symbol nont) =
    {prod(label("ConcreteTypedQuoted",meta(sort("Expression"))),[lit("("),rl,*symLits,rl,lit(")"),rl,lit("`"),bt,nont,bt,lit("`")],{}),  
     prod(label("ConcreteTypedQuoted",meta(sort("Pattern"))),[lit("("),rl,*symLits,rl,lit(")"),rl,lit("`"),bt,nont,bt,lit("`")],{})      
    |  symLits := symbolLiterals(nont)};  

// TODO: this does not generate productions for bound parameterized symbols
public set[Production] toRascal(Grammar object) {
  return  // { prod(label("MetaVariable",nont),[lit("\<"),rl,*symbolLiterals(nont),rl,meta(lex("Name")),rl,lit("\>")],{}) // for the future
          { prod(label("MetaVariable",nont),[lit("\<"),rl,meta(sort("Pattern")),rl,lit("\>")],{})  
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
    case \start(s) : return [lit("start"),rl,lit("["),rl,*symbolLiterals(s),rl,lit("]")]; //SPLICE
    case \label(n,s) : return symbolLiterals(s); 
    case \lit(n) : return [lit(quote(n))];
    case \cilit(n) : return [lit(ciquote(n))]; 
    case \layouts(n) : return [];
    case \iter(s) : return [*symbolLiterals(s),rl,lit("+")]; //SPLICE
    case \iter-star(s) : return [*symbolLiterals(s),rl,lit("*")];  
    case \iter-seps(s, [layouts(_)]) : return [*symbolLiterals(s),rl,lit("+")]; //SPLICE
    case \iter-seps(s, [layouts(_),sep,layouts(_)]) : return [lit("{"),rl,*symbolLiterals(s),rl,*symbolLiterals(sep),rl,lit("}"),rl,lit("+")];  
    case \iter-seps(s, seps) : return [lit("{"),rl,*symbolLiterals(s),rl,*tail([rl,*symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("+")]; 
    case \iter-star-seps(s, [layouts(_)]) : return [*symbolLiterals(s),rl,lit("*")]; 
    case \iter-star-seps(s, [layouts(_),sep,layouts(_)]) : return [lit("{"),rl,*symbolLiterals(s),rl,*symbolLiterals(sep),rl,lit("}"),rl,lit("*")]; 
    case \iter-star-seps(s, seps) : return [lit("{"),rl,*symbolLiterals(s),rl,*tail([rl,*symbolLiterals(t) | t <- seps]),rl,lit("}"),rl,lit("*")]; 
    case \alt(alts) : return [lit("("),rl,*tail(tail(tail([rl,lit("|"),rl,*symbolLiterals(t) | t <- alts ]))),rl,lit(")")]; 
    case \seq(elems) : return [lit("("),rl,*tail([rl,*symbolLiterals(t) | t <- elems]),rl,lit(")")];  
    case \parameterized-sort(n, params) : return [lit(n),rl,lit("["),rl,*tail(tail(tail([rl,lit(","),rl,*symbolLiterals(p) | p <- params]))),rl,lit("]")]; //SPLICE
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

