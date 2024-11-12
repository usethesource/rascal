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
@synopsis{This filter removes error trees until no ambiguities caused by error recovery are left.}
@description{
Error recovery often produces ambiguous trees where errors can be recovered in multiple ways. Ambiguity
clusters (`amb`) represent the choices between all the valid prefixes. This filter removes choices until
the last one is left. 

Note that regular ambiguous trees remain in the parse forest unless `allowAmbiguity` is set to false in 
which case an error is thrown.
}
@benefits{
* after this algorithm only one error is left at every input position with an error. Downstream
functionality does not have to deal with ambiguity anymore, making the code robust.
}
@pitfalls{
* this algorithm removes valid prefixes based on heuristics like "shortest error", which may 
remove interesting prefixes for downstream processing. In particular the accuracy of error repair and auto-complete
may be damaged by this function. So it is best to use it for error recovery, and not for error repair.
}
java Tree disambiguateErrors(Tree t, bool allowAmbiguity=true);

@synopsis{Removes error trees if they are in optional positions.}
@description{
Removing grammatically optional error trees can reduce the number of case distinctions
required to make algorithms that process parse trees robust against parse errors.

The algorithm works bottom-up such that only the smallest possible trees are removed.
After every removal, new error statistics are prepared for passing up to the next level.
If errors are completely removed by the filter, then the parents will remain unchanged.
}
@benefits{
* Removing error trees increases the robustness of downstream processors
* By removing error trees from lists, the relative `src` origins remain the same for
downstream processing. 
* Sets of trees in ambiguity clusters may be reduced to singletons, making the ambiguity cluster dissappear. 
This also improves the robustness of downstream processors.
}
@pitfalls{
* this algorithm may remove relatively large sub-trees and thus through away valuable information.
It is much better to use this as a recovery tool that increases the robustness of oblivious downstream processors,
then to start an error repair or auto-complete algorithm.
}
Tree filterOptionalErrorTrees(Tree x) = visit(x) {
    case t:appl(p:regular(/iter-sep|iter-star-sep/(_,[_])),[*pre, _sep, appl(error(_,_,_),_), *post])
        => appl(p, [*pre, *post])[@\loc=t@\loc]
    case t:appl(p:regular(/iter-sep|iter-star-sep/(_,[_])),[appl(error(_,_,_),_), _sep, *post])
        => appl(p, post)[@\loc=t@\loc]
    case t:appl(p:regular(/iter-sep|iter-star-sep/(_,[_,_,_])),[*pre, _sep1, _sep2, _sep3, appl(error(_,_,_),_), *post])
        => appl(p, [*pre, *post])[@\loc=t@\loc]
    case t:appl(p:regular(/iter-sep|iter-star-sep/(_,[_,_,_])),[appl(error(_,_,_),_), _sep1, _sep2, _sep3, *post])
        => appl(p, post)[@\loc=t@\loc]
    case t:appl(p:regular(/iter|iter-star/(_)),[*pre, appl(error(_,_,_),_), *post])
        => appl(p, [*pre, *post])[@\loc=t@\loc]
    case t:appl(p:regular(opt(_)), appl(error(_,_,_), _)) 
        => appl(p, [])[@\loc=t@\loc]
    // TODO: some forms of recursion could be flattened in the presence of errors mid-way.
};

