@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascalcore::grammar::definition::Productions
     
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
 
extend analysis::typepal::TypePal;
import lang::rascalcore::check::TypePalConfig;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Attributes;
import lang::rascalcore::grammar::definition::Names;
extend lang::rascalcore::grammar::definition::Grammar;
import List; 
import Set;
import String;      
import IO;  
import util::Math;
import util::Maybe;
import Message;


@doc{
.Synopsis
Convert a concrete production (i.e., the parse tree of a production) into an abstract production
}
   
public AProduction prod2prod(AType nt, Prod p) {
  src = p@\loc;
  switch(p) {
    case labeled(ProdModifier* ms, Name n, Sym* args) : {
      nt1 = nt[label=unescape("<n>")];
      m2a = mods2attrs(ms);
      if ([Sym x] := args.args, x is empty) {
        p1 = isEmpty(m2a) ? prod(nt1, [], src=src) : prod(nt1, [], attributes=m2a, src=src);
        return associativity(nt, \mods2assoc(ms), p1);
      }
      else {
        a2t = args2ATypes(args);
        p1 = isEmpty(m2a) ? prod(nt1, a2t, src=src) : prod(nt1, a2t, attributes=m2a, src=src);
        return associativity(nt, \mods2assoc(ms), p1);
      }
    }
    case unlabeled(ProdModifier* ms, Sym* args) : {
      m2a = mods2attrs(ms);
      if ([Sym x] := args.args, x is empty) {
        p1 = isEmpty(m2a) ? prod(nt, [], src=src) : prod(nt, [], attributes=m2a, src=src);
        return associativity(nt, mods2assoc(ms), p1);
      }
      else {
       a2t = args2ATypes(args);
        p1 = isEmpty(m2a) ? prod(nt, a2t, src=src) : prod(nt, a2t, attributes=m2a, src=src);
        return associativity(nt, mods2assoc(ms), p1);
      }
    } 
    case \all(Prod l, Prod r) :
      return choice(nt,{prod2prod(nt, l), prod2prod(nt, r)});
    case \first(Prod l, Prod r) : 
      return priority(nt,[prod2prod(nt, l), prod2prod(nt, r)]);
    case associativityGroup(\left(), Prod q) :
      return associativity(nt, Associativity::\left(), {prod2prod(nt, q)});
    case associativityGroup(\right(), Prod q) :
      return associativity(nt, Associativity::\right(), {prod2prod(nt, q)});
    case associativityGroup(\nonAssociative(), Prod q) :      
      return associativity(nt, \non-assoc(), {prod2prod(nt, q)});
    case associativityGroup(\associative(), Prod q) :      
      return associativity(nt, Associativity::\left(), {prod2prod(nt, q)});
    case reference(Name n): return \reference(nt, unescape("<n>"));
    default: throw "prod2prod, missed a case <p>"; 
  } 
}

private AProduction associativity(AType nt, nothing(), AProduction p) = p;
private default AProduction associativity(AType nt, just(Associativity a), AProduction p) = associativity(nt, a, {p});


@doc{
.Synopsis
Validate (i.e., type check) a production.
}
list[Message] validateProduction(p: prod(AType def, list[AType] asymbols)){
    if(isStartNonTerminalType(def)){
        def = getStartNonTerminalType(def);
    }
    
    msgs = [];
    visit(p){
        case \delete(t):
            if(lit(_) !:= t  && (t has syntaxRole && t.syntaxRole != keywordSyntax())) { 
                msgs += [ error("Exclude `\\` requires keywords as right argument, found <fmt(t)>", p.src) ]; 
            }
        case \seq(list[AType] symbols):
            forbidConsecutiveLayout(symbols, p.src);
            
        case \iter-seps(AType symbol, list[AType] separators):
            msgs += validateSeparators(separators, p.src); 

        case \iter-star-seps(AType symbol, list[AType] separators):
            msgs += validateSeparators(separators, p.src); 
    }
   
    if(!isEmpty(asymbols)){
        if(def.syntaxRole == keywordSyntax()){
            msgs += 
                for(t <- asymbols){
                    if(lit(_) !:= t){
                       append error("In keyword declaration only literals are allowed, found <fmt(t)>", p.src);
                    }
                }
        
        } else {
            msgs += requireNonLayout(asymbols[0], p.src, "at begin of production") + 
                    requireNonLayout(asymbols[-1], p.src, "at end of production");
            msgs += forbidConsecutiveLayout(asymbols, p.src);
        }
    }
    return msgs;
}

list[Message] validateProduction(AProduction p)
    = [];

list[Message] requireNonLayout(AType u, loc src, str msg)
    = isLayoutType(u) ? [ error("Layout type <fmt(u)> not allowed <msg>", src) ] : [];

list[Message] validateSeparators(list[AType] separators, loc src){
    msgs = [];
    if(all(sep <- separators, isLayoutType(sep))) msgs += [ error("At least one element of separators should be layout", src) ]; 
    msgs += forbidConsecutiveLayout(separators, src);
    return msgs;
}

list[Message] forbidConsecutiveLayout(list[AType] symbols, loc src){
    msgs = [];
    if([*_,t1, t2,*_] := symbols, isLayoutType(t1), isLayoutType(t2)){
       msgs += [error("Consecutive layout types <fmt(t1)> and <fmt(t2)> not allowed", src)];
    }
    return msgs;
}