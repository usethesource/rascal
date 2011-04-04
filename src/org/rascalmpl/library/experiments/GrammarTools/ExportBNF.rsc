@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module experiments::GrammarTools::ExportBNF

import experiments::GrammarTools::BNF;
import experiments::GrammarTools::Grammar;

import experiments::GrammarTools::Grammars;     // for testing
import experiments::GrammarTools::BNFGrammars;  // for testing

import Set;
import IO;
import String;

// Export a grammar to BNF
public BNF exportBNF(Grammar G){

   // Problem: `` is ambiguous but the disambiguation BNFRule* `` is also ambiguous!
   // (e.g., * can also be multiplication)
   // Potential solution: require in the Rascal syntax extra parentheses as in (BNFRule*) ``.
   // This resembles casts.
   
   println("exportBNF 1");
   BNFRule* bnfRules =  ``;
   println("exportBNF 2");
  
   for(<Symbol A, list[Symbol] symbols> <- G.rules){
       NT = toElement(A);
       Elems = toElements(symbols);
       bnfRules = BNFRule* `<bnfRules> <NT> ::= <Elems>;`;
   }
   bnfStart = toElement(G.start);
   return BNF `grammar <bnfStart> rules <bnfRules>`;
}

BNFElement toElement(Symbol s){
   println("toElement<s>");
   if(t(str name) := s)
      return Terminal `'<name>'`;
   if (nt(str name) := s)
      return NonTerminal `<name>`;
    throw IllegalArgument(s);  
}

BNFElement* toElements(list[Symbol] symbols){
   println("toElements<symbols>");
   BNFElement* result = BNFElement*``;
   for(Symbol sym <- symbols){
       elm = toElement(sym);
       result = BNFElement* `<result> <sym>`;
   }
   return result;
}


// TODO: this test always succeeds
test BNF b := exportBNF(G1);

