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
This module provides an essential building block for creating high-fidelity source-to-source code transformations.
It is common for industrial use cases of source-to-source transformation to extract
a list of text edits programmatically using parse tree pattern matching. This way the 
changes are made on the textual level, with less introduction of noise and fewer removals 
of valuable layout (indentation) and source code comments. 

The construction of such high-fidelity edit lists can be rather involved because it tangles
and scatters a number of concerns:
1. syntax-directed pattern matching
2. string substitution; construction of the rewritten text
   * retention of layout and in particular indentation
   * retention of source code comments
   * retention of specific case-insensitive keyword style
   * syntactic correctness of the result; especially in relation to list separators there are many corner-cases to thing of

On the other hand, ParseTree to ParseTree rewrites are much easier to write and get correct. 
They are "syntax directed" via the shape of the tree that follows the grammar of the language.
Some if not all of the above aspects are tackled by the rewriting mechanism with concrete patterns.
Especially the corner cases w.r.t. list separators are all handled by the rewriting mechanisms.
Also the rules are in "concrete syntax", on both the matching and the substition side. So they are 
readable for all who know the object language. The rules guarantee syntactic correctness of the 
rewritten source code. However, rewrite rules do quite some noisy damage to the layout, indentation 
and comments, of the result.

With this module we bring these two modalities of source-to-source transformations together:
1. The language engineer uses concrete syntax rewrite rules to derive a new ParseTree from the original;
2. We run ((treeDiff)) to obtain a set of minimal text edit;
3. We apply the text edits to the editor contents or the file system.
}
@benefits{
* Because the derived text edits change fewer characters, the end result is more "hifi" than simply
unparsing the rewritten ParseTree. More comments are retained and more indentation is kept the same. More
case-insensitive keywords retain their original shape.
* At the same time the rewrite rules are easier to maintain as they remain "syntax directed". 
* Changes to the grammar will be picked up when checking all source and target patterns. 
* The diff algorithm uses cross-cutting information from the parse tree (what is layout and what not,
 what is case-insensitive, etc.) which would otherwise have to be managed by the language engineer in _every rewrite rule_.
* The diff algoritm understands what indentation is and brings new sub-trees to the original level
of indentation (same as the sub-trees they are replacing)
* Typically the algorithm's run-time is lineair in the size of the tree, or better. Same for memory usage.
}
@pitfalls{
* ((treeDiff)) only works under the assumption that the second tree was derived from the first
by applying concrete syntax rewrite rules in Rascal. If there is no origin relation between the two
then its heuristics will not work. The algorithm could degenerate to substituting the entire file,
or worse it could degenerate to an exponential search for commonalities in long lists. 
* ((treeDiff))'s efficiency is predicated on the two trees being derived from each other in main memory of the currently running JVM.
This way both trees will share pointers where they are the same, which leads to very efficient equality
testing. If the trees are first independently serialized to disk and then deserialized again, and then ((treeDiff)) is called, 
this optimization is not present and the algorithm will perform (very) poorly.
* Substitution patterns should be formatted as best as possible. The algorithm will not infer
spacing or relative indentation inside of the substituted subtree. It will only infer indentation
for the entire subtree. 
}
module analysis::diff::edits::HiFiTreeDiff

extend analysis::diff::edits::TextEdits;
import ParseTree;
import List;
import String;
import Location;
import IO;

