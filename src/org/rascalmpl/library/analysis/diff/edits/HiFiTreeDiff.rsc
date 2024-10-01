@license{
Copyright (c) 2018-2023, NWO-I Centrum Wiskunde & Informatica
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
@synopsis{Infer ((TextEdit)) from the differences between two parse ((ParseTree::Tree))s}
@description{
This module will move to the Rascal standard library.
}
module analysis::diff::edits::HiFiTreeDiff

extend analysis::diff::edits::TextEdits;
import ParseTree;
import List;
import String;
import Locations;

@synopsis{Detects minimal differences between parse trees and makes them explicit as ((TextEdit)) instructions.}
@description{
This is a "diff" algorithm of two parse trees to generate a ((TextEdit)) script that applies the differences on 
the textual level, _with minimal collatoral damage in whitespace_. This is why it is called "HiFi": minimal unnecessary
noise introduction to the original file.

The resulting ((TextEdit))s are an intermediate representation for making changes in source code text files. They can be executed independently via ((ExecuteTextEdits)), or interactively via ((IDEServices)), or LanguageServer features. 

This top-down diff algorithm takes two arguments:
1. an _original_ parse tree for a text file, 
2. and a _derived_ parse tree that is mostly equal to the original but has pieces of it substituted or rewritten. 

From the tree node differences between these two trees, ((TextEdit))s are derived such that:
* when the edited source text is parsed again, the resulting tree would match the derived tree. 
However, the parsed tree could be different from the derived tree in terms of whitespace, indentation and case-insensitive literals (see below).
* when tree nodes (grammar rules) are equal, smaller edits are searched by pair-wise comparison of the children
* differences between respective layout or (case insensitve) literal nodes are always ignored 
* when lists have changed, careful editing of possible separators ensures syntactic correctness
* when new sub-trees are inserted, the replacement will be at the same indentation level as the original. (((TODO this is a todo)))
* when case-insensitive literals have been changed under a grammar rule that remained the same, no edits are produced.

The function comes in handy when we use Rascal to rewrite parse trees, and then need to communicate the effect
back to the IDE (for example using ((util::IDEServices)) or ((util::LanguageServer)) interfaces). We use
((ExecuteTextEdits)) to _test_ the effect of ((TextEdits)) while developing a source-to-source transformation. 
}
@benefits{
* This function allows the language engineer to work in terms of abstract and concrete syntax trees while manipulating source text. The
((TextEdit))s intermediate representation bridge the gap to the minute details of IDE interaction such as "undo" and "preview" features.
* Text editing is fraught with details of whitespace, comments, list separators; all of which are handled here by 
the exactness of syntactic and semantic knowledge of the parse trees. 
* Where possible the algorithm also retains the capitalization of case-insensitive literals.
* The algorithm retrieves and retains indentation levels from the original tree, even if sub-trees in the
derived tree have mangled indentation. This allows us to ignore the indentation concern while thinking of rewrite
rules for source-to-souce transformation, and focus on the semantic effect. 
}
@pitfalls{
* If the first argument is not an original parse tree, then basic assumptions of the algorithm fail and it may produce erroneous text edits.
* If the second argument is not derived from the original, then the algorithm will produce a single text edit to replace the entire source text.
* If the parse tree of the original does not reflect the current state of the text in the file, then the generated text edits will do harm. 
* If the original tree is not annotated with source locations, the algorithm fails.
* Both parse trees must be type correct, e.g. the number of symbols in a production rule, must be equal to the number of elements of the argument list of ((Tree::appl)).
* This algorithm does not work with ambiguous (sub)trees.
}
@examples{
If we rewrite parse trees, this can be done with concrete syntax matching.
The following example swaps the if-branch with the else-branch in Pico:

```rascal-shell
import lang::pico::\syntax::Main;
import IO;
import analysis::diff::edits::ExecuteTextEdits;
import analysis::diff::edits::TextEdits;
import analysis::diff::edits::TreeDiff;
// an example Pico program:
writeFile(|tmp://example.pico|,
    "begin
    '   declare
    '       a : natural,
    '       b : natural;
    '   if a then 
    '       a := b
    '   else
    '       b := a
    '   fi
    'end");
original = parse(#start[Program], |tmp://example.pico|);
// match and replace all conditionals
rewritten = visit(original) {
    case (Statement) `if <Expression e> then <{Statement ";"}* ifBranch> else <{Statement ";"}* elseBranch> fi`
      => (Statement) `if <Expression e> then 
                     '  <{Statement ";"}* elseBranch> 
                     'else 
                     '  <{Statement ";"}* ifBranch> 
                     'fi`
}
// Check the result as a string. It worked, but we see some collatoral damage in whitespace (indentation).
"<rewritten>"
// Now derive text edits from the two parse trees:
edits = treeDiff(original, rewritten);
// Wrap them in a single document edit
edit = changed(original@\loc.top, edits);
// Apply the document edit on disk:
executeDocumentEdit(edit);
// and when we read the result back, we see the transformation succeeded, and indentation was not lost:
readFile(tmp://example.pico|);
```
}
// equal trees generate empty diffs (note this already ignores whitespace differences)
list[TextEdit] treeDiff(Tree a, a) = [];

// skip production labels of original rules when diffing
list[TextEdit] treeDiff(
    appl(prod(label(_, Symbol s), syms, attrs), list[Tree] args), 
    Tree u)
    = treeDiff(appl(prod(s, syms, attrs), args), u);

// skip production labels of replacement rules when diffing
list[TextEdit] treeDiff(
    Tree t,
    appl(prod(label(_, Symbol s), syms, attrs), list[Tree] args))
    = treeDiff(t, appl(prod(s, syms, attrs), args));

// matched layout trees generate empty diffs such that the original is maintained
list[TextEdit] treeDiff(
    appl(prod(layouts(_), _, _), list[Tree] _), 
    appl(prod(layouts(_), _, _), list[Tree] _))
    = [];

// matched literal trees generate empty diffs 
list[TextEdit] treeDiff(
    appl(prod(lit(str l), _, _), list[Tree] _), 
    appl(prod(lit(l)    , _, _), list[Tree] _))
    = [];

// matched case-insensitive literal trees generate empty diffs such that the original is maintained 
list[TextEdit] treeDiff(
    appl(prod(cilit(str l), _, _), list[Tree] _), 
    appl(prod(cilit(l)    , _, _), list[Tree] _))
    = [];

// different lexicals generate small diffs even if the parent is equal
list[TextEdit] treeDiff(
    t:appl(prod(lex(str l), _, _), list[Tree] _), 
    r:appl(prod(lex(l)    , _, _), list[Tree] _))
    = [replace(t@\loc, learnIndentation("<r>", "<t>"))]
    when t != r;

// When the productions are different, we've found an edit, and there is no need to recurse deeper.
list[TextEdit] treeDiff(
    t:appl(Production p:prod(_,_,_), list[Tree] _), 
    r:appl(Production q:!p         , list[Tree] _))
    = t@\loc?  
        ? [replace(t@\loc, learnIndentation("<r>", "<t>"))] 
        : /* literals and layout (without @\loc) are ignored */ [];


// If list production are the same, then the element lists can still be of different length
// and we switch to listDiff which has different heuristics than normal trees.
list[TextEdit] treeDiff(
    Tree t:appl(Production p:regular(Symbol reg), list[Tree] aElems), 
    appl(p, list[Tree] bElems))
    = listDiff(t@\loc, seps(reg), aElems, bElems);

// When the productions are equal, but the children may be different, we dig deeper for differences
default list[TextEdit] treeDiff(appl(Production p, list[Tree] argsA), appl(p, list[Tree] argsB))
    = [*treeDiff(a, b) | <a,b> <- zip2(argsA, argsB)];

@synopsis{decide how many separators we have}
int seps(\iter-seps(_,list[Symbol] s))      = size(s);
int seps(\iter-star-seps(_,list[Symbol] s)) = size(s);
default int seps(Symbol _) = 0;

@synsopis{List diff is like text diff on lines; complex and easy to make slow}
list[TextEdit] listDiff(loc _span, int seps, list[Tree] originals, list[Tree] replacements) {
    assert originals != replacements && originals == [];
    <originals, replacements> = trimEqualElements(originals, replacements);
    span = cover([orig@\loc | orig <- originals, orig@\loc?]);

    assert originals != replacements && originals != [];
    <edits, originals, replacements> = commonSpecialCases(span, seps, originals, replacements);

    return [*edits, *genericListDiff(span, originals, replacements)];
}

@synopsis{trips equal elements from the front and the back of both lists, if any.}
tuple[list[Tree], list[Tree]] trimEqualElements([Tree a, *Tree aTail], [ a, *Tree bTail])
    = <aTail, bTail>;

tuple[list[Tree], list[Tree]] trimEqualElements([*Tree aHead, Tree a], [*Tree bHead, a])
    = <aHead, bHead>;

default tuple[list[Tree], list[Tree]] trimEqualElements(list[Tree] a, list[Tree] b)
    = <a, b>;

// only one element removed in front, then we are done
tuple[list[TextEdit], list[Tree], list[Tree]] commonSpecialCases(loc span, 0, [Tree a, *Tree tail], [*tail])
    = <[replace(a@\loc, "", "<t>")], [], []>;

// only one element removed in front, plus 1 separator, then we are done because everything is the same
tuple[list[TextEdit], list[Tree], list[Tree]] commonSpecialCases(loc span, 1, 
    [Tree a, Tree _sep, Tree tHead, *Tree tail], [tHead, *tail])
    = <[replace(fromUntil(a, tHead), "", "<t>")], [], []>;

@synopsis{Compute location span that is common between an element and a succeeding element}
@description{
The resulting loc is including the `from` but exclusing the `until`. It goes right
up to `until`.
```ascii-art
 [from] gap [until]
 <--------->
````
}
private loc fromUntil(loc from, loc until) = from.top(from.offset, until.offset - from.offset);

@synopsis{convenience overload for shorter code}
private loc fromUntil(Tree from, Tree until) = fromUntil(fro@\loc, until@\loc);

@synopsis{Finds minimal edits to list elements, taking extra care of removing separators when so required.}
@description{
To make this easy, we add source location information to each original separator first, and then 
reuse the rest of the algorithm which normally ignores separators.
}
list[TextEdit] listDiff(loc _span, [], []) = [];

// equal length, we assume only specific elements have changed. 
list[TextEdit] listDiff(loc _span, list[Tree] elemsA, list[Tree] elemsB) = equalLengthDiff(elemsA, elemsB) 
  when size(elemsA) == size(elemsB);

// additional elements, and possibly other elements have changed.
list[TextEdit] listDiff(loc span, list[Tree] elemsA, list[Tree] elemsB) = longerLengthDiff(span, elemsA, elemsB) 
  when size(elemsA) < size(elemsB);

// fewer elements, and possibly other elements have changed.
list[TextEdit] listDiff(loc span, list[Tree] elemsA, list[Tree] elemsB) = shorterLengthDiff(span, elemsA, elemsB) 
  when size(elemsA) > size(elemsB);

// this works only because we annotated the separators.
list[TextEdit] equalLengthDiff(list[Tree] elemsA, list[Tree] elemsB)
    = [*treeDiff(a,b) | <a,b> <- zip2(elemsA, elemsB)];

// added things to an empty list. this is also the final stage of a deep recursion
list[TextEdit] longerLengthDiff(loc span, [], list[Tree] elemsB) = [replace(span, yield(elemsB))];

// equal length lists can be forwarded (this happens when we already found the extra elements)
list[TextEdit] longerLengthDiff(loc span, list[Tree] elemsA, list[Tree] elemsB)
    = equalLengthDiff(elemsA, elemsB) when size(elemsA) == size(elemsB);

// always ignore identical trees, and continue with the rest
list[TextEdit] longerLengthDiff(loc span, [Tree a, *Tree elemsA], [a, *Tree elemsB])
    = longerLengthDiff(span[offset=a@\loc.offset][length=span.length-a@\loc.length], elemsA, elemsB); 

// a single elem is different and also new by definition because ("longerLengthDiff")
list[TextEdit] longerLengthDiff(loc span, [Tree a, *Tree elemsA], [Tree b:!a, *Tree elemsB])
    = [replace(span[length=0], "<b>")]          // we put b in front of a
    + (size(elemsA) + 1 == size(elemsB)         // and continue with the rest
        ? equalLengthDiff([a, *elemsA], elemsB) // this could have been the last additional element
        : longerLengthDiff(span, [a, *elemsA], elemsB)) // or we still have more to add
    ;

// we have to remove the elements that are replaced by an empty list
list[TextEdit] shorterLengthDiff(loc span, list[Tree] _, [])
    = [replace(span, "")];

// always ignore identical trees, and continue with the rest
list[TextEdit] shorterLengthDiff(loc span, [Tree a, *Tree elemsA], [a, *Tree elemsB])
    = shorterLengthDiff(span[offset=a@\loc.offset][length=span.length-a@\loc.length], elemsA, elemsB); 

// a single elem is different and also superfluous by definition because ("shorterLengthDiff")
list[TextEdit] shorterLengthDiff(loc span, [Tree a, *Tree elemsA], [Tree b:!a, *Tree elemsB])
    = [replace(a@\loc, "<b>")] // we replace a by b
    + shorterLengthDiff(span, elemsA, elemsB) // and continue with the rest
    // TODO: the lists could have become of equal length. Deal with that case.
    ;

private Production sepProd = prod(layouts("*separators*"),[],{});

@synopsis{yield a consecutive list of trees}
private str yield(list[Tree] elems) = "<for (e <- elems) {><e><}>";

@synopsis{Separator literals need location annotations because they have to be edited.}
private list[Tree] prepareSeparators([], int _) = [];

private list[Tree] prepareSeparators([Tree t], int _) = [t];

// we group the 3 separators into a single tree with accurate position information.
private list[Tree] prepareSeparators([Tree head, Tree l1, Tree sep, Tree l2, *Tree rest], 3) 
    = [head, appl(sepProd, [l1, newSep, l2])[@\loc=span], *prepareSeparators(rest)] 
    when
        span := head@\loc.top(end(head@\loc), size("<l1><sep><l2>"));
        
// single separators get accurate position informaiton (even if they are layout)
private list[Tree] prepareSeparators([Tree head, Tree sep, *Tree rest], 1) 
    = [head, sep[\loc=span], *prepareSeparators(rest)] 
    when 
        span := head@\loc.top(end(head@\loc), size("<sep>"));

// unseparated lists are ready
private list[Tree] prepareSeparators(list[Tree] elems, 0) = elems;

private int end(loc src) = src.offset + src.length;

private str learnIndentation(str replacement, str original) = replacement; // TODO: learn minimal indentaton from original
