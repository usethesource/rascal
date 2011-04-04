@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::GrammarTools::ImportBNF

import experiments::GrammarTools::BNF;
import experiments::GrammarTools::Grammar;

import experiments::GrammarTools::BNFGrammars;  // for testing
import experiments::GrammarTools::Grammars;     // for testing

import Set;
import IO;
import String;
import Exception;

// Import a grammar in BNF notation

public Grammar importBNF(BNF bnfGrammar){
    if(`grammar <NonTerminal Start> rules <BNFRule+ Rules>` := bnfGrammar){
       rules = {};
       for(/`<NonTerminal L> ::= <BNFElement* Elements> ; ` <- Rules){
            rules += <toSymbol(L), [toSymbol(E) | BNFElement E <- Elements]>;
       }
       return grammar(toSymbol(Start), rules); 
    }
}

// Convert a BNF Element to a Symbol value

Symbol toSymbol(BNFElement E){
   visit(E){
     case Terminal T:
          if(/'<text:[^']*>'/ := "<T>")
            return t(text);
     case NonTerminal NT:
          return nt("<NT>");
   }
   throw IllegalArgument(E);
}

// Tests
    
test importBNF(G1BNF) == G1;
test importBNF(G2BNF) == G2;