@synopsis{Detects minimal differences between parse trees and makes them explicit as ((TextEdit)) instructions.}
@description{
This is a "diff" algorithm of two parse trees to generate a ((TextEdit)) script that applies the differences on 
the textual level, _with minimal collatoral damage in whitespace_. This is why it is called "HiFi": minimal unnecessary
noise introduction to the original file.

The resulting ((TextEdit))s are an intermediate representation for making changes in source code text files. 
They can be executed independently via ((ExecuteTextEdits)), or interactively via ((IDEServices)), or LanguageServer features. 

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
* If the second argument was not produced from the first in the same JVM memory, it will not share many pointers to equal sub-trees
and the performance of the algorithm will degenerate quickly.
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
    = [replace(t@\loc, learnIndentation(t@\loc, "<r>", "<t>"))]
    when t != r;

// When the productions are different, we've found an edit, and there is no need to recurse deeper.
default list[TextEdit] treeDiff(
    t:appl(Production p:prod(_,_,_), list[Tree] _), 
    r:appl(Production q:!p         , list[Tree] _))
    = [replace(t@\loc, learnIndentation(t@\loc, "<r>", "<t>"))];

// If list production are the same, then the element lists can still be of different length
// and we switch to listDiff which has different heuristics than normal trees.
list[TextEdit] treeDiff(
    Tree t:appl(Production p:regular(Symbol reg), list[Tree] aElems), 
    appl(p, list[Tree] bElems))
    = listDiff(t@\loc, seps(reg), aElems, bElems) when bprintln("diving into <p>");

// When the productions are equal, but the children may be different, we dig deeper for differences
default list[TextEdit] treeDiff(appl(Production p, list[Tree] argsA), appl(p, list[Tree] argsB))
    = [*treeDiff(a, b) | <a,b> <- zip2(argsA, argsB)] when bprintln("diving into <p>");

@synopsis{decide how many separators we have}
int seps(\iter-seps(_,list[Symbol] s))      = size(s);
int seps(\iter-star-seps(_,list[Symbol] s)) = size(s);
default int seps(Symbol _) = 0;

@synsopis{List diff is like text diff on lines; complex and easy to make slow}
list[TextEdit] listDiff(loc span, int seps, list[Tree] originals, list[Tree] replacements) {
    // println("<span> listDiff: 
    //         '   <yield(originals)>
    //         '   <yield(replacements)>");
    edits = [];

    // this algorithm isolates commonalities between the two lists
    // by handling different special cases. It continues always with
    // what is left to be different. By maximizing commonalities,
    // the edits are minimized. Note that we float on source location parameters
    // not only for the edit locations but also for sub-tree identity.
    
    println("span before trim: <span>, size originals <size(originals)>");
    <span, originals, replacements> = trimEqualElements(span, originals, replacements);
    println("span after trim: <span>, size originals <size(originals)>");
    <specialEdits, originals, replacements> = commonSpecialCases(span, seps, originals, replacements);
    edits += specialEdits;
    println("special edits:");
    iprintln(edits);
        
    equalSubList = largestEqualSubList(originals, replacements);
    println("equal sublist:");
    println(yield(equalSubList));

    // by using the (or "a") largest common sublist as a pivot to divide-and-conquer
    // to the left and right of it, we minimize the number of necessary 
    // edit actions for the entire list.
    if (equalSubList != [],
        [*preO, *equalSubList, *postO] := originals,
        [*preR, *equalSubList, *postR] := replacements) {
        // TODO: what about the separators?
        // we align the prefixes and the postfixes and
        // continue recursively.
        return edits 
            + listDiff(beginCover(span, preO), seps, preO, preR)   
            + listDiff(endCover(span, postO), seps, postO, postR)
        ;
    }
    else if (originals == [], replacements == []) {
        return edits;
    }
    else { 
        // here we know there are no common elements anymore, only a common amount of different elements
        common = min(size(originals), size(replacements));

        return edits 
            // first the minimal length pairwise replacements, essential for finding accidental commonalities
            + [*treeDiff(a, b) | <a, b> <- zip2(originals[..common], replacements[..common])];
            // then we either remove the tail that became shorter:
            + [replace(cover(end(last), cover(originals[cover+1..])), "") | size(originals) > size(replacements), [*_, last] := originals[..common]]
            // or we add new elements to the end, while inheriting indentation from the originals:
            + [replace(end(last), learnIndentation(span, yield(replacements[common+1..]), yield(originals))) | size(originals) < size(replacements)]
        ;
    }
}

@synopsis{Finds the largest sublist that occurs in both lists}
@description{
Using list matching and backtracking, this algorithm detects which common
sublist is the largest. It assumes ((trimEqualElements)) has happened already,
and thus there are interesting differences left, even if we remove any equal
sublist.

Note that this is not a general algorithm for Largest Common Subsequence (LCS), since it
uses particular properties of the relation between the original and the replacement list.
* New elements are never equal to old elements (due to source locations)
* Equal prefixes and postfixes may be assumed to be maximal sublists as well (see above).
* Candidate equal sublists always have consecutive source locations from the origin.
* etc.
}
list[Tree] largestEqualSubList([*Tree sub], [*_, *sub, *_]) = sub;
list[Tree] largestEqualSubList([*_, *sub, *_], [*Tree sub]) = sub;
list[Tree] largestEqualSubList([*_, p, *sub, q, *_], [*_, !p, *Tree sub, !q, *_]) = sub;
default list[Tree] largestEqualSubList(list[Tree] _orig, list[Tree] _repl) = [];

@synopsis{trips equal elements from the front and the back of both lists, if any.}
tuple[loc, list[Tree], list[Tree]] trimEqualElements(loc span, 
    [Tree a, *Tree aPostfix], [ a, *Tree bPostfix])
    = trimEqualElements(endCover(span, aPostfix), aPostfix, bPostfix);

tuple[loc, list[Tree], list[Tree]] trimEqualElements(loc span, 
    [*Tree aPrefix, Tree a], [*Tree bPrefix, a])
    = trimEqualElements(beginCover(span, aPrefix), aPrefix, bPrefix);

default tuple[loc, list[Tree], list[Tree]] trimEqualElements(loc span, 
    list[Tree] a, list[Tree] b)
    = <span, a, b>;

// only one element removed in front, then we are done
tuple[list[TextEdit], list[Tree], list[Tree]] commonSpecialCases(loc span, 0, [Tree a, *Tree tail], [*tail])
    = <[replace(a@\loc, "")], [], []>;

// only one element removed in front, plus 1 separator, then we are done because everything is the same
tuple[list[TextEdit], list[Tree], list[Tree]] commonSpecialCases(loc span, 1, 
    [Tree a, Tree _sep, Tree tHead, *Tree tail], [tHead, *tail])
    = <[replace(fromUntil(a, tHead), "")], [], []>;

// only one element removed in front, plus 1 separator, then we are done because everything is the same
tuple[list[TextEdit], list[Tree], list[Tree]] commonSpecialCases(loc span, 3, 
    [Tree a, Tree _l1, Tree _sep, Tree _l2, Tree tHead, *Tree tail], [tHead, *tail])
    = <[replace(fromUntil(a, tHead), "")], [], []>;

// singleton replacement
tuple[list[TextEdit], list[Tree], list[Tree]] commonSpecialCases(loc span, int _, 
    [Tree a], [Tree b])
    = <treeDiff(a, b), [], []>;

default tuple[list[TextEdit], list[Tree], list[Tree]] commonSpecialCases(loc span, int _, list[Tree] a, list[Tree] b)
    = <[], a, b>;

@synopsis{convenience overload for shorter code}
private loc fromUntil(Tree from, Tree until) = fromUntil(from@\loc, until@\loc);

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
private int end(loc src) = src.offset + src.length;

private loc endCover(loc span, []) = span(span.offset + span.length, 0);
private loc endCover(loc span, [Tree x]) = x@\loc;
private default loc endCover(loc span, list[Tree] l) = cover(l);

private loc beginCover(loc span, []) = span(span.offset, 0);
private loc beginCover(loc span, [Tree x]) = x@\loc;
private default loc beginCover(loc span, list[Tree] l) = cover(l);

private loc cover(list[Tree] elems:[_, *_]) = cover([e@\loc | Tree e <- elems, e@\loc?]);

@synopsis{yield a consecutive list of trees}
private str yield(list[Tree] elems) = "<for (e <- elems) {><e><}>";

@synopsis{Make sure the subtitution is at least as far indented as the original}
@description{
This algorithm ignores the first line, since the first line is always preceeded by the layout of a parent node.

Then it measures the depth of indentation of every line in the original, and takes the minimum.
That minimum indentation is stripped off every line that already has that much indentation in the replacement,
and then _all_ lines are re-indented with the discovered minimum.
}
private str learnIndentation(loc span, str replacement, str original) {
    list[str] indents(str text) = [indent | /^<indent:[\t\ ]*>[^\ \t]/ <- split("\n", text)];

    origIndents = indents(original);
    replLines   = split("\n", replacement);

    if (replLines == []) {
        return "";
    }

    minIndent = "";
    if ([_] := origIndents) {
        // only one line. have to invent indentation from span
        minIndent = "<for (_ <- [0..span.begin.column]) {> <}>";
    }
    else {
        minIndent = sort(origIndents[1..])[0]? "";
    }

    stripped = [ /^<minIndent><rest:.*>$/ := line ? rest : line | line <- replLines];
    
    return indent(minIndent, "<for (l <- stripped) {><l>
                             '<}>"[..-1]);
}
