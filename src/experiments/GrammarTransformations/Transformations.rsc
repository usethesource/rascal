module experiments::GrammarTransformations::Transformations

import Grammar;

/*
 * Simple grammar transformations including:
 * - renaming of terminals
 * - renaming of non-terminals
 * - removal of rules for unused non-terminals
 */

// renameNonTerminal: rename a given non-terminal

Grammar renameNonTerminal(Grammar G, NonTerminal from, NonTerminal to){
  return visit(G){
    case [|<from>|] => to;
  }
}

// renameTerminal: rename a given terminal

Grammar renameTerminal(Grammar G, Terminal from, Terminal to){
  return visit(G){
    case [|<from>|] => to;
  }
}

// getNonTerminalUse: construct a relation that relates each non-terminal at the
// left-hand side of a rule with all non-terminals that occur in its right-hand side

rel[NonTerminal, NonTerminal] getNonTerminalUse(Grammar G){
  rel[NonTerminal, NonTerminal] use = {};
  visit(G){
    case [|<Nonterminal N1> ::= <{Alternative "|"}+ alts>;|]: {
      use += { <N1, N2> | <NonTerminal N2> <- alts };
    }
  }
  return use
}

// getStart: get the start symbol from the grammar

NonTerminal getStart(Grammar G){
  if([|<Nonterminal N> ::= <{Alternative "|"}+ alts>; <Rule* rules>|] := G)
    return N;
  // cannot happen
}

// removeUnused: remove all rules for non-terminals that cannot be reached from
// the start symbol

Grammar removeUnused(Grammar G){
  start = getStart(G);
  use = getNonTerminalUse(G);
  indirectUse = use+;
  indirectUseFromStart = indirectUse[start];
  
  Rule* rules = [| |];
  
  for(Rule r <- Grammar){
    nt = getLhs(r);
    if(nt == start || nt in indirectUseFromStart)
        rules += r;
  }
  return Grammar:[|<rules>|];
}

public bool test(){
  Grammar As = [| <A> ::= <A> "a" | ; <C> ::= "c"; |];
     
  assertEqual(renameNonTerminal(As, <A>, <B>), [|<B> ::= <B> "a" | ; <C> ::= "c";|]);
  assertEqual(renameTerminal(As, [|"a"|], [|"b"|]), [|<A> ::= <A> "b" | ; <C> ::= "c";|]);
  assertEqual(removeUnused(As),  [| <A> ::= <A> "a" | ; |]);
  
  Grammar Exp = [|
     <Exp> ::= <Exp> "+" <Exp>;
     <Exp> ::= <Exp> "*" <Exp>;
     <Exp> ::= "int";
     <Exp> ::= "id";
     |];
     
  return report();
}