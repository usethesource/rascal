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
module lang::rascalcore::agrammar::definition::Keywords

import lang::rascalcore::check::ATypeBase;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::agrammar::definition::Symbols;
//import IO;
import Node;

public AGrammar expandKeywords(AGrammar g) {
  //println("expandKeywords"); iprintln(g, lineLimit=10000);
  g1 = visit(g) {
    case cond:AType::conditional(sym, set[ACondition] conds) => inheritLabel(cond, AType::conditional(sym, expandKeywords(g, conds)))
  };
  //g1.rules = (n : g1.rules[n] | n <- g1.rules, n has syntaxRole ? (n.syntaxRole != keywordSyntax()) : true);
  //println("leave expandKeywords");
  return g1;
}

public set[ACondition] expandKeywords(AGrammar g, set[ACondition] conds) {
  names = {};
  done = {};
  todo = conds;

  solve(todo) {  
    for (cond <- todo, !(cond in done)) {
      todo -= {cond};
      
      if (cond has atype, aadt(name,_,keywordSyntax()) := cond.atype) {
        if (name notin names) {
        	names += {name};
        //	println("cond = <cond>");
        	todo += {cond[atype=s] | achoice(_, set[AProduction] alts) := g.rules[unset(cond.atype, "id")], prod(_,[s]) <- alts};
      	}  
      } else {
        done += cond;
      }
    }
  }
  
  return done;  
}
