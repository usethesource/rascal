@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascalcore::grammar::definition::Keywords

import lang::rascalcore::check::AType;
import lang::rascalcore::grammar::definition::Symbols;
//import IO;
import Node;

public AGrammar expandKeywords(AGrammar g) {
  //println("expandKeywords"); iprintln(g, lineLimit=10000);
  g1 = visit(g) {
    case AType::conditional(sym, set[ACondition] conds) => conditional(sym, expandKeywords(g, conds)) 
  };
  //g1.rules = (n : g1.rules[n] | n <- g1.rules, n has syntaxRole ? (n.syntaxRole != keywordSyntax()) : true);
  //println("leave expandKeywords");
  return g1;
}

private set[ACondition] expandKeywords(AGrammar g, set[ACondition] conds) {
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
        	todo += {cond[atype=s] | choice(_, set[AProduction] alts) := g.rules[unset(cond.atype, "id")], prod(_,[s]) <- alts};
      	}  
      } else {
        done += cond;
      }
    }
  }
  
  return done;  
}