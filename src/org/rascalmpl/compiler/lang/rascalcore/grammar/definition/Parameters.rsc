@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc{
  This module implements the support for parameterized syntax definitions
}
module lang::rascalcore::grammar::definition::Parameters

//import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::check::ATypeBase;
import lang::rascalcore::check::ATypeUtils;
import List;
import Set;
import Node;
import IO;

public AGrammar expandParameterizedSymbols(AGrammar g) {
  return grammar(g.starts, expand({g.rules[nt] | nt <- g.rules}));
} 

bool isParameterizedDef(aadt(str adtName,list[AType] parameters, SyntaxRole sr)) =  sr in {contextFreeSyntax(), lexicalSyntax()} && !isEmpty(parameters) && all(p <- parameters, aparameter(_,_) := p);
default bool isParameterizedDef(AType t) = false;

bool isParameterizedUse(aadt(str adtName,list[AType] parameters, SyntaxRole sr)) =  sr in {contextFreeSyntax(), lexicalSyntax()} && !isEmpty(parameters) && all(p <- parameters, aparameter(_,_) !:= p);
default bool isParameterizedUse(AType t) = false;


set[AProduction] expand(set[AProduction] prods) {
    //println("expand:");
    //iprintln(prods);
    prods = {unsetRec(p) | p <- prods};
  // First we collect all the parametrized definitions
  defs = { p | p <- prods, isParameterizedDef(p.def)};
  defAlts = {*alts | choice(a, alts) <- defs};
  result = { choice(a, diff) | choice(a, alts) <- prods, diff := alts-defAlts, !isEmpty(diff), !isParameterizedDef(a) };
  //println("prods: <size(prods)>, defs: <size(defs)>, result: <size(result)>");
  //println("defs: <defs>");
  //println("result: <result>");
  
  // Then we collect all the uses of parameterized sorts in the other productions
  uses = { s | /AType s <- result, isParameterizedUse(s) };
  
  // Now we copy each definition for each use and rename the parameters
  // Note that we assume normalization will remove the duplicates we introduce by instantiating the a definition twice
  // with the same actual parameters.
  
  instantiated = {};
  while (uses != {}) {
    instances = {};
    for (u <- uses, def <- defs, def.def.adtName == u.adtName) {
       name = u.adtName;
       actuals = u.parameters;
       formals = def.def.parameters;
       instantiated += {u};
       substs = (formals[i]:actuals[i] | int i <- index(actuals) & index(formals)); // do proper check
       instances = {*instances, visit (def) {
         case AType par:\aparameter(_,_) => substs[par]?par
       }}; 
    }
  
    // now, we may have created more uses of parameterized symbols, by instantiating nested parameterized symbols
    uses = { s | /AType s <- instances, isParameterizedUse(s), s notin instantiated};
    result += instances;
  }
  //println("After expand:");
  //iprintln(result);
  return result;
}
