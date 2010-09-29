module rascal::syntax::Actions

import rascal::syntax::Grammar;
import ParseTree;

public tuple[Grammar grammar, map[Production, Tree] actions] extractActions(Grammar g) {
  actions = ();
  
  g = visit (g) {
    case \action(Symbol nt, p:prod(_,_,_), Tree action) : {
      actions[p] = action;
      insert p;
    }
  }
  
  return <g, actions>;
}