@synopsis{Removes trees which contain error trees, if they are in optional positions.}
@description{
Removing grammatically optional error trees can reduce the number of case distinctions
required to make algorithms that process parse trees robust against parse errors.

The algorithm works bottom-up such that only the smallest possible trees are removed.
After every removal, new error statistics are prepared for passing up to the next level.
If errors are completely removed by the filter, then the parents will remain unchanged.
}
@benefits{
* this algorithm is more aggressive and more successful in removing error trees
then ((filterOptionalErrorTrees)) can be.
* Removing error trees increases the robustness of downstream processors.
* By removing error trees from lists, the relative `src` origins remain the same for
downstream processing. 
* Sets of trees in ambiguity clusters may be reduced to singletons, making the ambiguity cluster dissappear. 
This also improves the robustness of downstream processors.
}
@pitfalls{
* this algorithm may remove (very) large sub-trees if they contain one error somewhere, and thus through away valuable information.
It is much better to use this as a recovery tool that increases the robustness of oblivious downstream processors,
then to start an error repair or auto-complete algorithm.

}
Tree filterOptionalIndirectErrorTrees(Tree x) = bottom-up visit(addErrorStats(x)) {
    case t:appl(p:regular(/iter-sep|iter-star-sep/(_,[_])),[*pre, _sep, appl(_,_, erroneous=true), *post])
        => addStats(appl(p, [*pre, *post])[@\loc=t@\loc])
    case t:appl(p:regular(/iter-sep|iter-star-sep/(_,[_])),[appl(_,_, erroneous=true), _sep, *post])
        => addStats(appl(p, post)[@\loc=t@\loc])
    case t:appl(p:regular(/iter-sep|iter-star-sep/(_,[_,_,_])),[*pre, _sep1, _sep2, _sep3, appl(_,_, erroneous=true), *post])
        => addStats(appl(p, [*pre, *post])[@\loc=t@\loc])
    case t:appl(p:regular(/iter-sep|iter-star-sep/(_,[_,_,_])),[appl(_,_, erroneous=true), _sep1, _sep2, _sep3, *post])
        => addStats(appl(p, post)[@\loc=t@\loc])
    case t:appl(p:regular(/iter|iter-star/(_)),[*pre, appl(_,_, erroneous=true), *post])
        => addStats(appl(p, [*pre, *post])[@\loc=t@\loc])
    case t:appl(p:regular(opt(_)), appl(_, _, erroneous=true)) 
        => appl(p, [])[@\loc=t@\loc]
} 

@synopsis{Fields for storing the result of ((addErrorStats))}
data Tree(int skipped = 0, bool erroneous=false);

@synopsis{Annotates all nodes of a parse tree with error recovery statistics}
@description{
After this algorithm all nodes contain this information:
* `int skipped` for the total number of skipped characters in a tree
* `bool erroneous` that marks sub-trees which do contain errors with `true` while others remain `false`
}
@benefits{
* local information about aggegrated information can be handy when filtering
parse forests
}
@pitfalls{
* statistics do not tell the whole truth about sub-trees. Filtering based on these numbers
must be seen as a heuristic that sometimes pays-off, and often hides crucial information.
}
Tree addErrorStats(Tree x) = bottom-up visit(x) {
    case Tree t => addStats(t)
};

@synopsis{Reusable utility for re-computing error statistics per Tree node.}
private Tree addStats(t:appl(prod(_,_,_), args)) = t[skipped = (0 | it + a.skipped | a <- args)][erroneous = (false | it || a.erroneous | a <- args)];
private Tree addStats(t:appl(skipped(_), args))  = t[skipped = size(args)][erroneous = true];
private Tree addStats(t:appl(error(_,_,_), args))= t[skipped = (0 | it + a.skipped | a <- args)][erroneous = true];
private Tree addStats(t:amb(alts))               = t[skipped = (0 | min([it, a.skipped]) | a <- alts)][erroneous = (false | it && a.erroneous | a <- alts)];
default private Tree addStats(Tree t) = t;

@synopsis{Disambiguates error ambiguity clusters by selecting the alternatives with the shortest amount of skipped characters}
@benefits{
* this is an aggressive filter that can greatly reduce the complexity of dealing with recovered parse trees.
* chances are that after this filter all ambiguity has been removed, making downstream processing easier.
}
@pitfalls{
* the trees with the shortest skips are not always the most relevant trees to consider for repair or recovery.
}
Tree selectShortestSkips(Tree x) = visit(addErrorStats(x)) {
    case amb(alts) => amb({ a | a <- alts, a.skipped == minimum})
        when int minimum := min([a.skipped | a <- alts])
}

@synopsis{Disambiguates error ambiguity clusters by selecting the alternatives with the largest amount of skipped characters}
@benefits{
* this is an aggressive filter that can greatly reduce the complexity of dealing with recovered parse trees.
* chances are that after this filter all ambiguity has been removed, making downstream processing easier.
}
@pitfalls{
* the trees with the longest skips are not always the most relevant trees to consider for repair or recovery.
}
Tree selectLongestSkips(Tree x) = visit(addErrorStats(x)) {
    case amb(alts) => amb({ a | a <- alts, a.skipped == maximum})
        when int maximum := max([a.skipped | a <- alts])
}