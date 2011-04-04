@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module lang::rascal::syntax::Actions

import Grammar;
import lang::rascal::syntax::Generator;
import ParseTree;

public tuple[Grammar grammar, map[Production, Tree] actions] extractActions(Grammar g) {
  actions = ();
  
  g = visit (g) {
    case \action(Symbol nt, p:prod(_,_,_), Tree action) : {
      actions[unmeta(p)] = unmeta(action);
      insert p;
    }
  }
  
  return <g, actions>;
}
