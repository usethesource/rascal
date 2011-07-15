@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::GrammarTools::Grammar

import Set;
import IO;
// import experiments::GrammarTools::Grammars; // for testing
import String;

// Data structure for representing a grammar

public data Symbol  = t(str text) | nt(str name) | epsilon();
public alias Rule   = tuple[Symbol lhs, list[Symbol] symbols];
public data Grammar = grammar(Symbol startSym, set[Rule] rules);

// Utility predicates on Symbols

public bool isTermSymbol(Symbol s){
   return t(_) := s;
}

public bool isNonTermSymbol(Symbol s){
   return nt(_) := s;
}

// Get symbols, terminals or non-terminals from a grammar

public set[Symbol] symbols(Grammar G){
   return { A, sym | <Symbol A, list[Symbol] symbols> <- G.rules, sym <- symbols};
}

public set[Symbol] terminals(Grammar G){
   return { sym | Symbol sym <- symbols(G), isTermSymbol(sym)};
}

public set[Symbol] nonTerminals(Grammar G){
   return { sym | Symbol sym <- symbols(G), isNonTermSymbol(sym)};
}

// Get the use relation between non-terminals

public rel[Symbol, Symbol] nonTerminalUse(Grammar G){
    return { <A, sym> | <Symbol A, list[Symbol] symbols> <- G.rules, Symbol sym <- symbols, isNonTermSymbol(sym)};
}

// Get all non-terminals that are reachable from the start symbol

public set[Symbol] reachable(Grammar G){
   return (nonTerminalUse(G)+)[G.startSym];
}

// Get all non-terminals that are not reachable from the start symbol

public set[Symbol] nonReachable(Grammar G){
   return nonTerminals(G) - reachable(G);
}

/* TODO: should come from Grammars */

public Grammar G1 = grammar(nt("E"),
{
<nt("E"), [nt("E"), t("*"), nt("B")]>,
<nt("E"), [nt("E"), t("+"), nt("B")]>,
<nt("E"), [nt("B")]>,
<nt("B"), [t("0")]>,
<nt("B"), [t("1")]>
});

public Grammar G2 = grammar(nt("E"),
{
<nt("E"),  [nt("T"), nt("E1")]>,
<nt("E1"), [t("+"), nt("T"), nt("E1")]>,
<nt("E1"), []>,
<nt("T"),  [nt("F"), nt("T1")]>,
<nt("T1"), [t("*"), nt("F"), nt("T1")]>,
<nt("T1"), []>,
<nt("F"),  [t("("), nt("E"), t(")")]>,
<nt("F"),  [t("id")]>
});

test symbols(G1) == {nt("E"),t("1"),t("0"),nt("B"),t("+"),t("*")};
test terminals(G1)  == {t("1"),t("0"),t("+"),t("*")};
test nonTerminals(G1) == {nt("E"),nt("B")};
test nonTerminalUse(G1) == {<nt("E"), nt("E")>, <nt("E"), nt("B")>};
test reachable(G1) == {nt("E"), nt("B")};
test nonReachable(G1) == {};
    
test symbols(G2) == {nt("F"),nt("E"),nt("T"),nt("T1"),nt("E1"),t(")"),t("("),t("+"),t("id"),t("*")};
test terminals(G2) == {t(")"),t("("),t("+"),t("id"),t("*")};
test nonTerminals(G2) == {nt("F"),nt("E"),nt("T"),nt("T1"),nt("E1")};
test nonTerminalUse(G2) == {<nt("T1"),nt("F")>,<nt("E1"),nt("E1")>,<nt("E1"),nt("T")>,
                            <nt("T1"),nt("T1")>,<nt("T"),nt("F")>,<nt("F"),nt("E")>,
                            <nt("E"),nt("E1")>,<nt("E"),nt("T")>,<nt("T"),nt("T1")>};
test reachable(G2) == {nt("F"),nt("E"),nt("T"),nt("T1"),nt("E1")};
test nonReachable(G2) == {};
    
private Grammar G2x = grammar(G2.startSym, G2.rules + {<nt("X"),  [t("x")]>});
  
test nonTerminals(G2x) =={nt("F"),nt("E"),nt("T"),nt("T1"),nt("E1"), nt("X")};
test reachable(G2x) == {nt("F"),nt("E"),nt("T"),nt("T1"),nt("E1")};
test nonReachable(G2x) == {nt("X")};
