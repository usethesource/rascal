@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascalcore::grammar::definition::Layout

import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import IO;


// TODO: The following two functions were defined local to activeLayout
// but this gives an not yet explained validation error  for the
// function ids in the corresponding overloaded function
bool isManual(set[Attr] as) = (\tag("manual"()) in as);
bool isDefault(AType s) = (s == layouts("$default$"));
   
//@doc{computes which layout definitions are visible in a certain given module.
//     if a module contains a layout definition, this overrides any imported layout definition
//     if a module does not contain a layout definition, it will collect the definitions from all imports (not recursively),
//     and also collect the definitions from all extends (recursively).
//     the static checker should check whether multiple visible layout definitions are active, because this function
//     will just produce an arbitrary one if there are multiple definitions
//}
//AType activeLayout(str name, set[str] deps, AGrammarDefinition def) {
//
//  
//  if (/prod(l:layouts(_),_,as) := def.modules[name], !isDefault(l), !isManual(as)) 
//    return l;
//  else if (/prod(label(_,l:layouts(_)),_,as) := def.modules[name], !isDefault(l), !isManual(as)) 
//    return l;  
//  else if (i <- deps, /prod(l:layouts(_),_,as) := def.modules[i], !isDefault(l), !isManual(as)) 
//    return l;
//   else if (i <- deps, /prod(label(_,l:layouts(_)),_,as) := def.modules[i], !isDefault(l), !isManual(as)) 
//    return l;  
//  else 
//    return layouts("$default$"); 
//}  

@doc{intersperses layout symbols in all non-lexical productions}
public AGrammar layouts(AGrammar g, AType l, set[AType] others) {
  
  return top-down-break visit (g) {
    case p: prod(\start(a: aadt(s, list[AType] parameters, contextFreeSyntax())), [x]) => p[asymbols=[l, x, l]]
    case p: prod(aadt(s, list[AType] parameters, contextFreeSyntax()), list[AType] lhs) => p[asymbols=intermix(lhs, l, others)]
  }
} 

list[AType] intermix(list[AType] syms, AType l, set[AType] others) {
  if (syms == []) 
    return syms;
    
  syms = [ isLayoutType(sym) ? sym : regulars(sym, l, others) | sym <- syms ];
  others += {l};
  
  // Note that if a user manually put a layouts symbol, then this code makes sure not to override it and
  // not to surround it with new layout symbols  
  while ([*AType pre, AType sym1, AType sym2, *AType pst] := syms, !(sym1 in others || sym2 in others)) {
      syms = [*pre, sym1, l, sym2, *pst];
  }
  
  return syms;
}

private AType regulars(AType s, AType l, set[AType] others) {
  return visit(s) {
    case \iter(AType n) => \iter-seps(n, [l])
    case \iter-star(AType n) => \iter-star-seps(n, [l]) 
    case \iter-seps(AType n, [AType sep]) => \iter-seps(n,[l,sep,l]) when !(sep in others), !(seq([a,_,b]) := sep && (a in others || b in others))
    case \iter-star-seps(AType n,[AType sep]) => \iter-star-seps(n, [l, sep, l]) when !(sep in others), !(seq([a,_,b]) := sep && (a in others || b in others))
    case \seq(list[AType] elems) => \seq(intermix(elems, l, others))
  }
}
