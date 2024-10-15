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
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/
 
 module util::ErrorRecovery

import ParseTree;
import String;

@synopsis{Check if a parse tree contains any error nodes, the result of error recovery.}
bool hasErrors(Tree tree) = /appl(error(_, _, _), _) := tree;

@javaClass{org.rascalmpl.library.util.ErrorRecovery}
@synopsis{Find all error productions in a parse tree. The list is created by an outermost visit of the parse tree so if an error tree contains other errors the outermost tree is returned first.}
java list[Tree] findAllErrors(Tree tree);

@synopsis{Disambiguate the error ambiguities in a tree and return the list of remaining errors. 
The list is created by an outermost visit of the parse tree so if an error tree contains other errors the outermost tree is returned first.}
list[Tree] findBestErrors(Tree tree) = findAllErrors(disambiguateErrors(tree));

@synopsis{Get the symbol (sort) of the failing production}
Symbol getErrorSymbol(appl(error(Symbol sym, _, _), _)) = sym;

@synopsis{Get the production that failed}
Production getErrorProduction(appl(error(_, Production prod, _), _)) = prod;

@synopsis{Get the dot (position in the production) of the failing element in a production}
int getErrorDot(appl(error(_, _, int dot), _)) = dot;

@synopsis{Get the skipped tree}
Tree getSkipped(appl(error(_, _, _), [*_, skip:appl(skipped(_), _)])) = skip;

@synopsis{Get the text that failed to parse. This is only the text of the part that has been skipped to be able to continue parsing.
If you want the text of the whole error tree, you can just use string interpolation: "<error>".
}
str getErrorText(appl(error(_, _, _), [*_, appl(skipped(_), chars)])) = stringChars([c | char(c) <- chars]);

@javaClass{org.rascalmpl.library.util.ErrorRecovery}
@synopsis{Error recovery often produces ambiguous trees where errors can be recovered in multiple ways.
This filter removes error trees until no ambiguities caused by error recovery are left.
Note that regular ambiguous trees remain in the parse forest unless `allowAmbiguity` is set to false in which case an error is thrown.
}
java Tree disambiguateErrors(Tree t, bool allowAmbiguity=true);
