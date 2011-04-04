@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::GrammarTransformations::Transformations

import BNF;
import basic::Whitespace;

/*
 * Simple grammar transformations including:
 * - rename terminal
 * - rename non-terminal
 * - add a rule
 * - delete a rule
 * - remove all rules for unused non-terminals
 * - xxxxs
 */

// renameNonTerminal: rename a given non-terminal

BNF renameNonTerminal(BNF G, NonTerminal from, NonTerminal to){
  return visit(G){
    case [|<from>|] => to;
  };
}

// renameTerminal: rename a given terminal

BNF renameTerminal(BNF G, Terminal from, Terminal to){
  return visit(G){
    case [|<from>|] => to;
  };
}

// addRule: add a rule to a grammar

Grammar addRule(Grammar G, Rule r){
  if([| grammar <NonTerminal N> rules <Rule+ rules> |] := G){
    rules2 += rule;
    return [| grammar <N> rules <rules2> |];
  }
  // cannot happen
}

// delRule: delete a rule from a grammar

Grammar delRule(Grammar G, Rule r){
  if([| grammar <NonTerminal N> rules <Rule+ rules> |] := G){
    rules2 -= rule;
    return [| grammar N rules <rules2> |];
  }
  // cannot happen
}

// getNonTerminalUse: construct a relation that relates each non-terminal at the
// left-hand side of a rule with all non-terminals that occur in its right-hand side

rel[NonTerminal, NonTerminal] getNonTerminalUse(Grammar G){
  rel[NonTerminal, NonTerminal] use = {};
  visit(G){
    case [|<Nonterminal N1> ::= <Symbol* symbols>;|]: {
      use += { <N1, N2> | NonTerminal N2 <- symbols };
    }
  }
  return use
}

// getStart: get the start symbol from the grammar

NonTerminal getStart(Grammar G){
  if([| grammar N rules <Rule+ rules> |] := G)
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
  Grammar As = [| grammar <A> rules <A> ::= <A> "a"; <A> ::= ; <C> ::= "c"; |];
     
  assertEqual(renameNonTerminal(As, <A>, <B>), [|grammar <B> rules <B> ::= <B> "a"; <B> ::= ; <C> ::= "c";|]);
  assertEqual(renameTerminal(As, [|"a"|], [|"b"|]), [|grammar <A> rules <A> ::= <A> "b"; <A> ::= ; <C> ::= "c";|]);
  assertEqual(removeUnused(As),  [| grammar <A> rules <A> ::= <A> "a"; <A> ::= ; |]);
  
  Grammar Exp = [|
     <Exp> ::= <Exp> "+" <Exp>;
     <Exp> ::= <Exp> "*" <Exp>;
     <Exp> ::= "int";
     <Exp> ::= "id";
     |];
     
  return report();
}
