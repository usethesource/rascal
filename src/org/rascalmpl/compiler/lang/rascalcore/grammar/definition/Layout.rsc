@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascalcore::grammar::definition::Layout

import lang::rascalcore::check::ATypeBase;
import lang::rascalcore::check::ATypeUtils;

@doc{intersperses layout symbols in all non-lexical productions}
public AGrammar layouts(AGrammar g, AType l, set[AType] others) {
  
  res = top-down-break visit (g) {
    case p: prod(\start(aadt(_, list[AType] _, contextFreeSyntax())), [x]) => p[atypes=[l, x, l]]
       
    case p: prod(aadt(_, list[AType] _, contextFreeSyntax()), list[AType] lhs) => p[atypes=intermix(lhs, l, others)]
  }
  //iprintln(res);
  return res;
} 

list[AType] intermix(list[AType] syms, AType l, set[AType] others) {
  if (syms == []) 
    return syms;
    
  syms = [ isLayoutAType(sym) ? sym : regulars(sym, l, others) | sym <- syms ];
  others += {l};
  
  // Note that if a user manually put a layouts symbol, then this code makes sure not to override it and
  // not to surround it with new layout symbols  
  while ([*AType pre, AType sym1, AType sym2, *AType pst] := syms, !(sym1 in others || sym2 in others)) {
      syms = [*pre, sym1, l, sym2, *pst];
  }
  
  return syms;
}
//Moved to ATypeUtils;
//private AType inheritLabel(AType x, AType y) = (x.alabel?) ? y[alabel=x.alabel] : y;

private bool isValidSep(AType sep,  set[AType] others) = !(seq([a,_,b]) := sep && (a in others || b in others));
 
public AType regulars(AType s, AType l, set[AType] others) {
  return visit(s) {
    case x:\iter(AType n) => inheritLabel(x, \iter-seps(n, [l])) when !x.isLexical
    case x:\iter-star(AType n) => inheritLabel(x, \iter-star-seps(n, [l])) when !x.isLexical
    case x:\iter-seps(AType n, [AType sep]) => inheritLabel(x, \iter-seps(n,[l,sep,l])) when !x.isLexical,  sep != l, sep notin others, isValidSep(sep, others)
    case x:\iter-star-seps(AType n, [AType sep]) => inheritLabel(x, \iter-star-seps(n, [l, sep, l])) when !x.isLexical, sep != l, sep notin others, isValidSep(sep, others)
    case x:\seq(list[AType] elems) => inheritLabel(x, \seq(intermix(elems, l, others)))
  }
}
