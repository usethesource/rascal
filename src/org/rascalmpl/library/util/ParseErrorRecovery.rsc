/**
 * Copyright (c) 2024-2025, NWO-I Centrum Wiskunde & Informatica (CWI)
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
 
module util::ParseErrorRecovery

import ParseTree;
import String;

@synopsis{Library functions for handling parse trees with error nodes.}
@description{
When parse functions are called with `errorRecovery=true`, the resulting parse trees can contain error and skipped nodes. For an in-depth description of these nodes see the `ParseTree` module.
Functions in this module can be used to detect, isolate, and analyze these error nodes.
Note that these are fairly low-level functions. Better high-level support for error trees will be added to Rascal and its standard library in the future.
As such, this code should be considered experimental and used with care.
}

@synopsis{Check if a parse tree contains any error nodes, the result of error recovery.}
@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
java bool hasParseErrors(Tree tree);

@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
@synopsis{Find all error productions in a parse tree.
Note that children of an error tree can contain errors themselves.
The list of errors returned by this methood is created by an outermost visit of the parse tree so if an error tree contains other errors the outermost tree is
returned first.
Often error trees are highly ambiguous and can contain a lot of error trees. This function is primarily used to analyze small examples as calling this function
on a tree with many errors will result in long runtimes and out-of-memory errors.
}
java list[Tree] findAllParseErrors(Tree tree);

@synopsis{Get the symbol (sort) of the failing production}
Symbol getErrorSymbol(appl(error(Symbol sym, _, _), _)) = sym;

@synopsis{Get the production that failed}
Production getErrorProduction(appl(error(_, Production prod, _), _)) = prod;

@synopsis{Get the dot (position in the production) of the failing element in a production}
int getErrorDot(appl(error(_, _, int dot), _)) = dot;

@synopsis{Get the skipped tree}
Tree getSkipped(appl(error(_, _, _), [*_, skip:appl(skipped(_), _)])) = skip;

@synopsis{Get the text that failed to parse. This is only the text of the part that has been skipped to be able to continue parsing.
If you want the text of the whole error tree, you can just use string interpolation: `"<error>"`.
}
str getErrorText(appl(error(_, _, _), [*_, sk:appl(skipped(_), _)])) = "<sk>";

@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
@synopsis{Error recovery often produces ambiguous trees where errors can be recovered in multiple ways.
This filter removes error trees until no ambiguities caused by error recovery are left.
Note that regular ambiguous trees remain in the parse forest unless `allowAmbiguity` is set to false in which case an error is thrown.
This method uses simple and somewhat arbitrary heuristics, so its usefulness is limited.
}
java &T<:Tree disambiguateParseErrors(&T<:Tree t, bool allowAmbiguity=true);

@synopsis{Disambiguate the error ambiguities in a tree and return the list of remaining errors. 
The list is created by an outermost visit of the parse tree so if an error tree contains other errors the outermost tree is returned first.
Error disambiguation is based on heuristics and are therefore unsuitable for many use-cases. We primarily use this functionality
for testing and for presenting recovered errors in VSCode.
}
list[Tree] findBestParseErrors(Tree tree) = findAllParseErrors(disambiguateParseErrors(tree));

@synopsis{Create a parse filter based on `disambiguateErrors` with or without `allowAmbiguity`.}
Tree(Tree) createParseErrorFilter(bool allowAmbiguity) =
    Tree(Tree t)  { return disambiguateParseErrors(t, allowAmbiguity=allowAmbiguity); };


@javaClass{org.rascalmpl.library.util.ParseErrorRecovery}
java &T<:Tree pruneAmbiguities(&T<:Tree t, int maxDepth=3);
