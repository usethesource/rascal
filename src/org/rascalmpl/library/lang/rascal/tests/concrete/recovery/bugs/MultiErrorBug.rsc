/**
* Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
*
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
**/
module lang::rascal::tests::concrete::recovery::bugs::MultiErrorBug

import ParseTree;
import IO;
import util::ParseErrorRecovery;
import List;
import vis::Text;

start syntax Module	= SyntaxDefinition* ;

syntax SyntaxDefinition =  Prod ";" ;

lexical Name = [A-Z];

syntax Sym
	= Sym NonterminalLabel
	| StringConstant
	;

lexical StringConstant = "\"" StringCharacter* "\"" ;

lexical StringCharacter = ![\"] ;

lexical NonterminalLabel = [a-z] ;

syntax Prod = Sym* ;

// This is an open issue: instead of two small errors, one big error tree is returned.
bool multiErrorBug() {
    str input = "#\"a\";#\"b\";";
    Tree t = parser(#start[Module], allowRecovery=true, allowAmbiguity=true)(input, |unknown:///?visualize=false|);

    println(prettyTree(t));

    list[Tree] errors = findAllParseErrors(t);
    println("<size(errors)> Errors");
    for (Tree error <- errors) {
        Tree skipped = getSkipped(error);
        println("  <skipped@\loc>: <getErrorText(error)>");
    }
    Tree disambiguated = disambiguateParseErrors(t);
    list[Tree] disambiguatedErrors = findAllParseErrors(disambiguated);
    println("After disambiguating:");
    println("<size(disambiguatedErrors)> Errors");
    for (Tree error <- disambiguatedErrors) {
        Tree skipped = getSkipped(error);
        println("  <skipped@\loc>: <getErrorText(error)>");
    }
    return true;
}

