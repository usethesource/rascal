module experiments::GrammarTools::Grammar

import experiments::GrammarTools::BNF;
import experiments::GrammarTools::ImportBNF; // Only for testing
import basic::Whitespace;
import Set;
import IO;
import UnitTest;
import String;

// Data structure for representing the grammar

public data Symbol  = t(str text) | nt(str name) | epsilon();
public alias Rule   = tuple[Symbol name, list[Symbol] symbols];
public data Grammar = grammar(Symbol start, set[Rule] rules);

// Utility predicates on Symbols

public bool isTermSymbol(Symbol s){
   return t(_) := s;
}

public bool isNonTermSymbol(Symbol s){
   return nt(_) := s;
}

// Get symbols, terminals or non-terminals from a grammar

public set[Symbol] symbols(Grammar G){
   return { sym | Rule r <- G.rules, Symbol sym <- r.symbols};
}

public set[Symbol] terminals(Grammar G){
   return { sym | Rule r <- G.rules, Symbol sym <- r.symbols, isTermSymbol(sym)};
}

public set[Symbol] nonTerminals(Grammar G){
   return { sym | Rule r <- G.rules, Symbol sym <- r.symbols, isNonTermSymbol(sym)};
}

// Get the use relation between non-terminals

public rel[Symbol, Symbol] symbolUse(Grammar G){
    return { <A, sym> | <Symbol A, list[Symbol] symbols> <- G.symbols, Symbol sym <- symbols, isNonTermSymbol(sym)};
}

// Get all non-terminals that are reachable from the start symbol

public set[Symbol] reachable(Grammar G){
   return symbolUse(G)+[G.start];
}

// Get all non-terminals that are not reachable from the start symbol

public set[Symbol] nonReachable(Grammar G){
   return symbols(G) - reachable(G);
}

BNF G1 = `grammar E 
            rules
            	E ::= E '*' B;
           		E ::= E '+' B;
            	E ::= B;
            	B ::= '0';
            	B ::= '1';`;
            	
/*	  This list is too ambiguous ...          	
public Grammar G1converted = grammar("E",
{
<nt("E"), [nt("E"), t("*"), nt("B")]>,
<nt("E"), [nt("E"), t("+"), nt("B")]>,
<nt("E"), [nt("B")]>,
<nt("B"), [t("0")]>,
<nt("B"), [t("1")]>
});
*/

public bool test(){
    println(importBNF(G1));
    
    println(exportBNF(importBNF(G1)));
	return report("GrammarTools::Grammar");
}