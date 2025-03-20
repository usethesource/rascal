@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
@bootstrapParser
module lang::rascalcore::compile::Examples::RascalMetrics

import ParseTree;
import lang::rascal::\syntax::Rascal;
import util::Reflective;
import Set;

int cntAlt(Prod p){
    switch(p){
      case (Prod) `<Prod lhs> | <Prod rhs>`: return cntAlt(lhs) + cntAlt(rhs);
      case (Prod) `<Prod lhs> \> <Prod rhs>`: return cntAlt(lhs) + cntAlt(rhs);
    }
   return 1;
}

value main()
      = measure();

value measure(){

     //moduleLoc = |std:///lang/rascal/syntax/Rascal.rsc|;
    moduleLoc = |std:///experiments/Compiler/RVM/AST.rsc|;
    //moduleLoc = |std:///experiments/Compiler/RVM/Syntax.rsc|;
    //moduleLoc = |std:///experiments/Compiler/muRascal/Syntax.rsc|;
     //moduleLoc = |std:///experiments/Compiler/muRascal/AST.rsc|;
    //moduleLoc = |std:///demo/lang/Pico/Syntax.rsc|;
    //moduleLoc = |std:///demo/lang/Exp/Concrete/NoLayout/Syntax.rsc|;
    m = parse(#start[Module], moduleLoc).top;
    stats = ();
    nonterm = {};
    datadefs = {};
    
    visit(m){
      case (SyntaxDefinition) `<Visibility _> layout <Sym name> = <Prod p> ;`: {
            stats["layout"] ? 0 += 1;
            nonterm += "<name>";
            stats["layoutAlts"] ? 0 += cntAlt(p);
            }
      case (SyntaxDefinition) `lexical <Sym name> = <Prod p> ;` : {
            stats["lexical"] ? 0 += 1;
             nonterm += "<name>";
            stats["lexicalAlts"] ? 0 += cntAlt(p);
            }
      case (SyntaxDefinition) `keyword <Sym name> = <Prod p> ;` : {
            stats["keyword"] ? 0 += 1;
            nonterm += "<name>";
            stats["keywordAlts"] ? 0 += cntAlt(p);
            }
      case (SyntaxDefinition) `<Start _> syntax <Sym name> = <Prod p> ;` : {
            nonterm += "<name>";
            stats["syntax"] ? 0 += 1;
            stats["syntaxtAlts"] ? 0 += cntAlt(p);
        }
      case Declaration d:
            if(d is \data || d is \dataAbstract) datadefs += "<d.user>"; 
      case Variant _: 
          stats["variant"] ? 0 += 1;
    }
    stats["nonterminal"] = size(nonterm);
    stats["data"] = size(datadefs);
    return stats;
}