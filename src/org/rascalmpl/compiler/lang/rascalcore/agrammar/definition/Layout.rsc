@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascalcore::agrammar::definition::Layout

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
    case x:\iter(AType n) => inheritLabel(x, \iter-seps(n, [l])) when !isLexicalAType(x)
    case x:\iter-star(AType n) => inheritLabel(x, \iter-star-seps(n, [l])) when !isLexicalAType(x)
    case x:\iter-seps(AType n, [AType sep]) => inheritLabel(x, \iter-seps(n,[l,sep,l])) when !isLexicalAType(x),  sep != l, sep notin others, isValidSep(sep, others)
    case x:\iter-star-seps(AType n, [AType sep]) => inheritLabel(x, \iter-star-seps(n, [l, sep, l])) when !isLexicalAType(x), sep != l, sep notin others, isValidSep(sep, others)
    case x:\seq(list[AType] elems) => inheritLabel(x, \seq(intermix(elems, l, others)))
  }
}
